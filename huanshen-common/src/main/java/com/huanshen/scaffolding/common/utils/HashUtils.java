package com.huanshen.scaffolding.common.utils;

/**
 * Created on 2024/6/17.
 *
 */
public class HashUtils {


    public static final int PARTITION_COUNT = 10;

    private static long sumHash(String in) {
        long out = 0;
        for (char v : in.toCharArray()) {
            out += v;
        }
        return out;
    }

    public static long getPartition(String account) {
        return sumHash(account) % PARTITION_COUNT;
    }


}


