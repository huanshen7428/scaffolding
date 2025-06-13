package com.huanshen.scaffolding.common.exception;

import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class CommonAssert {
    private CommonAssert() {
    }

    /**
     * 指定httpCode
     */
    public static void error(ExceptionEnums exception, Object... keys) {
        throw new CommonException(exception, keys == null || keys.length == 0 ? null : Stream.of(keys).map(String::valueOf).collect(Collectors.joining(",")));
    }

    public static void isTrue(boolean param, ExceptionEnums exception, Object... keys) {
        if (!param) {
            error(exception, keys);
        }
    }

    public static void isFalse(boolean param, ExceptionEnums exception, Object... keys) {
        isTrue(!param, exception, keys);
    }

    public static void notNull(Object obj, ExceptionEnums exception, Object... keys) {
        isTrue(obj != null, exception, keys);
    }

    public static void notBlank(String obj, ExceptionEnums exception, Object... keys) {
        isTrue(obj != null && !obj.trim().isEmpty(), exception, keys);
    }

    public static void isNull(Object obj, ExceptionEnums exception, Object... keys) {
        isTrue(obj == null, exception, keys);
    }

    public static void notEmpty(Collection<?> collection, ExceptionEnums exception, Object... keys) {
        isTrue(CollUtil.isNotEmpty(collection), exception, keys);
    }

    public static void isEmpty(Collection<?> collection, ExceptionEnums exception, Object... keys) {
        isTrue(CollUtil.isEmpty(collection), exception, keys);
    }

    public static void notEmpty(String param, ExceptionEnums exception, Object... keys) {
        isTrue(param != null && !param.trim().isEmpty(), exception, keys);
    }

    public static void equals(Object obj1, Object obj2, ExceptionEnums exception, Object... keys) {
        isTrue(Objects.equals(obj1, obj2), exception, keys);
    }

    public static void ne(Object obj1, Object obj2, ExceptionEnums exception, Object... keys) {
        isFalse(Objects.equals(obj1, obj2), exception, keys);
    }

    public static <T> void in(T t, T[] array, ExceptionEnums exception, Object... keys) {
        isTrue(Arrays.asList(array).contains(t), exception, keys);
    }

    public static <T> void in(T t, List<T> array, ExceptionEnums exception, Object... keys) {
        isTrue(array.contains(t), exception, keys);
    }
}
