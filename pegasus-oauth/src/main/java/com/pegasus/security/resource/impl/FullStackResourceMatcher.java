package com.pegasus.security.resource.impl;

import com.pegasus.security.resource.CuxRequestMatcher;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by enHui.Chen on 2019/9/4.
 */
@Component
public class FullStackResourceMatcher implements CuxRequestMatcher {
    private List<AntPathRequestMatcher> antPathRequestMatchers = new ArrayList<>();

    @Override
    public boolean matches(HttpServletRequest request) {
        String accessToken = request.getParameter("access_token");
        String authorization = request.getHeader("Authorization");
        boolean tokenMatch = StringUtils.isNotEmpty(accessToken) ||
                (StringUtils.isNotEmpty(authorization) && authorization.startsWith("bearer"));

        // 携带token需要认证
        if (tokenMatch) {
            return true;
        }

        for (AntPathRequestMatcher antPathRequestMatcher : getAntPathRequestMatchers()) {
            if (antPathRequestMatcher.matches(request)) {
                return true;
            }
        }
        return false;
    }

    private List<AntPathRequestMatcher> getAntPathRequestMatchers() {
        return antPathRequestMatchers;
    }

    public FullStackResourceMatcher setAntPathRequestMatchers(AntPathRequestMatcher antPathRequestMatchers) {
        this.antPathRequestMatchers.add(antPathRequestMatchers);
        return this;
    }
}
