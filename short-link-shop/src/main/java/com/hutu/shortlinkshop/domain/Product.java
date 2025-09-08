package com.hutu.shortlinkshop.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 短链产品表
 * @TableName product
 */
@TableName(value ="product")
@Data
public class Product {
    /**
     * 
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 商品标题
     */
    @TableField(value = "title")
    private String title;

    /**
     * 详情
     */
    @TableField(value = "detail")
    private String detail;

    /**
     * 图片
     */
    @TableField(value = "img")
    private String img;

    /**
     * 产品层级：FIRST青铜、SECOND黄金、THIRD钻石
     */
    @TableField(value = "level")
    private String level;

    /**
     * 原价
     */
    @TableField(value = "old_amount")
    private Long oldAmount;

    /**
     * 现价
     */
    @TableField(value = "amount")
    private Long amount;

    /**
     * 工具类型 short_link、qrcode
     */
    @TableField(value = "plugin_type")
    private String pluginType;

    /**
     * 日次数：短链类型
     */
    @TableField(value = "day_times")
    private Integer dayTimes;

    /**
     * 总次数：活码才有
     */
    @TableField(value = "total_times")
    private Integer totalTimes;

    /**
     * 有效天数
     */
    @TableField(value = "valid_day")
    private Integer validDay;

    /**
     * 
     */
    @TableField(value = "gmt_modified")
    private Date gmtModified;

    /**
     * 
     */
    @TableField(value = "gmt_create")
    private Date gmtCreate;
}