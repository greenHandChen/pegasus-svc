package com.pegasus.security.oalogin;

import com.pegasus.security.config.PeSecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by enHui.Chen on 2021/6/8.
 */
@Component
public class OALoginAuthenticationSuccessRedirectHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Autowired
    private PeSecurityProperties peSecurityProperties;

    @PostConstruct
    private void init(){
        super.setDefaultTargetUrl(peSecurityProperties.getDefaultUrl());
        super.setTargetUrlParameter("targetUrl");
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        handle(request, response, authentication);
        clearAuthenticationAttributes(request);
    }
}
