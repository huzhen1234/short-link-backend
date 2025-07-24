package com.hutu.shortlinkaccount.domain.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 流量包
 * @TableName traffic
 */
@TableName(value ="traffic")
@Data
public class Traffic {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 每天限制多少条，短链
     */
    @TableField(value = "day_limit")
    private Integer dayLimit;

    /**
     * 当天用了多少条，短链
     */
    @TableField(value = "day_used")
    private Integer dayUsed;

    /**
     * 总次数，活码才用
     */
    @TableField(value = "total_limit")
    private Integer totalLimit;

    /**
     * 账号
     */
    @TableField(value = "account_no")
    private Long accountNo;

    /**
     * 订单号
     */
    @TableField(value = "out_trade_no")
    private String outTradeNo;

    /**
     * 产品层级：FIRST青铜、SECOND黄金、THIRD钻石
     */
    @TableField(value = "level")
    private String level;

    /**
     * 过期日期
     */
    @TableField(value = "expired_date")
    private Date expiredDate;

    /**
     * 插件类型
     */
    @TableField(value = "plugin_type")
    private String pluginType;

    /**
     * 商品主键
     */
    @TableField(value = "product_id")
    private Long productId;

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