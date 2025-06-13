package com.huanshen.scaffolding.business.common.auth.impl;

import com.huanshen.scaffolding.security.auth.AuthService;
import com.huanshen.scaffolding.security.auth.AuthTypeEnum;
import com.huanshen.scaffolding.security.domain.LoginUser;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * NoAuthService
 *
 * @description
 * @date 2024-04-03 11:08
 */
@Component
public class NoAuthService implements AuthService {

    @Override
    public String getAccount(HttpServletRequest request) {
        return "default";
    }

    @Override
    public LoginUser getLoginUser(AuthTypeEnum authType, String account) {
        LoginUser loginUser = new LoginUser();
        loginUser.setUserName("default");
        loginUser.setAccount("default");
        loginUser.setIsOwner(false);
        return loginUser;
    }

    @Override
    public AuthTypeEnum type() {
        return AuthTypeEnum.NO_AUTH;
    }

}
