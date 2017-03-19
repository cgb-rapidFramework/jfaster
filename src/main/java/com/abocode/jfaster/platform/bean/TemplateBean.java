package com.abocode.jfaster.platform.bean;

/**
 * 模版
 * Created by guanxf on 2016/5/15.
 */
public class TemplateBean {
    /**模版编码*/
    private java.lang.String theme;//主题
    /**模版名称*/
    private java.lang.String name;//模版名称
    /**风格*/
    private java.lang.String style;//模版风格
    /**主页*/
    private java.lang.String pageMain;//首页地址

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getPageMain() {
        return pageMain;
    }

    public void setPageMain(String pageMain) {
        this.pageMain = pageMain;
    }
}
