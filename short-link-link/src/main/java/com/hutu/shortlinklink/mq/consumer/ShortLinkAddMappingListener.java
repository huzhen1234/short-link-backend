package com.hutu.shortlinklink.mq.consumer;

import com.hutu.shortlinkcommon.common.RocketMQConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;


/**
 * MessageExt 可以是这个，也可以是String。
 * MessageExt可以接收全部的信息
 */
@Component
@RocketMQMessageListener(topic = RocketMQConstant.TOPIC_SHORT_LINK_EVENT
        , consumerGroup = RocketMQConstant.GROUP_SHORT_LINK_EVENT_CONSUMER_ADD_MAPPING
        , selectorExpression = RocketMQConstant.TAG_SHORT_LINK_ADD_MAPPING
        // 设置最大重试次数
        , maxReconsumeTimes = 3)
@Slf4j
public class ShortLinkAddMappingListener implements RocketMQListener<String> {

    @Override
    public void onMessage(String message) {
        try {
            log.info("=== 开始处理消息 ShortLinkAddMappingListener ===");
            log.info("消息内容: {}", message);
            log.info("处理时间: {}", new java.util.Date());
            log.info("线程: {}", Thread.currentThread().getName());
            
            System.out.println("=== 消费者收到消息 ShortLinkAddMappingListener ===");
            System.out.println("消息内容: " + message);
            System.out.println("处理时间: " + new java.util.Date());
            System.out.println("线程: " + Thread.currentThread().getName());
            System.out.println("=====================");
            // throw new BizException(400,"测试错误"); // 注释掉这行，避免消费者异常
        } catch (Exception e) {
            log.error("消息处理异常: {}", e.getMessage(), e);
            throw e; // 重新抛出异常，让RocketMQ进行重试
        }
    }
}