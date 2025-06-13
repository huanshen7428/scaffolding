package com.huanshen.scaffolding.common.cache.enums;

import lombok.Getter;

import java.util.concurrent.TimeUnit;

/**
 * CacheGroup
 *
 */
@Getter
public enum CacheGroup {
    DEFAULT("default", 3600, TimeUnit.SECONDS, 16, 1024),
    ACCOUNT("account"),
    SYSTEM("system", 10),
    ;

    private final String name;

    private final long expireTime;

    private final TimeUnit timeUnit;

    private final int initialCapacity;

    private final long maxSize;

    CacheGroup(String name) {
        this.name = name;
        this.expireTime = 3600L;
        this.timeUnit = TimeUnit.SECONDS;
        this.initialCapacity = 16;
        this.maxSize = 1024;
    }

    CacheGroup(String name, long expireTime) {
        this.name = name;
        this.expireTime = expireTime;
        this.timeUnit = TimeUnit.SECONDS;
        this.initialCapacity = 16;
        this.maxSize = 1024;
    }

    CacheGroup(String name, long expireTime, TimeUnit timeUnit, int initialCapacity, long maxSize) {
        this.name = name;
        this.expireTime = expireTime;
        this.timeUnit = timeUnit;
        this.maxSize = maxSize;
        this.initialCapacity = initialCapacity;
    }
}
