package com.huanshen.scaffolding.common.utils;

import java.util.Objects;

/**
 * ArrayUtil
 *
 * @description 数组工具类
 * @date 2023-09-27 17:27
 */
public class ArrayUtil {
    private ArrayUtil() {
    }

    /**
     * 是否包含
     *
     * @param tl
     * @param ts
     * @param <T>
     * @return
     */
    public static <T> boolean contains(T[] tl, T ts) {
        for (T t : tl) {
            if (Objects.equals(t, ts)) {
                return true;
            }
        }
        return false;
    }
}
