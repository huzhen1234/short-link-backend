package com.hutu.shortlinklink.domain.req;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class ShortLinkUpdateRequest implements Serializable {


    @Serial
    private static final long serialVersionUID = 5842974302793319438L;
    /**
     * 组
     */
    private Long groupId;

    /**
     * 映射id
     */
    private Long mappingId;

    /**
     * 短链码
     */
    private String code;


    /**
     * 标题
     */
    private String title;

}
