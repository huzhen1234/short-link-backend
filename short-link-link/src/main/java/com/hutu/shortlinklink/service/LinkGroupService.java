package com.hutu.shortlinklink.service;

import com.hutu.shortlinklink.domain.pojo.LinkGroup;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hutu.shortlinklink.domain.req.LinkGroupAddRequest;

/**
* @author EDY
* @description 针对表【link_group(短链分组表)】的数据库操作Service
* @createDate 2025-07-25 14:52:42
*/
public interface LinkGroupService extends IService<LinkGroup> {

    boolean saveLinkGroup(LinkGroupAddRequest addRequest);

}
