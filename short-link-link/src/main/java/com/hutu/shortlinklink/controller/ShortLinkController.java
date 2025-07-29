package com.hutu.shortlinklink.controller;


import com.hutu.shortlinkcommon.common.BaseResponse;
import com.hutu.shortlinkcommon.util.ResultUtils;
import com.hutu.shortlinklink.domain.req.ShortLinkAddRequest;
import com.hutu.shortlinklink.service.ShortLinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/link/v1")
@RequiredArgsConstructor
public class ShortLinkController {

    private final ShortLinkService shortLinkService;

    @PostMapping("add")
    public BaseResponse<Boolean> createShortLink(@RequestBody ShortLinkAddRequest request){
        return ResultUtils.success(shortLinkService.createShortLink(request));
    }

}

