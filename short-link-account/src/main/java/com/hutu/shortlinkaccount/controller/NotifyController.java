package com.hutu.shortlinkaccount.controller;

import com.google.code.kaptcha.Producer;
import com.hutu.shortlinkaccount.utils.MailUtil;
import com.hutu.shortlinkcommon.common.BaseResponse;
import com.hutu.shortlinkcommon.enums.BizCodeEnum;
import com.hutu.shortlinkcommon.exception.BizException;
import com.hutu.shortlinkcommon.util.CommonUtil;
import com.hutu.shortlinkcommon.util.ResultUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;



@RestController
@RequestMapping("/api/v1/notify")
@RequiredArgsConstructor
@Slf4j
public class NotifyController {


    private final Producer captchaProducer;

    private final RedisTemplate<String,String> redisTemplate;

    private final MailUtil mailUtil;


    /**
     * 验证码过期时间
     */
    private static final long CAPTCHA_CODE_EXPIRED = 1000 * 10 *  60;

    /**
     * 生成验证码
     * @param request
     * @param response
     */
    @GetMapping("captcha")
    public BaseResponse<Void> getCaptcha(HttpServletRequest request, HttpServletResponse response){
        // 获取验证码内容
        String captchaText = captchaProducer.createText();
        log.info("验证码内容:{}",captchaText);
        //存储redis,配置过期时间
        redisTemplate.opsForValue().set(getCaptchaKey(request),captchaText,CAPTCHA_CODE_EXPIRED,TimeUnit.MILLISECONDS);
        BufferedImage bufferedImage = captchaProducer.createImage(captchaText);
        try (ServletOutputStream outputStream = response.getOutputStream()){
            ImageIO.write(bufferedImage,"jpg",outputStream);
            outputStream.flush();
        } catch (IOException e) {
            log.error("获取流出错:{}",e.getMessage());
        }
        return ResultUtils.success(null);
    }



    private String getCaptchaKey(HttpServletRequest request){
        String ip = CommonUtil.getIpAddr(request);
        // TODO 换成用户 id
        String userAgent = request.getHeader("User-Agent");
        String key = "account-service:captcha:"+CommonUtil.MD5(ip+userAgent);
        log.info("验证码key:{}",key);
        return key;
    }





    /**
     * 发送邮箱
     */
    @GetMapping("send_code")
    public BaseResponse<Boolean> sendCode(@RequestParam("to") String to, HttpServletRequest request){
        try {
            mailUtil.sendVerificationCodeMail(to);
        } catch (Exception e){
            log.error("发送邮件异常:{}",e.getMessage());
            throw new BizException(BizCodeEnum.MAIL_SEND_ERROR);
        }
        return ResultUtils.success(true);
    }





}
