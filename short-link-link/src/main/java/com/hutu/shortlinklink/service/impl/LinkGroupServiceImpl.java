package com.hutu.shortlinklink.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hutu.shortlinkcommon.common.CurrentAccountInfo;
import com.hutu.shortlinkcommon.enums.BizCodeEnum;
import com.hutu.shortlinkcommon.interceptor.LoginInterceptor;
import com.hutu.shortlinkcommon.util.AssertUtils;
import com.hutu.shortlinklink.domain.pojo.LinkGroup;
import com.hutu.shortlinklink.domain.req.LinkGroupAddRequest;
import com.hutu.shortlinklink.service.LinkGroupService;
import com.hutu.shortlinklink.mapper.LinkGroupMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
* @author EDY
* @description 针对表【link_group(短链分组表)】的数据库操作Service实现
* @createDate 2025-07-25 14:52:42
*/
@Service
@RequiredArgsConstructor
public class LinkGroupServiceImpl extends ServiceImpl<LinkGroupMapper, LinkGroup>
    implements LinkGroupService{

    @Override
    public boolean saveLinkGroup(LinkGroupAddRequest addRequest) {
        CurrentAccountInfo currentAccountInfo = LoginInterceptor.threadLocal.get();
        AssertUtils.notNull(currentAccountInfo, BizCodeEnum.JWT_PARSE_ERROR);
        LinkGroup group = LinkGroup.builder()
                .title(addRequest.getTitle())
                .accountNo(currentAccountInfo.getAccountNo())
                .build();
        boolean save = save(group);
        AssertUtils.isTrue(save, BizCodeEnum.GROUP_ADD_FAIL);
        return true;
    }
}




