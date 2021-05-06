package com.pegasus.security;

import com.pegasus.security.config.PeSecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by enHui.Chen on 2021/4/22.
 */
@Component
public class CustomLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {
    @Autowired
    private PeSecurityProperties peSecurityProperties;
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 启用SSO，使用SSO登出地址
        if (peSecurityProperties.getSso().isEnabled()) {
            redirectStrategy.sendRedirect(request, response, peSecurityProperties.getSso().getServerLogout());
        }

        super.handle(request, response, authentication);
    }
}
