package com.huanshen.scaffolding.security.auth;

import lombok.Getter;

/**
 * AuthType
 *
 * @date 2024-04-01 14:43
 */
@Getter
public enum AuthTypeEnum {
    /**
     * token鉴权
     */
    TOKEN("token"),
    /**
     * api
     */
    API("api"),
    /**
     * smpp
     */
    SMPP("smpp"),
    /**
     * oauth鉴权
     */
    CLIENT_OAUTH("oauth"),
    /**
     * 无鉴权
     */
    NO_AUTH("no"),
    /**
     * token或无鉴权。 or逻辑的demo
     */
    TOKEN_OR_NO_AUTH("token_or_no"),
    ;
    private final String type;

    AuthTypeEnum(String type) {
        this.type = type;
    }

    public static AuthTypeEnum getDefault() {
        return AuthTypeEnum.TOKEN;
    }

}
