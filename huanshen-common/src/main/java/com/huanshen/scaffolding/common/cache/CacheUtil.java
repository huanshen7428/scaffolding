package com.huanshen.scaffolding.common.cache;

import cn.hutool.extra.spring.SpringUtil;
import com.huanshen.scaffolding.common.cache.enums.CacheGroup;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.Cache;
import org.springframework.cache.caffeine.CaffeineCache;

/**
 * CacheUtil
 *
 */
public class CacheUtil {
    private CacheUtil() {
    }

    public static void clearCache(CacheGroup group) {
        Cache cache = SpringUtil.getBean(CommonCacheManager.class).getCacheWithNull(group.getName());
        if (cache == null) {
            return;
        }
        cache.clear();
    }

    public static void clearAll() {
        CommonCacheManager manager = SpringUtil.getBean(CommonCacheManager.class);
        manager.clearCache();
    }

    public static void evictCache(CacheGroup group, String key) {
        Cache cache = SpringUtil.getBean(CommonCacheManager.class).getCache(group.getName());
        if (cache == null) {
            return;
        }
        cache.evict(key);
    }

    public static void setCache(CacheGroup group, Object key, Object value) {
        CommonCacheManager cacheManager = SpringUtil.getBean(CommonCacheManager.class);

        Cache cache = cacheManager.getCacheWithNull(group.getName());
        if (cache == null) {
            Caffeine<Object, Object> caffeineBuilder = Caffeine.newBuilder()
                    .initialCapacity(group.getInitialCapacity())
                    .maximumSize(group.getMaxSize())
                    .expireAfterWrite(group.getExpireTime(), group.getTimeUnit());
            com.github.benmanes.caffeine.cache.Cache<Object, Object> buildService = caffeineBuilder.build();
            cache = new CaffeineCache(group.getName(), buildService);
            cacheManager.registerCustomCache(group.getName(), buildService);
        }
        cache.put(key, value);
    }

    public static Object getValue(String group, String key) {
        Cache cache = SpringUtil.getBean(CommonCacheManager.class).getCache(group);
        if (cache == null) {
            return null;
        }
        Cache.ValueWrapper valueWrapper = cache.get(key);
        if (valueWrapper == null) {
            return null;
        }
        return valueWrapper.get();
    }

    public static void setCacheWithoutExpire(CacheGroup group, Object key, Object value) {
        CommonCacheManager cacheManager = SpringUtil.getBean(CommonCacheManager.class);

        Cache cache = cacheManager.getCacheWithNull(group.getName());
        if (cache == null) {
            Caffeine<Object, Object> caffeineBuilder = Caffeine.newBuilder()
                    .initialCapacity(group.getInitialCapacity())
                    .maximumSize(group.getMaxSize());
            com.github.benmanes.caffeine.cache.Cache<Object, Object> buildService = caffeineBuilder.build();
            cache = new CaffeineCache(group.getName(), buildService);
            cacheManager.registerCustomCache(group.getName(), buildService);
        }
        cache.put(key, value);
    }

    public static void putCache(CacheGroup group, Object key, Object value) {
        CommonCacheManager cacheManager = SpringUtil.getBean(CommonCacheManager.class);

        Cache cache = cacheManager.getCacheWithNull(group.getName());
        if (cache == null) {
            return;
        }
        cache.put(key, value);
    }
}
