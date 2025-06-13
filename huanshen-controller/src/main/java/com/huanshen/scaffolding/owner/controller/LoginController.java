package com.huanshen.scaffolding.owner.controller;

import com.huanshen.scaffolding.business.user.IUserService;
import com.huanshen.scaffolding.security.auth.AuthTypeEnum;
import com.huanshen.scaffolding.security.auth.CommonAuth;
import com.huanshen.scaffolding.security.domain.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * LoginController
 *
 */
@RestController("/api/v1/user")
public class LoginController {

    @Autowired
    private IUserService userService;

    @GetMapping("/login")
    @CommonAuth(type = AuthTypeEnum.NO_AUTH)
    public LoginUser login(String account, String password) {
        return userService.login(account, password);
    }
}
