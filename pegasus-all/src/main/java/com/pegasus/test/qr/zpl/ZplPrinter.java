package com.pegasus.test.qr.zpl;

import javax.print.*;
import javax.print.attribute.standard.PrinterName;
import java.io.UnsupportedEncodingException;

/**
 * 斑马打印机打印类-ZPL语音
 */
public class ZplPrinter {
    private PrintService printService = null;//打印机服务
    private String content = "";

    /*private String begin_1 = "\u0010CT~~CD,~CC^~CT~\n" +
            "^XA~TA000~JSN^LT0^MNW^MTT^PON^PMN^LH0,0^JMA^PR3,3~SD28^JUS^LRN^CI0^XZ\n" +
            "^XA\n" +
            "^MMT\n" +
            "^PW160\n" +
            "^LL0280\n" +
            "^LS0\n"; // 开始（个）*/

    private static final String begin = "\u0010CT~~CD,~CC^~CT~" +
            "^XA~TA000~JSN^LT0^MNW^MTT^PON^PMN^LH0,0^JMA^PR3,3~SD28^JUS^LRN^CI0^XZ" +
            "^XA" +
            "^MMT" +
            "^PW695" +
            "^LL0280" +
            "^LS0"; //开始(排)

//    private String logo_1 = "^FO64,0^GFA,01152,01152,00012,:Z64:\n" +
//            "eJztUjtOAzEQHcdYKRKxKbBERSiRaCgRi2D3Bou07nOMVMiIi6zEJaKsxHKUHINqzfy8QEWZBsu2np6eZ97MGOB//bVMaCZs+/2EQwhVximlwyQPoc3yYRjeFXvkg+L1CEWKGqb9zlB2cjgMBvfCmxGv+afyAM5JArsDWLyII4fSUAtfoJcUnyXtU8AnkngR+7c9lMLXZNNzBWsg+wUb8obKcsw/YAER5sxj5Im/RzcHsB/KmwYM87eId2A75fEIv+VYhjtEFXl6InrK2gnfYnThtzCCjSB69GMmfd+jWPXUzhy/SCnraQDV5OdxiCb79z/4C7TK/BnZ0TjnAHdAmYXH7bhXp6y30jfews+oLLqp/4amfiJziQkntpR5YZ8bCkUrlvh/bgRX9H907hscQNwIXhkf6kowGrp+FUgpnVNMVpaKqTUrxXDJG9SpqzKexasMf/3nY68vBLVZLg==:36A9\n";

    private static final String logo_1 = "^FO192,170^GFA,01536,01536,00012,:Z64:" +
            "eJztUjFOw0AQnLMTCoQciliiQrzANQWKzwV9Cm+fF/ACinsKT/FTTlSI4pTSBbJZ7+5Z+QEUWfmi0WhvbjK7wLX+uLbkV3yLjwzdEX3GhXxaG76RLzzwORk+HvkYPn0DdybTUe83ikvMcyjtVUdETvENmpQQBO/ZD3U+y7dziILvgZq8PvAEHOQX8mQPc8pWXoDnzO/l42KFxgw5L83KD9JfKe+I+dr4abrgOZ+t2A/sf8TOdNi/8kVAkVKQfo7G84XcH9GGnfKcZ732jzB97l+8a/+w/K1Vf/EifoqIalj9SPrCu4jyDDzatBxl/0xOUbSUJ6/5jCh5AO/C9xwndcq/oflM4UswgTy9Kt9iDihHwXXH8lvNvxm4t9K51MvcbYN25+XSoPNlCWd8kfQIz1xNisFptj+GOU7KC3eYpikaZpvrgrJ9dqS17A9yNSmu+HKfr/XP6xchOF9e:F887" +
            "^FO512,170^GFA,02048,02048,00016,:Z64:" +
            "eJztk0FOAzEMRX8aIhCtKIuOxBFYskQqEsMNBmm85xgsI3XJJVhyhFFn06PMEbqsVDTBCTR2uuIAY7WVvl5tf8cJMMUUqKjU9zOvpatNo/VV+kjccgWt3wCjCpiXGqZWevMFPKjyjl5jjVMsbBixEL1yRO2F6Dv0fTcT3bB/ckX7MFoxwK2JlAEmh/T7Z4fdNZAJo/U9MCjOLZB5rNypAbizKThS/qNwRwxb4fZ4OON8/quTvoENYcCTcMPzC78G5v1W8isY/kOR7zFKfsX7Q5Fv42w6P3rPfJmOR/dP3rP/uccahf+0/cwvPZY74D3zGq7V8+/4AHzqkTnvR85vwDKMZp95w+sjJ/vp0H9s7WfWrSFDldzQbzvyFEPWFNtXddZ9zF174fF+qhfwzO4RRMfSTvF59/vNnBm1oi1vL+xF83aI9AM6cnilI1eSx+cHoCLef635/vtCn7/fKab4V/wA4xhbmA==:B3EC" +
            "^FO352,170^GFA,01152,01152,00012,:Z64:" +
            "eJztUjFOxDAQHAeOO0WUFBQcUJ5S8QAKHsL9gZICiEnJK1JG+wpT0FNQXqR7wpVXEIXxrh3dA6DDsqPRaLw7GS/wv/50leInfObuMixadBkf67a14I184YYnJNy2PAm/fwHnqUwjnV8YPnHjiJPUtRCRwvAp1v3GGV7RjzQ+l69HZw2ugUq8NaDHJ/1CW3ZITql8BB4yv9INxAr3yVDhVWy8V/2F8YWQr4x3w/cBz3zKiOeg/x0uUx36N37G3femZzSeF7I+oDZ96ZlnlfVuB0z66N30Pv7WVD96UT+zgKWf/Gj6yh8FzLfArfItCsn+t3BD0FrGi9d82HXOB9gr3zFOaSy3DdYfvftULBAvb8a/uJGudoqrhuVLy3/9Su3S3qWK754m6IpuUBsuWaJI/GxjR3lylRh2TLPeG45xSh6452EYQsK0OQ0o7Y8Jxt+SjDk/YcKH8/wL6wd4H3r/:A9FE" +
            "^FO0,170^GFA,01536,01536,00012,:Z64:" +
            "eJztUjFOw0AQnDMkjixKCgoClJErHkDBQ8gfKCkAHy55hUtrX2EKegrKWMoTUqbAMnO7d1Z+gJCyurNGo7nd8dwBx/oXVYif8Lm7TzBr0CZ8qstqwRPpwC13F3HTcEf88Q1cxDa1tH5heO7GEfM4NRORzPAZ1v3GGV7Rj9Q+ta9GZwNugFK8DaDHZ/1CR7aITql8Ah4Tv9IFhA4P0VDmVWy8V/2l8ZmQL413w88Bz3yKgHPQ/w5XsQ/9Gz/j6nvTMxrPA0nfoTJ94ZlnmfRuB0z64N30PvzW1D94UT+zDks/+dH0lT/pkG+BO+UbZJL8b+GGTnsZL17z4dScF7BXvmWcUltuG6w/e/elWCBe3o1/dSNd7RSXNdsXlv/6jdql3UsZ7j2+oGu6QWW4YIss8rONbeXJlWLYMc1qbzjEKenBvQzD0EVMm9MDpf0xwvBbkjDfTzfhw/d8rD+rX2f5ev8=:D9E6";

    private static final String logo_2 = "^FO512,192^GFA,01152,01152,00012,:Z64:" +
            "eJztkUFKBDEQRX86E2agRUeYgOBGT6BLwU164T4DU3uP4BEis/Aa4zlmM0fppUcYbOg2oauq+waCWKvHS6iq/AB/rFZJ0dBOuYJVXsAEvQ7cCV9HrIXv+3I0VkO7hfB7911Je3hyzBZ22DO7PLiZxiak2VgefFH8aeTNzK+Kb0dem21AHPnGDAmv4onU43hMj+zzPtP9U63eBDP53Fx9ltrnCXiTfTYzXwMH8T4a9pdYfsmeHo4k0BrP3Z7f5fKeL+yXqa8+xDcRnr39POCW8ynP9ZwbzkAvTDD6kV2yZ/VbH4WvhqEVdkRB2OZ8hEtuWg/txC7gv36nfgDK2jZc:047B" +
            "^FO348,192^GFA,00768,00768,00008,:Z64:" +
            "eJzdkTFuwzAMRb8sCw6KwM4QrU2QOSg6dquHevcg7j1BzuCpvUZvkhzFYydn9ZT0k/IJOhXV8vAEipS+gL+zDhlBeuMKa+MGwbgHBmXXolV+zroFx/oNWVTX7xVZQtKWXKO5aYMtG5bWrkaxtHPkM92T/eJ7dQ5oQwfHAe/VDc5cknkxTbjQsXiFJ/OAMjuLzdt8HiPcRfv3ix9RDOqiJXjB7kvnJcSkDzziOjV63yASyUc/P+zIyGIh648BJ42n0zOAH+FnfX9C0MD8hGZUlzexfF7vd8srilh+Ne+f80056POQGX/xV/9l/QBSVjIV:871E" +
            "^FO160,192^GFA,01152,01152,00012,:Z64:" +
            "eJztkDFOw0AQRf/aCUQEYUfyNgjhlClTIkCKj7DFzl0oENqSY3CElCulSI7iY6RKGGBm7BNQIH719GzPjD/wl/M+oKfOeIm1cYfGOKFQdB5OuWh7fvSTCc/RQbN2/7EUrh2FILzG5qgLAnysbe0DZqO1E+ED+2tZO/aJ/Y14+AZT4dQecWGegvn5Lpfq3cjfYfANavP3gHmPYX6ZcKn38EXmscWVefr65Ds93pLeGUBBC92W+/ysPXiKUfil6levwpF7Vv94C5eF+Xed9lklVL3wlAvt1GdsknpaEDTn88mYKBo/7bIx92ZcDK/zRfjP7+UT49su6w==:8176" +
            "^FO0,192^GFA,00768,00768,00008,:Z64:" +
            "eJzVkbFuwjAQhn8npCCoSCrFC0IkIyNj1VZKHsGD7106IOSRx+ARGC0xwKPkMZhC7y7hCbq0Xj59lu/s+w38vXUcYKlV1tgpW5TKgERgLIwwqTreAiZ8Xgpm1fVUMwtDzjF3aO7SwMH6Qtu9Yza2mzBv7K/c7umBfckOWyJjhuqOF3Vy6otLTMXN6GsMXqJQ3wDqFkN9GjCV/nyDOs6Yq5McQYdDkPscyMmA5/Qav+S9lrxnfufdds/0PK/4xwomSjyl1AB5QN4xMx5Q5s8jGskjozfSvB6PXknklZ+XOObrlEk/5u5//3X/bv0AQ3wu6w==:13E8";

    private static final String end = "^PQ1,0,1,Y^XZ";

    private static final String BAR = "^BY2,3,37^FT${h},258^BCB,,Y,N" +
            "^FD>;${code}^FS";

    private static final String QR = "^FT$25,${h1}^BQN,4,5" +
            "^FH\\^FDLA,${code}^FS";
//            "^FT${h2},102^A0B,17,16^FH\\^FD${code}^FS";

    private static final String NUM = "${code}";
    private static final String H = "${h}";
    private static final String H1 = "${h1}";
    private static final String H2 = "${h2}";

    public ZplPrinter() {

    }

    public static void main(String[] args) {
//        ZplPrinter p = new ZplPrinter("\\\\192.168.0.12\\ZDesigner 105SLPlus-300dpi ZPL");
//        ZplPrinter p = new ZplPrinter("ZDesigner ZT210-200dpi ZPL");
        ZplPrinter p = new ZplPrinter();
//        1.打印单个条码
        String bar0 = "71503888";//条码内容
        String bar0Zpl = "^FO110,110^BY6,3.0,280^BCN,,Y,N,N^FD${data}^FS";//条码样式模板
//        p.setCharB("name", 20, 60, 30, 30);
//        p.setText("型号", 40, 60, 30, 30, 5, 1, 1, 24);
        p.setBegin();

        p.setQr(bar0, 150, 0);
//        p.setText2Img("壹米滴答", 150, 28, 25, 0);
//        p.setText2Img("资产编码:" + bar0, 150, 56, 25, 0);
//        p.setText2Img("资产名称:" + bar0, 150, 84, 25, 0);
//        p.setText2Img("设备:" + bar0, 150, 112, 25, 0);
//
//        p.setQr(bar0, 300, 0);
//        p.setText2Img("壹米滴答", 150, 178, 25, 0);
//        p.setText2Img("资产编码:" + bar0, 150, 206, 25, 0);
//        p.setText2Img("资产名称:" + bar0, 150, 234, 25, 0);
//        p.setText2Img("设备:" + bar0, 150, 262, 25, 0);
//
//        p.setQr(bar0, 450, 0);
//        p.setText2Img("壹米滴答", 150, 328, 25, 0);
//        p.setText2Img("资产编码:" + bar0, 150, 356, 25, 0);
//        p.setText2Img("资产名称:" + bar0, 150, 384, 25, 0);
//        p.setText2Img("设备:" + bar0, 150, 412, 25, 0);
//
//        p.setQr(bar0, 600, 0);
//        p.setText2Img("壹米滴答", 150, 478, 25, 0);
//        p.setText2Img("资产编码:" + bar0, 150, 506, 25, 0);
//        p.setText2Img("资产名称:" + bar0, 150, 534, 25, 0);
//        p.setText2Img("设备:" + bar0, 150, 562, 25, 0);
//        p.setLogo(1);
        p.setEnd();
//        p.setChinese("型号",50,60,30,30);
//        String bar0Zpl = "^FO102,0^BY2,3.0,40^BCB,,Y,N,N^FD${data}^FS";//条码样式模板
//        p.setBarcode(bar0, bar0Zpl);
//        p.setMB();
        String zpl = p.getZpl();
        System.out.println(zpl);
        Zpl2Pdf.send("http://api.labelary.com/v1/printers/12dpmm/labels/1.3x1.99/0/", zpl);
//        boolean result1 = p.print(zpl);//打印
//        if (result1) {
//            return;
//        }

//        p.resetZpl();//注意要清除上次的打印信息
    }

    /**
     * add LOGO
     *
     * @param i 样式
     */
    public void setLogo(int i) {
        if (i == 1) {
            content = content + logo_1;
        } else if (i == 2) {
            content = content + logo_2;
        }
    }

    /**
     * setEnd
     */
    public void setEnd() {
        content = content + end;
    }

    /**
     * setBegin
     */
    public void setBegin() {
        content = content + begin;
    }

    public void setText2Img(String str, int x, int y, int size, int style) {
        content += Font2ZplGF.getFontHex(bSubstring(str, 30), x, y, size, style);
    }

    /**
     * 构造方法
     *
     * @param printerURI 打印机路径
     */
    public ZplPrinter(String printerURI) {
        if (printerURI != null) {
            //打印机完整路径
            //初始化打印机
            PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
            if (services != null && services.length > 0) {
                for (PrintService service : services) {
                    if (printerURI.equals(service.getName())) {
                        printService = service;
                        break;
                    }
                }
            }
            if (printService == null) {
                //循环出所有的打印机
                if (services != null && services.length > 0) {
                    System.out.println("可用的打印机列表：");
                    for (PrintService service : services) {
                        System.out.println("[" + service.getName() + "]");
                    }
                }
                throw new RuntimeException("没有找到打印机：[" + printerURI + "]");
            } else {
                System.out.println("找到打印机：[" + printerURI + "]");
                System.out.println("打印机名称：[" + printService.getAttribute(PrinterName.class).getValue() + "]");
            }
        }
    }

    /**
     * 获取完整的ZPL
     *
     * @return
     */
    public String getZpl() {
        return content;
    }

    /**
     * 重置ZPL指令，当需要打印多张纸的时候需要调用。
     */
    public void resetZpl() {
        content = "";
    }

    /**
     * 打印
     *
     * @param zpl      完整的ZPL
     * @param quantity
     */
    public boolean print(String zpl, Integer quantity) {
        if (printService == null) {
            return false;
        }
        byte[] by = zpl.getBytes();
        // 直连打印
        DocPrintJob job = printService.createPrintJob();
        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
        Doc doc = new SimpleDoc(by, flavor, null);
        try {
            job.print(doc, null);
            System.out.println("已打印\n" + zpl);
            return true;
        } catch (PrintException e) {
            e.printStackTrace();
            return false;
        }
        // 复杂打印
        /*PrinterJob job = PrinterJob.getPrinterJob();
        PDDocument document = null;
        try {
            document = PDDocument.load(by);
            job.setPrintService(printService);
            // 设置纸张属性
            Paper paper = new Paper();
            double paperHigh = 250.56;
            double paperWidth = 100.8;
            Double marginLeft = 0.0;
            Double marginRight = 0.0;
            Double marginTop = 0.0;
            Double marginBottom = 0.0;
            // 设置纸张宽高 以1/72英寸为单位 纸张宽250mm 则paperWidth为 25cm/2.54*72
            paper.setSize(paperWidth, paperHigh);
            // 设置可成像区域
            paper.setImageableArea(marginLeft, marginTop, paperWidth - (marginLeft + marginRight), paperHigh - (marginTop + marginBottom));

            // 设置纸张及缩放
            PDFPrintable pdfPrintable = new PDFPrintable(document, Scaling.ACTUAL_SIZE);
            // 设置多页打印
            Book book = new Book();
            PageFormat pageFormat = new PageFormat();
            // 设置打印方向 横向
            pageFormat.setOrientation(PageFormat.LANDSCAPE);
            // 设置纸张
            pageFormat.setPaper(paper);
            book.append(pdfPrintable, pageFormat, document.getNumberOfPages());
            job.setPageable(book);
            // 设置打印份数
            job.setCopies(quantity);
            // 添加打印属性
            HashPrintRequestAttributeSet pars = new HashPrintRequestAttributeSet();
            // 设置单双页
            pars.add(Sides.DUPLEX);
            job.print(pars);
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            if (document != null) {
                try {
                    document.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }*/
    }

    /**
     * 条形码
     *
     * @param assetNum
     * @param h
     */
    public void setBar(String assetNum, int h) {
        if (assetNum.length() == 13) {
            assetNum = assetNum.replace(String.valueOf(assetNum.charAt(12)), ">6" + assetNum.charAt(12));
        }
        content = content + BAR.replace(NUM, assetNum).replace(H, String.valueOf(h));
    }

    /**
     * 按字节截取
     *
     * @param s
     * @param length
     * @return
     * @throws Exception
     */
    public static String bSubstring(String s, int length) {
        if (s == null) {
            return "";
        }
        try {
            byte[] bytes = s.getBytes("Unicode");
            int n = 0; // 表示当前的字节数
            int i = 2; // 要截取的字节数，从第3个字节开始
            for (; i < bytes.length && n < length; i++) {
                // 奇数位置，如3、5、7等，为UCS2编码中两个字节的第二个字节
                if (i % 2 == 1) {
                    n++; // 在UCS2第二个字节时n加1
                } else {
                    // 当UCS2编码的第一个字节不等于0时，该UCS2字符为汉字，一个汉字算两个字节
                    if (bytes[i] != 0) {
                        n++;
                    }
                }
            }
            // 如果i为奇数时，处理成偶数
            if (i % 2 == 1) {
                // 该UCS2字符是汉字时，去掉这个截一半的汉字
                if (bytes[i - 1] != 0)
                    i = i - 1;
                    // 该UCS2字符是字母或数字，则保留该字符
                else
                    i = i + 1;
            }
            String newStr = new String(bytes, 0, i, "Unicode");
            StringBuilder newS = new StringBuilder(newStr);
//            if (getWordCount(newStr) < length) {
//                for (int j = 0; j < length - getWordCount(newStr); j++) {
//                    newS.append(" ");
//                }
//            }
            return newS.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 二维码
     *
     * @param assetNum
     * @param x5
     * @param x6
     */
    public void setQr(String assetNum, int x5, int x6) {
        content = content + QR.replace(NUM, assetNum).replace(H1, String.valueOf(x5)).replace(H2, String.valueOf(x6)).replace(NUM, assetNum);
    }

    /**
     * 获取字符串的字节长度
     *
     * @param s
     * @return
     */
    public static int getWordCount(String s) {
        int length = 0;
        for (int i = 0; i < s.length(); i++) {
            int ascii = Character.codePointAt(s, i);
            if (ascii >= 0 && ascii <= 255)
                length++;
            else
                length += 2;

        }
        return length;
    }
}