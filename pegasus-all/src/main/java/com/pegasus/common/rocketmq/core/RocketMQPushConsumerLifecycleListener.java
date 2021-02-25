package com.pegasus.common.rocketmq.core;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;

/**
 * Created by enHui.Chen on 2020/4/27.
 */
public interface RocketMQPushConsumerLifecycleListener extends RocketMQConsumerLifecycleListener<DefaultMQPushConsumer> {
}
