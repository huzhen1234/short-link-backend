package com.hutu.shortlinklink.domain.resp;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
public class LinkGroupResp implements Serializable {


    @Serial
    private static final long serialVersionUID = 1252609686651920119L;

    private Long id;

    /**
     * 组名
     */
    private String title;

    /**
     * 账号唯一编号
     */
    private Long accountNo;

    private Date gmtCreate;

    private Date gmtModified;


}