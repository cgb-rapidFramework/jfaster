package com.abocode.jfaster.core.common.util;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

/**
 * Created by admin on 2015/11/21.
 */
@Slf4j
public class StrUtils {
    private StrUtils() {
    }

    /**
     * 获取登录用户IP地址
     *
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        String unknown = "unknown";
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip.equals("0:0:0:0:0:0:0:1")) {
            ip = "本地";
        }
        return ip;
    }


    public static boolean isEmpty(Object obj) {
        return obj == null || obj=="";
    }



    public static String random(int  size) {

        StringBuilder str=new StringBuilder();//定义变长字符串
         //随机生成数字，并添加到字符串
        for(int i=0;i<size;i++){
            str.append(IdUtils.nextInt(10));
        }
        //将字符串转换为数字并输出
        int num=Integer.parseInt(str.toString());
       return  num+"";
    }


    public static String join( List<String> array, char symbol) {
        String result="";
        StringBuilder buffer = new StringBuilder();
        if(array != null) {
            for(int i = 0; i < array.size(); ++i) {
                String temp = array.get(i);
                if(temp != null && temp.trim().length() > 0) {
                    buffer.append(temp + symbol);
                }
            }
            result=buffer.toString();
            if(result.length() > 1) {
                result = result.substring(0, result.length() - 1);
            }
        }

        return result;
    }

    public static boolean isNotEmpty(Object obj) {
        return obj!=null;
    }

    /**
     * 判断这个类是不是java自带的类
     * @param clazz
     * @return
     */
    public static boolean isJDKClass(Class<?> clazz) {
        if(clazz.isArray()){
           return false;
        }else if (clazz.isPrimitive()||clazz.getPackage()==null
                || clazz.getPackage().getName().equals("java.lang")
                || clazz.getPackage().getName().equals("java.math")
                || clazz.getPackage().getName().equals("java.util")) {
           return true;
        }
        return false;
    }

    /**
     * 解析前台encodeURIComponent编码后的参数
     *
     * @param property
     *            (encodeURIComponent(no))
     * @return
     */
    public static String getEncodePra(String property) {
        String trem = "";
        if (isNotEmpty(property)) {
            try {
                trem = URLDecoder.decode(property, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                log.error(e.getMessage());
            }
        }
        return trem;
    }

    /***
     * 替换所有字符
     * @param s
     * @param sf
     * @param sb
     * @return
     */
    public static String replaceAll(String s, String sf, String sb) {
        int i = 0;
        int l = sf.length();
        boolean b = true;
        boolean o = true;
        StringBuilder str = new StringBuilder();

        do {
            int j1 = i;
            i = s.indexOf(sf, i);
            if(i > j1) {
                str.append(s.substring(j1, i)).append(sb);
                i += l;
                o = false;
            } else {
                str.append( s.substring(j1));
                b = false;
            }
        } while(b);

        if(o) {
           return  s;
        }
        return str.toString();
    }

}
