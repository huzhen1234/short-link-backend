package com.hutu.shortlinkaccount.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName traffic_task
 */
@TableName(value ="traffic_task")
@Data
public class TrafficTask {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    @TableField(value = "account_no")
    private Long accountNo;

    /**
     * 
     */
    @TableField(value = "traffic_id")
    private Long trafficId;

    /**
     * 
     */
    @TableField(value = "use_times")
    private Integer useTimes;

    /**
     * 锁定状态锁定LOCK  完成FINISH-取消CANCEL
     */
    @TableField(value = "lock_state")
    private String lockState;

    /**
     * 唯一标识
     */
    @TableField(value = "message_id")
    private String messageId;

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