package com.huanshen.scaffolding.common.ratelimiter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * 幂等+分布式锁
 *
 * @description 幂等
 * @date 2024-02-02 14:19
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CommonRateLimiter {
    /**
     * 幂等依据的标识。
     * 默认key为account+当前方法
     */
    String key() default "";


    /**
     * 时间限制
     * @return
     */
    long timeLimit() default 1L;

    TimeUnit timeunit() default TimeUnit.SECONDS;

    long countLimit() default  1L;

    /**
     * 默认本地限流，如果为false则使用redis限流<br/>
     * @return
     */
//    boolean local() default true;

}
