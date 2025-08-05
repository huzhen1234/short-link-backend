package com.hutu.shortlinklink.mq.consumer;

import com.hutu.shortlinkcommon.common.RocketMQConstant;
import com.hutu.shortlinkcommon.enums.BizCodeEnum;
import com.hutu.shortlinkcommon.enums.EventMessageType;
import com.hutu.shortlinkcommon.event.BaseEvent;
import com.hutu.shortlinkcommon.exception.BizException;
import com.hutu.shortlinkcommon.util.AssertUtils;
import com.hutu.shortlinkcommon.util.JsonUtil;
import com.hutu.shortlinklink.domain.req.ShortLinkAddRequest;
import com.hutu.shortlinklink.service.ShortLinkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * MessageExt 可以是这个，也可以是String。
 * MessageExt可以接收全部的信息
 */
@Component
@RocketMQMessageListener(topic = RocketMQConstant.TOPIC_SHORT_LINK
        , consumerGroup = RocketMQConstant.CONSUMER_GROUP_ADD_LINK
//        , selectorExpression = RocketMQConstant.TAG_SHORT_LINK_ADD_LINK
        , maxReconsumeTimes = 1)
@Slf4j
@RequiredArgsConstructor
public class ShortLinkAddLinkListener implements RocketMQListener<String> {

    private final ShortLinkService shortLinkService;


    /*@Override
    public void onMessage(String message) {
        try {
            log.info("=== 开始处理消息 ShortLinkAddLinkListener ===");
            log.info("消息内容: {}", message);
            log.info("处理时间: {}", new java.util.Date());
            log.info("线程: {}", Thread.currentThread().getName());
            
            // 处理消息的逻辑
            System.out.println("=== 消费者收到消息 ShortLinkAddLinkListener ===");
            System.out.println("消息内容: " + message);
            System.out.println("处理时间: " + new java.util.Date());
            System.out.println("线程: " + Thread.currentThread().getName());
            System.out.println("=====================");
            
            log.info("=== 消息处理完成 ShortLinkAddLinkListener ===");

            throw new BizException(500,"ceshi");

        } catch (Exception e) {
            log.error("消息处理异常: {}", e.getMessage(), e);
            throw e; // 重新抛出异常，让RocketMQ进行重试
        }
    }*/

    @Override
    public void onMessage(String message) {
        try {
            BaseEvent<ShortLinkAddRequest> result = JsonUtil.json2Obj(message, BaseEvent.class, ShortLinkAddRequest.class);
            AssertUtils.notNull(result, BizCodeEnum.PARAM_ERROR);
            result.setEventMessageType(EventMessageType.SHORT_LINK_ADD_MAPPING.name());
            shortLinkService.handlerAddShortLink(result);
        } catch (Exception e) {
            log.error("消息处理异常: {}", e.getMessage());
            throw new BizException(BizCodeEnum.MQ_CONSUME_EXCEPTION);
        }
    }
}