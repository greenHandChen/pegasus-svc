package com.pegasus.test.qr;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.RectangleReadOnly;
import com.itextpdf.text.pdf.PdfWriter;
import sun.awt.SunHints;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 资产标签生成工具
 *
 * @author jun.fang01@hand-china.com 2020-3-9
 */
public class ZXingUtils {

    private static final int QRCOLOR = 0xFF000000;   //默认是黑色
    private static final int BGWHITE = 0xFFFFFFFF;   //背景颜色

    //二维码标准页签长宽
    private static final int QR_LABEL_WIDTH = 500;
    private static final int QR_LABEL_HEIGHT = 210;

    //二维码长宽
    private static final int QR_CODE_WH = 210;

    // 二维码上间距
    private static final int QR_CODE_TOP = 0;
    // 二维码左间距
    private static final int QR_CODE_LEFT = 0;

    //与二维码间隔
    private static final int QR_W = 240;

    //文字的 y 轴坐标
    private static final int QR_TEXT_Y = 150;

    private static final int SIX_FONT_SIZE = 20;
    private static final int TEN_FONT_SIZE = 26;
    private static final int PRODUCT_NAME_FONT_SIZE = 5;


    //条形码标准页签长宽
    private static final int BAR_LABEL_WIDTH = 400;
    private static final int BAR_LABEL_HEIGHT = 100;
    private static final int BAR_WIDTH = 380;
    private static final int BAR_HEIGHT = 50;

    private static final int BAR_LABEL_INTERVAL = 10;
    private static final int BAR_TOP = 50;


    /**
     * 生成资产标签
     *
     * @param content  生成标签的内容
     * @param codeType 标签类型
     * @param asset    资产
     * @return BufferedImage
     * @throws IOException 异常
     */
    public static BufferedImage generateAssetLabel(String content, String codeType, Asset asset, String filePath) {
        ZXingUtils zXingUtil = new ZXingUtils();
        BufferedImage image = null;
        if ("QR".equals(codeType)) {
            BufferedImage bim = zXingUtil.generateCode(content, BarcodeFormat.QR_CODE, QR_CODE_WH, QR_CODE_WH, zXingUtil.getDecodeHintType());
            image = zXingUtil.generateQRCOdeLabel(bim, filePath, asset.getAssetName(), asset.getAssetNum());
        } else if ("BAR".equals(codeType)) {
            image = zXingUtil.generateCode(content, BarcodeFormat.CODE_128, 240, BAR_HEIGHT, null);
        }
        /*//创建储存图片二进制流的输出流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //将二进制数据写入ByteArrayOutputStream
        ImageIO.write(image, "png", baos);
        return baos;*/
        return image;
    }


    public static String generatePDF(List<BufferedImage> imageList) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        Document document = new Document(new RectangleReadOnly(600, 200));
        Document document = new Document(new RectangleReadOnly(BAR_LABEL_WIDTH / 100 * 35, (imageList.get(0).getHeight() + 100) / 100 * 35 * imageList.size()), 0.0f, 0.0f, 0.0f, 0.0f);
//        Document document = new Document(new RectangleReadOnly(610, 697));
        String resultStr = null;
        try {
            //将PDF文档对象写入到流
            PdfWriter.getInstance(document, baos);
            document.open();
            for (int i = 0; i < imageList.size(); i++) {
                ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
                ImageIO.write(imageList.get(i), "PNG", baos1);
                com.itextpdf.text.Image img = com.itextpdf.text.Image.getInstance(baos1.toByteArray());
                //居中显示
                img.setAlignment(1);
                //显示位置，根据需要调整
                //img.setAbsolutePosition(60, 60);
                //显示为原条形码图片大小的比例，百分比
                img.scalePercent(35);
                document.add(img);
            }
            document.close();
            //转字符串设置编码
            resultStr = new String(baos.toByteArray(), StandardCharsets.ISO_8859_1);
            //如果跨域需设置编码
            resultStr = java.net.URLEncoder.encode(resultStr, "ISO-8859-1");
        } catch (DocumentException | IOException e) {
        } finally {
            try {
                baos.close();
            } catch (IOException e) {
            }
        }
        return resultStr;
    }


    /**
     * 设置二维码的格式参数
     *
     * @return hints
     */
    private Map<EncodeHintType, Object> getDecodeHintType() {
        // 用于设置QR二维码参数
        Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
        // 设置QR二维码的纠错级别（H为最高级别）具体级别信息
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.Q);
        // 设置编码方式
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.MARGIN, 0);
        hints.put(EncodeHintType.MAX_SIZE, 350);
        hints.put(EncodeHintType.MIN_SIZE, 100);
        return hints;
    }

    /**
     * 生成bufferedImage图片
     *
     * @param content       编码内容
     * @param barcodeFormat 编码类型
     * @param width         图片宽度
     * @param height        图片高度
     * @param hints         设置参数
     * @return BufferedImage
     */
    private BufferedImage generateCode(String content, BarcodeFormat barcodeFormat, int width, int height, Map<EncodeHintType, ?> hints) {
        int codeWidth = width;
        MultiFormatWriter multiFormatWriter = null;
        BitMatrix bm = null;
        BufferedImage image = null;
        multiFormatWriter = new MultiFormatWriter();
        // 参数顺序分别为：编码内容，编码类型，生成图片宽度，生成图片高度，设置参数
        try {
            bm = multiFormatWriter.encode(content, barcodeFormat, codeWidth, height, hints);
        } catch (WriterException e) {
        }

        int w = bm.getWidth();
        int h = bm.getHeight();
        image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        // 开始利用二维码数据创建Bitmap图片，分别设为黑（0xFFFFFFFF）白（0xFF000000）两色
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                image.setRGB(x, y, bm.get(x, y) ? QRCOLOR : BGWHITE);
            }
        }
        return image;
    }


    /**
     * 壹米滴答条形码标签
     *
     * @param bim        二维码BufferedImage
     * @param productNum 产品编号
     * @return BufferedImage
     * @throws IOException 异常
     */
    private BufferedImage generateBarCodeLabel(BufferedImage bim, String productNum) {
        //读取背景图片
        BufferedImage image = new BufferedImage(BAR_LABEL_WIDTH + 1, BAR_LABEL_HEIGHT + 10, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();

        // 设置RenderingHints(渲染提示)，以达到文字抗锯齿的功效,(key,value)形式赋值
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        rh.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        graphics.setRenderingHints(rh);


        // 绘制圆角矩形
        graphics.setColor(Color.BLACK);
        graphics.drawRoundRect(0, 0, BAR_LABEL_WIDTH, BAR_LABEL_HEIGHT, BAR_TOP, BAR_TOP);

        //画条形码
        graphics.drawImage(bim, BAR_LABEL_INTERVAL, BAR_LABEL_INTERVAL * 2, BAR_WIDTH, BAR_HEIGHT, null);
        //画文字
        graphics.setColor(Color.BLACK);
        Font font = new Font("微软雅黑", Font.BOLD, PRODUCT_NAME_FONT_SIZE);
        graphics.setFont(font); //字体、字型、字号
        graphics.setRenderingHints(rh);
        FontMetrics fontMetrics = graphics.getFontMetrics(font);
        int textWidth = fontMetrics.stringWidth(productNum);
        int x = BAR_LABEL_WIDTH / 2 - textWidth / 2;
        graphics.drawString(productNum, x, 95);

        graphics.dispose();
        bim.flush();
        image.flush();
        return image;
    }


    /**
     * 壹米滴答二维码标签
     *
     * @param bim         二维码BufferedImage
     * @param logoPic     logo
     * @param productName 产品名称
     * @param productNum  产品编号
     * @return BufferedImage
     */
    public BufferedImage generateQRCOdeLabel(BufferedImage bim, String logoPic, String productName,
                                             String productNum) {


        //读取背景图片
        BufferedImage image = new BufferedImage(QR_LABEL_WIDTH, QR_LABEL_HEIGHT + 10, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();

        // 设置RenderingHints(渲染提示)，以达到文字抗锯齿的功效,(key,value)形式赋值
//        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        rh.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
//        rh.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
//        graphics.setRenderingHints(rh);
        InputStream resourceAsStream = this.getClass().getResourceAsStream(logoPic);

//        // 绘制圆角矩形
//        graphics.setColor(Color.BLACK);
//        graphics.setFont(new Font("微软雅黑", Font.BOLD, FONT_SIZE)); //字体、字型、字号
//        graphics.drawRoundRect(0,0,QR_LABEL_WIDTH,QR_LABEL_HEIGHT,BAR_TOP,BAR_TOP);

        //读取logo图片
//        BufferedImage logo = null;
//        try {
//            logo = ImageIO.read(resourceAsStream);
//        } catch (IOException e) {
//        }


        //新的图片，把带logo的二维码下面加上文字
        //画二维码到新的面板
        graphics.drawImage(bim, QR_CODE_LEFT, QR_CODE_TOP, QR_CODE_WH, QR_CODE_WH, null);

        //绘制logo图片,设置logo的大小
//        int logoWidth = logo.getWidth() * 3 / 5,
//                logoheight = logo.getHeight() * 3 / 5;
//        graphics.drawImage(logo, QR_W, QR_CODE_TOP, logoWidth, logoheight, null);

        //画文字到新的面板
        // 标题
//        graphics.setColor(Color.BLACK);
        graphics.setFont(new Font("微软雅黑", Font.BOLD, TEN_FONT_SIZE)); //字体、字型、字号
        graphics.setRenderingHint(SunHints.KEY_ANTIALIASING, SunHints.VALUE_ANTIALIAS_OFF);
        graphics.setRenderingHint(SunHints.KEY_TEXT_ANTIALIASING, SunHints.VALUE_TEXT_ANTIALIAS_DEFAULT);
        graphics.setRenderingHint(SunHints.KEY_STROKE_CONTROL, SunHints.VALUE_STROKE_DEFAULT);
        graphics.setRenderingHint(SunHints.KEY_TEXT_ANTIALIAS_LCD_CONTRAST, 140);
        graphics.setRenderingHint(SunHints.KEY_FRACTIONALMETRICS, SunHints.VALUE_FRACTIONALMETRICS_OFF);
        graphics.setRenderingHint(SunHints.KEY_RENDERING, SunHints.VALUE_RENDER_DEFAULT);
//        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

//        graphics.setPaint(new Color(0, 0, 0, 64));//阴影颜色
//        graphics.drawString("壹米滴答", 280, 30);//先绘制阴影
        graphics.setPaint(Color.BLACK);//正文颜色
        graphics.drawString("壹米滴答", 280, 30);

//        // 资产编号
////        graphics.setColor(Color.BLACK);
//        graphics.setFont(new Font("微软雅黑", Font.PLAIN, SIX_FONT_SIZE)); //字体、字型、字号
//        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
//
//        graphics.setPaint(new Color(0, 0, 0, 64));//阴影颜色
//        graphics.drawString("资产条码: " + productNum, QR_W, 75);
//        graphics.setPaint(Color.BLACK);//正文颜色
//        graphics.drawString("资产条码: " + productNum, QR_W, 75);
//
//        // 资产名称
//        graphics.setFont(new Font("微软雅黑", Font.PLAIN, SIX_FONT_SIZE)); //字体、字型、字号
//        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
//
//        graphics.setPaint(new Color(0, 0, 0, 64));//阴影颜色
//        graphics.drawString("资产名称: " + productName, QR_W, 120);
//        graphics.setPaint(Color.BLACK);//正文颜色
//        graphics.drawString("资产名称: " + productName, QR_W, 120);
//
//        // 品牌
//        graphics.setFont(new Font("微软雅黑", Font.PLAIN, SIX_FONT_SIZE)); //字体、字型、字号
//        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
//
//        graphics.setPaint(new Color(0, 0, 0, 64));//阴影颜色
//        graphics.drawString("品牌: " + productName, QR_W, 165);
//        graphics.setPaint(Color.BLACK);//正文颜色
//        graphics.drawString("品牌: " + productName, QR_W, 165);
//
//        // 规格
//        graphics.setFont(new Font("微软雅黑", Font.PLAIN, SIX_FONT_SIZE)); //字体、字型、字号
//        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
//
//        graphics.setPaint(new Color(0, 0, 0, 64));//阴影颜色
//        graphics.drawString("规格: " + productName, QR_W, 210);
//        graphics.setPaint(Color.BLACK);//正文颜色
//        graphics.drawString("规格: " + productName, QR_W, 210);

        graphics.dispose();
//        logo.flush();
        bim.flush();
        image.flush();
        return image;
    }

}
