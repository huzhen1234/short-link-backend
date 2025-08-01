package com.hutu.shortlinkcommon.event;

import lombok.Data;

import java.util.Date;

/**
 * mq 事件
 * @param <T>
 */
@Data
public class BaseEvent<T> {
    private String id;
    private Date timestamp;
    private T data;
}