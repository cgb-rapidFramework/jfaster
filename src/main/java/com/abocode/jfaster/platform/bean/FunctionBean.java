package com.abocode.jfaster.platform.bean;
import com.abocode.jfaster.core.common.entity.IdEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *菜单权限表
 * @author  guanxf
 */
@SuppressWarnings("serial")
public class FunctionBean extends IdEntity implements java.io.Serializable {
	private FunctionBean TSFunction;//父菜单
	private String functionName;//菜单名称
	private Short functionLevel;//菜单等级
	private String functionUrl;//菜单地址
	private Short functionIframe;//菜单地址打开方式
	private String functionOrder;//菜单排序
	private Short functionType;//菜单类型
	private IconBean TSIcon = new IconBean();//菜单图标

	public boolean hasSubFunction(Map<Integer, List<FunctionBean>> map) {
		if (map.containsKey(this.getFunctionLevel() + 1)) {
			return hasSubFunction(map.get(this.getFunctionLevel() + 1));
		}
		return false;
	}

	public boolean hasSubFunction(List<FunctionBean> functions) {
		for (FunctionBean f : functions) {
			if (f.getTSFunction().getId().equals(this.getId())) {
				return true;
			}
		}
		return false;
	}

	private IconBean TSIconDesk;//云桌面菜单图标
	private List<FunctionBean> TSFunctions = new ArrayList<FunctionBean>();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "desk_iconid")
    public IconBean getTSIconDesk() {
        return TSIconDesk;
    }
    public void setTSIconDesk(IconBean TSIconDesk) {
        this.TSIconDesk = TSIconDesk;
    }
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "iconid")
	public IconBean getTSIcon() {
		return TSIcon;
	}
	public void setTSIcon(IconBean tSIcon) {
		TSIcon = tSIcon;
	}
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parentfunctionid")
	public FunctionBean getTSFunction() {
		return this.TSFunction;
	}

	public void setTSFunction(FunctionBean TSFunction) {
		this.TSFunction = TSFunction;
	}

	@Column(name = "functionname", nullable = false, length = 50)
	public String getFunctionName() {
		return this.functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	@Column(name = "functionlevel")
	public Short getFunctionLevel() {
		return this.functionLevel;
	}

	public void setFunctionLevel(Short functionLevel) {
		this.functionLevel = functionLevel;
	}
	
	@Column(name = "functiontype")
	public Short getFunctionType() {
		return this.functionType;
	}

	public void setFunctionType(Short functionType) {
		this.functionType = functionType;
	}
	
	@Column(name = "functionurl", length = 100)
	public String getFunctionUrl() {
		return this.functionUrl;
	}

	public void setFunctionUrl(String functionUrl) {
		this.functionUrl = functionUrl;
	}
	@Column(name = "functionorder")
	public String getFunctionOrder() {
		return functionOrder;
	}

	public void setFunctionOrder(String functionOrder) {
		this.functionOrder = functionOrder;
	}
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "TSFunction")
	public List<FunctionBean> getTSFunctions() {
		return TSFunctions;
	}
	public void setTSFunctions(List<FunctionBean> TSFunctions) {
		this.TSFunctions = TSFunctions;
	}
	@Column(name = "functioniframe")
	public Short getFunctionIframe() {
		return functionIframe;
	}
	public void setFunctionIframe(Short functionIframe) {
		this.functionIframe = functionIframe;
	}

}