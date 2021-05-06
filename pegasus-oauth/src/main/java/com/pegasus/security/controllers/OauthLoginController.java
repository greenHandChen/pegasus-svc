package com.pegasus.security.controllers;

import com.pegasus.security.dto.AuthenticationResult;
import com.pegasus.security.service.Oauth2LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

/**
 * Created by enHui.Chen on 2020/9/14.
 */
@RestController
@RequestMapping("/oauth")
public class OauthLoginController {
    @Autowired
    private Oauth2LoginService oauthLoginService;

    @PostMapping("/no-password")
    public ResponseEntity<AuthenticationResult> cuxAuth(HttpServletRequest request) {
        return ResponseEntity.ok(oauthLoginService.getOauth2AccessToken(request));
    }

}
