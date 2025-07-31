package com.hutu.shortlinklink.mq;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(topic = "test-mq-topic", consumerGroup = "springboot_consumer_group")
public class MyConsumer implements RocketMQListener<String> {

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