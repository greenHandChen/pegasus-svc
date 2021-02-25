package com.pegasus.security.config;

import com.pegasus.security.custom.authentication.oauth.ResourceOwnerNoPasswordTokenGranter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenGranter;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeTokenGranter;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.implicit.ImplicitTokenGranter;
import org.springframework.security.oauth2.provider.password.ResourceOwnerPasswordTokenGranter;
import org.springframework.security.oauth2.provider.refresh.RefreshTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by enHui.Chen on 2019/9/3.
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {
    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private TokenStore tokenStore;
    @Autowired
    private ClientDetailsService clientDetailsService;
    @Autowired
    @Qualifier("cuxUserService")
    private UserDetailsService userDetailsService;

//    @Autowired
//    @Qualifier("jwtTokenStore")
//    private TokenStore jwtTokenStore;
//    @Autowired
//    private JwtAccessTokenConverter jwtAccessTokenConverter;

    /**
     * @Author: enHui.Chen
     * @Description: 配置客户端详情
     * @Data 2020/9/24
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clientDetailsServiceConfigurer) throws Exception {
        // 数据库配置配置客户端
        clientDetailsServiceConfigurer.jdbc(dataSource);
    }

    /**
     * @Author: enHui.Chen
     * @Description: 配置令牌访问端点
     * @Data 2020/9/24
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpointsConfigurer) {
        endpointsConfigurer
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
                .tokenStore(tokenStore)// 自定义令牌策略
                .userDetailsService(userDetailsService)
                .authenticationManager(authenticationManager)// 开启密码授权模式(password)
                .authorizationCodeServices(new JdbcAuthorizationCodeServices(dataSource))// 开启授权码模式(authorization_code)
                .tokenGranter(tokenGranter(endpointsConfigurer))// 自定义grant授权策略

                // 设置JWT增强
//                .tokenEnhancer(jwtAccessTokenConverter)

                .setClientDetailsService(clientDetailsService);
    }

    /**
     * @Author: enHui.Chen
     * @Description: 配置安全策略
     * @Data 2020/9/24
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
        oauthServer
                .tokenKeyAccess("permitAll()")// /oauth/token_key公开可访问
                .checkTokenAccess("permitAll()")// /oauth/check_token公开可访问
                .allowFormAuthenticationForClients();// 允许表单认证
    }


    /**
     * @Author: enHui.Chen
     * @Description: 创建授权器
     * @Data 2020/9/14
     */
    private TokenGranter tokenGranter(AuthorizationServerEndpointsConfigurer endpoints) {
        return new TokenGranter() {
            private CompositeTokenGranter delegate;

            public OAuth2AccessToken grant(String grantType, TokenRequest tokenRequest) {
                if (this.delegate == null) {
                    this.delegate = new CompositeTokenGranter(getDefaultTokenGranters(endpoints));
                }

                return this.delegate.grant(grantType, tokenRequest);
            }
        };
    }

    /**
     * @Author: enHui.Chen
     * @Description: 自定义授权器
     * @Data 2020/9/14
     */
    private List<TokenGranter> getDefaultTokenGranters(AuthorizationServerEndpointsConfigurer endpoints) {
        ClientDetailsService clientDetails = endpoints.getClientDetailsService();
        AuthorizationServerTokenServices tokenServices = endpoints.getTokenServices();
        AuthorizationCodeServices authorizationCodeServices = endpoints.getAuthorizationCodeServices();
        OAuth2RequestFactory requestFactory = endpoints.getOAuth2RequestFactory();

        List<TokenGranter> tokenGranters = new ArrayList<>();
        tokenGranters.add(new AuthorizationCodeTokenGranter(tokenServices, authorizationCodeServices, clientDetails, requestFactory));
        tokenGranters.add(new RefreshTokenGranter(tokenServices, clientDetails, requestFactory));
        tokenGranters.add(new ImplicitTokenGranter(tokenServices, clientDetails, requestFactory));
        ClientCredentialsTokenGranter credentialsTokenGranter = new ClientCredentialsTokenGranter(tokenServices, clientDetails, requestFactory);
        credentialsTokenGranter.setAllowRefresh(true);
        tokenGranters.add(credentialsTokenGranter);
        if (this.authenticationManager != null) {
            tokenGranters.add(new ResourceOwnerPasswordTokenGranter(this.authenticationManager, tokenServices, clientDetails, requestFactory));
            // 自定义授权器
            tokenGranters.add(new ResourceOwnerNoPasswordTokenGranter(this.authenticationManager, tokenServices, clientDetails, requestFactory));
        }

        return tokenGranters;
    }

}

