package com.pegasus.test.qr.html2Pdf;


import com.itextpdf.text.pdf.codec.Base64;
import com.lowagie.text.Image;
import org.apache.commons.io.IOUtils;
import org.w3c.dom.Element;
import org.xhtmlrenderer.extend.FSImage;
import org.xhtmlrenderer.extend.ReplacedElement;
import org.xhtmlrenderer.extend.ReplacedElementFactory;
import org.xhtmlrenderer.extend.UserAgentCallback;
import org.xhtmlrenderer.layout.LayoutContext;
import org.xhtmlrenderer.pdf.ITextFSImage;
import org.xhtmlrenderer.pdf.ITextImageElement;
import org.xhtmlrenderer.render.BlockBox;
import org.xhtmlrenderer.simple.extend.FormSubmissionListener;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * <p>
 * 图片处理优化-支持html中img标签的src为url或者base64
 * </p>
 */
public class ImgReplacedElementFactory implements ReplacedElementFactory {

    private final static String IMG_ELEMENT_NAME = "img";
    private final static String SRC_ATTR_NAME = "src";
    private final static String URL_PREFIX_NAME = "data:image";
    private final static String URL_BASE64 = "base64,";

//    private final static Logger LOGGER = LoggerFactory.getLogger(ImgReplacedElementFactory.class);

    @Override
    public ReplacedElement createReplacedElement(LayoutContext c, BlockBox box, UserAgentCallback uac, int cssWidth, int cssHeight) {
        Element e = box.getElement();
        if (e == null) {
            return null;
        }
        String nodeName = e.getNodeName();
        // 找到img标签
        if (nodeName.equals(IMG_ELEMENT_NAME)) {
            String url = e.getAttribute(SRC_ATTR_NAME);
            FSImage fsImage;
            try {
                InputStream imageStream = this.getImageStream(url, 0);
                byte[] bytes = IOUtils.toByteArray(imageStream);
                // 生成itext图像
                fsImage = new ITextFSImage(Image.getInstance(bytes));
            } catch (Exception e1) {
                fsImage = null;
            }
            if (fsImage != null) {
                // 对图像进行缩放
                if (cssWidth != -1 || cssHeight != -1) {
                    fsImage.scale(cssWidth, cssHeight);
                }
                return new ITextImageElement(fsImage);
            }
        }
        return null;
    }

    @Override
    public void reset() {

    }

    @Override
    public void remove(Element e) {

    }

    @Override
    public void setFormSubmissionListener(FormSubmissionListener listener) {

    }

    /**
     * 重复获取网络图片3次，若三次失败则不再获取
     *
     * @param url
     * @param tryCount
     * @return
     */
    private InputStream getImageStream(String url, int tryCount) {
        if (tryCount > 2) {
            return null;
        }
        if (URL_PREFIX_NAME.equals(url.substring(0, 10))) {
            byte[] bytes = Base64.decode(url.substring(url.indexOf(URL_BASE64) + 7));
            //转化为输入流
            return new ByteArrayInputStream(bytes);
        }
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                return inputStream;
            } else {
                tryCount += 1;
//                LOGGER.info("connectionError : {} , msg : {}", connection.getResponseCode(), connection.getResponseMessage());
                return getImageStream(url, tryCount);
            }
        } catch (IOException e) {
//            LOGGER.error("connectionIOException : {} , trace : {}", e.getMessage(), e.getStackTrace());
        }
        return null;
    }

}