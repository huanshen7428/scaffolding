package com.huanshen.scaffolding.common.log.enums;

import lombok.Getter;

/**
 * OperatorResultEnum
 *
 * @description 操作日志-操作结果
 */
@Getter
public enum OperatorResult {
    SUCCESS("0"),
    FAIL("1");

    private final String index;

    OperatorResult(String index) {
        this.index = index;
    }
}
