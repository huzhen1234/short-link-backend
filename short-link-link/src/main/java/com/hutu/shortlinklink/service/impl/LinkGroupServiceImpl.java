package com.hutu.shortlinklink.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hutu.shortlinkcommon.common.CurrentAccountInfo;
import com.hutu.shortlinkcommon.enums.BizCodeEnum;
import com.hutu.shortlinkcommon.interceptor.LoginInterceptor;
import com.hutu.shortlinkcommon.util.AssertUtils;
import com.hutu.shortlinklink.domain.pojo.LinkGroup;
import com.hutu.shortlinklink.domain.req.LinkGroupAddRequest;
import com.hutu.shortlinklink.domain.req.LinkGroupUpdateRequest;
import com.hutu.shortlinklink.domain.resp.LinkGroupResp;
import com.hutu.shortlinklink.service.LinkGroupService;
import com.hutu.shortlinklink.mapper.LinkGroupMapper;
import org.springframework.beans.BeanUtils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author hutu
* @description 针对表【link_group(短链分组表)】的数据库操作Service实现
* @createDate 2025-07-25 14:52:42
*/
@Service
@RequiredArgsConstructor
public class LinkGroupServiceImpl extends ServiceImpl<LinkGroupMapper, LinkGroup>
    implements LinkGroupService{

    @Override
    public Boolean saveLinkGroup(LinkGroupAddRequest addRequest) {
        CurrentAccountInfo currentAccountInfo = LoginInterceptor.threadLocal.get();
        AssertUtils.notNull(currentAccountInfo, BizCodeEnum.JWT_PARSE_ERROR);
        LinkGroup group = LinkGroup.builder()
                .title(addRequest.getTitle())
                .accountNo(currentAccountInfo.getAccountNo())
                .build();
        boolean save = save(group);
        AssertUtils.isTrue(save, BizCodeEnum.GROUP_ADD_FAIL);
        return Boolean.TRUE;
    }

    @Override
    public Boolean delLinkGroup(Long groupId) {
        CurrentAccountInfo currentAccountInfo = LoginInterceptor.threadLocal.get();
        AssertUtils.notNull(currentAccountInfo, BizCodeEnum.JWT_PARSE_ERROR);
        boolean removed = lambdaUpdate()
                .eq(LinkGroup::getId, groupId)
                .eq(LinkGroup::getAccountNo, currentAccountInfo.getAccountNo())
                .remove();
        AssertUtils.isTrue(removed, BizCodeEnum.GROUP_DEL_FAIL);
        return Boolean.TRUE;
    }

    @Override
    public LinkGroupResp detail(Long groupId) {
        LinkGroupResp linkGroupResp = new LinkGroupResp();
        CurrentAccountInfo currentAccountInfo = LoginInterceptor.threadLocal.get();
        AssertUtils.notNull(currentAccountInfo, BizCodeEnum.JWT_PARSE_ERROR);
        LinkGroup group = lambdaQuery().eq(LinkGroup::getId, groupId)
                .eq(LinkGroup::getAccountNo, currentAccountInfo.getAccountNo())
                .one();
        AssertUtils.notNull(group, BizCodeEnum.GROUP_NOT_EXIST);
        BeanUtils.copyProperties(group, linkGroupResp);
        return linkGroupResp;
    }

    @Override
    public List<LinkGroupResp> findUserAllLinkGroup() {
        CurrentAccountInfo currentAccountInfo = LoginInterceptor.threadLocal.get();
        AssertUtils.notNull(currentAccountInfo, BizCodeEnum.JWT_PARSE_ERROR);
        List<LinkGroup> groupList = lambdaQuery().eq(LinkGroup::getAccountNo, currentAccountInfo.getAccountNo())
                .list();
        return groupList.stream().map(group -> {
            LinkGroupResp linkGroupResp = new LinkGroupResp();
            BeanUtils.copyProperties(group, linkGroupResp);
            return linkGroupResp;
        }).toList();
    }

    @Override
    public Boolean updateLinkGroup(LinkGroupUpdateRequest request) {
        CurrentAccountInfo currentAccountInfo = LoginInterceptor.threadLocal.get();
        AssertUtils.notNull(currentAccountInfo, BizCodeEnum.JWT_PARSE_ERROR);
        boolean update = lambdaUpdate().eq(LinkGroup::getId, request.getId())
                .eq(LinkGroup::getAccountNo, currentAccountInfo.getAccountNo())
                .set(LinkGroup::getTitle, request.getTitle())
                .update();
        AssertUtils.isTrue(update, BizCodeEnum.GROUP_OPER_FAIL);
        return Boolean.TRUE;
    }

    @Override
    public LinkGroup findByAccountNoAndId(Long accountNo, Long id) {
        return lambdaQuery().eq(LinkGroup::getId, id)
                .eq(LinkGroup::getAccountNo, accountNo)
                .one();
    }
}




