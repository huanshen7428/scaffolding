package com.huanshen.scaffolding.security.domain;

import com.huanshen.scaffolding.security.auth.AuthTypeEnum;
import org.apache.shiro.authc.AuthenticationToken;

public class JwtToken implements AuthenticationToken {

    private static final long serialVersionUID = -9105066814511456000L;

    private final AuthTypeEnum type;
    /**
     * 密钥
     */
    private final String account;

    public JwtToken(AuthTypeEnum type, String account) {
        this.type = type;
        this.account = account;
    }


    @Override
    public Object getPrincipal() {
        return type;
    }

    @Override
    public Object getCredentials() {
        return account;
    }

}
