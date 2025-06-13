package com.huanshen.scaffolding.business.user;

import com.huanshen.scaffolding.security.domain.LoginUser;

/**
 * IUserService
 *
 */
public interface IUserService {
    LoginUser login(String account, String password);
}
