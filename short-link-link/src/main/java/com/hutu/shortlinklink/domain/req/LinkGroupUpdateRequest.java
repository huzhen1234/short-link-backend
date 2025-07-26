package com.hutu.shortlinklink.domain.req;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;


@Data
public class LinkGroupUpdateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 3088378437974466868L;
    /**
     * 组id
     */
    private Long id;
    /**
     * 组名
     */
    private String title;
}
