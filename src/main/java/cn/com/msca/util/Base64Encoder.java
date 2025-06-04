package cn.com.msca.util;

import io.netty.util.internal.StringUtil;

import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @program: msca-ivp-extends
 * @description: base64
 * @author: oiiaioooooiai
 * @create: 2025-06-04 14:20
 **/
public class Base64Encoder {
    private static final Pattern BASE64_DATA_URI_PATTERN = Pattern.compile("^data:[^;]+;base64,");

    public static byte[] decodeToBytes(String base64String) {
        if (StringUtil.isNullOrEmpty(base64String)) {
            throw new IllegalArgumentException("Base64 字符串不能为空。");
        }
        Matcher matcher = BASE64_DATA_URI_PATTERN.matcher(base64String);
        if (matcher.find()) {
            base64String = base64String.substring(matcher.end());
        }
        try {
            return Base64.getDecoder().decode(base64String);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Base64 字符串格式无效：" + e.getMessage(), e);
        }
    }
}
