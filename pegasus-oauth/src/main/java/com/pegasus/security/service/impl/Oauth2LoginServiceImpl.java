package com.pegasus.security.service.impl;

import com.pegasus.security.dto.AuthenticationResult;
import com.pegasus.security.service.LoginNoPasswordService;
import com.pegasus.security.service.Oauth2LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by enHui.Chen on 2020/9/14.
 */
@Slf4j
@Service
public class Oauth2LoginServiceImpl implements Oauth2LoginService {
    @Autowired
    private LoginNoPasswordService loginNoPasswordService;

    @Override
    public AuthenticationResult getOauth2AccessToken(HttpServletRequest request) {
        return loginNoPasswordService.getOauth2AccessToken(request);
    }
}
