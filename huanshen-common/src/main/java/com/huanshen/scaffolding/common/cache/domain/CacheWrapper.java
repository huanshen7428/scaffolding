package com.huanshen.scaffolding.common.cache.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * CacheWrapper
 *
 */
@Data
@AllArgsConstructor
public class CacheWrapper {
    /**
     * 值
     */
    private Object value;
    /**
     * 失效时刻
     */
    private Long expireTime;
}
