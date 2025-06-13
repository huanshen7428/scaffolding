package com.huanshen.scaffolding.common.utils;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public enum TimeFormatterUtil {
    INS;

    private final Map<String, DateTimeFormatter> map = new HashMap<>();

    public DateTimeFormatter get(String pattern) {
        DateTimeFormatter formatter = map.get(pattern);
        if (formatter == null) {
            formatter = DateTimeFormatter.ofPattern(pattern);
            map.put(pattern, formatter);
        }
        return formatter;
    }
}
