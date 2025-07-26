package com.hutu.shortlinkcommon.common;

import com.hutu.shortlinkcommon.enums.BizCodeEnum;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 通用返回类
 */
@Data
public class BaseResponse<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 4050631619301328061L;
    private int code;

    private T data;

    private String message;

    public BaseResponse(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public BaseResponse(int code, T data) {
        this(code, data, "");
    }

    public BaseResponse(BizCodeEnum bizCodeEnum) {
        this(bizCodeEnum.getCode(), null, bizCodeEnum.getMessage());
    }
}
