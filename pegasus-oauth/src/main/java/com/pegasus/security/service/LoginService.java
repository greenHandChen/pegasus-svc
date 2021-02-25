package com.pegasus.security.service;

import com.pegasus.security.dto.AuthenticationResult;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by enHui.Chen on 2020/9/14.
 */
public abstract class LoginService implements InitializingBean {
    private TokenGranter tokenGranter;

    private ClientDetailsService clientDetailsService;

    private OAuth2RequestFactory oAuth2RequestFactory;

    private OAuth2RequestFactory defaultOAuth2RequestFactory;

    public LoginService() {
    }

    public LoginService(TokenGranter tokenGranter, ClientDetailsService clientDetailsService,
                        OAuth2RequestFactory oAuth2RequestFactory) {
        this.tokenGranter = tokenGranter;
        this.clientDetailsService = clientDetailsService;
        this.oAuth2RequestFactory = oAuth2RequestFactory;

    }

    public void afterPropertiesSet() throws Exception {
        Assert.state(tokenGranter != null, "TokenGranter must be provided");
        Assert.state(clientDetailsService != null, "ClientDetailsService must be provided");
        defaultOAuth2RequestFactory = new DefaultOAuth2RequestFactory(getClientDetailsService());
        if (oAuth2RequestFactory == null) {
            oAuth2RequestFactory = defaultOAuth2RequestFactory;
        }
    }

    /**
     * @Author: enHui.Chen
     * @Description: 获取token
     * @Data 2020/9/14
     */
    public abstract AuthenticationResult getOauth2AccessToken(HttpServletRequest request);

    /**
     * @Author: enHui.Chen
     * @Description: 构造认证信息
     * @Data 2020/9/14
     */
    public abstract Authentication buildAuthentication(HttpServletRequest request);

    /**
     * @Author: enHui.Chen
     * @Description: 对用户名进行认证
     * @Data 2020/9/14
     */
    public abstract void attemptAuthentication(Authentication authentication);


    /**
     * @Author: enHui.Chen
     * @Description: 对客户端信息进行认证, 返回token
     * @Data 2020/9/14
     */
    public abstract OAuth2AccessToken createOauth2AccessToken(HttpServletRequest request);

    protected Map<String, String> extractParameters(HttpServletRequest request) {
        Map<String, String> parameters = new HashMap(8);
        request.getParameterMap().forEach((key, vals) -> {
            parameters.put(key, vals != null && vals.length > 0 ? vals[0] : "");
        });
        return parameters;
    }

    public TokenGranter getTokenGranter() {
        return tokenGranter;
    }

    public void setTokenGranter(TokenGranter tokenGranter) {
        this.tokenGranter = tokenGranter;
    }

    public ClientDetailsService getClientDetailsService() {
        return clientDetailsService;
    }

    public void setClientDetailsService(ClientDetailsService clientDetailsService) {
        this.clientDetailsService = clientDetailsService;
    }

    public OAuth2RequestFactory getoAuth2RequestFactory() {
        return oAuth2RequestFactory;
    }

    public void setoAuth2RequestFactory(OAuth2RequestFactory oAuth2RequestFactory) {
        this.oAuth2RequestFactory = oAuth2RequestFactory;
    }

    public OAuth2RequestFactory getDefaultOAuth2RequestFactory() {
        return defaultOAuth2RequestFactory;
    }

    public void setDefaultOAuth2RequestFactory(OAuth2RequestFactory defaultOAuth2RequestFactory) {
        this.defaultOAuth2RequestFactory = defaultOAuth2RequestFactory;
    }
}
