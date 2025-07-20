package com.hutu.shortlinkcommon.util;

import com.hutu.shortlinkcommon.common.BaseResponse;
import com.hutu.shortlinkcommon.enums.BizCodeEnum;

/**
 * 返回工具类
 */
public class ResultUtils {

    /**
     * 成功
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(0, data, "ok");
    }

    /**
     * 失败
     */
    public static BaseResponse<?> error(BizCodeEnum bizCodeEnum) {
        return new BaseResponse<>(bizCodeEnum);
    }

    /**
     * 失败
     */
    public static BaseResponse<?> error(int code, String message) {
        return new BaseResponse<>(code, null, message);
    }

    /**
     * 失败
     */
    public static BaseResponse<?> error(BizCodeEnum bizCodeEnum, String message) {
        return new BaseResponse<>(bizCodeEnum.getCode(), null, message);
    }
}