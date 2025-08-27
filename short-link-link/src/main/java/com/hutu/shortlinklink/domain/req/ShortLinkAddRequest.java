package com.hutu.shortlinklink.domain.req;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
public class ShortLinkAddRequest implements Serializable {


    @Serial
    private static final long serialVersionUID = 7927063102838061518L;
    /**
     * 组
     */
    private Long groupId;

    /**
     * 短链标题
     */
    private String title;

    /**
     * 原生url
     */
    private String originalUrl;

    /**
     * 域名
     */
    private String domain;

    /**
     * 过期时间
     */
    private Date expired;

}
