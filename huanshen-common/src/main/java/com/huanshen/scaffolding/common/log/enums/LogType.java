package com.huanshen.scaffolding.common.log.enums;

import lombok.Getter;

/**
 * OperTypeEnum
 *
 * @description 操作日志-操作类型
 */
@Getter
public enum LogType {
    ADD("0"),
    MODIFY("1"),
    DELETE("2");

    private final String index;

    LogType(String index) {
        this.index = index;
    }

}
