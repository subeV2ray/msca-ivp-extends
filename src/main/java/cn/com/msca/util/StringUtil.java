package cn.com.msca.util;

import java.util.Objects;

/**
 * @program: msca-ivpforent
 * @description: 字符串相关的工具, 自定义, 性能更高
 * @author: ペイン
 * @create: 2025-04-19 14:50
 **/
public class StringUtil {

    /**
     * 判断字符串是否为空白（null 或全是空格）
     * @param str 字符串
     * @return true 表示是空白，false 表示不是空白
     */
    public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断字符串是否非空白（非 null，非空，且包含非空格字符）
     * @param str 字符串
     * @return true 表示不是空白，false 表示是空白或 null
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    /**
     * 判断字符串是否为空（null 或 空字符串）
     * @param str 字符串
     * @return true 表示是空，false 表示不是空
     */
    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    /**
     * 判断字符串是否非空（非 null，非空字符串）
     * @param str 字符串
     * @return true 表示非空，false 表示为空或 null
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 判断两个字符串是否相等（支持 null 安全）
     * @param a 字符串 A
     * @param b 字符串 B
     * @return true 表示相等，false 表示不等
     */
    public static boolean equals(String a, String b) {
        return Objects.equals(a, b); // 同一个引用或都为 null
    }

    /**
     * 判断两个字符串是否忽略大小写相等（支持 null 安全）
     * @param a 字符串 A
     * @param b 字符串 B
     * @return true 表示相等，false 表示不等
     */
    public static boolean equalsIgnoreCase(String a, String b) {
        if (Objects.equals(a, b)) {
            return true; // 同一个引用或都为 null
        }
        if (a == null || b == null) {
            return false;
        }
        return a.equalsIgnoreCase(b);
    }
}
