package com.abocode.jfaster.admin.system.dto;

import lombok.Data;

/**
 * Created by Franky on 2016/3/15.
 */
@Data
public class FunctionDto implements java.io.Serializable {
    private String id;
    private String functionName;//菜单名称
    private Short functionLevel;//菜单等级
    private String functionUrl;//菜单地址
    private Short functionIframe;//菜单地址打开方式
    private String functionOrder;//菜单排序
    private Short functionType;//菜单类型
}
