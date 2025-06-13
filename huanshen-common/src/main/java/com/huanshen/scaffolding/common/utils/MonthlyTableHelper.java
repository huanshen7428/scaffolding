package com.huanshen.scaffolding.common.utils;

import java.util.function.Supplier;

/**
 * 按月分表
 *
 */
public class MonthlyTableHelper {

    public static final String PREFIX = "_h_";

    private MonthlyTableHelper() {

    }

    private static final ThreadLocal<String> MONTHLY_TABLE_DATA = new ThreadLocal<>();

    private static void set(String suffix) {
        MONTHLY_TABLE_DATA.remove();
        MONTHLY_TABLE_DATA.set(PREFIX + suffix);
    }

    public static String get() {
        return MONTHLY_TABLE_DATA.get();
    }

    public static <T> T execute(String suffix, Supplier<T> e) {
        try {
            set(suffix);
            return e.get();
        } finally {
            MONTHLY_TABLE_DATA.remove();
        }
    }
}
