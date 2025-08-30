package com.hutu.shortlinklink.mq.producer;

import com.alibaba.fastjson2.JSON;
import com.hutu.shortlinkcommon.event.BaseEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class EventPublisher {

    @Resource
    private RocketMQTemplate rocketmqTemplate;

    // ================== 基础方法 ================== //

    /**
     * 普通消息（同步发送）
     */
    public void publish(String topic, BaseEvent<?> message) {
        try {
            String mqMessage = JSON.toJSONString(message);
            log.info("[普通消息] 发送MQ消息 topic:{} message:{}", topic, mqMessage);
            rocketmqTemplate.convertAndSend(topic, mqMessage);
        } catch (Exception e) {
            log.error("[普通消息] 发送失败 topic:{} message:{}", topic, JSON.toJSONString(message), e);
        }
    }

    /**
     * 延迟消息（同步发送）
     */
    public void publishDelivery(String topic, BaseEvent<?> message, int delayTimeLevel) {
        try {
            String mqMessage = JSON.toJSONString(message);
            log.info("[延迟消息] 发送MQ消息 topic:{} level:{} message:{}",
                    topic, delayTimeLevel, mqMessage);

            rocketmqTemplate.syncSend(
                    topic,
                    MessageBuilder.withPayload(mqMessage).build(),
                    2000,
                    delayTimeLevel
            );
        } catch (Exception e) {
            log.error("[延迟消息] 发送失败 topic:{} message:{}", topic, JSON.toJSONString(message), e);
        }
    }

    // ================== 扩展方法 ================== //

    /**
     * 异步发送（带回调处理）
     *
     * @param callback 发送结果回调
     */
    public void publishAsync(String topic, BaseEvent<?> message, SendCallback callback) {
        try {
            String mqMessage = JSON.toJSONString(message);
            log.info("[异步消息] 发送MQ消息 topic:{} message:{} 消息备注:{}", topic, mqMessage,message.getRemark());

            rocketmqTemplate.asyncSend(
                    topic,
                    MessageBuilder.withPayload(mqMessage).build(),
                    callback
            );
        } catch (Exception e) {
            log.error("[异步消息] 发送失败 topic:{} message:{}", topic, JSON.toJSONString(message), e);
        }
    }

    /**
     * 单向发送（不关心结果）
     */
    public void publishOneWay(String topic, BaseEvent<?> message) {
        try {
            String mqMessage = JSON.toJSONString(message);
            log.info("[单向消息] 发送MQ消息 topic:{} message:{}", topic, mqMessage);

            rocketmqTemplate.sendOneWay(topic, MessageBuilder.withPayload(mqMessage).build());
        } catch (Exception e) {
            log.error("[单向消息] 发送失败 topic:{} message:{}", topic, JSON.toJSONString(message), e);
        }
    }

    /**
     * 带TAG的消息发送
     *
     * @param tag 消息标签（用于消费端过滤）
     */
    public void publishWithTag(String topic, String tag, BaseEvent<?> message) {
        try {
            String mqMessage = JSON.toJSONString(message);
            String destination = topic + ":" + tag;
            log.info("[TAG消息] 发送MQ消息 destination:{} message:{}", destination, mqMessage);

            rocketmqTemplate.convertAndSend(destination, mqMessage);
        } catch (Exception e) {
            log.error("[TAG消息] 发送失败 topic:{} tag:{} message:{}",
                    topic, tag, JSON.toJSONString(message), e);
        }
    }

    /**
     * 顺序消息（同步发送）
     *
     * @param shardingKey 分区键（相同键的消息保证顺序）
     */
    public void publishOrderly(String topic, BaseEvent<?> message, String shardingKey) {
        try {
            String mqMessage = JSON.toJSONString(message);
            log.info("[顺序消息] 发送MQ消息 topic:{} key:{} message:{}",
                    topic, shardingKey, mqMessage);

            rocketmqTemplate.syncSendOrderly(
                    topic,
                    MessageBuilder.withPayload(mqMessage).build(),
                    shardingKey
            );
        } catch (Exception e) {
            log.error("[顺序消息] 发送失败 topic:{} key:{} message:{}",
                    topic, shardingKey, JSON.toJSONString(message), e);
        }
    }

    /**
     * 带TAG的延迟消息
     */
    public void publishDeliveryWithTag(String topic, String tag, BaseEvent<?> message, int delayLevel) {
        try {
            String mqMessage = JSON.toJSONString(message);
            String destination = topic + ":" + tag;
            log.info("[TAG延迟] 发送MQ消息 destination:{} level:{} message:{}",
                    destination, delayLevel, mqMessage);

            rocketmqTemplate.syncSend(
                    destination,
                    MessageBuilder.withPayload(mqMessage).build(),
                    2000,
                    delayLevel
            );
        } catch (Exception e) {
            log.error("[TAG延迟] 发送失败 topic:{} tag:{} message:{}",
                    topic, tag, JSON.toJSONString(message), e);
        }
    }

/*    Message<String> messages = MessageBuilder.withPayload(mqMessage)
            .setHeader(RocketMQHeaders.KEYS, "your_message_key")
            // 延迟等级
            .setHeader(RocketMQHeaders.DELAY,1)
            .build();*/
}