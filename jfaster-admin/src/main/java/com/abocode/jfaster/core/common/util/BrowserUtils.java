package com.abocode.jfaster.core.common.util;

import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 张代浩
 */
public class BrowserUtils {
    private BrowserUtils() {
    }
    public static String checkBrowse(HttpServletRequest request) {
        final String IE11 = "rv:11.0";
        final String IE10 = "MSIE 10.0";
        final String IE9 = "MSIE 9.0";
        final String IE8 = "MSIE 8.0";
        final String IE7 = "MSIE 7.0";
        final String IE6 = "MSIE 6.0";
        final String QQ = "QQBrowser";
        final String GREEN = "GreenBrowser";
        final String SE360 = "360SE";
        final String FIREFOX = "Firefox";
        final String OPERA = "Opera";
        final String CHROME = "Chrome";
        final String SAFARI = "Safari";
        final String OTHER = "其它";
        String userAgent = request.getHeader("USER-AGENT");
        if (regex(OPERA, userAgent))
            return OPERA;
        if (regex(CHROME, userAgent))
            return CHROME;
        if (regex(FIREFOX, userAgent))
            return FIREFOX;
        if (regex(SAFARI, userAgent))
            return SAFARI;
        if (regex(SE360, userAgent))
            return SE360;
        if (regex(GREEN, userAgent))
            return GREEN;
        if (regex(QQ, userAgent))
            return QQ;
        if (regex(IE11, userAgent))
            return IE11;
        if (regex(IE10, userAgent))
            return IE10;
        if (regex(IE9, userAgent))
            return IE9;
        if (regex(IE8, userAgent))
            return IE8;
        if (regex(IE7, userAgent))
            return IE7;
        if (regex(IE6, userAgent))
            return IE6;
        return OTHER;
    }

    public static boolean regex(String regex, String str) {
        Pattern p = Pattern.compile(regex, Pattern.MULTILINE);
        Matcher m = p.matcher(str);
        return m.find();
    }


    private static Map<String, String> langMap = new HashMap<>();

    static {
        langMap.put("zh", "zh-cn");
        langMap.put("en", "en");
    }

    /***
     * 获取语言信息
     *
     * @return
     */
    public static String getBrowserLanguage() {
        String browserLangCode;
        browserLangCode = (String) ContextHolderUtils.getSession().getAttribute("lang");
        if (StringUtils.isEmpty(browserLangCode)) {
            browserLangCode = "zh-cn";
        }
        return browserLangCode;
    }
}
