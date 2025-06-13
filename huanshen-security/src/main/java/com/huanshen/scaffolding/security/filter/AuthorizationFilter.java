package com.huanshen.scaffolding.security.filter;

import cn.hutool.extra.spring.SpringUtil;
import com.huanshen.scaffolding.common.exception.CommonAssert;
import com.huanshen.scaffolding.common.exception.CommonException;
import com.huanshen.scaffolding.common.exception.ExceptionEnums;
import com.huanshen.scaffolding.security.auth.AuthTypeEnum;
import com.huanshen.scaffolding.security.auth.DefaultAuth;
import com.huanshen.scaffolding.security.config.TokenConfigProperties;
import com.huanshen.scaffolding.security.constant.SecurityConsts;
import com.huanshen.scaffolding.security.domain.JwtToken;
import com.huanshen.scaffolding.security.utils.AuthUtil;
import com.huanshen.scaffolding.security.utils.JwtUtils;
import com.huanshen.scaffolding.security.utils.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Slf4j
public class AuthorizationFilter extends BasicHttpAuthenticationFilter {

    private TokenConfigProperties tokenConfigProperties;

    public AuthorizationFilter() {
    }

    public AuthorizationFilter(TokenConfigProperties tokenConfigProperties) {
        this.tokenConfigProperties = tokenConfigProperties;
    }


    /**
     * 检测Header里面是否包含Authorization字段，有就进行Token登录认证授权
     */
//    @Override
//    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
//        HttpServletRequest req = (HttpServletRequest) request;
//        String authorization = req.getHeader(SecurityConsts.REQUEST_HEADER_AUTH);
//        String username = req.getHeader(SecurityConsts.REQUEST_HEADER_USERNAME);
//        String signature = req.getHeader(SecurityConsts.REQUEST_HEADER_SIGNATURE);
//        return authorization != null;
//    }


    private AuthTypeEnum systemDefaultAuthTypes = null;

    /**
     * 进行AccessToken登录认证授权
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        //客户端账户鉴权，尝试从注解中获取
        JwtToken tokenDto = AuthUtil.getAccountFromAnnotation(httpServletRequest, getSystemDefaultAuthType());
        if (tokenDto == null) {
            return false;
        }
        this.getSubject(request, response).login(tokenDto);
        return true;
    }

    private AuthTypeEnum getSystemDefaultAuthType() {
        if (systemDefaultAuthTypes != null) {
            return systemDefaultAuthTypes;
        }
        Map<String, Object> map = SpringUtil.getApplicationContext().getBeansWithAnnotation(SpringBootApplication.class);
        Object bean = map.values().iterator().next();
        DefaultAuth annotation = bean.getClass().getAnnotation(DefaultAuth.class);
        if (annotation == null) {
            systemDefaultAuthTypes = AuthTypeEnum.getDefault();
        } else {
            systemDefaultAuthTypes = annotation.value();
        }
        return systemDefaultAuthTypes;
    }

    /**
     * 检查是否需要,若需要则校验时间戳，刷新Token，并更新时间戳
     */
    private void refreshTokenIfNeed(String account, String token, ServletResponse response) {
        Long currentTimeMillis = System.currentTimeMillis();
        //检查刷新规则
        if (this.refreshCheck(token, currentTimeMillis)) {
            //时间戳一致，则颁发新的令牌
            log.info("为账户{}颁发新的令牌, 当前token[{}]", account, token);
            String newToken = JwtUtils.INS.sign(account);
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            httpServletResponse.setHeader(SecurityConsts.REQUEST_HEADER_AUTH, newToken);
            httpServletResponse.setHeader("Access-Control-Expose-Headers", SecurityConsts.REQUEST_HEADER_AUTH);
        }
    }

    /**
     * 检查是否需要更新Token
     */
    private boolean refreshCheck(String authorization, Long currentTimeMillis) {
        String tokenMillis = JwtUtils.INS.getClaim(authorization, SecurityConsts.CURRENT_TIME_MILLIS);
        CommonAssert.notBlank(tokenMillis, ExceptionEnums.INVALID_TOKEN);

        long millDiff = currentTimeMillis - Long.parseLong(tokenMillis);
        return millDiff > (tokenConfigProperties.getRefreshCheckTime() * 60 * 1000L);
    }

    /**
     * 是否允许访问
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        //检测Header里面是否包含Authorization字段
//        boolean isLoginAttempt = isLoginAttempt(request, response);
//        if (!isLoginAttempt) {
//            ResponseUtil.response(ExceptionEnums.EMPTY_AUTHORIZATION);
//            return false;
//        }
        try {
            boolean res = this.executeLogin(request, response);
            if (res) {
                return true;
            }
        } catch (CommonException e) {
            ResponseUtil.response(e.getHttpCode(), e.getErrorCode(), e.getErrorCode());
        }
        return false;
    }

    /**
     * 重写 onAccessDenied 方法，避免父类中调用再次executeLogin
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) {
        this.sendChallenge(request, response);
        return false;
    }

}
