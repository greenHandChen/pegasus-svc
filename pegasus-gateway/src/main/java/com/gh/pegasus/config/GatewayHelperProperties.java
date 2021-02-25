package com.gh.pegasus.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * Created by enHui.Chen on 2020/10/20.
 */
@Data
@ConfigurationProperties(prefix = GatewayHelperProperties.GATEWAY_HELPER_PREFIX)
public class GatewayHelperProperties {
    public static final String GATEWAY_HELPER_PREFIX = "pegasus.gateway";

    private List<String> skipPaths;

    private String jwtKey;
}
