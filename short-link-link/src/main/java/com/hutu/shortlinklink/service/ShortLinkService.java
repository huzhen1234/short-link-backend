package com.hutu.shortlinklink.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hutu.shortlinkcommon.event.BaseEvent;
import com.hutu.shortlinklink.domain.pojo.GroupCodeMapping;
import com.hutu.shortlinklink.domain.pojo.ShortLink;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hutu.shortlinklink.domain.req.ShortLinkAddRequest;
import com.hutu.shortlinklink.domain.req.ShortLinkDelRequest;
import com.hutu.shortlinklink.domain.req.ShortLinkPageRequest;
import com.hutu.shortlinklink.domain.req.ShortLinkUpdateRequest;
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

    /**
     * add 短链
     */
    Boolean handlerAddShortLink(BaseEvent<ShortLinkAddRequest> baseEvent) throws InterruptedException;

    /**
     * 解析短链
     */
    ShortLinkVO parseShortLinkCode(String shortLinkCode);

    Page<GroupCodeMapping> pageByGroupId(ShortLinkPageRequest pageRequest);

    /**
     * 删除短链 -- B、C端都需要删除
     * 发送MQ信息 由MQ处理
     */
    Boolean del(ShortLinkDelRequest request);

    /**
     * 删除短链 -- B、C端都需要删除
     */
    void handlerDelShortLink(BaseEvent<ShortLinkDelRequest> result);


    Boolean update(ShortLinkUpdateRequest request);
    /**
     * 更新短链码 -- 不允许更新 code 字段
     * TODO 其他都可以更改，不再重新生成code 增加复杂度
     */
    void handlerUpdateShortLink(BaseEvent<ShortLinkUpdateRequest> result);
}
