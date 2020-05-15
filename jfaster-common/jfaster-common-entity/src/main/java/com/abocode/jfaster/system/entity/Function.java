package com.abocode.jfaster.system.entity;

import com.abocode.jfaster.core.AbstractIdEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 菜单权限表
 */
@Entity
@Table(name = "t_s_function")
@Data
public class Function extends AbstractIdEntity implements java.io.Serializable {
    //父菜单
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentfunctionid")
    private Function parentFunction;
    //菜单名称
    @Column(nullable = false, length = 50)
    private String functionName;
    //菜单等级
    private Short functionLevel;
    //菜单地址
    @Column(length = 100)
    private String functionUrl;
    //菜单地址打开方式
    private Short functionIframe;
    //菜单排序
    private String functionOrder;
    //菜单类型
    private Short functionType;
    //菜单图标
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "iconId")
    private Icon icon = new Icon();
    @ManyToOne(fetch = FetchType.LAZY)
    //云桌面菜单图标
    @JoinColumn(name = "desk_iconid")
    private Icon iconDesk = new Icon();
    private List<Function> functions = new ArrayList<Function>();
}