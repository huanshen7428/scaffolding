package com.huanshen.scaffolding.business.common.auth.impl;

import com.alibaba.fastjson.JSONObject;
import com.huanshen.scaffolding.common.exception.CommonAssert;
import com.huanshen.scaffolding.common.exception.ExceptionEnums;
import com.huanshen.scaffolding.security.auth.AuthService;
import com.huanshen.scaffolding.security.auth.AuthTypeEnum;
import com.huanshen.scaffolding.security.constant.SecurityConsts;
import com.huanshen.scaffolding.security.domain.LoginUser;
import com.huanshen.scaffolding.security.utils.AesCipherUtils;
import com.huanshen.scaffolding.security.utils.JwtUtils;
import com.huanshen.scaffolding.security.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * TokenAuthService
 *
 */
@Component
public class TokenAuthService implements AuthService {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public String getAccount(HttpServletRequest request) {
        String authorization = request.getHeader(SecurityConsts.REQUEST_HEADER_AUTH);
        CommonAssert.notEmpty(authorization, ExceptionEnums.EMPTY_AUTHORIZATION);

        String token = null;
        try {
            token = AesCipherUtils.INS.deCrypto(authorization);
        } catch (Exception e) {
            CommonAssert.error(ExceptionEnums.INVALID_TOKEN);
        }
        boolean verifyToken = JwtUtils.INS.verify(token);
        CommonAssert.isTrue(verifyToken, ExceptionEnums.INVALID_TOKEN);

        return JwtUtils.INS.getClaim(token, SecurityConsts.ACCOUNT);
    }

    @Override
    public LoginUser getLoginUser(AuthTypeEnum authType, String account) {
        String tokenKey = SecurityConsts.LOGIN_TOKEN_KEY + account;
        if (Boolean.FALSE.equals(redisTemplate.hasKey(tokenKey))) {
            ResponseUtil.response(ExceptionEnums.INVALID_TOKEN);
            return null;
        }
        String userJson = redisTemplate.opsForValue().get(tokenKey);
        LoginUser loginUser = JSONObject.parseObject(userJson, LoginUser.class);
        loginUser.setLang(getLang());
        return loginUser;
    }

    @Override
    public AuthTypeEnum type() {
        return AuthTypeEnum.TOKEN;
    }

}
