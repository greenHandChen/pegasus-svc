package com.gh.pegasus.support.filter;

import com.gh.pegasus.support.GatewayContext;
import com.gh.pegasus.support.GatewayFilterHelperChain;
import com.gh.pegasus.support.GatewayHelperFilter;
import com.gh.pegasus.support.dto.CuxUserDetails;
import com.gh.pegasus.support.feign.POauthFeignClient;
import com.gh.pegasus.support.service.GetUserDetailsService;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Map;

/**
 * Created by enHui.Chen on 2020/10/26.
 */
@Component
public class GetUserDetailsFilter implements GatewayHelperFilter {
    private final GetUserDetailsService getUserDetailsService;
    private final POauthFeignClient pOauthFeignClient;

    public GetUserDetailsFilter(GetUserDetailsService getUserDetailsService, POauthFeignClient pOauthFeignClient) {
        this.getUserDetailsService = getUserDetailsService;
        this.pOauthFeignClient = pOauthFeignClient;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public void doFilter(GatewayContext gatewayContext, GatewayFilterHelperChain.InnerGatewayFilterHelperChain chain) {
        // 获取当前用户信息
        Map<String, Object> user = pOauthFeignClient.user(new Principal() {
            @Override
            public String getName() {
                return null;
            }
        }, "Bearer" + gatewayContext.getAccessToken());

        // 解析用户信息
        CuxUserDetails cuxUserDetails = getUserDetailsService.buildUserDetails(user);
        gatewayContext.setCuxUserDetails(cuxUserDetails);
        chain.doFilter(gatewayContext, chain);
    }
}
