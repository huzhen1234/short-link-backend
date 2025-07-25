package com.hutu.shortlinklink.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 短链接信息表
 * @TableName short_link
 */
@TableName(value ="short_link")
@Data
public class ShortLink {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 组
     */
    @TableField(value = "group_id")
    private Long groupId;

    /**
     * 短链标题
     */
    @TableField(value = "title")
    private String title;

    /**
     * 原始url地址
     */
    @TableField(value = "original_url")
    private String originalUrl;

    /**
     * 短链域名
     */
    @TableField(value = "domain")
    private String domain;

    /**
     * 短链压缩码
     */
    @TableField(value = "code")
    private String code;

    /**
     * 长链的md5码，方便查找
     */
    @TableField(value = "sign")
    private String sign;

    /**
     * 过期时间，长久就是-1
     */
    @TableField(value = "expired")
    private Date expired;

    /**
     * 账号唯一编号
     */
    @TableField(value = "account_no")
    private Long accountNo;

    /**
     * 创建时间
     */
    @TableField(value = "gmt_create")
    private Date gmtCreate;

    /**
     * 修改时间
     */
    @TableField(value = "gmt_modified")
    private Date gmtModified;

    /**
     * 0是默认，1是删除
     */
    @TableField(value = "del")
    private Integer del;

    /**
     * 状态，lock是锁定不可用，active是可用
     */
    @TableField(value = "state")
    private String state;

    /**
     * 链接产品层级：FIRST 免费青铜、SECOND黄金、THIRD钻石
     */
    @TableField(value = "link_type")
    private String linkType;
}