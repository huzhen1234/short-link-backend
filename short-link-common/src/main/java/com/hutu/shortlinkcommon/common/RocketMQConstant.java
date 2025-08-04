package com.hutu.shortlinkcommon.common;

import lombok.Data;

@Data
public class RocketMQConstant {

    /**
     * 短链事件  MQ 主题
     */
    public static final String TOPIC_SHORT_LINK_EVENT = "short-link-topic";

    /**
     * 短链事件  MQ 延迟主题
     */
    public static final String TOPIC_SHORT_LINK_DELAY_EVENT = "short-link-delay-topic";

    /**
     * 短链事件 MQ TAG add-link
     */
    public static final String TAG_SHORT_LINK_ADD_LINK = "add-link";

    /**
     * 短链事件 MQ TAG add-mapping
     */
    public static final String TAG_SHORT_LINK_ADD_MAPPING = "add-mapping";

    /**
     * 短链事件 MQ 消费组
     */
    public static final String GROUP_SHORT_LINK_EVENT = "short-link-group";



}
