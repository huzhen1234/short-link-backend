package com.hutu.shortlinkcommon.common;

import lombok.Data;

@Data
public class RocketMQConstant {

    /**
     * 创建短链主题
     */
    public static final String TOPIC_SHORT_LINK_CREATE = "topic-short-link-create";

    /**
     * 删除短链主题
     */
    public static final String TOPIC_SHORT_LINK_DELETE = "topic-short-link-delete";

    /**
     * 短链事件 MQ 消费组 add消费组
     */
    public static final String CONSUMER_GROUP_ADD_LINK = "consumer_group_add_link";

    /**
     * 短链事件 MQ 消费组 add消费组
     */
    public static final String CONSUMER_GROUP_ADD_MAPPING = "consumer_group_add_mapping";

    /**
     * 短链事件 MQ 消费组 删除消费组
     */
    public static final String CONSUMER_GROUP_DEL_LINK = "consumer_group_del_link";

    /**
     * 短链事件 MQ 消费组 删除消费组
     */
    public static final String CONSUMER_GROUP_DEL_MAPPING = "consumer_group_del_mapping";


    /**
     * 死信队列消费者组
     */
    public static final String CONSUMER_GROUP_DLQ_HANDLER = "consumer_group_dlq_handler";

    /**
     * 死信队列Topic前缀
     */
    public static final String DLQ_TOPIC_PREFIX = "%DLQ%";

    /**
     * 死信队列消费者组
     */
    public static final String GROUP_DLQ_HANDLER = "dlq_add_link";



    /**
     * 短链事件 MQ TAG add-link --未使用
     */
    public static final String TAG_SHORT_LINK_ADD_LINK = "add-link";

    /**
     * 短链事件 MQ TAG add-mapping --未使用
     */
    public static final String TAG_SHORT_LINK_ADD_MAPPING = "add-mapping";

    /**
     * 短链事件  MQ 延迟主题 --未使用
     */
    public static final String TOPIC_SHORT_LINK_CREATE_DELAY_EVENT = "short-link-delay-topic";

}
