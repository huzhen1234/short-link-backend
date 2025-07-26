package com.hutu.shortlinkaccount.domain.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;

/**
 * 账号注册请求参数
 */
@Data
public class AccountRegisterReq implements Serializable {

    @Serial
    private static final long serialVersionUID = 6786522038179912439L;

    @NotBlank(message = "用户名不能为空")
    @Size(max = 20,message = "用户名长度不能超过20位")
    private String accountName;

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6,max = 20,message = "密码长度6-20位")
    private String pwd;

    @NotBlank(message = "邮箱不能为空")
    @Pattern(regexp = "^\\w+@qq\\.com$", message = "仅支持QQ邮箱")
    private String mail;

    // 图形验证码
    @NotBlank(message = "图形验证码不能为空")
    private String captcha;

    // 邮箱验证码
    @NotBlank(message = "邮箱验证码不能为空")
    private String mailCode;
}
