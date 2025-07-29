package com.hutu.shortlinklink.service;

import com.hutu.shortlinklink.domain.pojo.LinkGroup;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hutu.shortlinklink.domain.req.LinkGroupAddRequest;
import com.hutu.shortlinklink.domain.req.LinkGroupUpdateRequest;
import com.hutu.shortlinklink.domain.resp.LinkGroupResp;

import java.util.List;

/**
* @author EDY
* @description 针对表【link_group(短链分组表)】的数据库操作Service
* @createDate 2025-07-25 14:52:42
*/
public interface LinkGroupService extends IService<LinkGroup> {
    /**
     * 保存短链分组
     */
    Boolean saveLinkGroup(LinkGroupAddRequest addRequest);

    /**
     * 删除短链分组
     */
    Boolean delLinkGroup(Long groupId);

    /**
     * 详情
     */
    LinkGroupResp detail(Long groupId);

    /**
     * 查询用户所有分组
     */
    List<LinkGroupResp> findUserAllLinkGroup();

    /**
     * 修改短链分组
     */
    Boolean updateLinkGroup(LinkGroupUpdateRequest request);

    /**
     * 通过账号和ID查询分组
     */
    LinkGroup findByAccountNoAndId(Long accountNo, Long id);
}
