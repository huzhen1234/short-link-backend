package com.hutu.shortlinklink.domain.req;
import lombok.Data;
import java.io.Serializable;

@Data
public class LinkGroupAddRequest implements Serializable {

    private static final long serialVersionUID = -4706508947290383539L;

    /**
     * 组名
     */
    private String title;
}
