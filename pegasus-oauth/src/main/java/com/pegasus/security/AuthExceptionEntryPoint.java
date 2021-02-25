package com.pegasus.security;

import com.pegasus.security.utils.SecurityUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: enHui.Chen
 * @Description: 401未授权处理终端(token无效 、 超时)
 * @Data 2019/9/4
 */
@Component
public class AuthExceptionEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        SecurityUtil.setCorsHeader(request, response);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }
}
