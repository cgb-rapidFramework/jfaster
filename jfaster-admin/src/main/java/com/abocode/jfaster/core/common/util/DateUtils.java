package com.abocode.jfaster.core.common.util;

import java.beans.PropertyEditorSupport;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
@Slf4j
public class DateUtils extends PropertyEditorSupport {// 各种时间格式


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
     * 字符串转换成日期
     *
     * @param str
     * @param sdf
     * @return
     */
    public static Date strToDate(String str, String sdf) {
        if (null == str || "".equals(str)) {
            return null;
        }
        Date date;
        try {
            date = getSimpleDateFormat(sdf).parse(str);
            return date;
        } catch (ParseException e) {
            log.error(e.getMessage());
        }
        return null;
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
     * 系统当前的时间戳
     *
     * @return 系统当前的时间戳
     */
    public static Timestamp getTimestamp() {
        return new Timestamp(new Date().getTime());
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
     * 指定毫秒数表示日期的默认显示
     *
     * @param millis 指定的毫秒数
     */
    public static String formatDate(long millis, String format) {
        return getSimpleDateFormat(format).format(new Date(millis));
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
    public static String formatTime(long millis, String format) {
        return getSimpleDateFormat(format).format(new Date(millis));
    }

    /**
     * 指定日期的默认显示，具体格式：年-月-日 时：分
     *
     * @param cal 指定的日期
     * @return 指定日期按“年-月-日 时：分“格式显示
     */
    public static String formatTime(Calendar cal, String format) {
        return getSimpleDateFormat(format).format(cal.getTime());
    }

    /**
     * 指定日期的默认显示，具体格式：年-月-日 时：分
     *
     * @param date 指定的日期
     * @return 指定日期按“年-月-日 时：分“格式显示
     */
    public static String formatTime(Date date, String format) {
        return getSimpleDateFormat(format).format(date);
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
        return formatDate(cal, pattern);
    }

    /**
     * 根据指定的格式将字符串转换成Date 如输入：To003-11-19 11:To0:To0将按照这个转成时间
     *
     * @param src     将要转换的原始字符窜
     * @param pattern 转换的匹配格式
     * @return 如果转换成功则返回转换后的时间戳
     */
    public static Timestamp parseTimestamp(String src, String pattern) {
        try {
            Date date = parseDate(src, pattern);
            return new Timestamp(date.getTime());
        } catch (ParseException e) {
            throw new IllegalArgumentException("时间转换错误");
        }

    }

    /**
     * String类型 转换为Date,
     * 如果参数长度为10 转换格式”yyyy-MM-dd“
     * 如果参数长度为19 转换格式”yyyy-MM-dd HH:mm:ss“
     * * @param text
     * String类型的时间值
     */
    public void setAsText(String text) throws IllegalArgumentException {
        String YYYY_MM_DD = "yyyy-MM-dd";
        String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
        if (StringUtils.hasText(text)) {
            try {
                if (text.indexOf(":") == -1 && text.length() == 10) {
                    setValue(getSimpleDateFormat(YYYY_MM_DD).parse(text));
                } else if (text.indexOf(":") > 0 && text.length() == 19) {
                    setValue(getSimpleDateFormat(YYYY_MM_DD_HH_MM_SS).parse(text));
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
     *
     * @return
     */
    public static int getYear() {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        return calendar.get(Calendar.YEAR);
    }


}
