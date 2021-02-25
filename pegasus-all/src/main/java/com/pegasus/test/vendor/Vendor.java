package com.pegasus.test.vendor;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import sun.misc.BASE64Encoder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * Created by enHui.Chen on 2019/11/28.
 */
public class Vendor {
    public void orc() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
//        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
//        HttpEntity<MultiValueMap> requestEntity = new HttpEntity<>(requestBody);
//        requestBody.add("grant_type", "client_credentials");
//        requestBody.add("client_id", "nTsZ04kjpZoucmWm8G1vAADk");
//        requestBody.add("client_secret", "P7NSY6nebT71GDv1xBvTfQRm8BGdgp6r");
//        String url = "https://aip.baidubce.com/oauth/2.0/token";
//        Map s = restTemplate.postForObject(url, requestBody, Map.class);
//        String url = "https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials&client_id=nTsZ04kjpZoucmWm8G1vAADk&client_secret=P7NSY6nebT71GDv1xBvTfQRm8BGdgp6r";
//        restTemplate.getForObject(url,String.class);
        FileInputStream fileInputStream = new FileInputStream(new File("C:\\Users\\Administrator\\Desktop\\a.BMP"));
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bytes = new byte[1024];
        int n = 0;
        while ((n = fileInputStream.read(bytes)) > 0) {
            byteArrayOutputStream.write(bytes, 0, n);
        }
        fileInputStream.close();

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("image", new BASE64Encoder().encode(byteArrayOutputStream.toByteArray()));
        requestBody.add("probability", "true");
        HttpHeaders requestHeader = new HttpHeaders();
        requestHeader.add("Content-Type", "application/x-www-form-urlencoded");
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(requestBody, requestHeader);
        String s = restTemplate.postForObject("https://aip.baidubce.com/rest/2.0/ocr/v1/accurate_basic?access_token=24.6cb6da36de291d32339f13afc4ff5fdf.2592000.1576907324.282335-17821869", httpEntity, String.class);
        System.out.println(s);
    }
}
