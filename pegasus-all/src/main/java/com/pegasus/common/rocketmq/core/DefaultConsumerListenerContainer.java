package com.pegasus.common.rocketmq.core;

import com.pegasus.common.exception.CommonException;
import com.pegasus.common.rocketmq.annotation.RocketMQConsumeListener;
import com.pegasus.common.rocketmq.support.JsonUtil;
import com.pegasus.common.rocketmq.support.RocketMQUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.consumer.rebalance.AllocateMessageQueueAveragely;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.RPCHook;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.SmartLifecycle;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;

/**
 * Created by enHui.Chen on 2020/4/27.
 */
@Data
@Slf4j
public class DefaultConsumerListenerContainer implements InitializingBean, DisposableBean, SmartLifecycle, ApplicationContextAware {
    private ApplicationContext context;

    private String nameServer;// nameServer地址

    private String consumerName;// 消费者名称

    private RocketMQListener consumer;// 接收消息实例

    private RocketMQConsumeListener consumeListener;// 接收消息实例的监听注解

    private String topic;// 订阅topic

    private String tag;// 待过滤的tag

    private String consumerGroup;// 消费组

    private long suspendCurrentQueueTimeMillis = 1000;

    private DefaultMQPushConsumer defaultMQPushConsumer;// 消费者

    private boolean running;// bean是否已被注册(true)/销毁(false)

    private Type messageType;// RocketMQListener的泛型类型

    private String charset = "UTF-8";

    public DefaultConsumerListenerContainer(RocketMQConsumeListener consumeListener) {
        this.consumeListener = consumeListener;

        this.topic = consumeListener.topic();
        this.tag = consumeListener.tag();
        this.consumerGroup = consumeListener.consumerGroup();
        this.nameServer = consumeListener.nameServer();
    }

    /**
     * @Author: enHui.Chen
     * @Description: bean被销毁时, 关闭消费者
     * @Data 2020/4/27
     */
    @Override
    public void destroy() throws Exception {
        this.setRunning(false);
        if (consumer != null) {
            defaultMQPushConsumer.shutdown();
        }
        log.info("{}'s DefaultConsumerListenerContainer destroyed", this.consumerName);
    }

    /**
     * @Author: enHui.Chen
     * @Description: bean被创建后, 初始化defaultMQPushConsumer
     * @Data 2020/4/27
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        createDefaultMQPushConsumer();
        this.messageType = this.getMessageType();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    /**
     * @Author: enHui.Chen
     * @Description: 启动defaultMQPushConsumer
     * @Data 2020/4/27
     */
    @Override
    public void start() {
        if (isRunning()) {
            throw new CommonException("defaultConsumerListenerContainer already running" + this.toString());
        }
        try {
            defaultMQPushConsumer.start();
        } catch (Exception e) {
            log.error("defaultConsumerListenerContainer running failed:", e);
        }

        this.setRunning(true);

        log.info("defaultConsumerListenerContainer is running");
    }

    /**
     * @Author: enHui.Chen
     * @Description: 停止
     * @Data 2020/4/27
     */
    @Override
    public void stop() {
        if (isRunning()) {
            if (defaultMQPushConsumer != null) {
                defaultMQPushConsumer.shutdown();
            }
            setRunning(false);
        }
    }


    /**
     * @Author: enHui.Chen
     * @Description: 停止(回调)
     * @Data 2020/4/27
     */
    @Override
    public void stop(Runnable callback) {
        this.stop();
        callback.run();
    }

    /**
     * @Author: enHui.Chen
     * @Description: 判断实例是否正在运行
     * @Data 2020/4/27
     */
    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public int getPhase() {
        // Returning Integer.MAX_VALUE only suggests that
        // we will be the first bean to shutdown and last bean to start
        return Integer.MAX_VALUE;
    }

    /**
     * @Author: enHui.Chen
     * @Description: 初始化创建defaultMQPushConsumer
     * @Data 2020/4/27
     */
    public void createDefaultMQPushConsumer() throws MQClientException {
        if (consumer == null) {
            throw new IllegalArgumentException("Property 'rocketMQListener' is required");
        }
        Assert.notNull(consumerGroup, "Property 'consumerGroup' is required");
        Assert.notNull(nameServer, "Property 'nameServer' is required");
        Assert.notNull(topic, "Property 'topic' is required");

        Environment environment = this.context.getEnvironment();
        boolean enableMsgTrace = consumeListener.msgTrace();
        String nameServer = environment.resolveRequiredPlaceholders(consumeListener.nameServer());
        String accessKey = consumeListener.accessKey();
        String secretKey = consumeListener.secretKey();
        String customizedTraceTopic = environment.resolveRequiredPlaceholders(consumeListener.customizedTraceTopic());

        RPCHook rpcHook = RocketMQUtil.getRPCHookByAkSk(environment, accessKey, secretKey);
        if (Objects.nonNull(rpcHook)) {
            defaultMQPushConsumer = new DefaultMQPushConsumer(consumerGroup, rpcHook, new AllocateMessageQueueAveragely(),
                    enableMsgTrace, customizedTraceTopic);
            defaultMQPushConsumer.setInstanceName(RocketMQUtil.getInstanceName(rpcHook, consumerGroup));
        } else {
            log.debug("Access-key or secret-key not configure in " + this + ".");
            defaultMQPushConsumer = new DefaultMQPushConsumer(consumerGroup, enableMsgTrace, customizedTraceTopic);
        }

        defaultMQPushConsumer.setVipChannelEnabled(false);// 禁用虚拟IP
        defaultMQPushConsumer.setNamesrvAddr(StringUtils.isEmpty(nameServer) ? this.nameServer : nameServer);// 服务地址
        defaultMQPushConsumer.setMessageListener(new DefaultMessageListenerOrderly());// 按顺序消费
        defaultMQPushConsumer.subscribe(topic, tag);// 订阅


        // 用户可自定义启动前的操作
        if (consumer instanceof RocketMQPushConsumerLifecycleListener) {
            ((RocketMQPushConsumerLifecycleListener) consumer).prepareStart(defaultMQPushConsumer);
        }
    }

    /**
     * @Author: enHui.Chen
     * @Description: 按顺序消费消息监听器
     * @Data 2020/4/27
     */
    public class DefaultMessageListenerOrderly implements MessageListenerOrderly {

        @SuppressWarnings("unchecked")
        @Override
        public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
            for (MessageExt messageExt : msgs) {
                log.debug("received msg: {}", messageExt);
                try {
                    long now = System.currentTimeMillis();
                    handleMessage(messageExt);
                    long costTime = System.currentTimeMillis() - now;
                    log.info("consume {} cost: {} ms", messageExt.getMsgId(), costTime);
                } catch (Exception e) {
                    // 消息消费失败后，队列挂起一段时间
                    log.warn("consume message failed. messageExt:{}", messageExt, e);
                    context.setSuspendCurrentQueueTimeMillis(suspendCurrentQueueTimeMillis);
                    return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
                }
            }

            return ConsumeOrderlyStatus.SUCCESS;
        }
    }

    private void handleMessage(MessageExt messageExt) throws MQClientException, RemotingException, InterruptedException {
        if (consumer != null) {
            consumer.onConsumeMessage(doConvertMessage(messageExt));
        }
    }

    @SuppressWarnings("unchecked")
    private Object doConvertMessage(MessageExt messageExt) {
        if (Objects.equals(messageType, MessageExt.class)) {
            return messageExt;
        } else {
            String str = new String(messageExt.getBody(), Charset.forName(charset));
            if (Objects.equals(messageType, String.class)) {
                return str;
            } else {
                // If msgType not string, use objectMapper change it.
                try {
                    //if the messageType has not Generic Parameter
                    return JsonUtil.toObject(str, (Class<?>) messageType);
                } catch (Exception e) {
                    log.info("convert failed. str:{}, msgType:{}", str, messageType);
                    throw new RuntimeException("cannot convert message to " + messageType, e);
                }
            }
        }
    }

    /**
     * @Author: enHui.Chen
     * @Description: 获取消息泛例类型
     * @Data 2020/4/27
     */
    private Type getMessageType() {
        // 获取目标类型
        Class<?> targetClass = AopProxyUtils.ultimateTargetClass(consumer);

        // 获取接口类型是RocketMQListener类型的接口类型(且必须带泛型)
        Type matchedGenericInterface = null;
        while (Objects.nonNull(targetClass)) {
            // 获取目标类型所实现的接口类型
            Type[] interfaces = targetClass.getGenericInterfaces();
            if (Objects.nonNull(interfaces)) {
                for (Type type : interfaces) {
                    if (type instanceof ParameterizedType &&
                            (Objects.equals(((ParameterizedType) type).getRawType(), RocketMQListener.class))) {
                        matchedGenericInterface = type;
                        break;
                    }
                }
            }
            targetClass = targetClass.getSuperclass();
        }

        if (Objects.isNull(matchedGenericInterface)) {
            return Object.class;
        }

        // 获取接口类型的泛型
        Type[] actualTypeArguments = ((ParameterizedType) matchedGenericInterface).getActualTypeArguments();
        if (Objects.nonNull(actualTypeArguments) && actualTypeArguments.length > 0) {
            return actualTypeArguments[0];
        }
        return Object.class;
    }
}
