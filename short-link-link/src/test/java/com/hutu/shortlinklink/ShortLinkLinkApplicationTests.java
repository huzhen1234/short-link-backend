package com.hutu.shortlinklink;

import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

@SpringBootTest
class ShortLinkLinkApplicationTests {
    
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

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

}
