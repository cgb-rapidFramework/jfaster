package com.abocode.jfaster.core.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@Slf4j
public class ConvertUtils {
    public static boolean isEmpty(Object object) {
        if (object == null) {
            return (true);
        }
        if (object.equals("")) {
            return (true);
        }
        if (object.equals("null")) {
            return (true);
        }
        return (false);
    }

    public static boolean isNotEmpty(Object object) {
        if (object != null && !object.equals("") && !object.equals("null")) {
            return (true);
        }
        return (false);
    }

    public static String decode(String strIn, String sourceCode, String targetCode) {
        String temp = code2code(strIn, sourceCode, targetCode);
        return temp;
    }

    private static String code2code(String strIn, String sourceCode, String targetCode) {
        String strOut = null;
        if (strIn == null || (strIn.trim()).equals(""))
            return strIn;
        try {
            byte[] b = strIn.getBytes(sourceCode);
            for (int i = 0; i < b.length; i++) {
                System.out.print(b[i] + "  ");
            }
            strOut = new String(b, targetCode);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
        return strOut;
    }

    public static int getInt(String s, int defval) {
        if (StringUtils.isEmpty(s)) {
            return (defval);
        }
        try {
            return (Integer.parseInt(s));
        } catch (NumberFormatException e) {
            return (defval);
        }
    }

    public static int getInt(String s) {
        if (StringUtils.isEmpty(s)) {
            return 0;
        }
        try {
            return (Integer.parseInt(s));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static boolean getBoolean(String s) {
        if (StringUtils.isEmpty(s)) {
            return false;
        }
        try {
            return (Boolean.parseBoolean(s));
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static Integer[] getInts(String[] s) {
        if (s == null) {
            return null;
        }
        Integer[] integer = new Integer[s.length];
        for (int i = 0; i < s.length; i++) {
            integer[i] = Integer.parseInt(s[i]);
        }
        return integer;

    }

    public static Short getShort(String s) {
        if (!StringUtils.isEmpty(s)) {
            return (Short.parseShort(s));
        } else {
            return null;
        }
    }

    public static int getInt(Object object, int defval) {
        if (isEmpty(object)) {
            return (defval);
        }
        try {
            return (Integer.parseInt(object.toString()));
        } catch (NumberFormatException e) {
            return (defval);
        }
    }


    public static String getString(String s) {
        return (getString(s, ""));
    }

    public static String getString(Object object) {
        if (isEmpty(object)) {
            return "";
        }
        return (object.toString().trim());
    }

    public static String getString(int i) {
        return (String.valueOf(i));
    }

    public static String getString(float i) {
        return (String.valueOf(i));
    }

    public static String getString(String s, String defval) {
        if (isEmpty(s)) {
            return (defval);
        }
        return (s.trim());
    }

    public static String getString(Object s, String defval) {
        if (isEmpty(s)) {
            return (defval);
        }
        return (s.toString().trim());
    }


    /**
     * 获取本机IP
     */
    public static String getIp() {
        String ip = null;
        try {
            InetAddress address = InetAddress.getLocalHost();
            ip = address.getHostAddress();

        } catch (UnknownHostException e) {
            log.error(e.getMessage());
        }
        return ip;
    }

    /**
     * java去除字符串中的空格、回车、换行符、制表符
     *
     * @param str
     * @return
     */
    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;

    }

    /***
     * 替换下划线
     * @param str
     * @return
     */
    public static String replaceUnderLine(String str) {
        if (str.contains("_")) {
            return str.replaceAll("_", "\\.");
        }
        return str;
    }

    /**
     * 判断元素是否在数组内
     *
     * @param substring
     * @param source
     * @return
     */
    public static boolean isIn(String substring, String[] source) {
        if (source == null || source.length == 0) {
            return false;
        }
        for (int i = 0; i < source.length; i++) {
            String aSource = source[i];
            if (aSource.equals(substring)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取Map对象
     */
    public static Map<Object, Object> getHashMap() {
        return new HashMap<Object, Object>();
    }

    /**
     * SET转换MAP
     *
     * @param setobj
     * @return
     */
    public static Map<Object, Object> SetToMap(Set<Object> setobj) {
        Map<Object, Object> map = getHashMap();
        for (Iterator iterator = setobj.iterator(); iterator.hasNext(); ) {
            Map.Entry<Object, Object> entry = (Map.Entry<Object, Object>) iterator.next();
            map.put(entry.getKey().toString(), entry.getValue() == null ? "" : entry.getValue().toString().trim());
        }
        return map;

    }

    public static String decode(String str) {
        try {
            return URLDecoder.decode(str, "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
