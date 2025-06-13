package com.huanshen.scaffolding.business.common.auth.impl;

import com.huanshen.scaffolding.common.exception.CommonException;
import com.huanshen.scaffolding.security.auth.AuthService;
import com.huanshen.scaffolding.security.auth.AuthTypeEnum;
import com.huanshen.scaffolding.security.domain.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * or逻辑的demo
 *
 */
@Component
public class TokenOrNoAuthService implements AuthService {
    @Autowired
    private TokenAuthService tokenAuthService;
    @Autowired
    private NoAuthService noAuthService;

    @Override
    public String getAccount(HttpServletRequest request) {
        String token;
        try {
            token = tokenAuthService.getAccount(request);
        } catch (CommonException e) {
            token = noAuthService.getAccount(request);
        }
        return token;
    }

    @Override
    public LoginUser getLoginUser(AuthTypeEnum authType, String account) {
        if (authType == AuthTypeEnum.NO_AUTH) {
            return noAuthService.getLoginUser(authType, account);
        }
        if (authType == AuthTypeEnum.TOKEN) {
            return tokenAuthService.getLoginUser(authType, account);
        }
        return null;
    }

    @Override
    public AuthTypeEnum type() {
        return AuthTypeEnum.TOKEN_OR_NO_AUTH;
    }
}
