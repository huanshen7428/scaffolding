package com.huanshen.scaffolding.security.realms;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSONObject;
import com.huanshen.scaffolding.common.exception.ExceptionEnums;
import com.huanshen.scaffolding.security.auth.AuthFactory;
import com.huanshen.scaffolding.security.auth.AuthTypeEnum;
import com.huanshen.scaffolding.security.auth.IPermission;
import com.huanshen.scaffolding.security.domain.JwtToken;
import com.huanshen.scaffolding.security.domain.LoginUser;
import com.huanshen.scaffolding.security.domain.UserContext;
import com.huanshen.scaffolding.security.utils.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.BeansException;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created on 2022/4/24.
 *
 */
@Component
@Slf4j
public class ShiroRealm extends AuthorizingRealm {

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 该方法需要的参数是AuthenticationToken对象，AuthenticationToken
     *
     * @param auth auth
     * @return AuthenticationInfo
     * @throws AuthenticationException AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
        AuthTypeEnum authType = (AuthTypeEnum) auth.getPrincipal();
        String account = (String) auth.getCredentials();

        LoginUser loginUser = AuthFactory.INS.getAuthService(authType).getLoginUser(authType, account);
        if (loginUser == null) {
            ResponseUtil.response(ExceptionEnums.NO_PERMISSION);
            return null;
        }
        UserContext.setCurrentUser(loginUser);
        return new SimpleAuthenticationInfo(JSONObject.toJSONString(loginUser), account, "shiroRealm");
    }


    /**
     * 该方法需要的参数是PrincipalCollection对象，
     * 这个对象表示经过认证后的登录主体，这个方法作用就是要给这个登录的主体授权，返回一个授权后的主体simpleAuthorizationInfo
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     *
     * @param principals principals
     * @return AuthorizationInfo
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        LoginUser userInfo = JSONObject.parseObject(String.valueOf(principals.getPrimaryPrincipal()), LoginUser.class);

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.addRole(userInfo.getRoleName());

        //根据role查询permission
        IPermission permissionService;
        try {
            permissionService = SpringUtil.getBean(IPermission.class);
        } catch (BeansException ignored) {
            return authorizationInfo;
        }
        if (permissionService == null) {
            return authorizationInfo;
        }
        List<String> permissions = permissionService.getPermissions(userInfo.getRoleName());
        if (permissions == null) {
            return authorizationInfo;
        }
        permissions.forEach(authorizationInfo::addStringPermission);

        return authorizationInfo;
    }

}
