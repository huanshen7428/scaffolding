package com.huanshen.scaffolding.common.utils;


import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LocalDateUtil {
    private LocalDateUtil() {
    }

    public static String getCommonStr() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
    }

    public static String getCommonSimpleStr() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddhhmmss"));
    }

    public static LocalDateTime fromDate(Date date) {
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        return instant.atZone(zoneId).toLocalDateTime();
    }

    public static Date fromLocalDate(LocalDateTime date) {
        Instant instant = date.atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

    public static LocalDateTime atStartOfDay(LocalDate date) {
        return LocalDateTime.of(date, LocalTime.MIN);
    }

    public static LocalDateTime atEndOfDay(LocalDate date) {
        return LocalDateTime.of(date, LocalTime.MAX);
    }
//    public static Date atStartOfDay(LocalDate date) {
//        return fromLocalDate(LocalDateTime.of(date, LocalTime.MIN));
//    }
//
//    public static Date atEndOfDay(LocalDate date) {
//        return fromLocalDate(LocalDateTime.of(date, LocalTime.MAX));
//    }

    /**
     * 指定两个时间,计算它们之间的所有月份. yyyyMM格式
     *
     * @param start
     * @param end
     * @return
     */
    public static List<String> calcMonths(LocalDateTime start, LocalDateTime end) {
        List<String> resultList = new ArrayList<>();
        //start与end倒置
        if (start.compareTo(end) > 0) {
            return resultList;
        }
        while (true) {
            resultList.add(end.format(TimeFormatterUtil.INS.get("yyyyMM")));
            if (start.getYear() == end.getYear() && start.getMonth() == end.getMonth()) {
                break;
            }
            end = end.minusMonths(1);
        }
        return resultList;
    }

    public static List<LocalDate> getAllDatesBetween(Date startDate, Date endDate) {
        LocalDate localStartDate = LocalDate.ofInstant(Instant.ofEpochMilli(startDate.getTime()), ZoneId.systemDefault());
        LocalDate localEndDate = LocalDate.ofInstant(Instant.ofEpochMilli(endDate.getTime()), ZoneId.systemDefault());
        List<LocalDate> dates = new ArrayList<>();
        while (!localStartDate.isAfter(localEndDate)) {
            dates.add(localStartDate);
            localStartDate = localStartDate.plusDays(1);
        }
        return dates;
    }


    /**
     * 指定两个时间,计算它们之间的所有day. 返回分区，p1格式
     *
     * @param start
     * @param end
     * @return
     */
    public static List<String> calcPartitions(LocalDateTime start, LocalDateTime end) {
        List<String> resultList = new ArrayList<>();
        //start与end倒置
        if (start.compareTo(end) > 0) {
            return resultList;
        }
        while (true) {
            resultList.add("p" + end.getDayOfMonth());
            if (start.getDayOfMonth() == end.getDayOfMonth()) {
                break;
            }
            end = end.minusDays(1);
        }
        return resultList;
    }

    public static List<String> calcMonths(Date start, Date end) {
        return calcMonths(fromDate(start), fromDate(end));
    }

    public static List<String> calcPartitions(Date start, Date end) {
        return calcPartitions(fromDate(start), fromDate(end));
    }

    public static boolean onRange(LocalTime currentTime, LocalTime startTime, LocalTime endTime) {
        return currentTime.isAfter(startTime) && currentTime.isBefore(endTime);
    }

    public static long getMinus() {
        // 直接获取当前日期和时间的 LocalDateTime
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES); // 截取到分钟级别

        // 转换为系统默认时区的 ZonedDateTime
        ZonedDateTime zonedDateTime = now.atZone(ZoneId.systemDefault());

        // 获取自 Unix 纪元以来的毫秒数
        return zonedDateTime.toInstant().toEpochMilli();
    }

    /**
     * yyyy-MM
     *
     * @param month
     * @return
     */
    public static LocalDateTime[] monthRange(String month) {
        LocalDate localDate = LocalDate.parse("01/" + month, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        LocalDateTime startTime = localDate.atTime(LocalTime.MIN);
        LocalDateTime endTime = localDate.plusMonths(1).minusDays(1).atTime(LocalTime.MAX);
        return new LocalDateTime[]{startTime, endTime};
    }

    /**
     * yyyy-MM-dd
     *
     * @param day
     * @return
     */
    public static LocalDateTime fromDay(String day, LocalTime time) {
        return LocalDate.parse(day, DateTimeFormatter.ofPattern("dd/MM/yyyy")).atTime(time);
    }

    public static String formatDate(Date date, DateTimeFormatter formatter) {
        if (date == null) {
            return null;
        }
        LocalDateTime localDateTime = LocalDateUtil.fromDate(date);
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        return zonedDateTime.format(formatter);
    }

}
