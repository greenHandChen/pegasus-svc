package com.pegasus.test.httpproxy;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.Map.Entry;

public class HttpUtils {
    private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    private HttpUtils() {
    }

    private static void initPostHeaders(HttpURLConnection httpURLConnection, String documentServerToken) {
        try {
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            httpURLConnection.setRequestProperty("token", documentServerToken);
        } catch (IOException var3) {
            logger.error("设置请求报头 HTTP Headers异常：{}", var3.getMessage());
        }
    }

    public static void httpUrlConnection() throws IOException {
        String url = "http://eam.yimidida.com/file/hrpt/hrpt01/0/fc2a22da0d0d456885375f016c435f95%40HALM-BAR.rtf";

        InetSocketAddress inetAddress = new InetSocketAddress("10.200.2.3", 38080);
        Proxy proxy = new Proxy(Proxy.Type.HTTP, inetAddress);

        URL downUrl = new URL(url);

        HttpURLConnection conn = (HttpURLConnection) downUrl.openConnection(proxy);
//        conn.setRequestMethod("POST");
        conn.setRequestProperty("User-agent","Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.87 Safari/537.36");
//        initPostHeaders(conn,null);
//        conn.setConnectTimeout(10000);
//        conn.setReadTimeout(10000);
        Map<String, Collection<String>> headers = new LinkedHashMap();
        Iterator var4 = conn.getHeaderFields().entrySet().iterator();

        while (var4.hasNext()) {
            Entry<String, List<String>> field = (Entry) var4.next();
            if (field.getKey() != null) {
                headers.put(field.getKey(), field.getValue());
            }
        }

        Integer length = conn.getContentLength();
        if (length == -1) {
            length = null;
        }

        int status = conn.getResponseCode();
        InputStream stream;
        if (status >= 400) {
            stream = conn.getErrorStream();
        } else {
            stream = conn.getInputStream();
        }

        logger.info("java http status:{}", status);
        logger.info("java http header:{}", headers);
        logger.info("java http responseMessage:{}", conn.getResponseMessage());
    }

    public static void httpClientRequest() throws IOException {
        // 设置代理
        HttpHost httpHost = new HttpHost("10.200.2.3", 38080, "http");
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setProxy(httpHost)
                .build();

        CloseableHttpClient closeableHttpClient = HttpClients.custom().build();

        HttpUriRequest httpUriRequest = RequestBuilder
                .create("POST")
                .setUri("http://eam1.yimidida.com/oauth/oauth/token")
                .addParameter("grant_type", "client_credentials")
                .addParameter("scope", "default")
                .addParameter("client_id", "hzero-front-prod")
                .addParameter("client_secret", "secret")
                .setConfig(defaultRequestConfig)
                .build();

        CloseableHttpResponse execute = closeableHttpClient.execute(httpUriRequest);

        HttpEntity entity = execute.getEntity();
        String responseContent = EntityUtils.toString((HttpEntity) entity, StandardCharsets.UTF_8);

        logger.info("测试输出内容:{}", responseContent);
    }
}
