package com.huanshen.scaffolding.common.utils;


import cn.hutool.core.date.DateUtil;
import com.github.f4b6a3.ulid.Ulid;
import com.github.f4b6a3.ulid.UlidCreator;
import org.apache.commons.lang3.StringUtils;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;


/**
 * id生成类
 *
 */
public class IDGenerator {

    private IDGenerator() {
    }

    /**
     * 获取唯一ULID
     *
     * @return String
     */
    public static String getULIDStr() {
        Ulid ulid = UlidCreator.getUlid(System.currentTimeMillis());
        return ulid.toString();
    }

    /**
     * 获取年月信息 202401
     *
     * @param ulid
     * @return
     */
    public static String getYearMonthByUlid(String ulid) {
        Date dateByUlid = getDateByUlid(ulid);
        if (dateByUlid == null) {
            return StringUtils.EMPTY;
        }
        String format = DateUtil.format(dateByUlid, "yyyyMM");
        return format;


    }

    /**
     * 获取日期
     *
     * @param ulid
     * @return
     */
    public static Date getDateByUlid(String ulid) {
        boolean valid = Ulid.isValid(ulid);
        if (!valid) {
            return null;
        }
        Instant instant = Ulid.getInstant(ulid);
        long epochSecond = instant.getEpochSecond();
        return new Date(epochSecond * 1000);
    }

    /**
     * 获取唯一UUID
     *
     * @return
     */
    public static String getUUIDStr() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    public static int generateUniqueInt() {
        UUID uuid = UUID.randomUUID();
        return Math.abs(uuid.hashCode());
    }


}
