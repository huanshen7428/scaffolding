package com.huanshen.scaffolding.common.cache.annotations;

import com.huanshen.scaffolding.common.cache.enums.CacheGroup;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * CommonCachePut
 *
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface CommonCachePut {
    CacheGroup group();

    //默认为入参的toString()
    String key() default "";

}
