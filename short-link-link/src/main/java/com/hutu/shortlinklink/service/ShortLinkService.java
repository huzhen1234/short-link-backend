package com.hutu.shortlinklink.service;

import com.hutu.shortlinkcommon.event.BaseEvent;
import com.hutu.shortlinklink.domain.pojo.ShortLink;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hutu.shortlinklink.domain.req.ShortLinkAddRequest;
import com.hutu.shortlinklink.domain.vo.ShortLinkVO;

/**
* @author hutu
* @description 针对表【short_link(短链接信息表)】的数据库操作Service
* @createDate 2025-07-25 14:52:42
*/
public interface ShortLinkService extends IService<ShortLink> {

    /**
     * 创建短链 把信息投递给MQ 由MQ处理
     */
    Boolean createShortLink(ShortLinkAddRequest request);


    boolean handlerAddShortLink(BaseEvent<ShortLinkAddRequest> baseEvent) throws InterruptedException;

    /**
     * 解析短链
     */
    ShortLinkVO parseShortLinkCode(String shortLinkCode);

}
