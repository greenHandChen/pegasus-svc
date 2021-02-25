package com.gh.pegasus.support.filter;

import com.gh.pegasus.support.GatewayContext;
import com.gh.pegasus.support.GatewayFilterHelperChain;
import com.gh.pegasus.support.GatewayHelperFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.Signer;
import org.springframework.stereotype.Component;

/**
 * Created by enHui.Chen on 2020/10/19.
 */
@Slf4j
@Component
public class AuthenticationGatewayFilter implements GatewayHelperFilter {
    private static final String JWT_HEADER_NAME = "jwt_token";
    private final Signer signer;

    public AuthenticationGatewayFilter(Signer signer) {
        this.signer = signer;
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public void doFilter(GatewayContext gatewayContext, GatewayFilterHelperChain.InnerGatewayFilterHelperChain chain) {
        gatewayContext.addHeader(JWT_HEADER_NAME, gatewayContext.getJwtToken(signer));
        chain.doFilter(gatewayContext, chain);
    }
}
