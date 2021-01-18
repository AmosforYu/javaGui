package com.yyb.gui.utils;

/**
 * @author Yamos
 * @description StringUtils
 * @date 2021/1/14 0014 15:23
 */
public class StringUtils {

    public static boolean isEmpty(String str) {
        return "".equals(str) || null == str;
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }
}
