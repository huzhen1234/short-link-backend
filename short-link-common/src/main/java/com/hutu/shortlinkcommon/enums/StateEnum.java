package com.hutu.shortlinkcommon.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StateEnum {

    // 删除
    DELETED(1,"DELETED"),
    // 激活
    ACTIVE(0,"ACTIVE");

    private final Integer code;
    private final String message;

}
