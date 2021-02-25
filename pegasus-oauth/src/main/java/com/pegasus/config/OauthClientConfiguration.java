package com.pegasus.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.common.AuthenticationScheme;

/**
 * Created by enHui.Chen on 2019/11/12.
 */
@Configuration
public class OauthClientConfiguration {
    @Value("${pegasus.security.oauth2.client.access-token-uri:}")
    private String accessTokenURI;
    @Value("${pegasus.security.oauth2.client.client-id:}")
    private String clientID;
    @Value("${pegasus.security.oauth2.client.client-secret:}")
    private String clientSecret;
    @Value("${pegasus.security.oauth2.client.grant-type:}")
    private String grantType;
    @Value("${pegasus.security.oauth2.client.authentication-scheme:}")
    private String authenticationScheme;

//    @Bean
//    public OkHttp3ClientHttpRequestFactory httpRequestFactory() {
//        OkHttp3ClientHttpRequestFactory httpRequestFactory = new OkHttp3ClientHttpRequestFactory();
//        httpRequestFactory.setConnectTimeout(20000);//连接超时
//        httpRequestFactory.setReadTimeout(30000);//数据读取超时时间
//        httpRequestFactory.setWriteTimeout(30000);//连接不够用的等待时间
//        return httpRequestFactory;
//    }

    @Bean
    public OAuth2RestTemplate oAuth2RestTemplate() {
        ClientCredentialsResourceDetails details = new ClientCredentialsResourceDetails();
        details.setAccessTokenUri(accessTokenURI);
        details.setClientId(clientID);
        details.setClientSecret(clientSecret);
        details.setGrantType(grantType);
        // 发送请求时指定access_token是存在于请求体还是URI中
        details.setAuthenticationScheme(AuthenticationScheme.valueOf(authenticationScheme));
        OAuth2RestTemplate oAuth2RestTemplate = new OAuth2RestTemplate(details, new DefaultOAuth2ClientContext());
//        oAuth2RestTemplate.setRequestFactory(httpRequestFactory);
        return oAuth2RestTemplate;
    }
}
