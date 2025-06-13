package com.huanshen.scaffolding.common.log.enums;

import lombok.Getter;

/**
 * ModuleEnum
 *
 * @description 操作日志-模块
 */
@Getter
public enum LogModule {
    HOME(0, "首页"),
    SYSTEM_MGMT(1,"系统管理"),
    ;
    private final String index;
    private final String desc;

    LogModule(Integer index, String desc) {
        this.index = String.valueOf(index);
        this.desc = desc;
    }
}
