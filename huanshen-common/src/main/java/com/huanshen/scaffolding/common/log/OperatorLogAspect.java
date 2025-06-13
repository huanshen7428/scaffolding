package com.huanshen.scaffolding.common.log;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.huanshen.scaffolding.common.log.annotations.OperatorLog;
import com.huanshen.scaffolding.common.log.annotations.OperatorLogService;
import com.huanshen.scaffolding.common.log.enums.OperatorResult;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;

@Slf4j
@Component
@Aspect
public class OperatorLogAspect {

    @Around("@annotation(com.huanshen.scaffolding.common.log.annotations.OperatorLog)")
    public Object doAround(ProceedingJoinPoint point) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) point.getSignature();

        Method method = methodSignature.getMethod();

        OperatorLog annotation = method.getAnnotation(OperatorLog.class);

        Object res = null;
        Exception exception = null;
        OperatorResult resultEnum;
        try {
            res = point.proceed();
            resultEnum = OperatorResult.SUCCESS;
        } catch (Exception e) {
            exception = e;
            resultEnum = OperatorResult.FAIL;
        }

        Map<String, OperatorLogService> beanMap = SpringUtil.getBeansOfType(OperatorLogService.class);
        if (CollUtil.isNotEmpty(beanMap)) {
            for (OperatorLogService authService : beanMap.values()) {
                authService.process(annotation, resultEnum);
            }
        }
        if (exception != null) {
            throw exception;
        }
        return res;
    }
}
