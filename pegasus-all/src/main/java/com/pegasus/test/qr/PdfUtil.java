package com.pegasus.test.qr;

import com.itextpdf.text.pdf.BaseFont;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import lombok.extern.slf4j.Slf4j;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by enHui.Chen on 2020/4/13.
 */
@Slf4j
public class PdfUtil {
    /**
     * @Author: enHui.Chen
     * @Description: html 转 pdf
     * @Data 2020/4/13
     */
    public static void html2Pdf(String html, ByteArrayOutputStream fileOutputStream) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("=========生成pdf的html信息=========");
            log.debug("{}", html);
        }

        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        ITextFontResolver fontResolver = renderer.getFontResolver();
        // 获取字体文件路径
        fontResolver.addFont(getFontPath(), BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        renderer.getSharedContext().setReplacedElementFactory(new ImgReplacedElementFactory());
        renderer.layout();
        renderer.createPDF(fileOutputStream);
        fileOutputStream.flush();
    }

    /**
     * @Author: enHui.Chen
     * @Description: pdf合并
     * @Data 2020/4/13
     */
    public static void mergePdfFiles(List<ByteArrayOutputStream> baosList, ByteArrayOutputStream result) throws Exception {
        Document document = null;
        try {
            document = new Document();
            PdfCopy copy = new PdfCopy(document, result);
            document.open();
            for (ByteArrayOutputStream baos : baosList) {
                PdfReader reader = new PdfReader(baos.toByteArray());
                int n = reader.getNumberOfPages();
                for (int j = 1; j <= n; j++) {
                    document.newPage();
                    PdfImportedPage page = copy.getImportedPage(reader, j);
                    copy.addPage(page);
                }
            }
        } finally {
            if (document != null) {
                document.close();
            }
        }
    }

    /**
     * @Author: enHui.Chen
     * @Description: 设置字体文件
     * @Data 2020/4/13
     */
    private static String getFontPath() {
        return Constants.AssetLabel.FONT_PATH;
    }

    /**
     * @Author: enHui.Chen
     * @Description: 设置WEB返回数据
     * @Data 2020/4/13
     */
    public static HttpServletResponse getWebResponse(byte[] data, String fileName, HttpServletResponse response) throws IOException {
        response.setContentLength(data.length);
        response.setCharacterEncoding("utf-8");
//            response.setContentType("multipart/form-data");
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment;fileName=" + fileName + ".pdf");
        response.getOutputStream().write(data);
        return response;
    }
}
