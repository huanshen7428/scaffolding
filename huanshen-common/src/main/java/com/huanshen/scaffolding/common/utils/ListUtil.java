package com.huanshen.scaffolding.common.utils;

import java.util.Collections;
import java.util.List;

/**
 * ListUtil
 *
 * @date 2024-06-28 10:25
 */
public class ListUtil {
    private ListUtil() {
    }

    public static <T> List<T> page(List<T> list, int pageNum, int pageSize) {
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }
        int startIndex = (pageNum - 1) * pageSize;
        int endIndex = startIndex + pageSize;
        int listSize = list.size();
        if (listSize < startIndex) {
            return Collections.emptyList();
        }
        if (listSize < endIndex) {
            endIndex = listSize;
        }
        return list.subList(startIndex, endIndex);
    }
}
