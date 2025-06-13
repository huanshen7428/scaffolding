package com.huanshen.scaffolding.general.aspect;

import com.alibaba.fastjson.JSON;
import com.huanshen.scaffolding.common.page.util.PageUtils;
import com.huanshen.scaffolding.security.domain.LoginUser;
import com.huanshen.scaffolding.security.domain.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 执行顺序：在shiro之前
 */
@Slf4j
@Component
@Aspect
public class ControllerAspect {

    @Pointcut("execution(public * com.huanshen.scaffolding.*.controller.*.*(..))")
    public void controllerCut() {
    }

    @After("controllerCut()")
    public void after() {
        PageUtils.clearPage();
    }

    @Before("controllerCut()")
    public void doBefore(JoinPoint joinPoint) {
        doLog(joinPoint);
    }

    private void doLog(JoinPoint joinPoint) {
        String user = null;
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        MDC.clear();
        //设置单前日志入库属性
        String mdcSeq = String.format("Trace%s", RandomStringUtils.randomNumeric(16));
        MDC.put("log_id", mdcSeq);
        if (attributes == null) {
            return;
        }
        HttpServletRequest request = attributes.getRequest();
        // 记录请求内容，这里只记录非GET请求的日志信息
        String url = request.getRequestURI();
        String ip = request.getRemoteHost();
        int port = request.getRemotePort();
        try {
            LoginUser loginUser = UserContext.getCurrentUser();
            if (loginUser != null) {
                user = loginUser.getAccount();
            }
        } catch (Exception ignored) {

        }
        if (!url.contains("serviceTps")) {
            log.info(String.format("start url:%s,host:%s,port:%s,user:%s", url, ip, port, user));
            Object[] paramArr = joinPoint.getArgs();
            //序列化时过滤掉request和response
            List<Object> logArgs = streamOf(paramArr).filter(arg -> (!(arg instanceof HttpServletRequest) && !(arg instanceof HttpServletResponse)) && !(arg instanceof MultipartFile)).collect(Collectors.toList());
            log.info("URI:{},请求参数:{}", url, JSON.toJSONString(logArgs));
        }
    }

    public static <T> Stream<T> streamOf(T[] array) {
        return ArrayUtils.isEmpty(array) ? Stream.empty() : Arrays.stream(array);
    }
}
