package com.gh.pegasus.autoconfigure;

import com.gh.pegasus.security.CustomAuthenticationHandler;
import org.apereo.cas.authentication.AuthenticationEventExecutionPlan;
import org.apereo.cas.authentication.AuthenticationEventExecutionPlanConfigurer;
import org.apereo.cas.authentication.handler.support.AbstractPreAndPostProcessingAuthenticationHandler;
import org.apereo.cas.authentication.principal.DefaultPrincipalFactory;
import org.apereo.cas.services.ServicesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by enHui.Chen on 2021/2/27.
 */
@Configuration
public class CustomAuthConfigurer implements AuthenticationEventExecutionPlanConfigurer {
    @Autowired
    @Qualifier("servicesManager")
    private ServicesManager servicesManager;

    /**
     * @param
     * @return org.apereo.cas.authentication.handler.support.AbstractPreAndPostProcessingAuthenticationHandler
     * @throws
     * @desc 注入自定义认证器
     * @date 2021-02-26 23:09:01
     */
    @Bean
    public AbstractPreAndPostProcessingAuthenticationHandler cuxAuthHandler() {
        return new CustomAuthenticationHandler("customAuthenticationHandler", servicesManager, new DefaultPrincipalFactory(), 1);
    }

    /**
     * @param plan
     * @return void
     * @throws
     * @desc 注册自定义认证器
     * @date 2021-02-26 23:10:14
     */
    @Override
    public void configureAuthenticationExecutionPlan(final AuthenticationEventExecutionPlan plan) {
        plan.registerAuthenticationHandler(cuxAuthHandler());
    }
}
