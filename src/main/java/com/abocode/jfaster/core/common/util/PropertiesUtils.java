package com.abocode.jfaster.core.common.util;


import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.Properties;

/**
 * @author 张代浩
 */
public class PropertiesUtils {
    private String properiesName = "";

    public PropertiesUtils() {

    }

    public PropertiesUtils(String fileName) {
        this.properiesName = fileName;
    }

    public String readProperty(String key) {
        String value = "";
        InputStream is = null;
        try {
            is = PropertiesUtils.class.getClassLoader().getResourceAsStream(
                    properiesName);
            Properties p = new Properties();
            p.load(is);
            value = p.getProperty(key);
        } catch (IOException e) {
            LogUtils.error(e.getMessage());
        } finally {
            IOUtils.closeQuietly(is);
        }
        return value;
    }

    public Properties getProperties() {
        Properties p = new Properties();
        InputStream is = null;
        try {
            is = PropertiesUtils.class.getClassLoader().getResourceAsStream(
                    properiesName);
            p.load(is);
        } catch (IOException e) {
            LogUtils.error(e.getMessage());
        } finally {
            IOUtils.closeQuietly(is);
        }
        return p;
    }

    public void writeProperty(String key, String value) {
        InputStream is = null;
        OutputStream os = null;
        Properties p = new Properties();
        try {
            is = new FileInputStream(properiesName);
            p.load(is);
            os = new FileOutputStream(PropertiesUtils.class.getClassLoader().getResource(properiesName).getFile());

            p.setProperty(key, value);
            p.store(os, key);
            os.flush();
            os.close();
        } catch (Exception e) {
            LogUtils.error(e.getMessage());
        } finally {
            try {
                if (null != is)
                    is.close();
                if (null != os)
                    os.close();
            } catch (IOException e) {
                LogUtils.error(e.getMessage());
            }
        }

    }
}
