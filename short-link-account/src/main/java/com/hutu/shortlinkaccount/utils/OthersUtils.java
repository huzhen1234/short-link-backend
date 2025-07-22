package com.hutu.shortlinkaccount.utils;

import com.hutu.shortlinkcommon.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OthersUtils {

    public static String getCaptchaKey(Long accountId) {
        // todo
        String key = "account-service:captcha:"+ CommonUtil.MD5(String.valueOf(accountId));
        log.info("验证码key:{}",key);
        return key;
    }
}
