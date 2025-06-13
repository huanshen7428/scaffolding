package com.huanshen.scaffolding.security.auth;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * DefaultAuth
 * 指定系统默认的鉴权方式，非必须，默认token
 *
 * @date 2024-07-31 15:18
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DefaultAuth {
    AuthTypeEnum value();
}
