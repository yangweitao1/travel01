package com.itheima.travel.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 包名:com.itheima.travel.utils
 * 作者:Leevi
 * 日期2019-07-19  11:54
 */
public class DateUtil {
    public static String getCurrentDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return  dateFormat.format(new Date());
    }
}
