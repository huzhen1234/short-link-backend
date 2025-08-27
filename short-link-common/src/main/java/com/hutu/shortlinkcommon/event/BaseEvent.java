package com.hutu.shortlinkcommon.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * mq 事件
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class BaseEvent<T> {
    /**
     * 消息队列ID
     */
    private String messageId;

    /**
     * 事件类型
     */
    private String eventMessageType;

    /**
     * 业务ID
     */
    private String bizId;

    /**
     * 账号
     */
    private Long accountNo;

    /**
     * 备注
     */
    private String remark;

    /**
     * 消息体
     */
    private T data;
}