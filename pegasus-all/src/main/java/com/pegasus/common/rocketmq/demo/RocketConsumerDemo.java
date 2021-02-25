package com.pegasus.common.rocketmq.demo;

import com.pegasus.common.rocketmq.annotation.RocketMQConsumeListener;
import com.pegasus.common.rocketmq.core.RocketMQListener;
import com.pegasus.common.rocketmq.core.RocketMQPushConsumerLifecycleListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.springframework.stereotype.Component;

/**
 * Created by enHui.Chen on 2020/4/27.
 */
@Slf4j
@Component
@RocketMQConsumeListener(topic = "test", tag = "*", consumerGroup = "pegasus_demo_222")
public class RocketConsumerDemo implements RocketMQListener<MdpHrDataUpdateVo>, RocketMQPushConsumerLifecycleListener {
     /**
       * @Author: enHui.Chen
       * @Description: 处理数据
       * @Data 2020/8/26
       */
    @Override
    public void onConsumeMessage(MdpHrDataUpdateVo message) {
        log.info("MdpHrDataUpdate:{}", message.toString());
    }

     /**
       * @Author: enHui.Chen
       * @Description: 启动前
       * @Data 2020/8/26
       */
    @Override
    public void prepareStart(DefaultMQPushConsumer consumer) {

    }
}
