package com.itheima.travel.utils;

/**
 * 包名:com.itheima.travel.utils
 * 作者:Leevi
 * 日期2019-07-19  09:05
 */
public class StringUtil {
    /**
     * 判断字符串是否为空
     * 字符窜为空则返回true，不为空则返回false
     * @param str
     * @return
     */
    public static boolean isEmpty(String str){
        return str == null || "".equals(str) || "null".equals(str);
    }
}
