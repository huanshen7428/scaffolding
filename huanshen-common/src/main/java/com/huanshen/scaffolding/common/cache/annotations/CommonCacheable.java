package com.huanshen.scaffolding.common.cache.annotations;

import com.huanshen.scaffolding.common.cache.enums.CacheGroup;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * CommonCacheable
 * <p>
 * group-name-{key:value}
 *
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface CommonCacheable {
    CacheGroup group();

    String key() default "";

    /**
     * 是否异步缓存,默认false<br />
     * 赋值true后，当缓存已失效，查询时会立即返回当前的缓存内容，同时异步刷新缓存
     * @return
     */
    boolean sync() default false;

}
