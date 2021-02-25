package com.pegasus.common.rocketmq.autoconfigure;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(RocketMQAutoConfiguration.class)
public @interface EnablePegasusRocketMQ {
}
