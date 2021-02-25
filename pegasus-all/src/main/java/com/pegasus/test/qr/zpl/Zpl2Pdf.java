package com.pegasus.test.qr.zpl;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * zpl指令转pdf
 */
public class Zpl2Pdf {
    public static void main(String[] args) {
        String url = "http://api.labelary.com/v1/printers/12dpmm/labels/3.42x1.37/0/";
        send(url, "CT~~CD,~CC^~CT~^XA~TA000~JSN^LT0^MNW^MTT^PON^PMN^LH0,0^JMA^PR3,3~SD28^JUS^LRN^CI0^XZ^XA^MMT^PW695^LL0280^LS0^FT$25,150^BQN,4,5^FH\\^FDLA,71503888^FS^PQ1,0,1,Y^XZ");
    }


    public static void send(String url0, String param) {
        URL url;
        HttpURLConnection connection;
        try {
            //创建连接
            url = new URL(url0);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setConnectTimeout(30000);
            connection.setReadTimeout(30000);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Accept", "application/pdf");
            connection.setRequestMethod("POST");// 提交模式

            connection.getOutputStream().write(param.getBytes());


            //读取响应
            BufferedInputStream bis = new BufferedInputStream(connection.getInputStream());
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File("C:\\Users\\Administrator\\Desktop\\a.pdf")));
            byte[] in = new byte[1024];
            int rst;
            while ((rst = bis.read(in)) > -1) {
                bos.write(in, 0, rst);
            }

            bos.flush();
            bos.close();
            bis.close();
        } catch (IOException e) {
            throw new RuntimeException(e.toString());
        }
    }


}