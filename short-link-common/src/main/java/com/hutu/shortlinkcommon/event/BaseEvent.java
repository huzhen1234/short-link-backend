package com.hutu.shortlinkcommon.event;

import lombok.Data;

/**
 * mq 事件
 */
@Data
public class BaseEvent {
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
     * 异常备注
     */
    private String remark;

    /**
     * 消息体
     */
    private String data;
}