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
     * 短链事件 MQ 消费组 add消费组
     */
    public static final String GROUP_SHORT_LINK_EVENT_CONSUMER_ADD_LINK = "group_short_link_event_consumer_add_link";

    /**
     * 短链事件 MQ 消费组 add消费组
     */
    public static final String GROUP_SHORT_LINK_EVENT_CONSUMER_ADD_MAPPING = "group_short_link_event_consumer_add_mapping";

    /**
     * 死信队列消费者组
     */
    public static final String GROUP_DLQ_HANDLER = "dlq_handler_group";

    /**
     * 死信队列Topic前缀
     */
    public static final String DLQ_TOPIC_PREFIX = "%DLQ%";

}
