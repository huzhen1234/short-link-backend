package com.hutu.shortlinkaccount.controller;


import com.hutu.shortlinkaccount.domain.req.AccountRegisterReq;
import com.hutu.shortlinkaccount.service.AccountService;
import com.hutu.shortlinkcommon.common.BaseResponse;
import com.hutu.shortlinkcommon.config.MinioUtil;
import com.hutu.shortlinkcommon.util.ResultUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountController {

    private final MinioUtil minioUtil;

    private final AccountService accountService;

    // 注册接口
    @PostMapping("register")
    public BaseResponse<String> register(@Valid @RequestBody AccountRegisterReq accountRegisterReq){
        accountService.register(accountRegisterReq);
        return ResultUtils.success("注册成功");
    }


    /**
     * 上传头像
     * @param file 文件
     * @return  url
     */
    @PostMapping("upload")
    public BaseResponse<String> uploadAvatar(@RequestPart("file")MultipartFile file){
        String url = minioUtil.uploadFile(file,"test");
        return ResultUtils.success(url);
    }



}

