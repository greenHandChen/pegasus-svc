package com.gh.pegasus.support.filter;

import com.gh.pegasus.config.GatewayHelperProperties;
import com.gh.pegasus.support.GatewayContext;
import com.gh.pegasus.support.GatewayFilterHelperChain;
import com.gh.pegasus.support.GatewayHelperFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

/**
 * Created by enHui.Chen on 2020/10/20.
 */
@Slf4j
@Component
public class PermissionCheckGatewayFilter implements GatewayHelperFilter {
    private final AntPathMatcher matcher = new AntPathMatcher();
    @Autowired
    private GatewayHelperProperties gatewayHelperProperties;

    @Override
    public int filterOrder() {
        return Integer.MIN_VALUE;
    }

    @Override
    public void doFilter(GatewayContext gatewayContext, GatewayFilterHelperChain.InnerGatewayFilterHelperChain chain) {
        String requestPath = gatewayContext.getRequestPath();

        boolean skipResult = gatewayHelperProperties
                .getSkipPaths()
                .stream()
                .anyMatch(skipPath -> matcher.match(skipPath, requestPath));

        if (skipResult) {
            return;
        }

        chain.doFilter(gatewayContext, chain);
    }
}
