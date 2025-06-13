package com.huanshen.scaffolding.general.aspect;

import cn.hutool.core.util.StrUtil;
import com.huanshen.scaffolding.common.exception.CommonAssert;
import com.huanshen.scaffolding.common.exception.ExceptionEnums;
import com.huanshen.scaffolding.common.ratelimiter.CommonRateLimiter;
import com.huanshen.scaffolding.common.ratelimiter.RateLimiter;
import com.huanshen.scaffolding.security.domain.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@Aspect
public class RateLimiterAspect {

//    @Autowired
//    private RedisTemplate<String, String> redisTemplate;

    private final ConcurrentHashMap<String, RateLimiter> limiterMap = new ConcurrentHashMap<>();

    @Around("@annotation(com.huanshen.scaffolding.common.ratelimiter.CommonRateLimiter)")
    public Object doAroundIde(ProceedingJoinPoint point) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) point.getSignature();

        Method method = methodSignature.getMethod();

        CommonRateLimiter annotation = method.getAnnotation(CommonRateLimiter.class);
        String key = annotation.key();
        long timeout = annotation.timeLimit();
        TimeUnit timeUnit = annotation.timeunit();
        long count = annotation.countLimit();

        String value;
        try {
            //获取指定key的值
            value = getValue(key, methodSignature.getParameterNames(), point.getArgs());
        } catch (Exception e) {
            log.error("get value failed,", e);
            return point.proceed();
        }

        String account = UserContext.getCurrentUser().getAccount();
        String controllerName = method.getDeclaringClass().getSimpleName();
        String methodName = method.getName();
        //redis-key为 account + controllerName + method + paramName + paramValue
        String redisKey = String.join("-", account, controllerName, methodName, key, value);

        //抽象封装
        boolean setResult = checkCache(redisKey, timeout, timeUnit, count);

        CommonAssert.isTrue(setResult, ExceptionEnums.FREQUENCY_LIMIT_EXCEPTION);

        return point.proceed();
    }

    private synchronized boolean checkCache(String redisKey, long timeout, TimeUnit timeUnit, long countLimit) {
        RateLimiter rateLimiter = limiterMap.computeIfAbsent(redisKey, k -> RateLimiter.create((double)countLimit/timeUnit.toSeconds(timeout), timeUnit.toSeconds(timeout)));
        return rateLimiter.tryAcquire();
//        return Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(redisKey, "1", timeout, timeUnit));
    }

    private String getValue(String key, String[] paramNames, Object[] paramValues) throws Exception {
        if (StrUtil.isBlank(key)) {
            return "";
        }
        String[] paramParts = key.split("\\.");

        for (int i = 0; i < paramNames.length; i++) {
            if (!paramNames[i].equals(paramParts[0])) {
                continue;
            }
            if (paramParts.length == 1) {
                return String.valueOf(paramValues[i]);
            }
            String paramIn = paramParts[1];
            Object tObj = paramValues[i];
            Class<?> clazz = tObj.getClass();

            Method method = clazz.getDeclaredMethod(StrUtil.genGetter(paramIn));

            return StrUtil.str(method.invoke(tObj), StandardCharsets.UTF_8);
        }
        return "";
    }

}
