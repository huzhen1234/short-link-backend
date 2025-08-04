package com.hutu.shortlinklink.mq.consumer;

import com.hutu.shortlinkcommon.common.RocketMQConstant;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * MessageExt 可以是这个，也可以是String。
 * MessageExt可以接收全部的信息
 */
@Component
@RocketMQMessageListener(topic = RocketMQConstant.TOPIC_SHORT_LINK_EVENT
        , consumerGroup = RocketMQConstant.GROUP_SHORT_LINK_EVENT
        , selectorExpression = RocketMQConstant.TAG_SHORT_LINK_ADD_MAPPING
        // 设置最大重试次数
        , maxReconsumeTimes = 5)
public class ShortLinkAddMappingListener implements RocketMQListener<String> {

    @Override
    public void onMessage(String message) {
        // 处理消息的逻辑
        System.out.println("=== 消费者收到消息 ===");
        System.out.println("消息内容: " + message);
        System.out.println("处理时间: " + new java.util.Date());
        System.out.println("线程: " + Thread.currentThread().getName());
        System.out.println("=====================");
    }
}