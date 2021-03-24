package com.pegasus.security.config;

import com.pegasus.security.CustomAuthenticationSuccessHandler;
import com.pegasus.security.custom.authentication.provider.NoUserDetailsAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Created by enHui.Chen on 2019/9/3.
 */
@Configuration
@EnableConfigurationProperties({PeSecurityProperties.class})
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    @Qualifier("cuxUserService")
    private UserDetailsService userDetailsService;
    @Autowired(required = false)
    @Qualifier("noUserDetailsAuthenticationProvider")
    private NoUserDetailsAuthenticationProvider noUserDetailsAuthenticationProvider;
    @Autowired
    private CustomAuthenticationSuccessHandler authenticationSuccessHandler;
    @Autowired
    private CasAuthenticationEntryPoint casAuthenticationEntryPoint;
    @Autowired
    private PeSecurityProperties peSecurityProperties;

    public static final String[] PERMIT_ALL_PATH = {"/login", "/login/**", "/jquery/**", "/bootstrap/**",
            "/img/**", "/layui/**", "/oauth/token", "/actuator/**"};

    @Override
    public void configure(WebSecurity web) throws Exception {
//        web.ignoring()
//                .antMatchers("/scripts/**/*.{js,html}");
    }

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        final String loginPage = peSecurityProperties.getLogin().getPage();
        final String logoutPage = peSecurityProperties.getLogin().getPage();

        httpSecurity
                .authorizeRequests()
                .antMatchers(PERMIT_ALL_PATH).permitAll()
                .and()
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin().loginPage(loginPage).successHandler(this.authenticationSuccessHandler)
                .and()
                .logout().logoutUrl(logoutPage).logoutSuccessUrl(loginPage) // 配置注销路径，注销成功后跳转到/login路径
                .and()
                .csrf().disable();
        // 设置自定义provider
        httpSecurity.authenticationProvider(this.noUserDetailsAuthenticationProvider);

        // 设置sso
        if (peSecurityProperties.getSso().isEnabled()) {
            httpSecurity.exceptionHandling().authenticationEntryPoint(casAuthenticationEntryPoint);
        }

    }

    /**
     * @Author: enHui.Chen
     * @Description: 使用数据库中的用户进行认证
     * @Data 2018/4/10
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(this.userDetailsService);
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    @Bean(name = "authenticationManagerBean")
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
