package com.huanshen.scaffolding.security.auth;

import cn.hutool.extra.spring.SpringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * AuthFactory
 *
 */
public class AuthFactory {

    public final static AuthFactory INS = new AuthFactory();
    private final static Map<AuthTypeEnum, AuthService> map = new HashMap<>();

    private AuthFactory() {
    }

    public AuthService getAuthService(AuthTypeEnum typeEnum) {
        if (map.isEmpty()) {
            init();
        }
        return map.get(typeEnum);
    }

    private void init() {
        Map<String, AuthService> beanMap = SpringUtil.getBeansOfType(AuthService.class);
        if (beanMap == null || beanMap.isEmpty()) {
            return;
        }
        for (AuthService authService : beanMap.values()) {
            map.put(authService.type(), authService);
        }
    }
}
