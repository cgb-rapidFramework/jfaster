package com.abocode.jfaster.admin.system.dto.view;
import com.abocode.jfaster.core.AbstractIdEntity;

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
public class FunctionView extends AbstractIdEntity implements java.io.Serializable {
	private FunctionView TSFunction;//父菜单
	private String functionName;//菜单名称
	private Short functionLevel;//菜单等级
	private String functionUrl;//菜单地址
	private Short functionIframe;//菜单地址打开方式
	private String functionOrder;//菜单排序
	private Short functionType;//菜单类型
	private IconView TSIcon = new IconView();//菜单图标

	public boolean hasSubFunction(Map<Integer, List<FunctionView>> map) {
		if (map.containsKey(this.getFunctionLevel() + 1)) {
			return hasSubFunction(map.get(this.getFunctionLevel() + 1));
		}
		return false;
	}

	public boolean hasSubFunction(List<FunctionView> functions) {
		for (FunctionView f : functions) {
			if (f.getFunction().getId().equals(this.getId())) {
				return true;
			}
		}
		return false;
	}

	private IconView TSIconDesk;//云桌面菜单图标
	private List<FunctionView> TSFunctions = new ArrayList<FunctionView>();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "desk_iconid")
    public IconView getIconDesk() {
        return TSIconDesk;
    }
    public void setIconDesk(IconView TSIconDesk) {
        this.TSIconDesk = TSIconDesk;
    }
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "iconid")
	public IconView getIcon() {
		return TSIcon;
	}
	public void setIcon(IconView tSIcon) {
		TSIcon = tSIcon;
	}
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parentfunctionid")
	public FunctionView getFunction() {
		return this.TSFunction;
	}

	public void setFunction(FunctionView TSFunction) {
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
	public List<FunctionView> getFunctions() {
		return TSFunctions;
	}
	public void setFunctions(List<FunctionView> TSFunctions) {
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