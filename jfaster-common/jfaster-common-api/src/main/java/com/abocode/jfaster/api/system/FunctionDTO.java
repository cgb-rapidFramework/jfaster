package com.abocode.jfaster.api.system;

import lombok.Data;

/**
 * Description:
 *
 * @author: guanxf
 * @date: 2020/9/10
 */
@Data
public class FunctionDTO {
    //父菜单
    private FunctionDTO parentFunction;
    //菜单名称
    private String id;
    private String functionName;
    private Short functionLevel;
    private String functionUrl;
    private Short functionIframe;
    private String functionOrder;
    private Short functionType;
}
