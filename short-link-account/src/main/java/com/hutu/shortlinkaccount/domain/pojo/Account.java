package com.hutu.shortlinkaccount.domain.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 用户表
 * @TableName account
 */
@TableName(value ="account")
@Data
public class Account {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 
     */
    @TableField(value = "account_no")
    private Long accountNo;

    /**
     * 头像
     */
    @TableField(value = "head_img")
    private String headImg;

    /**
     * 手机号
     */
    @TableField(value = "phone")
    private String phone;

    /**
     * 密码
     */
    @TableField(value = "pwd")
    private String pwd;

    /**
     * 盐，用于个人敏感信息处理
     */
    @TableField(value = "secret")
    private String secret;

    /**
     * 邮箱
     */
    @TableField(value = "mail")
    private String mail;

    /**
     * 用户名
     */
    @TableField(value = "username")
    private String username;

    /**
     * 认证级别，DEFAULT，REALNAME，ENTERPRISE，访问次数不一样
     */
    @TableField(value = "auth")
    private String auth;

    /**
     * 
     */
    @TableField(value = "gmt_create")
    private Date gmtCreate;

    /**
     * 
     */
    @TableField(value = "gmt_modified")
    private Date gmtModified;
}