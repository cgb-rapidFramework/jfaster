package com.abocode.jfaster.core.platform.view;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用类型字典表
 *
 * @author guanxf
 */
@Data
public class TypeView implements java.io.Serializable {
    private String id;
    private TypeGroupView typeGroup;//类型分组
    private TypeView type;//父类型
    private String typeName;//类型名称
    private String typeCode;//类型编码
    private List<TypeView> types = new ArrayList();

}