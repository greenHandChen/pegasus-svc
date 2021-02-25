package com.pegasus.common.rocketmq.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableConfigurationProperties(RocketMQProperties.class)
@ConditionalOnProperty(prefix = "rocketmq", value = "name-server", matchIfMissing = true)
@Import(value = {RocketMQProducerConfiguration.class, ListenContainerConfiguration.class})
public class RocketMQAutoConfiguration {
}
