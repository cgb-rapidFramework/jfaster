package com.abocode.jfaster.core.platform.view;

import lombok.Data;

/**
 * 模版
 * Created by guanxf on 2016/5/15.
 */
@Data
public class TemplateView {
    /**
     * 模版编码
     */
    private java.lang.String theme;//主题
    /**
     * 模版名称
     */
    private java.lang.String name;//模版名称
    /**
     * 风格
     */
    private java.lang.String style;//模版风格
    /**
     * 主页
     */
    private java.lang.String pageMain;//首页地址
}
