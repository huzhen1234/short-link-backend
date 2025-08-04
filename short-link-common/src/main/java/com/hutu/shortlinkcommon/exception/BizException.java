package com.hutu.shortlinkcommon.exception;

import com.hutu.shortlinkcommon.enums.BizCodeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@EqualsAndHashCode(callSuper = true)
@Data
public class BizException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -2609067725385471243L;

    private Integer code;

    private String msg;

    public BizException(Integer code, String message) {
        super(message);
        this.code = code;
        this.msg = message;
    }



    public BizException(BizCodeEnum bizCodeEnum){
        super(bizCodeEnum.getMessage());
        this.code = bizCodeEnum.getCode();
        this.msg = bizCodeEnum.getMessage();
    }


}
