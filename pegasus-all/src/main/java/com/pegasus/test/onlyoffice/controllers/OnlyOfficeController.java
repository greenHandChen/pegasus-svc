package com.pegasus.test.onlyoffice.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

/**
 * Created by enHui.Chen on 2021/4/22.
 */
@RestController
@RequestMapping("/v1")
public class OnlyOfficeController {

    /**
     * @param request
     * @param response
     * @return
     * @date 2021-04-22 17:59:36
     * @desc onlyoffice服务端回调接口-获取office数据流
     **/
    @GetMapping("/test/preview-office")
    public void testPreviewOffice(HttpServletRequest request, HttpServletResponse response) throws Exception {
        File file = new File("C:\\Users\\Administrator\\Desktop\\安全运维问题案例_集中化集中门户系统_202104.xlsx");
        File aaa = new File("C:\\Users\\Administrator\\Desktop\\bb.docx");
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(aaa));
        byte[] buffer = new byte[1024];
        int readByte = 0;
        while ((readByte = bis.read(buffer)) != -1) {
            response.getOutputStream().write(buffer, 0, readByte);
            bos.write(buffer, 0, readByte);
        }
        bos.flush();
        response.getOutputStream().flush();
        bis.close();
        bos.close();
        response.setContentType("application/octet-stream");
        response.setContentLength((int) file.length());
        response.setHeader("Content-disposition", String.format("attachment; filename=\"%s\"", URLEncoder.encode(file.getName(), "UTF-8")));
    }

}
