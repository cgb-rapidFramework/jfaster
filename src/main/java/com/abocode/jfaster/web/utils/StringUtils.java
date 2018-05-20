package com.abocode.jfaster.web.utils;

import com.abocode.jfaster.core.util.LogUtils;
import com.abocode.jfaster.platform.util.StringExpandUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Random;

/**
 * Created by admin on 2015/11/21.
 */
public class StringUtils extends StringExpandUtils {
    /**
     * 获取登录用户IP地址
     *
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip.equals("0:0:0:0:0:0:0:1")) {
            ip = "本地";
        }
        return ip;
    }


    public static boolean isEmpty(List list) {
        return list == null || list.isEmpty();
    }

    public static boolean isEmptyNull(String s) {
        return s == null || s.equals("") ||  s.equals("null");
    }


    public static String random(int  size) {

        StringBuilder str=new StringBuilder();//定义变长字符串
        Random random=new Random();
         //随机生成数字，并添加到字符串
        for(int i=0;i<size;i++){
            str.append(random.nextInt(10));
        }
        //将字符串转换为数字并输出
        int num=Integer.parseInt(str.toString());
       return  num+"";
    }


    public static String join(List array, char symbol) {
        String result="";
        StringBuffer buffer = new StringBuffer();
        if(array != null) {
            for(int i = 0; i < array.size(); ++i) {
                String temp = array.get(i).toString();
                if(temp != null && temp.trim().length() > 0) {
                    buffer.append(temp + symbol);
//                    result = result + temp + symbol;
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
        return  !isEmpty(obj);
    }

    /**
     * 判断这个类是不是java自带的类
     * @param clazz
     * @return
     */
    public static boolean isJDKClass(Class<?> clazz) {
        boolean isBaseClass = false;
        if(clazz.isArray()){
            isBaseClass = false;
        }else if (clazz.isPrimitive()||clazz.getPackage()==null
                || clazz.getPackage().getName().equals("java.lang")
                || clazz.getPackage().getName().equals("java.math")
                || clazz.getPackage().getName().equals("java.util")) {
            isBaseClass =  true;
        }
        return isBaseClass;
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
                LogUtils.error(e.getMessage());
            }
        }
        return trem;
    }


}
