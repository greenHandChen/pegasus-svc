package com.gh.pegasus.config;

import com.gh.pegasus.filter.XForwardedFilter;
import com.gh.pegasus.support.GatewayFilterHelperChain;
import com.gh.pegasus.support.GatewayHelperFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.jwt.crypto.sign.MacSigner;
import org.springframework.security.jwt.crypto.sign.Signer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.List;

/**
 * Created by enHui.Chen on 2020/9/23.
 */
@Configuration
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.gh.pegasus.support.feign"})
@EnableConfigurationProperties({GatewayHelperProperties.class})
public class GatewayConfiguration {
    @Autowired
    private GatewayHelperProperties gatewayHelperProperties;

    @Bean
    public XForwardedFilter xForwardedForFilter() {
        return new XForwardedFilter();
    }

    /**
     * @Author: enHui.Chen
     * @Description: 自定义CORS, 解决跨域问题
     * @Data 2020/9/25
     */
    @Bean
    @Order(Integer.MIN_VALUE)
    public CorsWebFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedMethod("*");
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.setAllowCredentials(true);

        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(new PathPatternParser());
        source.registerCorsConfiguration("/**", config);

        return new CorsWebFilter(source);
    }

    /**
     * @Author: enHui.Chen
     * @Description: 内置拦截链
     * @Data 2020/10/20
     */
    @Bean
    public GatewayFilterHelperChain gatewayFilterHelperChain(List<GatewayHelperFilter> gatewayHelperFilters) {
        return new GatewayFilterHelperChain(gatewayHelperFilters);
    }

    /**
     * @Author: enHui.Chen
     * @Description: jwt编码
     * @Data 2020/10/22
     */
    @Bean
    @ConditionalOnMissingBean({Signer.class})
    public Signer jwtSigner() {
        return new MacSigner(gatewayHelperProperties.getJwtKey());
    }

}
