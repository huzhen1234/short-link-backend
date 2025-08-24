package com.hutu.shortlinklink.mq.consumer;

import com.hutu.shortlinkcommon.common.RocketMQConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(
    topic = RocketMQConstant.DLQ_TOPIC_PREFIX + RocketMQConstant.CONSUMER_GROUP_ADD_LINK, // 死信队列名称格式：%DLQ% + 消费者组名
    consumerGroup = RocketMQConstant.GROUP_DLQ_HANDLER // 死信处理组
)
@Slf4j
public class StudentDlqListener implements RocketMQListener<MessageExt> {
    @Override
    public void onMessage(MessageExt message) {
        log.error("[死信消息] 收到死信消息，开始处理");
        log.error("死信消息详情 - Topic: {}, MessageId: {}, ReconsumeTimes: {}",
                 message.getTopic(), message.getMsgId(), message.getReconsumeTimes());
        log.error("死信消息内容: {}", new String(message.getBody()));

        // 人工处理逻辑（如告警、补偿等）
        System.out.println("=== 死信队列监听器收到消息 ===");
        System.out.println("Topic: " + message.getTopic());
        System.out.println("MessageId: " + message.getMsgId());
        System.out.println("ReconsumeTimes: " + message.getReconsumeTimes());
        System.out.println("消息内容: " + new String(message.getBody()));
        System.out.println("=====================");
    }
}