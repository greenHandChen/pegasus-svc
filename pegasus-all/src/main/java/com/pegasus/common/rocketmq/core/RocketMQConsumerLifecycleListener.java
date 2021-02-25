package com.pegasus.common.rocketmq.core;

/**
 * Created by enHui.Chen on 2020/4/27.
 */
public interface RocketMQConsumerLifecycleListener<T> {
    void prepareStart(final T consumer);
}

