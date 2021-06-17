package com.pegasus.common.openfeign;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by enHui.Chen on 2021/6/17.
 */
@Configuration
@EnableConfigurationProperties({FeignGatewayProperties.class})
public class FeignGatewayAutoConfiguration {
    @Bean
    @ConditionalOnProperty(prefix = FeignGatewayProperties.PREFIX, name = "enabled", havingValue = "true")
    public FeignGatewayClient feignGatewayClient(@Autowired FeignGatewayProperties feignGatewayProperties) {
        return new FeignGatewayClient(null, null, feignGatewayProperties);
    }
}
