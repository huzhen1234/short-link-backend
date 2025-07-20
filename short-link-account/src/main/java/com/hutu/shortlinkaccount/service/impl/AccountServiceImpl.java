package com.hutu.shortlinkaccount.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hutu.shortlinkaccount.domain.Account;
import com.hutu.shortlinkaccount.service.AccountService;
import com.hutu.shortlinkaccount.mapper.AccountMapper;
import org.springframework.stereotype.Service;

/**
* @author huzhen
* @description 针对表【account(用户表)】的数据库操作Service实现
* @createDate 2025-07-20 16:31:38
*/
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account>
    implements AccountService{

}




