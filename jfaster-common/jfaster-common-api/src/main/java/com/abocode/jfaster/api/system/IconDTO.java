package com.abocode.jfaster.api.system;

import lombok.Data;

/**
 * Description:
 *
 * @author: guanxf
 * @date: 2020/9/10
 */
@Data
public class IconDTO {
    private String id;
    private String iconName;
    private Short iconType;
    private String iconPath;
    private byte[] iconContent;
    private String iconClazz;
    private String iconExtend;
}
