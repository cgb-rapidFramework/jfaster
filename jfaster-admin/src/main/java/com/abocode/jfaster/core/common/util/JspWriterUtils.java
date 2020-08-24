package com.abocode.jfaster.core.common.util;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.jsp.JspWriter;
import java.io.IOException;

/**
 *
 * Created by guanxf on 2016/4/17.
 */
@Slf4j
public class JspWriterUtils {
    public static void write(JspWriter out, String text) {
        try {
            out.print(text);
            out.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }

    }
}
