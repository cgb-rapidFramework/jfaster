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

    // 判断是否是IE
    public static boolean isIE(HttpServletRequest request) {
        return (request.getHeader("USER-AGENT").toLowerCase().indexOf("msie") > 0 || request
                .getHeader("USER-AGENT").toLowerCase().indexOf("rv:11.0") > 0) ? true
                : false;
    }


    private final static String IE11 = "rv:11.0";
    private final static String IE10 = "MSIE 10.0";
    private final static String IE9 = "MSIE 9.0";
    private final static String IE8 = "MSIE 8.0";
    private final static String IE7 = "MSIE 7.0";
    private final static String IE6 = "MSIE 6.0";
    private final static String MAXTHON = "Maxthon";
    private final static String QQ = "QQBrowser";
    private final static String GREEN = "GreenBrowser";
    private final static String SE360 = "360SE";
    private final static String FIREFOX = "Firefox";
    private final static String OPERA = "Opera";
    private final static String CHROME = "Chrome";
    private final static String SAFARI = "Safari";
    private final static String OTHER = "其它";

    public static String checkBrowse(HttpServletRequest request) {
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
        if (regex(MAXTHON, userAgent))
            return MAXTHON;
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


    private static Map<String, String> langMap = new HashMap<String, String>();
    private final static String ZH = "zh";
    private final static String ZH_CN = "zh-cn";

    private final static String EN = "en";
    private final static String EN_US = "en";


    static {
        langMap.put(ZH, ZH_CN);
        langMap.put(EN, EN_US);
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
            browserLangCode = ZH_CN;
        }
        return browserLangCode;
    }
}
