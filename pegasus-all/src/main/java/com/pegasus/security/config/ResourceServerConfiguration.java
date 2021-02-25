//package com.pegasus.security.config;
//
//import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
//import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
//import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
//import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
//import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
//
///**
// * Created by enHui.Chen on 2019/9/4.
// */
//@Configuration
//@EnableResourceServer
//public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
//
//
//    @Override
//    public void configure(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity.authorizeRequests()
//                .antMatchers("/v1/**").authenticated()// 需要oauth2认证的url
//                .and()
//                .csrf().disable();
//    }
//
//    @Override
//    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
//        resources.tokenServices(remoteTokenServices());// 验证令牌的服务
//    }
//
//    /**
//     * @Author: enHui.Chen
//     * @Description: 创建远程令牌校验服务
//     * @Data 2020/9/24
//     */
//    @Bean
//    public ResourceServerTokenServices remoteTokenServices() {
//        RemoteTokenServices remoteTokenServices = new RemoteTokenServices();
//        remoteTokenServices.setCheckTokenEndpointUrl("http://localhost:8080/oauth/oauth/check_token");
//        remoteTokenServices.setClientId("localhost");
//        remoteTokenServices.setClientSecret("localhost");
//        return remoteTokenServices;
//    }
//}
//
