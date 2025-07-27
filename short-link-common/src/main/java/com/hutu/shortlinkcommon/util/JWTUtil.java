package com.hutu.shortlinkcommon.util;

import com.hutu.shortlinkcommon.common.CurrentAccountInfo;
import com.hutu.shortlinkcommon.enums.BizCodeEnum;
import com.hutu.shortlinkcommon.exception.BizException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;


@Slf4j
public class JWTUtil {


    /**
     * 主题
     */
    private static final String SUBJECT = "hutu_link";

    /**
     * 加密密钥
     */
    private static final String SECRET = "93b2636e4bcd48e68e4f1a9df2f43a56";

    /**
     * 令牌前缀
     */
    private static final String TOKEN_PREFIX = "hutu_short_link";


    /**
     * token过期时间，7天
     */
    private static final long EXPIRED = 1000 * 60 * 60 * 24 * 7;


    /**
     * 生成token
     *
     * @param loginUser
     * @return
     */
    public static String geneJsonWebToken(CurrentAccountInfo loginUser) {
        String token = Jwts.builder().setSubject(SUBJECT)
                //配置payload TODO 添加ip 防止jwt令牌盗用
                .claim("head_img", loginUser.getHeadImg())
                .claim("account_no", loginUser.getAccountNo())
                .claim("username", loginUser.getUsername())
                .claim("mail", loginUser.getMail())
                .claim("phone", loginUser.getPhone())
                .claim("auth", loginUser.getAuth())
                .setIssuedAt(new Date())
                .setExpiration(new Date(CommonUtil.getCurrentTimestamp() + EXPIRED))
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8)),SignatureAlgorithm.HS256).compact();
        token = TOKEN_PREFIX + token;
        return token;
    }


    /**
     * 解密jwt
     */
    public static Claims checkJWT(String token) {
        try {
            // 将SECRET转换为Key对象
            SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
            return Jwts.parserBuilder()
                    .setSigningKey(key)  // 使用Key对象
                    .build()
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                    .getBody();
        } catch (Exception e) {
            log.error("jwt 解密失败", e);  // 建议打印异常信息便于调试
            throw new BizException(BizCodeEnum.JWT_PARSE_ERROR);
        }

    }


}
