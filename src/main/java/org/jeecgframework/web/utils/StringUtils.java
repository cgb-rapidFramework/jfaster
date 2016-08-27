package org.jeecgframework.web.utils;

import org.jeecgframework.core.util.StringParentUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Random;

/**
 * Created by admin on 2015/11/21.
 */
public class StringUtils extends StringParentUtils {
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
}
