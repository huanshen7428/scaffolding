package com.huanshen.scaffolding.common.log.annotations;

import com.huanshen.scaffolding.common.log.enums.OperatorResult;

/**
 * OperatorLogService
 *
 * @date 2024-07-31 17:54
 */
public interface OperatorLogService {
    void process(OperatorLog annotation, OperatorResult resultEnum);
}
