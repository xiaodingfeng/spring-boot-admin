package com.ciicgat.circlefkbff.common.sdk.lang.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.ciicgat.circlefkbff.common.sdk.lang.exception.BusinessRuntimeException;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JWTUtil {

    //定义token返回头部
    private String header;

    //token前缀
    private String tokenPrefix;

    //签名密钥
    private String secret;

    //有效期
    private long expireTime;

    //存进客户端的token的key名
    public static final String USER_LOGIN_TOKEN = "USER_LOGIN_TOKEN";


    /**
     * 创建TOKEN
     * @param sub
     * @return
     */
    public String createToken(String sub){
//        return tokenPrefix + JWT.create()
//                .withSubject(sub)
//                .withExpiresAt(new Date(System.currentTimeMillis() + expireTime))
//                .sign(Algorithm.HMAC512(secret));

        return JWT.create()
                .withSubject(sub)
                .withExpiresAt(new Date(System.currentTimeMillis() + expireTime))
                .sign(Algorithm.HMAC512(secret));
    }


    /**
     * 验证token
     * @param token
     */
    public String validateToken(String token){
        try {
            return JWT.require(Algorithm.HMAC512(secret))
                    .build()
                    .verify(token.replace(tokenPrefix, ""))
                    .getSubject();
        } catch (TokenExpiredException e){
            throw new BusinessRuntimeException(-1100110011, "token已经过期");
        } catch (Exception e){
            throw new BusinessRuntimeException(-1100110011, "token验证失败");
        }
    }

    /**
     * 检查token是否需要更新
     * @param token
     * @return
     */
    public boolean isNeedUpdate(String token){
        //获取token过期时间
        Date expiresAt = null;
        try {
            expiresAt = JWT.require(Algorithm.HMAC512(secret))
                    .build()
                    .verify(token.replace(tokenPrefix, ""))
                    .getExpiresAt();
        } catch (TokenExpiredException e){
            return true;
        } catch (Exception e){
            throw new BusinessRuntimeException(-1, "token验证失败");
        }
        //如果剩余过期时间少于过期时常的一半时 需要更新
        return (expiresAt.getTime() - System.currentTimeMillis()) < (expireTime>>1);
    }
}
