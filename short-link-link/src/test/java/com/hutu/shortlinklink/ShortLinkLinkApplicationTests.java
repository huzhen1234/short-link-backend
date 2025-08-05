package com.hutu.shortlinklink;

import com.hutu.shortlinkcommon.common.RocketMQConstant;
import com.hutu.shortlinkcommon.event.BaseEvent;
import com.hutu.shortlinklink.mq.producer.EventPublisher;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import javax.annotation.Resource;

@SpringBootTest
class ShortLinkLinkApplicationTests {
    
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    @Resource
    private EventPublisher eventPublisher;

    @Test
    void contextLoads() throws InterruptedException {
        System.out.println("=== 开始测试RocketMQ消息发送和消费 ===");
        
        // 检查 RocketMQTemplate 是否正确注入
        if (rocketMQTemplate == null) {
            System.err.println("错误：RocketMQTemplate 未正确注入");
            return;
        }
        System.out.println("RocketMQTemplate 注入成功");
        
        // 发送同步消息测试
        Message<String> syncMsg = MessageBuilder.withPayload("同步消息测试-" + System.currentTimeMillis()).build();
        try {
            SendResult syncResult = rocketMQTemplate.syncSend("test-mq-topic", syncMsg);
            System.out.println("同步消息发送结果: " + syncResult);
        } catch (Exception e) {
            System.err.println("同步消息发送失败: " + e.getMessage());
            e.printStackTrace();
            return;
        }
        
        // 发送异步消息测试
        SendCallback sendCallback = new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                System.out.println("异步消息发送成功: " + sendResult);
            }
            @Override
            public void onException(Throwable throwable) {
                System.out.println("异步消息发送失败: " + throwable.getMessage());
            }
        };
        Message<String> asyncMsg = MessageBuilder.withPayload("异步消息测试-" + System.currentTimeMillis()).build();
        rocketMQTemplate.asyncSend("test-mq-topic", asyncMsg, sendCallback);
        
        // 等待消费者处理消息
        System.out.println("等待消费者处理消息...");
        Thread.sleep(2000);
        System.out.println("=== 测试完成 ===");
    }

    @Test
    void testSend() {
        eventPublisher.publish("test-mq-topic"
                , BaseEvent.builder()
                        .data("test")
                        .bizId("tets-id")
                        .messageId("message_id")
                        .accountNo(2L)
                        .remark("ceshi")
                        .eventMessageType("test")
                        .build());
    }

    @Test
    void testSendWithTag() {
        // 测试带TAG的消息发送
        eventPublisher.publishWithTag(RocketMQConstant.TOPIC_SHORT_LINK
                , RocketMQConstant.TAG_SHORT_LINK_ADD_LINK
                , BaseEvent.builder()
                        .data("test-with-tag")
                        .bizId("test-tag-id")
                        .messageId("message_tag_id")
                        .accountNo(2L)
                        .remark("测试带TAG的消息")
                        .eventMessageType("test-tag")
                        .build());
        
        System.out.println("已发送带TAG的消息到: " + RocketMQConstant.TOPIC_SHORT_LINK + ":" + RocketMQConstant.TAG_SHORT_LINK_ADD_LINK);
    }

    @Test
    void testDeadLetterQueue() throws InterruptedException {
        // 测试死信队列功能
        // 发送消息到add-link消费者，该消费者会抛出异常，触发重试后进入死信队列
        eventPublisher.publishWithTag(RocketMQConstant.TOPIC_SHORT_LINK
                , RocketMQConstant.TAG_SHORT_LINK_ADD_LINK
                , BaseEvent.builder()
                        .data("test-dlq")
                        .bizId("test-dlq-id")
                        .messageId("message_dlq_id")
                        .accountNo(2L)
                        .remark("测试死信队列功能")
                        .eventMessageType("test-dlq")
                        .build());
        
        System.out.println("已发送测试死信队列的消息");
        System.out.println("注意：ShortLinkAddLinkListener 会抛出异常，消息会重试后进入死信队列");
        System.out.println("等待消息处理和重试...");
        
        // 等待足够的时间让消息处理、重试和进入死信队列
        Thread.sleep(10000);
        
        System.out.println("死信队列测试完成");
    }

}
