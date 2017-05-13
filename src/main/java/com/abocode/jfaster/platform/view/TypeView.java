package com.abocode.jfaster.platform.view;

import com.abocode.jfaster.core.common.entity.IdEntity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * 通用类型字典表
 *
 * @author guanxf
 */
public class TypeView extends IdEntity implements java.io.Serializable {

    private TypeGroupView TSTypegroup;//类型分组
    private TypeView TSType;//父类型
    private String typename;//类型名称
    private String typecode;//类型编码
    private List<TypeView> TSTypes = new ArrayList();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "typegroupid")
    public TypeGroupView getTSTypegroup() {
        return this.TSTypegroup;
    }

    public void setTSTypegroup(TypeGroupView TSTypegroup) {
        this.TSTypegroup = TSTypegroup;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "typepid")
    public TypeView getTSType() {
        return this.TSType;
    }

    public void setTSType(TypeView TSType) {
        this.TSType = TSType;
    }

    @Column(name = "typename", length = 50)
    public String getTypename() {
        return this.typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    @Column(name = "typecode", length = 50)
    public String getTypecode() {
        return this.typecode;
    }

    public void setTypecode(String typecode) {
        this.typecode = typecode;
    }

    //	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "TSType")
//	public List<TPProcess> getTSProcesses() {
//		return this.TSProcesses;
//	}
//
//	public void setTSProcesses(List<TPProcess> TSProcesses) {
//		this.TSProcesses = TSProcesses;
//	}
    public List<TypeView> getTSTypes() {
        return this.TSTypes;
    }

    public void setTSTypes(List<TypeView> TSTypes) {
        this.TSTypes = TSTypes;
    }

}