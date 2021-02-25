package com.pegasus.security.config;

import com.pegasus.security.filter.CustomJwtTokenConverter;
import com.pegasus.security.filter.JwtAuthenticationFilter;
import com.pegasus.security.filter.JwtTokenExtractor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import javax.servlet.Filter;

import static javax.servlet.DispatcherType.REQUEST;

/**
 * Created by enHui.Chen on 2020/10/23.
 */
@Configuration
public class JwtTokenConfiguration {
    private static final String SIGNING_KEY = "pegasus";
    private static final String RESOURCE_ID = "DEFAULT";


    @Bean
    public JwtTokenExtractor jwtTokenExtractor() {
        return new JwtTokenExtractor();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(TokenExtractor jwtTokenExtractor, ResourceServerTokenServices jwtTokenService) {
        return new JwtAuthenticationFilter(jwtTokenExtractor, jwtTokenService, RESOURCE_ID);
    }

    @Bean
    public FilterRegistrationBean<Filter> filterRegistrationBean(JwtAuthenticationFilter jwtAuthenticationFilter) {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(jwtAuthenticationFilter);
        filterRegistrationBean.addUrlPatterns("/v1/*");
        filterRegistrationBean.setName("jwtAuthenticationFilter");
        filterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        filterRegistrationBean.setDispatcherTypes(REQUEST);
        return filterRegistrationBean;
    }


    /**
     * @Author: enHui.Chen
     * @Description: token转换jwtToken
     * @Data 2020/10/22
     */
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setAccessTokenConverter(new CustomJwtTokenConverter());
        converter.setSigningKey(SIGNING_KEY); //对称秘钥，资源服务器使用该秘钥来验证
        return converter;
    }

    /**
     * @Author: enHui.Chen
     * @Description: jwtToken存储
     * @Data 2020/10/22
     */
    @Bean
    public TokenStore jwtTokenStore(JwtAccessTokenConverter accessTokenConverter) {
        return new JwtTokenStore(accessTokenConverter);
    }

    /**
     * @Author: enHui.Chen
     * @Description: 自定义jwt令牌资源服务
     * @Data 2020/10/22
     */
    @Bean
    public ResourceServerTokenServices jwtTokenService(TokenStore tokenStore) {
        DefaultTokenServices service = new DefaultTokenServices();
        service.setTokenStore(tokenStore);
        return service;
    }

}
