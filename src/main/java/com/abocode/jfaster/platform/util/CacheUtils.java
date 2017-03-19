package com.abocode.jfaster.platform.util;

import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * 缓存文件
 * Created by guanxf on 2016/6/10.
 */
public class CacheUtils {


    public static Cookie putCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        //设置cookie有效期为一个月
        cookie.setMaxAge(3600 * 24 * 30);
        return cookie;
    }

    public static String getCookie(HttpServletRequest request, String key) {
        String val = "";
        try {
            Cookie[] cookies = request.getCookies();
            for (Cookie cookie : cookies) {
                if (cookie == null || StringUtils.isEmpty(cookie.getName())) {
                    continue;
                }
                if (cookie.getName().equalsIgnoreCase(key)) {
                    val = cookie.getValue();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return val;
    }
}
