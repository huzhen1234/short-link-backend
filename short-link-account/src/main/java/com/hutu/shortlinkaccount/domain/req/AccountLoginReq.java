package com.hutu.shortlinkaccount.domain.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;

@Data
public class AccountLoginReq implements Serializable {

    @Serial
    private static final long serialVersionUID = -2008829563226912228L;
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;
    @NotBlank(message = "密码不能为空")
    @Size(min = 6,max = 20,message = "密码长度6-20位")
    private String pwd;
}