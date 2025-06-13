package com.huanshen.scaffolding.security.domain;


import org.apache.shiro.SecurityUtils;

import java.io.Serializable;

public class UserContext implements Serializable {


    private static final long serialVersionUID = -1404676696189000511L;

    public static LoginUser getCurrentUser() {
        return (LoginUser) SecurityUtils.getSubject().getSession().getAttribute("user");
    }

    public static void setCurrentUser(LoginUser userInfo) {
        SecurityUtils.getSubject().getSession().setAttribute("user", userInfo);
    }

}
