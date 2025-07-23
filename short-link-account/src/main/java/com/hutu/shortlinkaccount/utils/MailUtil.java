package com.hutu.shortlinkaccount.utils;

import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Component
public class MailUtil {

    // 限流相关常量
    private static final String RATE_LIMIT_KEY_PREFIX = "MAIL_RATE_LIMIT:";
    private static final String DAILY_LIMIT_KEY_PREFIX = "MAIL_DAILY_LIMIT:";
    public static final String VERIFICATION_CODE_KEY_PREFIX = "VERIFICATION_CODE:";
    private static final int RATE_LIMIT_EXPIRE_MINUTES = 1;
    private static final int VERIFICATION_CODE_EXPIRE_MINUTES = 5;
    private static final int DAILY_LIMIT_COUNT = 10;

    @Resource
    private JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String from;
    @Resource
    private RedisTemplate<String, String> redisTemplate;

    // 限流Lua脚本（原子操作）
    private static final String RATE_LIMIT_LUA_SCRIPT =
            "local rateKey = KEYS[1]\n" +
                    "local dailyKey = KEYS[2]\n" +
                    "local rateExpire = tonumber(ARGV[1])\n" +
                    "local dailyExpire = tonumber(ARGV[2])\n" +
                    "local dailyLimit = tonumber(ARGV[3])\n" +
                    "\n" +
                    "-- 兜底参数\n" +
                    "if not rateExpire then rateExpire = 60 end\n" +
                    "if not dailyExpire then dailyExpire = 86400 end\n" +
                    "if not dailyLimit then dailyLimit = 10 end\n" +
                    "\n" +
                    "-- 检查频率限制\n" +
                    "if redis.call('exists', rateKey) == 1 then\n" +
                    "    return 0\n" +
                    "end\n" +
                    "\n" +
                    "-- 检查每日限制\n" +
                    "local dailyValue = redis.call('get', dailyKey)\n" +
                    "local dailyCount = tonumber(dailyValue)\n" +
                    "if not dailyCount then dailyCount = 0 end\n" +
                    "\n" +
                    "if dailyCount >= dailyLimit then\n" +
                    "    return 0\n" +
                    "end\n" +
                    "\n" +
                    "-- 通过检查，设置限制\n" +
                    "redis.call('setex', rateKey, rateExpire, '1')\n" +
                    "redis.call('incr', dailyKey)\n" +
                    "if dailyCount == 0 then\n" +
                    "    redis.call('expire', dailyKey, dailyExpire)\n" +
                    "end\n" +
                    "return 1";

    /**
     * 异步方法
     * 默认是8个线程 原始配置类 TaskExecutionProperties -- 效率之所以高是因为把任务当到了阻塞队列中
     * 发送验证码邮件（带限流）
     * @param to 目标邮箱
     * @throws MessagingException
     */
    @Async("threadExecutor")
    public void sendVerificationCodeMail(String to) throws MessagingException {
        // 1. 生成限流Key
        String rateLimitKey = RATE_LIMIT_KEY_PREFIX + ":" + to;
        String dailyLimitKey = DAILY_LIMIT_KEY_PREFIX + ":" + to;

        // 2. 执行限流检查
        if (!passRateLimitCheck(rateLimitKey, dailyLimitKey)) {
            throw new RuntimeException("发送频率过高，请稍后再试");
        }

        // 3. 生成并存储验证码
        String verificationCode = generateVerificationCode();
        storeVerificationCode(to, verificationCode);

        // 4. 发送HTML邮件
        String subject = "您的验证码";
        String htmlContent = String.format(
                "<html><body>" +
                        "<p>您的验证码是：<strong>%s</strong></p>" +
                        "<p>有效期为5分钟，请尽快验证。</p>" +
                        "</body></html>",
                verificationCode
        );
        MailUtil proxy = (MailUtil) AopContext.currentProxy();
        proxy.sendHtmlMail(to, subject, htmlContent);
    }

    /**
     * 使用Lua脚本进行原子限流检查
     */
    private boolean passRateLimitCheck(String rateLimitKey, String dailyLimitKey) {
        // 计算当天剩余秒数
        long remainSecondsToday = getRemainSecondsToday();

        // 创建Redis脚本
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(
                RATE_LIMIT_LUA_SCRIPT,
                Long.class
        );

        // 执行脚本
        Long result = redisTemplate.execute(
                redisScript,
                Arrays.asList(rateLimitKey, dailyLimitKey),
                String.valueOf(RATE_LIMIT_EXPIRE_MINUTES * 60),
                String.valueOf(remainSecondsToday),
                String.valueOf(DAILY_LIMIT_COUNT)
        );

        return result != null && result == 1;
    }

    /**
     * 存储验证码到Redis（5分钟有效期）
     */
    private void storeVerificationCode(String email, String code) {
        String key = VERIFICATION_CODE_KEY_PREFIX + email;
        redisTemplate.opsForValue().set(
                key,
                code,
                VERIFICATION_CODE_EXPIRE_MINUTES,
                TimeUnit.MINUTES
        );
    }

    /**
     * 获取当天剩余秒数
     */
    private long getRemainSecondsToday() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime midnight = now.toLocalDate().atTime(23, 59, 59);
        return Duration.between(now, midnight).getSeconds() + 1;
    }

    /**
     * 生成4位随机验证码
     */
    public String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(10000);
        return String.format("%04d", code);
    }

    /**
     * 发送HTML邮件
     */
    public void sendHtmlMail(String to, String subject, String html) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(html, true);
        mailSender.send(message);
    }

    /**
     * 验证验证码（业务层调用）
     * @param email 邮箱
     * @param code 用户输入的验证码
     * @return 是否验证成功
     */
    public boolean verifyCode(String email, String code) {
        String key = VERIFICATION_CODE_KEY_PREFIX + email;
        String storedCode = redisTemplate.opsForValue().get(key);
        return code != null && code.equals(storedCode);
    }
}