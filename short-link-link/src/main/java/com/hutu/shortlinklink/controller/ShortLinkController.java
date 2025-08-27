package com.hutu.shortlinklink.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hutu.shortlinkcommon.common.BaseResponse;
import com.hutu.shortlinkcommon.util.ResultUtils;
import com.hutu.shortlinklink.domain.pojo.GroupCodeMapping;
import com.hutu.shortlinklink.domain.req.ShortLinkAddRequest;
import com.hutu.shortlinklink.domain.req.ShortLinkDelRequest;
import com.hutu.shortlinklink.domain.req.ShortLinkPageRequest;
import com.hutu.shortlinklink.service.ShortLinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/link/v1")
@RequiredArgsConstructor
public class ShortLinkController {

    private final ShortLinkService shortLinkService;

    @PostMapping("add")
    public BaseResponse<Boolean> createShortLink(@RequestBody ShortLinkAddRequest request){
        return ResultUtils.success(shortLinkService.createShortLink(request));
    }

    /**
     * 分页查找短链 -- 根据分组id
     */

    @PostMapping("page")
    public BaseResponse<Page<GroupCodeMapping>> pageByGroupId(@RequestBody ShortLinkPageRequest pageRequest){
        Page<GroupCodeMapping> page =  shortLinkService.pageByGroupId(pageRequest);
        return ResultUtils.success(page);

    }


    /**
     * 删除短链
     * @param request
     * @return
     */
    @PostMapping("del")
    public BaseResponse<Boolean> del(@RequestBody ShortLinkDelRequest request){
        return ResultUtils.success(shortLinkService.del(request));
    }
}

