package com.hutu.shortlinklink.controller;
import com.hutu.shortlinkcommon.common.BaseResponse;
import com.hutu.shortlinkcommon.util.ResultUtils;
import com.hutu.shortlinklink.domain.req.LinkGroupAddRequest;
import com.hutu.shortlinklink.domain.req.LinkGroupUpdateRequest;
import com.hutu.shortlinklink.domain.resp.LinkGroupResp;
import com.hutu.shortlinklink.service.LinkGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/group/v1")
@RequiredArgsConstructor
public class LinkGroupController {

    private final LinkGroupService linkGroupService;

    /**
     * 创建分组
     */
    @PostMapping("/add")
    public BaseResponse<Boolean> saveLinkGroup(@RequestBody LinkGroupAddRequest addRequest){
        return ResultUtils.success(linkGroupService.saveLinkGroup(addRequest));
    }


    /**
     * 根据id删除分组
     */
    @DeleteMapping("/del/{group_id}")
    public BaseResponse<Boolean> delLinkGroup(@PathVariable("group_id") Long groupId){
        return ResultUtils.success(linkGroupService.delLinkGroup(groupId));
    }


    /**
     * 根据id找详情
     */
    @GetMapping("detail/{group_id}")
    public BaseResponse<LinkGroupResp> detail(@PathVariable("group_id") Long groupId){
        return ResultUtils.success(linkGroupService.detail(groupId));

    }


    /**
     * 列出用户全部分组
     */
    @GetMapping("list")
    public BaseResponse<List<LinkGroupResp>> findUserAllLinkGroup(){
        return ResultUtils.success(linkGroupService.findUserAllLinkGroup());
    }



    /**
     * 更新组名
     */
    @PutMapping("update")
    public BaseResponse<Boolean> updateLinkGroup(@RequestBody LinkGroupUpdateRequest request){
        return ResultUtils.success(linkGroupService.updateLinkGroup(request));
    }
}

