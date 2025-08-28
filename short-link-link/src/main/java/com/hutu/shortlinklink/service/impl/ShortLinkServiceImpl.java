package com.hutu.shortlinklink.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hutu.shortlinkcommon.common.CurrentAccountInfo;
import com.hutu.shortlinkcommon.common.RocketMQConstant;
import com.hutu.shortlinkcommon.enums.BizCodeEnum;
import com.hutu.shortlinkcommon.enums.EventMessageType;
import com.hutu.shortlinkcommon.enums.ShortLinkStateEnum;
import com.hutu.shortlinkcommon.enums.StateEnum;
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
import com.hutu.shortlinklink.domain.req.ShortLinkDelRequest;
import com.hutu.shortlinklink.domain.req.ShortLinkPageRequest;
import com.hutu.shortlinklink.domain.req.ShortLinkUpdateRequest;
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
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.Collections;

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

    private final LinkGroupService linkGroupService;
    private final ShortLinkUtil shortLinkUtil;
    private final EventPublisher eventPublisher;
    private final RedisTemplate<String, String> redisTemplate;
    private final GroupCodeMappingService mappingService;
    // 设置锁过期时间，可以长一点
    // B，C端可以重入，为了防止可能有信息堆积，设置锁过期时间长一些
    private static final int LOCK_EXPIRE_SECONDS = 1000;

    @Override
    public Boolean createShortLink(ShortLinkAddRequest request) {
        CurrentAccountInfo currentAccountInfo = LoginInterceptor.threadLocal.get();
        AssertUtils.notNull(currentAccountInfo, BizCodeEnum.JWT_PARSE_ERROR);
        LinkGroup linkGroup = linkGroupService.findByAccountNoAndId(currentAccountInfo.getAccountNo(), request.getGroupId());
        AssertUtils.notNull(linkGroup, BizCodeEnum.GROUP_NOT_EXIST);
        // 原始URL随机拼接
        String newOriginalUrl = shortLinkUtil.addUrlPrefix(request.getOriginalUrl());
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
        eventPublisher.publishAsync(RocketMQConstant.TOPIC_SHORT_LINK_CREATE, baseEvent, callback);
        return Boolean.TRUE;
    }

    public Boolean handlerAddShortLink(BaseEvent<ShortLinkAddRequest> baseEvent) throws InterruptedException {
        ShortLinkAddRequest addRequest = baseEvent.getData();
        // 长链摘要
        String sign = CommonUtil.MD5(addRequest.getOriginalUrl());
        // 生成短链码
        String shortLinkCode = shortLinkUtil.createShortLinkCode(addRequest.getOriginalUrl());
        Long accountNo = baseEvent.getAccountNo();
        String eventMessageType = baseEvent.getEventMessageType();

        //加锁
        //key1是短链码，ARGV[1]是accountNo,ARGV[2]是过期时间
        String script = "if redis.call('EXISTS',KEYS[1])==0 then redis.call('set',KEYS[1],ARGV[1]); redis.call('expire',KEYS[1],ARGV[2]); return 1;" +
                " elseif redis.call('get',KEYS[1]) == ARGV[1] then return 2;" +
                " else return 0; end;";

        Long result = redisTemplate.execute(
                new DefaultRedisScript<>(script, Long.class),
                Collections.singletonList(shortLinkCode),
                String.valueOf(accountNo), String.valueOf(LOCK_EXPIRE_SECONDS)
        );
        boolean isDuplicate;
        if (result > 0) {
            if (EventMessageType.SHORT_LINK_ADD_LINK.name().equalsIgnoreCase(eventMessageType)) {
                isDuplicate = handleCShortLink(accountNo, shortLinkCode, addRequest, sign);
            } else if (EventMessageType.SHORT_LINK_ADD_MAPPING.name().equalsIgnoreCase(eventMessageType)) {
                isDuplicate = handleBShortLink(accountNo, shortLinkCode, addRequest, sign);
            } else {
                log.error("未知的事件类型:{}", eventMessageType);
                return false;
            }
        } else {
            log.error("短链码加锁失败:{}", JsonUtil.obj2Json(baseEvent));
            return retryWithNewUrl(baseEvent, addRequest);
        }
        //
        if (isDuplicate) {
            return retryWithNewUrl(baseEvent, addRequest);
        }
        return true;
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

    @Override
    public Page<GroupCodeMapping> pageByGroupId(ShortLinkPageRequest pageRequest) {
        CurrentAccountInfo currentAccountInfo = LoginInterceptor.threadLocal.get();
        AssertUtils.notNull(currentAccountInfo, BizCodeEnum.JWT_PARSE_ERROR);
        Page<GroupCodeMapping> page = new Page<>(pageRequest.getPage(), pageRequest.getSize());
        return mappingService.lambdaQuery()
                .eq(GroupCodeMapping::getGroupId, pageRequest.getGroupId())
                .eq(GroupCodeMapping::getAccountNo, currentAccountInfo.getAccountNo())
                .eq(GroupCodeMapping::getDel, StateEnum.ACTIVE.getCode())
                .page(page);
    }

    @Override
    public Boolean del(ShortLinkDelRequest request) {
        CurrentAccountInfo currentAccountInfo = LoginInterceptor.threadLocal.get();
        AssertUtils.notNull(currentAccountInfo, BizCodeEnum.JWT_PARSE_ERROR);
        BaseEvent<ShortLinkDelRequest> baseEvent = BaseEvent.<ShortLinkDelRequest>builder()
                .messageId(IDUtil.generateSnowflakeId().toString())
                .eventMessageType(EventMessageType.SHORT_LINK_DEL.name())
                .accountNo(currentAccountInfo.getAccountNo())
                .remark("delete short link")
                .data(request)
                .build();
        SendCallback callback = new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                log.info("del 删除短链码");
            }

            @Override
            public void onException(Throwable throwable) {
                log.error("del 删除短链码失败: {}", throwable.getMessage());
            }
        };
        eventPublisher.publishAsync(RocketMQConstant.TOPIC_SHORT_LINK_DELETE, baseEvent, callback);
        return Boolean.TRUE;
    }

    @Override
    public void handlerDelShortLink(BaseEvent<ShortLinkDelRequest> result) {
        CurrentAccountInfo currentAccountInfo = LoginInterceptor.threadLocal.get();
        AssertUtils.notNull(currentAccountInfo, BizCodeEnum.JWT_PARSE_ERROR);
        Long accountNo = currentAccountInfo.getAccountNo();
        String messageType = result.getEventMessageType();
        ShortLinkDelRequest request = result.getData();
        //C端解析
        if (EventMessageType.SHORT_LINK_DEL_LINK.name().equalsIgnoreCase(messageType)) {
            remove(lambdaQuery().eq(ShortLink::getCode, request.getCode()).eq(ShortLink::getAccountNo, accountNo));
        } else if (EventMessageType.SHORT_LINK_DEL_MAPPING.name().equalsIgnoreCase(messageType)) {
            //B端处理
            mappingService.remove(mappingService.lambdaQuery().eq(GroupCodeMapping::getId, request.getMappingId())
                    .eq(GroupCodeMapping::getGroupId, request.getGroupId()));
        }
    }

    @Override
    public void handlerUpdateShortLink(BaseEvent<ShortLinkUpdateRequest> result) {
        CurrentAccountInfo currentAccountInfo = LoginInterceptor.threadLocal.get();
        AssertUtils.notNull(currentAccountInfo, BizCodeEnum.JWT_PARSE_ERROR);
        String messageType = result.getEventMessageType();
        ShortLinkUpdateRequest request = result.getData();
        //C端处理
        if (EventMessageType.SHORT_LINK_UPDATE_LINK.name().equalsIgnoreCase(messageType)) {
            lambdaUpdate().eq(ShortLink::getCode, request.getCode())
                    .eq(ShortLink::getAccountNo, currentAccountInfo.getAccountNo())
                    .eq(ShortLink::getDel, StateEnum.ACTIVE.getCode())
                    .eq(ShortLink::getTitle, request.getTitle())
                    .update();
        } else if (EventMessageType.SHORT_LINK_UPDATE_MAPPING.name().equalsIgnoreCase(messageType)) {
            mappingService.lambdaUpdate().eq(GroupCodeMapping::getId, request.getMappingId())
                    .eq(GroupCodeMapping::getGroupId, request.getGroupId())
                    .eq(GroupCodeMapping::getDel, StateEnum.ACTIVE.getCode())
                    .eq(GroupCodeMapping::getTitle, request.getTitle())
                    .update();
        }

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
        String newOriginalUrl = shortLinkUtil.addUrlPrefixVersion(addRequest.getOriginalUrl());
        addRequest.setOriginalUrl(newOriginalUrl);
        baseEvent.setData(addRequest);
        log.warn("短链码保存失败，重新生成:{}", JsonUtil.obj2Json(addRequest));
        return handlerAddShortLink(baseEvent);
    }
}




