package com.hutu.shortlinklink.domain.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * 短链表-商家维度
 * @TableName group_code_mapping
 */
@TableName(value ="group_code_mapping")
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class GroupCodeMapping {
    /**
     * 
     */
    @TableId(value = "id")
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
     * 原始URL地址
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
     * 长链的MD5码，方便查找
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
     * 链接产品层级：FIRST 免费青铜、SECOND 黄金、THIRD 钻石
     */
    @TableField(value = "link_type")
    private String linkType;
}