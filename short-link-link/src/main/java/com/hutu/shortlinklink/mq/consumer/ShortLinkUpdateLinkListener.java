package com.hutu.shortlinklink.mq.consumer;

import com.hutu.shortlinkcommon.common.RocketMQConstant;
import com.hutu.shortlinkcommon.enums.BizCodeEnum;
import com.hutu.shortlinkcommon.enums.EventMessageType;
import com.hutu.shortlinkcommon.event.BaseEvent;
import com.hutu.shortlinkcommon.exception.BizException;
import com.hutu.shortlinkcommon.util.AssertUtils;
import com.hutu.shortlinkcommon.util.JsonUtil;
import com.hutu.shortlinklink.domain.req.ShortLinkDelRequest;
import com.hutu.shortlinklink.domain.req.ShortLinkUpdateRequest;
import com.hutu.shortlinklink.service.ShortLinkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;


@Component
@RocketMQMessageListener(topic = RocketMQConstant.TOPIC_SHORT_LINK_UPDATE
        , consumerGroup = RocketMQConstant.CONSUMER_GROUP_UPDATE_LINK
        , maxReconsumeTimes = 3)
@Slf4j
@RequiredArgsConstructor
public class ShortLinkUpdateLinkListener implements RocketMQListener<String> {


    private final ShortLinkService shortLinkService;

    @Override
    public void onMessage(String message) {
        try {
            BaseEvent<ShortLinkUpdateRequest> result = JsonUtil.json2Obj(message, BaseEvent.class, ShortLinkUpdateRequest.class);
            AssertUtils.notNull(result, BizCodeEnum.PARAM_ERROR);
            result.setEventMessageType(EventMessageType.SHORT_LINK_UPDATE_LINK.name());
            shortLinkService.handlerUpdateShortLink(result);
        } catch (Exception e) {
            log.error("消息处理异常: {}", e.getMessage());
            throw new BizException(BizCodeEnum.MQ_CONSUME_EXCEPTION);
        }
    }
}
