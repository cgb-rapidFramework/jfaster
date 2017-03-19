package com.abocode.jfaster.platform.util;

import javax.servlet.jsp.JspWriter;
import java.io.IOException;

/**
 *
 * Created by guanxf on 2016/4/17.
 */
public class JspWriterUtils {
    public static void write(JspWriter out, String text) {
        try {
            out.print(text);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
