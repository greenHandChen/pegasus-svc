package com.pegasus.security.service;

import com.pegasus.security.custom.authentication.oauth.UsernameNoPasswordClientAuthenticationDetails;
import com.pegasus.security.custom.authentication.provider.dao.UsernameNoPasswordAuthenticationToken;
import com.pegasus.security.dto.AuthenticationResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by enHui.Chen on 2020/9/14.
 */
@Slf4j
public class LoginNoPasswordService extends LoginService {
    private final AuthenticationManager authenticationManager;
    private final ClientDetailsService clientDetailsService;

    public LoginNoPasswordService(TokenGranter tokenGranter, ClientDetailsService clientDetailsService,
                                  OAuth2RequestFactory oAuth2RequestFactory, AuthenticationManager authenticationManager) {
        super(tokenGranter, clientDetailsService, oAuth2RequestFactory);
        this.clientDetailsService = clientDetailsService;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResult getOauth2AccessToken(HttpServletRequest request) {
        // 0.构造认证信息
        Authentication authentication = buildAuthentication(request);
        // 1.对用户名进行认证
        this.attemptAuthentication(authentication);
        // 2.对客户端信息进行认证,返回token
        OAuth2AccessToken oauth2AccessToken = this.createOauth2AccessToken(request);
        // 3.构造自定义认证结果
        AuthenticationResult authenticationResult = new AuthenticationResult();
        authenticationResult.authenticateSuccess().setOauth2AccessToken(oauth2AccessToken);
        return authenticationResult;
    }

    @Override
    public Authentication buildAuthentication(HttpServletRequest request) {
        String username = request.getParameter("username");

        AbstractAuthenticationToken userAuth = new UsernameNoPasswordAuthenticationToken(username, "");

        userAuth.setDetails(new UsernameNoPasswordClientAuthenticationDetails(request));
        return null;
    }


    @Override
    public void attemptAuthentication(Authentication authentication) {
        Authentication result = null;
        try {
            result = this.authenticationManager.authenticate(authentication);
        } catch (Exception e) {
            log.error("auth failed:", e);
        }

        SecurityContextHolder.getContext().setAuthentication(result);
    }

    @Override
    public OAuth2AccessToken createOauth2AccessToken(HttpServletRequest request) {
        String clientId = request.getParameter("client_id");

        ClientDetails authenticatedClient = clientDetailsService.loadClientByClientId(clientId);

        Map<String, String> parameters = this.extractParameters(request);
        TokenRequest tokenRequest = getoAuth2RequestFactory().createTokenRequest(parameters, authenticatedClient);

        return getTokenGranter().grant(tokenRequest.getGrantType(), tokenRequest);
    }
}
