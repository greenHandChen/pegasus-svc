package com.pegasus.security.config;

import com.pegasus.security.CustomAuthenticationSuccessHandler;
import com.pegasus.security.custom.authentication.provider.NoUserDetailsAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
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
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    @Qualifier("cuxUserService")
    private UserDetailsService userDetailsService;
    @Autowired
    private CustomAuthenticationSuccessHandler authenticationSuccessHandler;
    @Autowired(required = false)
    @Qualifier("noUserDetailsAuthenticationProvider")
    private NoUserDetailsAuthenticationProvider noUserDetailsAuthenticationProvider;

    public static final String[] PERMIT_ALL_PATH = {"/login", "/jquery/**", "/bootstrap/**",
            "/img/**", "/layui/**", "/oauth/token", "/actuator/**"};

    @Override
    public void configure(WebSecurity web) throws Exception {
//        web.ignoring()
//                .antMatchers("/scripts/**/*.{js,html}");
    }

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeRequests()
                .antMatchers(PERMIT_ALL_PATH).permitAll()
                .and()
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin().loginPage("/login").successHandler(authenticationSuccessHandler)
                .and()
                .logout().logoutUrl("/logout").logoutSuccessUrl("/login") // 配置注销路径，注销成功后跳转到/login路径
                .and()
                .csrf().disable();
        // 设置自定义provider
        httpSecurity.authenticationProvider(noUserDetailsAuthenticationProvider);
    }

    /**
     * @Author: enHui.Chen
     * @Description: 使用数据库中的用户进行认证
     * @Data 2018/4/10
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(userDetailsService);
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
