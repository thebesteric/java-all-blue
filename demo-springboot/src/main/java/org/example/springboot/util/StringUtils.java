package org.example.springboot.util;

/**
 * StringUtils
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-09-15 17:44:56
 */
public class StringUtils {

    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static String contact(String str1, String str2, String separator) {
        return str1 + separator + str2;
    }

}
