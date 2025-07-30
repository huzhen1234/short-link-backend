package com.hutu.shortlinklink.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hutu.shortlinkcommon.common.CurrentAccountInfo;
import com.hutu.shortlinkcommon.enums.BizCodeEnum;
import com.hutu.shortlinkcommon.interceptor.LoginInterceptor;
import com.hutu.shortlinkcommon.util.AssertUtils;
import com.hutu.shortlinkcommon.util.CommonUtil;
import com.hutu.shortlinklink.domain.pojo.LinkGroup;
import com.hutu.shortlinklink.domain.pojo.ShortLink;
import com.hutu.shortlinklink.domain.req.ShortLinkAddRequest;
import com.hutu.shortlinklink.domain.vo.ShortLinkVO;
import com.hutu.shortlinklink.service.LinkGroupService;
import com.hutu.shortlinklink.service.ShortLinkService;
import com.hutu.shortlinklink.mapper.ShortLinkMapper;
import com.hutu.shortlinklink.utils.ShortLinkUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
* @author hutu
* @description 针对表【short_link(短链接信息表)】的数据库操作Service实现
* @createDate 2025-07-25 14:52:42
*/
@Service
@RequiredArgsConstructor
public class ShortLinkServiceImpl extends ServiceImpl<ShortLinkMapper, ShortLink>
    implements ShortLinkService{

    private final LinkGroupService linkGroupService;
    private final ShortLinkUtil shortLinkUtil;

    @Override
    public Boolean createShortLink(ShortLinkAddRequest request) {
        CurrentAccountInfo currentAccountInfo = LoginInterceptor.threadLocal.get();
        AssertUtils.notNull(currentAccountInfo, BizCodeEnum.JWT_PARSE_ERROR);
        LinkGroup linkGroup = linkGroupService.findByAccountNoAndId(currentAccountInfo.getAccountNo(), request.getGroupId());
        AssertUtils.notNull(linkGroup, BizCodeEnum.GROUP_NOT_EXIST);
        ShortLink shortLink = new ShortLink();
        BeanUtils.copyProperties(request, shortLink);
        shortLink.setSign(CommonUtil.MD5(request.getOriginalUrl()));
        shortLink.setCode(shortLinkUtil.createShortLinkCode(request.getOriginalUrl()));
        boolean save = save(shortLink);
        AssertUtils.isTrue(save, BizCodeEnum.LINK_ADD_FAIL);
        return Boolean.TRUE;
    }

    @Override
    public ShortLinkVO parseShortLinkCode(String shortLinkCode) {
        ShortLink one = lambdaQuery().eq(ShortLink::getCode, shortLinkCode).one();
        if (one != null) {
            ShortLinkVO shortLinkVO = new ShortLinkVO();
            BeanUtils.copyProperties(one, shortLinkVO);
            return shortLinkVO;
        }
        return null;
    }
}




