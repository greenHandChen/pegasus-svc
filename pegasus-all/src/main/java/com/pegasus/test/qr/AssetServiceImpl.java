package com.pegasus.test.qr;

import com.pegasus.common.exception.CommonException;
import com.pegasus.platform.domain.User;
import com.pegasus.platform.repository.UserRepository;
import com.pegasus.platform.service.IDepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 资产/设备基本信息应用服务默认实现
 *
 * @author zhisheng.zhang@hand-china.com 2019-01-24 16:43:37
 */
@Service
@Slf4j
public class AssetServiceImpl implements AssetService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private IDepartmentService departmentService;

    public void qrcodeImage(String codeType, Asset asset, HttpServletRequest request, HttpServletResponse response, String filePath) {
        //二维码里面的内容
        String content = asset.getAssetNum();
        BufferedImage image = ZXingUtils.generateAssetLabel(content, codeType, asset, filePath);
        ByteArrayOutputStream imageOs = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", imageOs);
            ByteArrayInputStream inStream = new ByteArrayInputStream(imageOs.toByteArray());
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);

            // 循环取出流中的数据
            byte[] b = new byte[2048];
            int len;
            byte[] data = null;

            while ((len = inStream.read(b)) > 0) {
                bos.write(b, 0, len);
            }
            bos.close();
            inStream.close();
            data = bos.toByteArray();
            // 设置输出的格式
            response.setContentLength(data.length);
            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition", "attachment;fileName=" + new String("资产台账标签".getBytes("GB2312"), "ISO_8859_1") + ".jpg");
            response.getOutputStream().write(data);
            response.flushBuffer();
        } catch (IOException e) {
        }
    }

    /**
     * @Author: enHui.Chen
     * @Description: 批量生成二维码(HTML转PDF, PDF合并)
     * @Data 2020/4/13
     */
    @Override
    public void batchQrCodePdf(List<Long> assetIds, HttpServletRequest request, HttpServletResponse response) {
        try {
            int length = 1;
            log.info("标签PDF页数:{}", length);

            String html = Constants.AssetLabel.YMDD_HTML_TEMPLATE;
            StringBuilder content = new StringBuilder();
            List<ByteArrayOutputStream> baosList = new ArrayList<>(length);

            // 设置PDF页数
            for (int page = 0; page < length; page++) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                baosList.add(baos);

                // 获取当前资产
                content.append(Constants.AssetLabel.YMDD_CONTENT_TEMPLATE
                        .replace(Constants.AssetLabel.$ENCODE_ASSET_NUM, URLEncoder.encode("asset", "UTF-8"))
                        .replace(Constants.AssetLabel.$ASSET_NUM, "asset")
                        .replace(Constants.AssetLabel.$ASSET_NAME, "asset")
                        .replace(Constants.AssetLabel.$BRAND, "asset")
                        .replace(Constants.AssetLabel.$MODEL, "asset"));

                html = html.replace(Constants.AssetLabel.$CONTENT, content.toString());
                // html-> pdf
                PdfUtil.html2Pdf(html, baos);

                html = Constants.AssetLabel.YMDD_HTML_TEMPLATE;
                content = new StringBuilder();
            }

            // pdf合并
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            PdfUtil.mergePdfFiles(baosList, result);

            //读取响应
            BufferedInputStream bis = new BufferedInputStream(new ByteArrayInputStream(result.toByteArray()));
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File("C:\\Users\\Administrator\\Desktop\\a.pdf")));
            byte[] in = new byte[1024];
            int rst;
            while ((rst = bis.read(in)) > -1) {
                bos.write(in, 0, rst);
            }
            bos.flush();
            bis.close();
            bos.close();

            // 返回pdf文件
            response = PdfUtil.getWebResponse(result.toByteArray(), "资产二维码标签", response);
        } catch (Exception e) {
            log.error("未知错误:{}", e.getMessage());
            throw new CommonException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean amountOfTransaction() {
        try {
            User user = userRepository.findById(1L).get();
            user.setNickName("admin-roll");
            user.setEmail("admin@163.com");
            userRepository.save(user);
            throw new CommonException();
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void noRollTransaction() {
        User user = userRepository.findById(1L).get();
        user.setNickName("admin-no-roll");
        userRepository.save(user);
    }

    public void qrcodePdf(String codeType, List<Asset> assetList, HttpServletRequest request, HttpServletResponse response, String filePath) {

        List<BufferedImage> imageList = new ArrayList<>();
        for (Asset asset : assetList) {
            //二维码里面的内容
            String content = asset.getAssetNum();
            BufferedImage image = ZXingUtils.generateAssetLabel(content, codeType, asset, filePath);
            imageList.add(image);
        }


        String result = ZXingUtils.generatePDF(imageList);
        byte[] data = null;
        try {
            //生成PDF方法，返回字节数组文件流转成的字符串
            //如果跨域需设置解码
            result = java.net.URLDecoder.decode(result, "ISO-8859-1");

            ByteArrayInputStream inStream = new ByteArrayInputStream(
                    result.getBytes(StandardCharsets.ISO_8859_1));
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);

            // 循环取出流中的数据
            byte[] b = new byte[2048];
            int len;

            while ((len = inStream.read(b)) > 0) {
                bos.write(b, 0, len);
            }
            bos.close();
            inStream.close();
            data = bos.toByteArray();
            // 设置输出的格式
            String fileName = "资产台账标签";
            String userAgent = request.getHeader("user-agent");
            if (userAgent.contains("MSIE") || userAgent.contains("Trident") || userAgent.contains("Edge")) {
                fileName = URLEncoder.encode(fileName, "UTF-8");
            } else {
                // 非IE浏览器的处理：
                fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
            }
            response.setContentLength(data.length);
            response.setCharacterEncoding("utf-8");
//            response.setContentType("multipart/form-data");
            response.setContentType("application/x-msdownload;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;fileName=" + fileName + ".pdf");
            response.getOutputStream().write(data);
            response.flushBuffer();
        } catch (IOException e) {
        }
    }
}
