package com.huanshen.scaffolding.security.utils;

import cn.hutool.extra.spring.SpringUtil;
import com.huanshen.scaffolding.common.exception.CommonException;
import com.huanshen.scaffolding.common.exception.ExceptionEnums;
import com.huanshen.scaffolding.security.auth.AuthFactory;
import com.huanshen.scaffolding.security.auth.AuthTypeEnum;
import com.huanshen.scaffolding.security.auth.CommonAuth;
import com.huanshen.scaffolding.security.domain.JwtToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * AuthorizationUtil
 *
 * @description 授权工具类
 * @date 2024-04-01 13:52
 */
@Slf4j
public class AuthUtil {

    public static JwtToken getAccountFromAnnotation(HttpServletRequest request, AuthTypeEnum defaultAuthType) {
        CommonAuth annotations = getAnnotation(request);
        AuthTypeEnum[] typeEnums = annotations == null ? new AuthTypeEnum[]{defaultAuthType} : annotations.type();
        if (typeEnums == null) {
            return null;
        }
        CommonException exception = null;
        for (AuthTypeEnum typeEnum : typeEnums) {
            String account = null;
            try {
                account = AuthFactory.INS.getAuthService(typeEnum).getAccount(request);
            } catch (CommonException e) {
                exception = e;
            }
            if (account != null) {
                return new JwtToken(typeEnum, account);
            }
        }
        if (exception == null) {
            ResponseUtil.response(ExceptionEnums.NO_PERMISSION);
        } else {
            throw exception;
        }
        return null;
    }

    private static CommonAuth getAnnotation(HttpServletRequest request) {
        HandlerExecutionChain handler = getHandler(request);
        if (handler == null) {
            log.warn("handler is null");
            return null;
        }
        HandlerMethod handlerObject = (HandlerMethod) handler.getHandler();
        Method method = handlerObject.getMethod();
        return method.getAnnotation(CommonAuth.class);
    }

    private static HandlerExecutionChain getHandler(HttpServletRequest request) {
        try {
            HandlerMapping handlerMapping = SpringUtil.getBean("requestMappingHandlerMapping");
            return handlerMapping.getHandler(request);
        } catch (Exception e) {
            log.warn("get handler error: {}", e.getMessage());
            return null;
        }
    }

}
