package org.jeecgframework.web.utils;

import org.jeecgframework.core.util.StringUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by admin on 2015/11/21.
 */
public class StringUtils extends StringUtil {
    /**
     * 处理value中的特殊字符
     */
    public static String trimSpecialText(String str) {
        StringBuffer sb = new StringBuffer();
        for (int i=0; i<str.length(); i++) {
            char c = str.charAt(i);
            switch (c){
                case '\"':
                    sb.append("\\\"");
                    break;
                case '\\':
                    sb.append("\\\\");
                    break;
                case '/':
                    sb.append("\\/");
                    break;
                case '\b':
                    sb.append("\\b");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                default:
                    sb.append(c);
            }
        }
        return sb.toString();
    }


    public static String getTextFromHtml(String htmlStr) {
        if (StringUtil.isNotEmpty(htmlStr)){
            String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式
            String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式
            String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
            String regEx_space = "\\s*|\t|\r|\n";//定义空格回车换行符
            Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
            Matcher m_script = p_script.matcher(htmlStr);
            htmlStr = m_script.replaceAll(""); // 过滤script标签

            Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
            Matcher m_style = p_style.matcher(htmlStr);
            htmlStr = m_style.replaceAll(""); // 过滤style标签

            Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
            Matcher m_html = p_html.matcher(htmlStr);
            htmlStr = m_html.replaceAll(""); // 过滤html标签

            Pattern p_space = Pattern.compile(regEx_space, Pattern.CASE_INSENSITIVE);
            Matcher m_space = p_space.matcher(htmlStr);
            htmlStr = m_space.replaceAll(""); // 过滤空格回车标签
            htmlStr = htmlStr.trim(); // 返回文本字符串
//            htmlStr = htmlStr.replaceAll("&nbsp;", "");
//            htmlStr = htmlStr.substring(0, htmlStr.indexOf("。") + 1);
        }
        return htmlStr;
    }
    public static String getSomechar(String text , int  length) {
        String str="";
        if(StringUtil.isNotEmpty(text)){
             str=text.substring(0,text.length()>length?length:text.length());
        }
        return  str;
    }


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


    public static String getUUid() {
        return UUID.randomUUID().toString().replace("-","");
    }

}
