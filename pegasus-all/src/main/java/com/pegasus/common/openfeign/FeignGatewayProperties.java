package com.pegasus.common.openfeign;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by enHui.Chen on 2021/6/17.
 */
@Data
@ConfigurationProperties(prefix = FeignGatewayProperties.PREFIX)
public class FeignGatewayProperties {
    public static final String PREFIX = "hzero.feign-gateway";

    // 是否启动gateway调用(默认不启动)
    private boolean enabled = false;

    private String gatewayPath = "http://localhost:8080";
}
