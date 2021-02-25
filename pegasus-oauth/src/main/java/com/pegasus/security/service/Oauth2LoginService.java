package com.pegasus.security.service;

import com.pegasus.security.dto.AuthenticationResult;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by enHui.Chen on 2020/9/14.
 */
public interface Oauth2LoginService {
    AuthenticationResult getOauth2AccessToken(HttpServletRequest request);

}
