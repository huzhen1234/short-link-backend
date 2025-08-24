package com.hutu.shortlinklink.mq.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 测试死信队列监听器
 */
@Component
@RocketMQMessageListener(
    // 监听所有可能的死信队列
    topic = "%DLQ%group_short_link_event_consumer_add_link",
    consumerGroup = "test-dlq-consumer-group"
)
@Slf4j
public class TestDeadLetterListener implements RocketMQListener<MessageExt> {
    
    @PostConstruct
    public void init() {
        log.info("=== TestDeadLetterListener 初始化 ===");
        log.info("监听Topic: %DLQ%group_short_link_event_consumer_add_link");
        log.info("消费者组: test-dlq-consumer-group");
        log.info("================================");
    }
    
    @Override
    public void onMessage(MessageExt message) {
        try {
            log.error("=== TestDeadLetterListener 收到死信消息 ===");
            log.error("Topic: {}", message.getTopic());
            log.error("Tags: {}", message.getTags());
            log.error("Keys: {}", message.getKeys());
            log.error("消息内容: {}", new String(message.getBody()));
            log.error("重试次数: {}", message.getReconsumeTimes());
            log.error("消息ID: {}", message.getMsgId());
            log.error("=====================================");
            
        } catch (Exception e) {
            log.error("TestDeadLetterListener 处理死信消息异常: {}", e.getMessage(), e);
        }
    }
} 