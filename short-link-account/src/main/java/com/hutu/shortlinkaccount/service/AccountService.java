package com.hutu.shortlinkaccount.service;

import com.hutu.shortlinkaccount.domain.pojo.Account;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hutu.shortlinkaccount.domain.req.AccountLoginReq;
import com.hutu.shortlinkaccount.domain.req.AccountRegisterReq;

/**
* @author huzhen
* @description 针对表【account(用户表)】的数据库操作Service
* @createDate 2025-07-20 16:31:38
*/
public interface AccountService extends IService<Account> {

    void register(AccountRegisterReq accountRegisterReq);

    String login(AccountLoginReq request);
}
