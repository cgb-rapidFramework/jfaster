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
    @JoinColumn(name = "parent_id")
    private Function parentFunction;
    //菜单名称
    @Column(name = "function_name",nullable = false, length = 50)
    private String functionName;
    //菜单等级
    @Column(name = "function_level")
    private Short functionLevel;
    //菜单地址
    @Column(name = "function_url",length = 100)
    private String functionUrl;
    //菜单地址打开方式
    @Column(name = "function_iframe")
    private Short functionIframe;
    //菜单排序
    @Column(name = "function_order")
    private String functionOrder;
    //菜单类型
    @Column(name = "function_type")
    private Short functionType;
    //菜单图标
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "icon_id")
    private Icon icon = new Icon();
    @ManyToOne(fetch = FetchType.LAZY)
    //云桌面菜单图标
    @JoinColumn(name = "desk_icon_id")
    private Icon iconDesk = new Icon();
    //子菜单
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parentFunction")
    private List<Function> functions = new ArrayList<>();

    @Override
    public String toString() {
        return "Function{" +
                "id='" + getId() + '\'' +
                ", functionName='" + functionName + '\'' +
                ", functionLevel=" + functionLevel +
                ", functionUrl='" + functionUrl + '\'' +
                ", functionIframe=" + functionIframe +
                ", functionOrder='" + functionOrder + '\'' +
                ", functionType=" + functionType +
                '}';
    }
}