package com.pegasus.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.URLEncodedUtils;
import org.springframework.util.Assert;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;


/**
 * Created by enHui.Chen on 2020/5/16.
 */
@Slf4j
public class HttpUtil {
    private static final int SAVE_LENGTH = 1024;

    /**
     * @Author: enHui.Chen
     * @Description: 图片下载, 获取文件流
     * @Data 2020/5/16
     */
    public static InputStream downloadFile(String url) {
        Assert.notNull(url, "url can't be null");

        try {
            String h = "http://oa-test.yimidida.com:8081/";
            String s = "/TemplateFile/202005/E/财务二级科目对应预算科目 (2) (2).xlsx";
            URL fileUrl = new URL(h + URLEncoder.encode(s, "UTF-8").replaceAll("\\+", "%20"));

            HttpURLConnection connection = (HttpURLConnection) fileUrl.openConnection();
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            return connection.getInputStream();
        } catch (Exception e) {
            log.error("downloadFile error-url:{}:", url);
            log.error("downloadFile error:", e);
        }
        return null;
    }

    /**
     * @Author: enHui.Chen
     * @Description: 图片下载, 获取文件字节数组
     * @Data 2020/5/16
     */
    public static byte[] downloadFileByte(String url) {
        InputStream inputStream = downloadFile(url);
        if (inputStream == null) {
            return null;
        }

        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(inputStream);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] fileBytes = new byte[1024];
            int offset;
            while ((offset = bis.read(fileBytes)) != -1) {
                // 偏移量读取，防止最后一次读取时，读到数组为空的内容
                bos.write(fileBytes, 0, offset);
            }
            return bos.toByteArray();
        } catch (IOException e) {
            log.error("downloadFileByte error-url:{}:", url);
            log.error("downloadFileByte error:", e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                log.error("downloadFileByte is close error-url:{}:", url);
                log.error("downloadFileByte is close error:", e);
            }

            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    log.error("downloadFileByte bis close error-url:{}:", url);
                    log.error("downloadFileByte bis close error:", e);
                }
            }
        }
        return null;
    }
}
