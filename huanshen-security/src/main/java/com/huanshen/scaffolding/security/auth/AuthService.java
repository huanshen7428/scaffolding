package com.huanshen.scaffolding.security.auth;

import com.huanshen.scaffolding.security.domain.LoginUser;
import com.huanshen.scaffolding.common.page.util.ServletUtils;
import com.huanshen.scaffolding.security.constant.SecurityConsts;

import javax.servlet.http.HttpServletRequest;

/**
 * AuthService
 *
 */
public interface AuthService {
    /**
     * 鉴权并返回账号
     *
     * @param request
     * @return 如果鉴权失败，返回null
     */
    String getAccount(HttpServletRequest request);

    AuthTypeEnum type();

    /**
     * 获取登录用户信息
     * @param account
     * @return
     */
    LoginUser getLoginUser(AuthTypeEnum authType, String account);

    default String getLang() {
        HttpServletRequest request = ServletUtils.getRequest();
        if (request == null) {
            return null;
        }
        return request.getHeader(SecurityConsts.REQUEST_HEADER_LANG);
    }

}
