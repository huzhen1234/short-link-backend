package com.hutu.shortlinklink.mq.consumer;

import com.hutu.shortlinkcommon.common.RocketMQConstant;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(
    topic = "%DLQ%" + RocketMQConstant.TOPIC_SHORT_LINK_EVENT, // 死信队列名称必须严格匹配
    consumerGroup = "DLQ_HANDLER_GROUP" // 死信处理组（需不同组名）
)
public class StudentDlqListener implements RocketMQListener<MessageExt> {
    @Override
    public void onMessage(MessageExt message) {
        System.err.println("[死信消息] 原因: " + message.getReconsumeTimes() + "次重试失败");
        // 人工处理逻辑（如告警、补偿等）
    }
}