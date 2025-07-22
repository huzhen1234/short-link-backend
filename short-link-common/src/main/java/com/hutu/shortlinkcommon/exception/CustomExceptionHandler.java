package com.hutu.shortlinkcommon.exception;

import com.hutu.shortlinkcommon.common.BaseResponse;
import com.hutu.shortlinkcommon.enums.BizCodeEnum;
import com.hutu.shortlinkcommon.util.ResultUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class CustomExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(CustomExceptionHandler.class);
    @ExceptionHandler(BizException.class)
    public BaseResponse<?> businessExceptionHandler(BizException e) {
        log.error("BizException {}" , e.getMessage());
        return ResultUtils.error(e.getCode(), e.getMessage());
    }

    /**
     * validation参数校验异常
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class) // 监听该类型的错误
    public BaseResponse<?> methodArgumentNotValidException(MethodArgumentNotValidException exception){
        String errorMsg = exception.getBindingResult().getFieldErrors().stream()
                .map(item -> item.getField() + item.getDefaultMessage())
                .collect(Collectors.joining("，")); // 使用中文逗号分隔
        return ResultUtils.error(BizCodeEnum.PARAM_ERROR.getCode(),errorMsg);
    }
}
