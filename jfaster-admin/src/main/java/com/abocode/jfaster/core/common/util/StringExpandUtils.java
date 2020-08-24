package com.abocode.jfaster.core.common.util;
import org.springframework.util.StringUtils;

import java.util.UUID;
/**
 * Created by admin on 2016/1/29.
 */
public class StringExpandUtils  extends StringUtils{

    /***
     * 替换所有字符
     * @param s
     * @param sf
     * @param sb
     * @return
     */
    public static String replaceAll(String s, String sf, String sb) {
        int i = 0;
        boolean j = false;
        int l = sf.length();
        boolean b = true;
        boolean o = true;
        String str = "";

        do {
            int j1 = i;
            i = s.indexOf(sf, i);
            if(i > j1) {
                str = str + s.substring(j1, i);
                str = str + sb;
                i += l;
                o = false;
            } else {
                str = str + s.substring(j1);
                b = false;
            }
        } while(b);

        if(o) {
            str = s;
        }

        return str;
    }
}
