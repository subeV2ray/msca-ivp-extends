package cn.com.msca.util;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author fhl
 * @version 1.0
 * @description: 时间格式化工具类
 * @date 2025/3/14 17:13
 */
public class DateFormatUtil {
    public final static String FORMAT_YYYY = "yyyy";

    public final static String FORMAT_YYYYMM = "yyyyMM";

    public final static String FORMAT_YYYYMMDD = "yyyyMMdd";

    public final static String FORMAT_yyyyMMddHHmmss = "yyyyMMddHHmmss";

    public final static String FORMAT_yyyyMMddHHmmssSSS = "yyyyMMddHHmmssSSS";

    public final static String FORMAT_YYYY_MM = "yyyy-MM";

    public final static String FORMAT_YYYY_MM_DD = "yyyy-MM-dd";

    public final static String FORMATyyyyMMdd_CHN = "yyyy年MM月dd日";

    public final static String FORMAT_yyyy_MM_dd_HH_mmss_cn = "yyyy-MM-dd HH:mm:ss";

    public final Integer DAY_MOLIS = 1000 * 60 * 60 * 24;
    public final Integer HOUR_MOLIS = 1000 * 60 * 60;
    public final Integer MINUTE_MOLIS = 1000 * 60;
    public final Integer SECOND_MOLIS = 1000;

    /**
     * 获取String类型时间 格式(yyyyMMddHHmmssSSS)
     */
    public static long getNowTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        String formattedTime = LocalDateTime.now().format(formatter);
        return Long.parseLong(formattedTime);
    }

    /**
     * 获取前N天的时间 格式(yyyyMMddHHmmssSSS)
     */
    public static long getNDaysAgoTime(int days) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        // 获取当前时间并减去两天
        LocalDateTime twoDaysAgo = LocalDateTime.now().minusDays(days);
        String formattedTime = twoDaysAgo.format(formatter);
        return Long.parseLong(formattedTime);
    }


    /**
     * 获取long类型时间 格式(yyyyMMddHHmmssSSS)
     */
    public static Long getTimeByDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_yyyyMMddHHmmssSSS);
        String value = dateTime.format(formatter);
        return Long.valueOf(value);
    }


    /**
     * 获取long类型时间 格式(yyyyMMddHHmmssSSS)
     */
    public static String getMonth() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_YYYYMM);
        return LocalDate.now().format(formatter);
    }

    public static String getLastMonth() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
        // 获取当前日期并减去一个月
        return LocalDate.now().minusMonths(1).format(formatter);
    }


}
