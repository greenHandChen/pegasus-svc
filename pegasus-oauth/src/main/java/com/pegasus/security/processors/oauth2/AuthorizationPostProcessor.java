package com.pegasus.security.processors.oauth2;

import com.pegasus.security.processors.SecurityProcessor;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author enhui.chen
 * @desc 生成TOKEN的POST处理器
 * @date 2021-03-16 23:12:06
 */
public interface AuthorizationPostProcessor<T> extends SecurityProcessor<T> {
    T process(HttpServletRequest request, HttpServletResponse response, OAuth2AccessToken accessToken);

}
