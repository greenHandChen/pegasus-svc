package com.pegasus.common.rocketmq.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by enHui.Chen on 2020/4/27.
 */
@ConfigurationProperties(prefix = "rocketmq")
public class RocketMQProperties {
    private String nameServer;
}
