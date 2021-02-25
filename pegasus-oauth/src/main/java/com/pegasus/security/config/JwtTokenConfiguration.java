package com.pegasus.security.config;

import com.pegasus.security.custom.CustomRedisTokenStore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.Arrays;

/**
 * Created by enHui.Chen on 2020/10/22.
 */
@Configuration
public class JwtTokenConfiguration {
    private static final String SIGNING_KEY = "DEFAULT";

//    /**
//     * @Author: enHui.Chen
//     * @Description: token转换jwtToken
//     * @Data 2020/10/22
//     */
//    @Bean
//    public JwtAccessTokenConverter accessTokenConverter() {
//        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
//        converter.setSigningKey(SIGNING_KEY); //对称秘钥，资源服务器使用该秘钥来验证
//        return converter;
//    }
//
//    /**
//     * @Author: enHui.Chen
//     * @Description: jwtToken存储
//     * @Data 2020/10/22
//     */
//    @Bean(name = "jwtTokenStore")
//    public TokenStore tokenStore(JwtAccessTokenConverter accessTokenConverter) {
//        return new JwtTokenStore(accessTokenConverter);
//    }
//
//
//    /**
//     * @Author: enHui.Chen
//     * @Description: 自定义jwt令牌服务
//     * @Data 2020/10/22
//     */
//    @Bean(name = "jwtTokenStore")
//    public AuthorizationServerTokenServices tokenService(JwtAccessTokenConverter accessTokenConverter, ClientDetailsService jdbcClientDetailsService, TokenStore tokenStore) {
//        DefaultTokenServices service = new DefaultTokenServices();
//        service.setClientDetailsService(jdbcClientDetailsService);
//        service.setSupportRefreshToken(true);
//        service.setTokenStore(tokenStore);
//
//        // 设置增强
//        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
//        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(accessTokenConverter));
//        service.setTokenEnhancer(tokenEnhancerChain);
//
//        service.setAccessTokenValiditySeconds(7200); // 令牌默认有效期2小时
//        service.setRefreshTokenValiditySeconds(259200); // 刷新令牌默认有效期3天
//        return service;
//
//    }
}
