package com.hutu.shortlinklink.domain.req;

import lombok.Data;

import java.util.Date;

@Data
public class ShortLinkAddRequest {


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
