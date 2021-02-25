package com.pegasus.security.config;

import com.pegasus.security.AuthExceptionEntryPoint;
import com.pegasus.security.resource.CuxRequestMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Created by enHui.Chen on 2019/9/4.
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
    public static final String RESOURCE_ID = "OAUTH";

    @Autowired
    private CuxRequestMatcher cuxRequestMatcher;
    @Autowired
    private AuthExceptionEntryPoint authExceptionEntryPoint;

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        // 需要放行的preflight路径
        cuxRequestMatcher
                .setAntPathRequestMatchers(new AntPathRequestMatcher("/v1/**"))
                .setAntPathRequestMatchers(new AntPathRequestMatcher("/wfl/**"));

//        httpSecurity.requestMatcher(cuxRequestMatcher)
//                .authorizeRequests()
//                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//                .antMatchers("/v1/activiti/approve-rule/list").permitAll()
//                .antMatchers("/v1/oauth/token/**").permitAll()
//                .antMatchers("/v1/**").authenticated();// 需要oauth2认证的url
        httpSecurity
                .antMatcher("/api/**")
                .authorizeRequests()
                .anyRequest().authenticated();

    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        // 401处理终端
        resources.authenticationEntryPoint(authExceptionEntryPoint);
    }
}

