package com.abocode.jfaster.web.utils;

import java.beans.PropertyEditorSupport;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.springframework.util.StringUtils;
/**
 * Created by Franky on 2016/3/15.
 */
public class DateUtils extends PropertyEditorSupport {// 各种时间格式
     public static final String  YYYY_MM_DD ="yyyy-MM-dd";
    public static final String YYYYMMDD ="yyyyMMdd";
    public static final String YYYY_MM_DD_HH_MM =  "yyyy-MM-dd HH:mm";
    public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    public static final String HH_MM ="HH:mm";
    public static final  String YYYY_MM_DD_HH_MM_SS ="yyyy-MM-dd HH:mm:ss";
    private static final long DAY_TO_MILLIS = 24 * 3600 * 1000;
    private static final long HOUR_TO_MILLIS = 3600 * 1000;
    private static final long MINUTE_TO_MILLIS = 60 * 1000;
    private static final long SECOND_TO_MILLIS = 1000;

    // 指定模式的时间格式
    private static SimpleDateFormat getSimpleDateFormat(String pattern) {
        return new SimpleDateFormat(pattern);
    }
    /**
     * 指定毫秒数表示的日期
     *
     * @param millis 毫秒数
     * @return 指定毫秒数表示的日期
     */
    public static Date getDate(long millis) {
        return new Date(millis);
    }

    /**
     * 字符串转换时间戳
     * @param str
     * @return
     */
    public static Timestamp strToTimestamp(String str,String  format) {
        Date date = strToDate(str,format);
        return new Timestamp(date.getTime());
    }

    /**
     * 字符串转换成日期
     *
     * @param str
     * @param sdf
     * @return
     */
    public static Date strToDate(String str, String  sdf) {
        if (null == str || "".equals(str)) {
            return null;
        }
        Date date;
        try {
            date = getSimpleDateFormat(sdf).parse(str);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 格式化时间
     *
     * @param date
     * @param format
     * @return
     */
    public static String dateToStr(String date, String format) {
        SimpleDateFormat sdf = getSimpleDateFormat(format);
        Date _date = null;
        try {
            _date = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sdf.format(_date);
    }

    /**
     * 日期转换为字符串
     * 日期
     *
     * @param format 日期格式
     * @return 字符串
     */
    public static String getDate(String format) {
        Date date = new Date();
        if (null == date) {
            return null;
        }
        SimpleDateFormat sdf = getSimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * 指定毫秒数的时间戳
     *
     * @param millis 毫秒数
     * @return 指定毫秒数的时间戳
     */
    public static Timestamp getTimestamp(long millis) {
        return new Timestamp(millis);
    }

    /**
     * 系统当前的时间戳
     *
     * @return 系统当前的时间戳
     */
    public static Timestamp getTimestamp() {
        return new Timestamp(new Date().getTime());
    }

    /**
     * 指定日期的时间戳
     *
     * @param date 指定日期
     * @return 指定日期的时间戳
     */
    public static Timestamp getTimestamp(Date date) {
        return new Timestamp(date.getTime());
    }

    /**
     * 系统时间的毫秒数
     * @return 系统时间的毫秒数
     */
    public static long getMillis() {
        return new Date().getTime();
    }

    /**
     * 指定日历的毫秒数
     *
     * @param cal 指定日历
     * @return 指定日历的毫秒数
     */
    public static long getMillis(Calendar cal) {
        return cal.getTime().getTime();
    }

    /**
     * 指定日期的毫秒数
     *
     * @param date 指定日期
     * @return 指定日期的毫秒数
     */
    public static long getMillis(Date date) {
        return date.getTime();
    }

    /**
     * 指定时间戳的毫秒数
     *
     * @param ts 指定时间戳
     * @return 指定时间戳的毫秒数
     */
    public static long getMillis(Timestamp ts) {
        return ts.getTime();
    }



    /**
     * 指定毫秒数表示日期的默认显示
     *
     * @param millis 指定的毫秒数
     */
    public static String formatDate(long millis,String format) {
        return  getSimpleDateFormat(format).format(new Date(millis));
    }

    /**
     * 默认日期按指定格式显示
     *
     * @param pattern 指定的格式
     * @return 默认日期按指定格式显示
     */
    public static String formatDate(String pattern) {
        return getSimpleDateFormat(pattern).format(new Date());
    }

    /**
     * 指定日期按指定格式显示
     *
     * @param cal     指定的日期
     * @param pattern 指定的格式
     * @return 指定日期按指定格式显示
     */
    public static String formatDate(Calendar cal, String pattern) {
        return getSimpleDateFormat(pattern).format(cal.getTime());
    }

    /**
     * 指定日期按指定格式显示
     *
     * @param date    指定的日期
     * @param pattern 指定的格式
     * @return 指定日期按指定格式显示
     */
    public static String formatDate(Date date, String pattern) {
        return getSimpleDateFormat(pattern).format(date);
    }
    /**
     * 指定毫秒数表示日期的默认显示，具体格式：年-月-日 时：分
     *
     * @param millis 指定的毫秒数
     * @return 指定毫秒数表示日期按“年-月-日 时：分“格式显示
     */
    public static String formatTime(long millis,String  format) {
        return  getSimpleDateFormat(format).format(new Date(millis));
    }

    /**
     * 指定日期的默认显示，具体格式：年-月-日 时：分
     *
     * @param cal 指定的日期
     * @return 指定日期按“年-月-日 时：分“格式显示
     */
    public static String formatTime(Calendar cal,String format) {
        return  getSimpleDateFormat(format).format(cal.getTime());
    }

    /**
     * 指定日期的默认显示，具体格式：年-月-日 时：分
     *
     * @param date 指定的日期
     * @return 指定日期按“年-月-日 时：分“格式显示
     */
    public static String formatTime(Date date,String format) {
        return  getSimpleDateFormat(format).format(date);
    }
    /**
     * 根据指定的格式将字符串转换成Date 如输入：To003-11-19 11:To0:To0将按照这个转成时间
     *
     * @param src     将要转换的原始字符窜
     * @param pattern 转换的匹配格式
     * @return 如果转换成功则返回转换后的日期
     * @throws ParseException
     */
    public static Date parseDate(String src, String pattern)
            throws ParseException {
        return getSimpleDateFormat(pattern).parse(src);

    }

    /**
     * 根据指定的格式将字符串转换成Date 如输入：To003-11-19 11:To0:To0将按照这个转成时间
     *
     * @param src     将要转换的原始字符窜
     * @param pattern 转换的匹配格式
     * @return 如果转换成功则返回转换后的日期
     * @throws ParseException
     */
    public static Calendar parseCalendar(String src, String pattern)
            throws ParseException {

        Date date = parseDate(src, pattern);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    public static String formatDate(String date, String pattern, int amount)
            throws ParseException {
        Calendar cal;
        cal = parseCalendar(date, pattern);
        cal.add(Calendar.DATE, amount);
        return formatDate(cal,pattern);
    }

    /**
     * 根据指定的格式将字符串转换成Date 如输入：To003-11-19 11:To0:To0将按照这个转成时间
     *
     * @param src     将要转换的原始字符窜
     * @param pattern 转换的匹配格式
     * @return 如果转换成功则返回转换后的时间戳
     * @throws ParseException
     */
    public static Timestamp parseTimestamp(String src, String pattern)
            throws ParseException {
        Date date = parseDate(src, pattern);
        return new Timestamp(date.getTime());
    }
    /**
     * String类型 转换为Date,
     * 如果参数长度为10 转换格式”yyyy-MM-dd“
     * 如果参数长度为19 转换格式”yyyy-MM-dd HH:mm:ss“
     * * @param text
     * String类型的时间值
     */
    public void setAsText(String text) throws IllegalArgumentException {
        if (StringUtils.hasText(text)) {
            try {
                if (text.indexOf(":") == -1 && text.length() == 10) {
                    setValue( getSimpleDateFormat(DateUtils.YYYY_MM_DD).parse(text));
                } else if (text.indexOf(":") > 0 && text.length() == 19) {
                    setValue(getSimpleDateFormat(DateUtils.YYYY_MM_DD_HH_MM_SS).parse(text));
                } else {
                    throw new IllegalArgumentException(
                            "Could not parse date, date format is error ");
                }
            } catch (ParseException ex) {
                IllegalArgumentException iae = new IllegalArgumentException(
                        "Could not parse date: " + ex.getMessage());
                iae.initCause(ex);
                throw iae;
            }
        } else {
            setValue(null);
        }
    }

    /**
     * 获取年
     * @return
     */
    public static int getYear() {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 计算两个时间之间的差值，根据标志的不同而不同
     *
     * @param flag      计算标志，表示按照年/月/日/时/分/秒等计算
     * @param beginTime 减数
     * @param endTime   被减数
     * @return 两个日期之间的差值
     */
    public static int getDateDiff(char flag, Date beginTime, Date endTime) {
        long millisDiff = getMillis(endTime) - getMillis(beginTime);
        if (flag == 'd') {
            return (int) (millisDiff / DAY_TO_MILLIS);
        }

        if (flag == 'h') {
            return (int) (millisDiff / HOUR_TO_MILLIS);
        }

        if (flag == 'm') {
            return (int) (millisDiff / MINUTE_TO_MILLIS);
        }

        if (flag == 's') {
            return (int) (millisDiff / SECOND_TO_MILLIS);
        }
        return 0;
    }

    /***
     * 获取n分钟以后的时间
     * @param minute
     * @return
     */
    public static String getDateAfterMinutes(int minute, String format) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, minute);// 5分钟之前的时间
        String date = DateUtils.formatDate(cal, format);
        return date;
    }
}
