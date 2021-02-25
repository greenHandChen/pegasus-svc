package com.pegasus.common.rocketmq.annotation;

import java.lang.annotation.*;

/**
 * Created by enHui.Chen on 2020/4/27.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RocketMQConsumeListener {
    String NAME_SERVER_PLACEHOLDER = "${rocketmq.name-server:}";
    String ACCESS_KEY_PLACEHOLDER = "${rocketmq.consumer.access-key:}";
    String SECRET_KEY_PLACEHOLDER = "${rocketmq.consumer.secret-key:}";
    String TRACE_TOPIC_PLACEHOLDER = "${rocketmq.consumer.customized-trace-topic:}";
    String ACCESS_CHANNEL_PLACEHOLDER = "${rocketmq.access-channel:}";

    String topic();// 订阅topic

    String tag() default "";// 待过滤的tag

    String consumerGroup();// 消费组

    String nameServer() default NAME_SERVER_PLACEHOLDER;// nameServer地址

    boolean msgTrace() default true;// 是否开启消息追踪

    String accessKey() default ACCESS_KEY_PLACEHOLDER;

    String secretKey() default SECRET_KEY_PLACEHOLDER;

    String customizedTraceTopic() default TRACE_TOPIC_PLACEHOLDER;

}
