package com.pegasus.security.custom.authentication;

import com.pegasus.security.service.LoginNoPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerEndpointsConfiguration;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;

/**
 * Created by enHui.Chen on 2020/9/14.
 */
@Configuration
@AutoConfigureAfter({AuthorizationServerEndpointsConfiguration.class})
public class LoginConfiguration {
    private AuthorizationServerEndpointsConfigurer endpoints = new AuthorizationServerEndpointsConfigurer();

    @Autowired
    private List<AuthorizationServerConfigurer> configurers = Collections.emptyList();
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private AuthorizationServerTokenServices authorizationServerTokenServices;


    @PostConstruct
    public void init() {
        for (AuthorizationServerConfigurer configurer : this.configurers) {
            try {
                configurer.configure(this.endpoints);
            } catch (Exception e) {
                throw new IllegalStateException("Cannot configure endpoints", e);
            }
        }
    }

    @Bean
    public LoginNoPasswordService loginNoPasswordService() {
        return new LoginNoPasswordService(
                this.endpoints.getTokenGranter(),
                this.endpoints.getClientDetailsService(),
                this.endpoints.getOAuth2RequestFactory(),
                authenticationManager);
    }
}
