package com.huanshen.scaffolding.common.log.annotations;


import com.huanshen.scaffolding.common.log.enums.LogLevel;
import com.huanshen.scaffolding.common.log.enums.LogModule;
import com.huanshen.scaffolding.common.log.enums.LogType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface OperatorLog {
    LogModule module();

    LogType type();

    LogLevel level() default LogLevel.NORMAL;

    String msg();
}
