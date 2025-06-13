package com.huanshen.scaffolding.common.cache;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.huanshen.scaffolding.common.cache.annotations.CommonCacheEvict;
import com.huanshen.scaffolding.common.cache.annotations.CommonCachePut;
import com.huanshen.scaffolding.common.cache.annotations.CommonCacheable;
import com.huanshen.scaffolding.common.cache.domain.CacheWrapper;
import com.huanshen.scaffolding.common.cache.enums.CacheGroup;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@Aspect
public class CacheAspect {

    @Autowired
    private CommonCacheManager cacheManager;

    @Around("@annotation(com.huanshen.scaffolding.common.cache.annotations.CommonCacheable)")
    public Object doAround(ProceedingJoinPoint point) {
        MethodSignature methodSignature = (MethodSignature) point.getSignature();

        Method method = methodSignature.getMethod();

        CommonCacheable annotation = method.getAnnotation(CommonCacheable.class);
        CacheGroup cacheGroup = annotation.group();
        String key = annotation.key();
        boolean sync = annotation.sync();

        String cacheKey = getValue(key, methodSignature.getParameterNames(), point.getArgs());

        Cache cache = cacheManager.getCacheWithNull(cacheGroup.getName());
        if (cache != null) {
            Cache.ValueWrapper valueWrapper = cache.get(cacheKey);
            if (valueWrapper != null) {
                Object cacheValue = valueWrapper.get();
                //cache value exists
                if (!sync) {
                    return cacheValue;
                } else {
                    CacheWrapper wrapper = (CacheWrapper) cacheValue;
                    if (wrapper.getExpireTime() < System.currentTimeMillis()) {
                        Executors.newSingleThreadExecutor().submit(() -> {
                            Object result = processAndGet(point);

                            CacheUtil.setCacheWithoutExpire(cacheGroup, cacheKey, new CacheWrapper(result, getExpiredTime(cacheGroup)));
                        });
                    }
                    return wrapper.getValue();
                }
            }
        }

        //cache value not exists
        Object result = processAndGet(point);
        if (sync) {
            CacheUtil.setCacheWithoutExpire(cacheGroup, cacheKey, new CacheWrapper(result, getExpiredTime(cacheGroup)));
        } else {
            CacheUtil.setCache(cacheGroup, cacheKey, result);
        }
        return result;
    }

    private Object processAndGet(ProceedingJoinPoint point) {
        Object result;
        try {
            result = point.proceed();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Before("@annotation(com.huanshen.scaffolding.common.cache.annotations.CommonCacheEvict)")
    public void doBeforeEvict(JoinPoint point) {
        MethodSignature methodSignature = (MethodSignature) point.getSignature();

        Method method = methodSignature.getMethod();
        CommonCacheEvict annotation = method.getAnnotation(CommonCacheEvict.class);
        CacheGroup cacheGroup = annotation.group();
        String key = annotation.key();

        String cacheKey = getValue(key, methodSignature.getParameterNames(), point.getArgs());
        CacheUtil.evictCache(cacheGroup, cacheKey);
    }

    @Around("@annotation(com.huanshen.scaffolding.common.cache.annotations.CommonCachePut)")
    public Object doBeforePut(ProceedingJoinPoint point) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) point.getSignature();

        Method method = methodSignature.getMethod();

        CommonCachePut annotation = method.getAnnotation(CommonCachePut.class);
        CacheGroup cacheGroup = annotation.group();
        String key = annotation.key();

        Object result = point.proceed();
        String cacheKey = getValue(key, methodSignature.getParameterNames(), point.getArgs());

        CacheUtil.putCache(cacheGroup, cacheKey, result);
        return result;
    }

    private String toStr(Object[] args) {
        return ArrayUtil.join(args, ",");
    }

    private String getValue(String key, String[] paramNames, Object[] args) {
        if (StrUtil.isBlank(key)) {
            return toStr(args);
        }
        String[] paramParts = key.split("\\.");

        for (int i = 0; i < paramNames.length; i++) {
            if (!paramNames[i].equals(paramParts[0])) {
                continue;
            }
            if (paramParts.length == 1) {
                return String.valueOf(args[i]);
            }
            String paramIn = paramParts[1];
            Object tObj = args[i];
            Class<?> clazz = tObj.getClass();

            String result;
            try {
                Method method = clazz.getDeclaredMethod(StrUtil.genGetter(paramIn));
                result = String.valueOf(method.invoke(tObj));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return result;
        }
        return "";
    }

    private long getExpiredTime(CacheGroup cacheGroup) {
        return System.currentTimeMillis() + TimeUnit.MILLISECONDS.convert(cacheGroup.getExpireTime(), cacheGroup.getTimeUnit());
    }
}
