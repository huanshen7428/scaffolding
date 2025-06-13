package com.huanshen.scaffolding.security.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * CommonAuth
 *
 * @date 2024-04-01 14:47
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CommonAuth {
    /**
     * 验证类型, 逻辑or
     */
    AuthTypeEnum[] type();
}
