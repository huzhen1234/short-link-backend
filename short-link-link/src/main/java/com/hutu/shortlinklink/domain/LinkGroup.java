package com.hutu.shortlinklink.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 短链分组表
 * @TableName link_group
 */
@TableName(value ="link_group")
@Data
public class LinkGroup {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 组名
     */
    @TableField(value = "title")
    private String title;

    /**
     * 账号唯一编号
     */
    @TableField(value = "account_no")
    private Long accountNo;

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