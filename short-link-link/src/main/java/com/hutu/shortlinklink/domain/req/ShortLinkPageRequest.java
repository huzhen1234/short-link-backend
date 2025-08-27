package com.hutu.shortlinklink.domain.req;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 短链分组
 */
@Data
public class ShortLinkPageRequest implements Serializable {


    @Serial
    private static final long serialVersionUID = -5511113167480658658L;
    /**
     * 组
     */
    private Long groupId;

    /**
     * 第几页
     */
    private int page;

    /**
     * 每页多少条
     */
    private int size;

}
