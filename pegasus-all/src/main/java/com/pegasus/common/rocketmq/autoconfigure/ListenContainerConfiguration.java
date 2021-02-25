package com.pegasus.common.rocketmq.autoconfigure;

import com.pegasus.common.rocketmq.annotation.RocketMQConsumeListener;
import com.pegasus.common.rocketmq.core.DefaultConsumerListenerContainer;
import com.pegasus.common.rocketmq.core.RocketMQListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by enHui.Chen on 2020/4/27.
 */
@Slf4j
@Configuration
public class ListenContainerConfiguration implements ApplicationContextAware, SmartInitializingSingleton {
    private ConfigurableApplicationContext context;

    private AtomicInteger atomicInteger = new AtomicInteger(0);

    /**
     * @Author: enHui.Chen
     * @Description: spring中所有单例初始化之后, 为每个consumer注册listener
     * @Data 2020/4/27
     */
    @Override
    public void afterSingletonsInstantiated() {
        Map<String, Object> beans = this.context.getBeansWithAnnotation(RocketMQConsumeListener.class);
        beans.forEach(this::registerConsumerWithListener);
    }

    /**
     * @Author: enHui.Chen
     * @Description: 设置容器上下
     * @Data 2020/4/27
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = (ConfigurableApplicationContext) applicationContext;
    }

    /**
     * @Author: enHui.Chen
     * @Description: 为每个consumer注册listener
     * @Data 2020/4/27
     */
    public void registerConsumerWithListener(String consumerName, Object consumer) {
        // 获取代理对象clazz
        Class<?> clazz = AopProxyUtils.ultimateTargetClass(consumer);

        // 获取RocketMQConsumeListener注解信息
        RocketMQConsumeListener consumerInfo = clazz.getAnnotation(RocketMQConsumeListener.class);

        // 手动注册listener到容器,并开启监听
        GenericApplicationContext genericApplicationContext = (GenericApplicationContext) this.context;
        String containerName = String.format("%s_%s", DefaultConsumerListenerContainer.class.getName(), atomicInteger.getAndIncrement());
        genericApplicationContext.registerBean(containerName, DefaultConsumerListenerContainer.class,
                () -> this.createDefaultConsumerListenerContainer(consumerName, (RocketMQListener) consumer, consumerInfo));

        // 如果没启动,再次启动
        DefaultConsumerListenerContainer container = context.getBean(containerName, DefaultConsumerListenerContainer.class);
        if (!container.isRunning()) {
            container.start();
        }

        log.info("start consumerName:{} with listenerContainer:{}", consumerName, containerName);
    }

    /**
     * @Author: enHui.Chen
     * @Description: 初始化DefaultConsumerListenerContainer
     * @Data 2020/4/27
     */
    public DefaultConsumerListenerContainer createDefaultConsumerListenerContainer(String consumerName, RocketMQListener consumer,
                                                                                   RocketMQConsumeListener consumerInfo) {
        DefaultConsumerListenerContainer container = new DefaultConsumerListenerContainer(consumerInfo);
        container.setConsumer(consumer);
        container.setConsumerName(consumerName);
        container.setNameServer(this.context.getEnvironment().resolvePlaceholders(consumerInfo.nameServer()));
        return container;
    }
}

