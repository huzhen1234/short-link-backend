package com.hutu.shortlinkcommon.enums;

import lombok.Getter;

/**
 * @Description 状态码定义约束，共6位数，前三位代表服务，后3位代表接口
 *  比如 商品服务210,购物车是220、用户服务230，403代表权限
 *  通用状态码: 100-开头
 *  用户模块：200-开头
 *  短链模块：300-开头
 *
 **/
public enum BizCodeEnum {


    /**
     * 短链分组
     */
    // 短链分组添加失败
    GROUP_ADD_FAIL(300001,"分组添加失败"),
    // 短链分组删除失败
    GROUP_DEL_FAIL(300002,"分组删除失败"),
    GROUP_NOT_EXIST(300003,"分组不存在"),
    GROUP_OPER_FAIL(300004,"分组名操作失败"),
    GROUP_REPEAT(23001,"分组名重复"),



    /**
     *验证码
     */
    CODE_TO_ERROR(240001,"接收号码不合规"),
    CODE_LIMITED(240002,"验证码发送过快"),
    CODE_ERROR(240003,"验证码错误"),
    CODE_CAPTCHA_ERROR(240101,"图形验证码错误"),



    /**
     * 账号
     */
    ACCOUNT_REPEAT(250001,"账号已经存在"),
    ACCOUNT_UNREGISTER(250002,"账号不存在"),
    ACCOUNT_PWD_ERROR(250003,"账号或者密码错误"),
    ACCOUNT_UNLOGIN(250004,"账号未登录"),


    /**
     * 短链
     */
    SHORT_LINK_NOT_EXIST(260404,"短链不存在"),


    /**
     * 订单
     */
    ORDER_CONFIRM_PRICE_FAIL(280002,"创建订单-验价失败"),
    ORDER_CONFIRM_REPEAT(280008,"订单恶意-重复提交"),
    ORDER_CONFIRM_TOKEN_EQUAL_FAIL(280009,"订单令牌缺少"),
    ORDER_CONFIRM_NOT_EXIST(280010,"订单不存在"),

    /**
     * 支付
     */
    PAY_ORDER_FAIL(300001,"创建支付订单失败"),
    PAY_ORDER_CALLBACK_SIGN_FAIL(300002,"支付订单回调验证签失败"),
    PAY_ORDER_CALLBACK_NOT_SUCCESS(300003,"支付宝回调更新订单失败"),
    PAY_ORDER_NOT_EXIST(300005,"订单不存在"),
    PAY_ORDER_STATE_ERROR(300006,"订单状态不正常"),
    PAY_ORDER_PAY_TIMEOUT(300007,"订单支付超时"),


    /**
     * 流控操作
     */
    CONTROL_FLOW(500101,"限流控制"),
    CONTROL_DEGRADE(500201,"降级控制"),
    CONTROL_AUTH(500301,"认证控制"),


    /**
     * 流量包操作
     */
    TRAFFIC_FREE_NOT_EXIST(600101,"免费流量包不存在，联系客服"),

    TRAFFIC_REDUCE_FAIL(600102,"流量不足，扣减失败"),

    TRAFFIC_EXCEPTION(600103,"流量包数据异常,用户无流量包"),


    /**
     * 通用操作码
     */

    OPS_REPEAT(100000,"重复操作"),
    OPS_NETWORK_ADDRESS_ERROR(100001,"网络地址错误"),

    MAIL_SEND_ERROR(100002, "发送邮件异常"),
    // 参数异常
    PARAM_ERROR(100003,"参数错误"),


    /**
     * 用户模块异常码
     */
    // 图形验证码已过期
    CAPTCHA_CODE_EXPIRED(210000,"图形验证码已过期"),
    // 图形验证码错误
    CAPTCHA_ERROR(210001,"图形验证码错误"),
    // 邮箱验证码已过期
    MAIL_CAPTCHA_EXPIRED(210003,"邮箱验证码已过期"),
    // 邮箱验证码错误
    MAIL_CAPTCHA_ERROR(210002,"邮箱验证码错误"),
    // 保存用户信息失败
    SAVE_USER_INFO_FAIL(210004,"保存用户信息失败"),
    // 手机号重复
    PHONE_REPEAT(210005,"手机号重复"),
    // 邮箱重复
    MAIL_REPEAT(210006,"邮箱重复"),
    // 用户不存在
    USER_NOT_EXIST(210007, "用户不存在"),
    // 用户密码错误
    USER_PWD_ERROR(210008, "用户密码错误"),
    // JWT解析失败 显示请重新登陆
    JWT_PARSE_ERROR(210009, "请重新登陆"),

    /**
     * 文件相关
     */
    FILE_UPLOAD_USER_IMG_FAIL(700101,"用户头像文件上传失败");

    @Getter
    private final String message;

    @Getter
    private final int code;

    private BizCodeEnum(int code, String message){
        this.code = code;
        this.message = message;
    }
}