package com.abocode.jfaster.api.system;

import lombok.Data;

import java.util.Date;

@Data
public class TemplateQuery {
    /**
     * 模版编码
     */
    private String theme;
    /**
     * 模版名称
     */
    private String name;
    /**
     * 风格
     */
    private String style;
    /**
     * 模版图片
     */
    private String image;
    /**
     * 主页
     */
    private String pageMain;
    /**
     * 登录页面
     */
    private String pageLogin;
    /**
     * 状态0-未使用，1-使用
     */
    private Integer status;
    private String updateBy;
    private String updateById;
    private Date updateDate;
}
