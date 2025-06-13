package com.huanshen.scaffolding.security.auth;

import java.util.List;

/**
 * IPermission
 *
 * @date 2024-04-23 11:24
 */
public interface IPermission {

    List<String> getPermissions(String roleName);
}
