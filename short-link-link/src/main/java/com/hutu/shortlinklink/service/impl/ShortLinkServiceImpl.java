package com.hutu.shortlinklink.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hutu.shortlinkcommon.common.CurrentAccountInfo;
import com.hutu.shortlinkcommon.common.RocketMQConstant;
import com.hutu.shortlinkcommon.enums.BizCodeEnum;
import com.hutu.shortlinkcommon.enums.EventMessageType;
import com.hutu.shortlinkcommon.enums.ShortLinkStateEnum;
import com.hutu.shortlinkcommon.event.BaseEvent;
import com.hutu.shortlinkcommon.interceptor.LoginInterceptor;
import com.hutu.shortlinkcommon.util.AssertUtils;
import com.hutu.shortlinkcommon.util.CommonUtil;
import com.hutu.shortlinkcommon.util.IDUtil;
import com.hutu.shortlinkcommon.util.JsonUtil;
import com.hutu.shortlinklink.domain.pojo.GroupCodeMapping;
import com.hutu.shortlinklink.domain.pojo.LinkGroup;
import com.hutu.shortlinklink.domain.pojo.ShortLink;
import com.hutu.shortlinklink.domain.req.ShortLinkAddRequest;
import com.hutu.shortlinklink.domain.vo.ShortLinkVO;
import com.hutu.shortlinklink.mapper.ShortLinkMapper;
import com.hutu.shortlinklink.mq.producer.EventPublisher;
import com.hutu.shortlinklink.service.GroupCodeMappingService;
import com.hutu.shortlinklink.service.LinkGroupService;
import com.hutu.shortlinklink.service.ShortLinkService;
import com.hutu.shortlinklink.utils.ShortLinkUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author hutu
 * @description 针对表【short_link(短链接信息表)】的数据库操作Service实现
 * @createDate 2025-07-25 14:52:42
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ShortLinkServiceImpl extends ServiceImpl<ShortLinkMapper, ShortLink>
        implements ShortLinkService {

    private final static String LOCK_KEY_PREFIX = "short_link:add:";

    private final LinkGroupService linkGroupService;
    private final ShortLinkUtil shortLinkUtil;
    private final EventPublisher eventPublisher;
    private final RedisTemplate<String, String> redisTemplate;
    private final GroupCodeMappingService mappingService;
    private final RedissonClient redissonClient;

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

        BaseEvent<ShortLinkAddRequest> baseEvent = BaseEvent.<ShortLinkAddRequest>builder()
                .messageId(IDUtil.generateSnowflakeId().toString())
                .eventMessageType(EventMessageType.SHORT_LINK_ADD.name())
                .accountNo(currentAccountInfo.getAccountNo())
                .remark("create short link")
                .data(request)
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

    public boolean handlerAddShortLink(BaseEvent<ShortLinkAddRequest> baseEvent) throws InterruptedException {
        ShortLinkAddRequest addRequest = baseEvent.getData();
        // 长链摘要
        String sign = CommonUtil.MD5(addRequest.getOriginalUrl());
        // 生成短链码
        String shortLinkCode = shortLinkUtil.createShortLinkCode(addRequest.getOriginalUrl());
        Long accountNo = baseEvent.getAccountNo();
        String eventMessageType = baseEvent.getEventMessageType();

        RLock lock = redissonClient.getLock(LOCK_KEY_PREFIX + accountNo);
        // 使用更合理的锁等待和持有时间
        boolean triedLock = lock.tryLock(1, 5, TimeUnit.SECONDS);

        if (!triedLock) {
            log.error("短链码加锁失败:{}", JsonUtil.obj2Json(baseEvent));
            return retryWithNewUrl(baseEvent, addRequest);
        }

        try {
            boolean isDuplicate = false;

            if (EventMessageType.SHORT_LINK_ADD_LINK.name().equalsIgnoreCase(eventMessageType)) {
                isDuplicate = handleCShortLink(accountNo, shortLinkCode, addRequest, sign);
            } else if (EventMessageType.SHORT_LINK_ADD_MAPPING.name().equalsIgnoreCase(eventMessageType)) {
                isDuplicate = handleBShortLink(accountNo, shortLinkCode, addRequest, sign);
            } else {
                log.error("未知的事件类型:{}", eventMessageType);
                return false;
            }

            if (isDuplicate) {
                return retryWithNewUrl(baseEvent, addRequest);
            }

            return true;
        } finally {
            lock.unlock();
        }
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


    private boolean handleCShortLink(Long accountNo, String shortLinkCode,
                                     ShortLinkAddRequest request, String sign) {
        ShortLink shortLink = lambdaQuery().eq(ShortLink::getCode, shortLinkCode).one();
        if (shortLink != null) {
            log.error("C端短链码重复:{}", shortLinkCode);
            return true;
        }

        ShortLink shortLinkDO = buildShortLink(accountNo, shortLinkCode, request, sign);
        save(shortLinkDO);
        return false;
    }

    private boolean handleBShortLink(Long accountNo, String shortLinkCode,
                                     ShortLinkAddRequest request, String sign) {
        GroupCodeMapping mapping = mappingService.findByCodeAndGroupId(shortLinkCode, accountNo);
        if (mapping != null) {
            log.error("B端短链码重复:{}", shortLinkCode);
            return true;
        }

        GroupCodeMapping groupCodeMappingDO = buildGroupCodeMapping(accountNo, shortLinkCode, request, sign);
        mappingService.save(groupCodeMappingDO);
        return false;
    }

    private ShortLink buildShortLink(Long accountNo, String code,
                                     ShortLinkAddRequest request, String sign) {
        return ShortLink.builder()
                .accountNo(accountNo)
                .code(code)
                .title(request.getTitle())
                .originalUrl(request.getOriginalUrl())
                .expired(request.getExpired())
                .sign(sign)
                .state(ShortLinkStateEnum.ACTIVE.name())
                .del(0)
                .build();
    }

    private GroupCodeMapping buildGroupCodeMapping(Long accountNo, String code,
                                                   ShortLinkAddRequest request, String sign) {
        return GroupCodeMapping.builder()
                .accountNo(accountNo)
                .code(code)
                .title(request.getTitle())
                .originalUrl(request.getOriginalUrl())
                .groupId(request.getGroupId())
                .expired(request.getExpired())
                .sign(sign)
                .state(ShortLinkStateEnum.ACTIVE.name())
                .del(0)
                .build();
    }

    private boolean retryWithNewUrl(BaseEvent<ShortLinkAddRequest> baseEvent,
                                    ShortLinkAddRequest addRequest) throws InterruptedException {
        // 重复之后，重新生成一个新的原始url
        String newOriginalUrl = CommonUtil.addUrlPrefixVersion(addRequest.getOriginalUrl());
        addRequest.setOriginalUrl(newOriginalUrl);
        baseEvent.setData(addRequest);
        log.warn("短链码保存失败，重新生成:{}", JsonUtil.obj2Json(addRequest));
        return handlerAddShortLink(baseEvent);
    }
}




