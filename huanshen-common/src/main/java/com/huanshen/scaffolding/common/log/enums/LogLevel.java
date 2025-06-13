package com.huanshen.scaffolding.common.log.enums;

import lombok.Getter;

/**
 * LevelEnum
 *
 */
@Getter
public enum LogLevel {
    NORMAL("0", "正常"),
    WARN("1", "警告"),
    ERROR("2", "错误"),
    FATAL("3", "严重错误"),
    ;

    final String index;
    final String desc;

    LogLevel(String index, String desc) {
        this.index = index;
        this.desc = desc;
    }
}
