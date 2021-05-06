package com.pegasus.security.sso.cas.config;

import com.pegasus.security.config.PeSecurityProperties;
import com.pegasus.security.sso.cas.filter.CasAuthenticationFilter;
import com.pegasus.security.sso.cas.processor.CasSignInPostProcessor;
import com.pegasus.security.sso.cas.processor.CasSignOutPostProcessor;
import com.pegasus.security.sso.cas.provider.CasUserDetailService;
import com.pegasus.security.sso.cas.provider.CasUserDetailsAuthenticationProvider;
import org.jasig.cas.client.proxy.ProxyGrantingTicketStorage;
import org.jasig.cas.client.proxy.ProxyGrantingTicketStorageImpl;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.validation.Cas20ServiceTicketValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.cas.web.authentication.ServiceAuthenticationDetailsSource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import java.util.Collections;
import java.util.List;

/**
 * Created by enHui.Chen on 2021/3/1.
 */
@Order(Integer.MAX_VALUE - 1)
@Configuration
@ConditionalOnProperty(value = PeSecurityProperties.PREFIX + ".sso.enabled", havingValue = "true")
public class CasSsoSecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private PeSecurityProperties peSecurityProperties;
    @Autowired
    private CasUserDetailService casUserDetailService;
    @Autowired
    private List<CasSignInPostProcessor> casSignInPostProcessors;
    @Autowired
    private List<CasSignOutPostProcessor> casSignOutPostProcessors;

    /**
     * @param httpSecurity
     * @return void
     * @throws
     * @desc 设置拦截/login/cas请求的请求链
     * @date 2021-03-01 23:25:34
     */
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .antMatcher("/login/cas/**")
                .authorizeRequests()
                .anyRequest().permitAll()
                .and()
                .csrf().disable()
                .addFilterBefore(casAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(casLogoutFilter(), LogoutFilter.class);
//                .addFilterBefore(casSingleSignOutFilter(), CasAuthenticationFilter.class);
    }

    /**
     * 指定service相关信息
     */
    @Bean
    @ConditionalOnMissingBean({ServiceProperties.class})
    public ServiceProperties serviceProperties() {
        ServiceProperties serviceProperties = new ServiceProperties();
        serviceProperties.setService(peSecurityProperties.getSso().getCasService() + peSecurityProperties.getSso().getCasServiceLogin());
        serviceProperties.setAuthenticateAllArtifacts(true);
        return serviceProperties;
    }

    /**
     * 认证的入口
     */
    @Bean
    public CasAuthenticationEntryPoint casAuthenticationEntryPoint() {
        CasAuthenticationEntryPoint casAuthenticationEntryPoint = new CasAuthenticationEntryPoint();
        casAuthenticationEntryPoint.setLoginUrl(peSecurityProperties.getSso().getServerLogin());
        casAuthenticationEntryPoint.setServiceProperties(serviceProperties());
        return casAuthenticationEntryPoint;
    }


    /**
     * 自定义CAS认证过滤器
     */
    @Bean
    public CasAuthenticationFilter casAuthenticationFilter() {
        ProviderManager providerManager = new ProviderManager(Collections.singletonList(casAuthenticationProvider()));

        CasAuthenticationFilter casAuthenticationFilter = new CasAuthenticationFilter();
        casAuthenticationFilter.setAuthenticationManager(providerManager);
        casAuthenticationFilter.setServiceProperties(serviceProperties());
        casAuthenticationFilter.setProxyGrantingTicketStorage(proxyGrantingTicketStorage());
        casAuthenticationFilter.setAuthenticationDetailsSource(new ServiceAuthenticationDetailsSource(serviceProperties()));
        return casAuthenticationFilter;
    }

    @Bean
    @ConditionalOnMissingBean({ProxyGrantingTicketStorage.class})
    public ProxyGrantingTicketStorage proxyGrantingTicketStorage() {
        return new ProxyGrantingTicketStorageImpl();
    }

    /**
     * 自定义cas 认证 Provider
     */
    @Bean
    public CasUserDetailsAuthenticationProvider casAuthenticationProvider() {
        CasUserDetailsAuthenticationProvider casAuthenticationProvider = new CasUserDetailsAuthenticationProvider();
        casAuthenticationProvider.setAuthenticationUserDetailsService(casUserDetailService);
//        casAuthenticationProvider.setUserDetailsService(customUserDetailsService()); //这里只是接口类型，实现的接口不一样，都可以的。
        casAuthenticationProvider.setServiceProperties(serviceProperties());
        casAuthenticationProvider.setTicketValidator(cas20ServiceTicketValidator());
        casAuthenticationProvider.setKey("casAuthenticationProviderKey");
        return casAuthenticationProvider;
    }

    /**
     * cas 认证器用于SERVER认证
     */
    @Bean
    public Cas20ServiceTicketValidator cas20ServiceTicketValidator() {
        return new Cas20ServiceTicketValidator(peSecurityProperties.getSso().getServerUrl());
    }

    /**
     * order小于100(springSecurityFilterChain为100)
     */
    @Bean
    public FilterRegistrationBean singleSignOutFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(this.casSingleSignOutFilter());
        filterRegistrationBean.addUrlPatterns(new String[]{"/*"});
        filterRegistrationBean.setOrder(Integer.MIN_VALUE);
        return filterRegistrationBean;
    }

    /**
     * 单点登出过滤器
     */
//    @Bean
    public SingleSignOutFilter casSingleSignOutFilter() {
        SingleSignOutFilter singleSignOutFilter = new SingleSignOutFilter();
        singleSignOutFilter.setCasServerUrlPrefix(peSecurityProperties.getSso().getServerUrl());
        singleSignOutFilter.setIgnoreInitConfiguration(true);

        singleSignOutFilter.casSignInProcessors(casSignInPostProcessors)
                .casSignOutProcessors(casSignOutPostProcessors);
        return singleSignOutFilter;
    }

    /**
     * 请求单点退出过滤器
     */
    @Bean
    public LogoutFilter casLogoutFilter() {
        LogoutFilter logoutFilter = new LogoutFilter(peSecurityProperties.getSso().getServerLogout(), new SecurityContextLogoutHandler());
        logoutFilter.setFilterProcessesUrl(peSecurityProperties.getSso().getCasServiceLogout());
        return logoutFilter;
    }


}
