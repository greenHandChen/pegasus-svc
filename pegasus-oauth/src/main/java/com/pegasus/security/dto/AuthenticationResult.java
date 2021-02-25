package com.pegasus.security.dto;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

/**
 * Created by enHui.Chen on 2020/9/14.
 */
@Data
public class AuthenticationResult {
    private boolean success;
    private String code;
    private String message;
    private String accessToken;
    private String refreshToken;
    private Integer expiresIn;
    private String scope;
    private String redirectUrl;
    private Long userId;

    public AuthenticationResult authenticateSuccess() {
        this.success = true;
        this.code = "login.success";
        this.message = "Login Success";
        return this;
    }

    public AuthenticationResult authenticateFail(String code, String message) {
        this.success = false;
        this.code = code;
        this.message = message;
        return this;
    }

    public AuthenticationResult setOauth2AccessToken(OAuth2AccessToken accessToken) {
        this.accessToken = accessToken.getValue();
        this.expiresIn = accessToken.getExpiresIn();
        this.scope = StringUtils.join(accessToken.getScope(), ",");
        if (accessToken.getRefreshToken() != null) {
            this.refreshToken = accessToken.getRefreshToken().getValue();
        }
        return this;
    }
}
