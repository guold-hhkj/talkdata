package com.hhkj.talkdata.util;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间格式转换工具
 * Created by litj on 2016/8/15.
 */
public class DateUtil {

    public static final String DEFAULT_STYLE1 = "yyyy-MM-dd HH:mm:ss";
    public static final String DEFAULT_STYLE2 = "yyyy年MM月dd日 HH:mm:ss";
    public static final String DEFAULT_STYLE3 = "yyyy-MM-dd";
    public static final String DEFAULT_STYLE4 = "yyyy-MM-dd HH:mm";
    public static final String DEFAULT_STYLE5 = "MM-dd HH:mm";
    public static final String DEFAULT_STYLE6 = "yyyy-MM";

    /**
     * 获得当前时间
     * @return 结果
     */
    public static String getNowTime(){
        return getNowTime(DEFAULT_STYLE1);
    }

    public static String getNowTime(String formatStr){
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        return format.format(new Date(System.currentTimeMillis()));
    }

    /**
     * 获得比当前时间迟几分钟的时间
     * @param formate 要转换的格式
     * @param minutes 迟几分钟
     * @return 结果
     */
    public static String getLaterNowTime(String formate, int minutes){
        SimpleDateFormat format = new SimpleDateFormat(formate);
        return format.format(new Date(System.currentTimeMillis() + minutes * 1000 * 60));
    }

    /**
     * 获得下一天的时间
     * @return 结果
     */
    public static String getNextDayTime(){
        SimpleDateFormat format = new SimpleDateFormat(DEFAULT_STYLE3);
        return format.format(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24));
    }

    /**
     * 自定义格式化时间
     *
     * @param dateStr       日期
     * @param dateFormatStr 传入的日期格式
     * @param formatStr     得到的日期格式
     * @return
     */
    public static String formatDate(String dateStr, String dateFormatStr, String formatStr) {
        if (!TextUtils.isEmpty(dateStr)) {
            Date date;
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(dateFormatStr);
                date = null;
                try {
                    date = sdf.parse(dateStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return new SimpleDateFormat(formatStr).format(date);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return dateStr;
        }
        return "";
    }

    /**
     * 根据时间类型转换成Date
     * @param dateStr 字符串
     * @param dateFormatStr 时间格式
     * @return date
     */
    public static Date getDate(String dateStr, String dateFormatStr){
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormatStr);
            try {
                date = sdf.parse(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

}
