package com.hutu.shortlinkaccount.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hutu.shortlinkaccount.domain.pojo.Account;
import com.hutu.shortlinkaccount.domain.req.AccountLoginReq;
import com.hutu.shortlinkaccount.domain.req.AccountRegisterReq;
import com.hutu.shortlinkaccount.service.AccountService;
import com.hutu.shortlinkaccount.mapper.AccountMapper;
import com.hutu.shortlinkaccount.utils.OthersUtils;
import com.hutu.shortlinkcommon.common.CurrentAccountInfo;
import com.hutu.shortlinkcommon.enums.AuthTypeEnum;
import com.hutu.shortlinkcommon.enums.BizCodeEnum;
import com.hutu.shortlinkcommon.util.AssertUtils;
import com.hutu.shortlinkcommon.util.CommonUtil;
import com.hutu.shortlinkcommon.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.Md5Crypt;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.hutu.shortlinkaccount.utils.MailUtil.VERIFICATION_CODE_KEY_PREFIX;

/**
* @author huzhen
* @description 针对表【account(用户表)】的数据库操作Service实现
* @createDate 2025-07-20 16:31:38
*/
@Service
@RequiredArgsConstructor
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account>
    implements AccountService{

    private final RedisTemplate<String,String> redisTemplate;

    @Override
    public void register(AccountRegisterReq accountRegisterReq) {
        // 校验图形验证码
        verifyCaptcha(accountRegisterReq.getCaptcha());
        // 校验邮箱验证码
        verifyEmailCaptcha(accountRegisterReq.getMail(), accountRegisterReq.getMailCode());
        // 检查唯一性 手机号对应一个对应的邮件
        verifyUniqueness(accountRegisterReq.getPhone(),accountRegisterReq.getMail());
        boolean save = save(fillAccountInfo(accountRegisterReq));
        AssertUtils.isTrue(save, BizCodeEnum.SAVE_USER_INFO_FAIL);
        // TODO 发放福利，发放流量包
    }

    @Override
    public String login(AccountLoginReq request) {
        String phone = request.getPhone();
        Account account = lambdaQuery().eq(Account::getPhone, phone).one();
        AssertUtils.notNull(account, BizCodeEnum.USER_NOT_EXIST);
        AssertUtils.isTrue(Md5Crypt.md5Crypt(request.getPwd().getBytes(),account.getSecret()).equals(account.getPwd()), BizCodeEnum.USER_PWD_ERROR);
        CurrentAccountInfo currentAccountInfo = CurrentAccountInfo.builder().build();
        BeanUtils.copyProperties(account,currentAccountInfo);
        return JWTUtil.geneJsonWebToken(currentAccountInfo);
    }

    private void verifyUniqueness(String phone, String mail) {
        Account account = lambdaQuery().eq(Account::getPhone, phone).one();
        AssertUtils.isNull(account, BizCodeEnum.PHONE_REPEAT);
        account = lambdaQuery().eq(Account::getMail, mail).one();
        AssertUtils.isNull(account, BizCodeEnum.MAIL_REPEAT);
    }

    private Account fillAccountInfo(AccountRegisterReq accountRegisterReq) {
        Account account = new Account();
        BeanUtils.copyProperties(accountRegisterReq,account);
        //认证级别
        account.setAuth(AuthTypeEnum.DEFAULT.name());
        //生成唯一的账号  TODO
        account.setAccountNo(CommonUtil.getCurrentTimestamp());
        //设置密码 秘钥 盐
        account.setSecret("$1$"+CommonUtil.getStringNumRandom(8));
        String cryptPwd = Md5Crypt.md5Crypt(account.getPwd().getBytes(),account.getSecret());
        account.setPwd(cryptPwd);
        return account;
    }

    private void verifyEmailCaptcha(String mail, String mailCode) {
        String key = VERIFICATION_CODE_KEY_PREFIX + mail;
        String code = redisTemplate.opsForValue().get(key);
        AssertUtils.notNull(code, BizCodeEnum.MAIL_CAPTCHA_EXPIRED);
        // 判断是否相等
        AssertUtils.isTrue(Objects.equals(mailCode,code), BizCodeEnum.MAIL_CAPTCHA_ERROR);
    }

    private void verifyCaptcha(String captcha) {
        // 判断是否过期
        String cacheCaptcha = redisTemplate.opsForValue().get(OthersUtils.getCaptchaKey(1L));
        AssertUtils.notNull(cacheCaptcha, BizCodeEnum.CAPTCHA_CODE_EXPIRED);
        // 判断是否相等
        AssertUtils.isTrue(Objects.equals(cacheCaptcha,captcha), BizCodeEnum.CAPTCHA_ERROR);
    }
}




