package com.huanshen.scaffolding.security.utils;

import com.huanshen.scaffolding.security.config.TokenConfigProperties;
import com.huanshen.scaffolding.security.constant.SecurityConsts;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * JwtUtils
 *
 * @description
 * @date 2023-11-24 15:57
 */
@Slf4j
public enum JwtUtils {
    INS;
    private TokenConfigProperties tokenConfigProperties;

    public void setConfig(TokenConfigProperties tokenConfigProperties) {
        this.tokenConfigProperties = tokenConfigProperties;
    }

    /**
     *
     * 校验token是否正确
     *
     * @param token
     * @return
     * @throws com.auth0.jwt.exceptions.TokenExpiredException
     */
    public boolean verify(String token) {
        String secret = getClaim(token, SecurityConsts.ACCOUNT) + this.tokenConfigProperties.getSecretKey();
        Algorithm algorithm = Algorithm.HMAC256(secret);
        JWTVerifier verifier = JWT.require(algorithm)
                .build();
        try {
            verifier.verify(token);
            return true;
        } catch (Exception e) {
            log.warn("verify token failed,", e);
            return false;
        }
    }

    public boolean verify(String token, String username) {
        String secret = getClaim(token, SecurityConsts.ACCOUNT) + this.tokenConfigProperties.getSecretKey();
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim(SecurityConsts.ACCOUNT, username)
                    .build();
            verifier.verify(token);
            return true;
        } catch (Exception e) {
            log.info("verifyToken = {}", e.getMessage());
            return false;
        }
    }

    /**
     * 获得Token中的信息无需secret解密也能获得
     *
     * @param token
     * @param claim
     * @return
     */
    public String getClaim(String token, String claim) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim(claim).asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 生成签名,n分钟后过期
     *
     * @param account 账号
     * @return token
     */
    public String sign(String account) {
        long currentTimeMillis = System.currentTimeMillis();
        // 帐号加JWT私钥加密
        String secret = account + this.tokenConfigProperties.getSecretKey();
        // 此处过期时间，单位:毫秒
        Date date = new Date(currentTimeMillis + this.tokenConfigProperties.getTokenExpireTime() * 60 * 1000L);
        Algorithm algorithm = Algorithm.HMAC256(secret);

        return JWT.create()
                .withClaim(SecurityConsts.ACCOUNT, account)
                .withClaim(SecurityConsts.CURRENT_TIME_MILLIS, String.valueOf(currentTimeMillis))
                .withExpiresAt(date)
                .sign(algorithm);
    }
}
