package com.hutu.shortlinkaccount.controller;


import com.hutu.shortlinkcommon.common.BaseResponse;
import com.hutu.shortlinkcommon.config.MinioUtil;
import com.hutu.shortlinkcommon.util.ResultUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountController {

    private final MinioUtil minioUtil;


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

