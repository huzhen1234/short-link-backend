package com.hutu.shortlinkcommon.exception;

import com.hutu.shortlinkcommon.common.BaseResponse;
import com.hutu.shortlinkcommon.util.ResultUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(CustomExceptionHandler.class);
    @ExceptionHandler(BizException.class)
    public BaseResponse<?> businessExceptionHandler(BizException e) {
        log.error("BizException {}" , e.getMessage());
        return ResultUtils.error(e.getCode(), e.getMessage());
    }

}
