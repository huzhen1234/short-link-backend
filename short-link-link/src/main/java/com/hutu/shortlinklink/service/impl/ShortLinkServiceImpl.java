package com.hutu.shortlinklink.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hutu.shortlinkcommon.common.CurrentAccountInfo;
import com.hutu.shortlinkcommon.common.RocketMQConstant;
import com.hutu.shortlinkcommon.enums.BizCodeEnum;
import com.hutu.shortlinkcommon.enums.EventMessageType;
import com.hutu.shortlinkcommon.event.BaseEvent;
import com.hutu.shortlinkcommon.interceptor.LoginInterceptor;
import com.hutu.shortlinkcommon.util.AssertUtils;
import com.hutu.shortlinkcommon.util.CommonUtil;
import com.hutu.shortlinkcommon.util.IDUtil;
import com.hutu.shortlinkcommon.util.JsonUtil;
import com.hutu.shortlinklink.domain.pojo.LinkGroup;
import com.hutu.shortlinklink.domain.pojo.ShortLink;
import com.hutu.shortlinklink.domain.req.ShortLinkAddRequest;
import com.hutu.shortlinklink.domain.vo.ShortLinkVO;
import com.hutu.shortlinklink.mq.producer.EventPublisher;
import com.hutu.shortlinklink.service.LinkGroupService;
import com.hutu.shortlinklink.service.ShortLinkService;
import com.hutu.shortlinklink.mapper.ShortLinkMapper;
import com.hutu.shortlinklink.utils.ShortLinkUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
* @author hutu
* @description 针对表【short_link(短链接信息表)】的数据库操作Service实现
* @createDate 2025-07-25 14:52:42
*/
@Service
@RequiredArgsConstructor
@Slf4j
public class ShortLinkServiceImpl extends ServiceImpl<ShortLinkMapper, ShortLink>
    implements ShortLinkService{

    private final LinkGroupService linkGroupService;
    private final ShortLinkUtil shortLinkUtil;
    private final EventPublisher eventPublisher;

    @Override
    public Boolean createShortLink(ShortLinkAddRequest request) {
        CurrentAccountInfo currentAccountInfo = LoginInterceptor.threadLocal.get();
        AssertUtils.notNull(currentAccountInfo, BizCodeEnum.JWT_PARSE_ERROR);
        LinkGroup linkGroup = linkGroupService.findByAccountNoAndId(currentAccountInfo.getAccountNo(), request.getGroupId());
        AssertUtils.notNull(linkGroup, BizCodeEnum.GROUP_NOT_EXIST);

/*        ShortLink shortLink = new ShortLink();
        BeanUtils.copyProperties(request, shortLink);
        shortLink.setSign(CommonUtil.MD5(request.getOriginalUrl()));
        shortLink.setCode(shortLinkUtil.createShortLinkCode(request.getOriginalUrl()));
        boolean save = save(shortLink);
        AssertUtils.isTrue(save, BizCodeEnum.LINK_ADD_FAIL);*/

        String newOriginalUrl = CommonUtil.addUrlPrefix(request.getOriginalUrl());
        request.setOriginalUrl(newOriginalUrl);

        BaseEvent baseEvent = BaseEvent.builder()
                .messageId(IDUtil.generateSnowflakeId().toString())
                .eventMessageType(EventMessageType.SHORT_LINK_ADD.name())
                .accountNo(currentAccountInfo.getAccountNo())
                .remark("create short link")
                .data(JsonUtil.obj2Json(request))
                .build();

        SendCallback callback = new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                log.info("createShortLink 发送消息成功");
            }

            @Override
            public void onException(Throwable throwable) {
                log.error("createShortLink 发送消息失败: {}", throwable.getMessage());
            }
        };
        eventPublisher.publishAsync(RocketMQConstant.TOPIC_SHORT_LINK, baseEvent, callback);
        return Boolean.TRUE;
    }

    @Override
    public ShortLinkVO parseShortLinkCode(String shortLinkCode) {
        ShortLink one = lambdaQuery().eq(ShortLink::getCode, shortLinkCode).one();
        if (one != null) {
            ShortLinkVO shortLinkVO = new ShortLinkVO();
            BeanUtils.copyProperties(one, shortLinkVO);
            return shortLinkVO;
        }
        return null;
    }
}




