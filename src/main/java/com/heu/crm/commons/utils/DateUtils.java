package com.heu.crm.commons.utils;
/**
 * @author LinXueHang
 * @date 2022/11/19 11:09
 */

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 对 Date 类型数据进行处理的工具类
 * @author LinXueHang
 * @version 1.0
 * Create by 2022/11/19 11:09
 */
public class DateUtils {

    /**
     * 对指定的 Date 对象进行格式化：yyyy-MM-dd:HH:mm:ss
     * @param date
     * @return
     */
    public static String formatDataTime(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss");
        String nowStr = sdf.format(new Date());
        return nowStr;
    }

    /**
     * 对指定的 Date 对象进行格式化：yyyy-MM-dd
     * @param date
     * @return
     */
    public static String formatData(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String nowStr = sdf.format(new Date());
        return nowStr;
    }

    /**
     * 对指定的 Date 对象进行格式化：HH:mm:ss
     * @param date
     * @return
     */
    public static String formatTime(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String nowStr = sdf.format(new Date());
        return nowStr;
    }

}
