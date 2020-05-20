package com.abocode.jfaster.core.platform.view;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 *菜单权限表
 * @author  guanxf
 */
@Data
public class FunctionView  implements java.io.Serializable {
	private String id;
	//父菜单
	private FunctionView parentFunction;
	//菜单名称
	private String functionName;
	//菜单等级
	private Short functionLevel;
	//菜单地址
	private String functionUrl;
	//菜单地址打开方式
	private Short functionIframe;
	//菜单排序
	private String functionOrder;
	//菜单类型
	private Short functionType;
	//菜单图标
	private IconView icon = new IconView();
	//云桌面菜单图标
	private IconView iconDesk = new IconView();
	//子菜单
	private List<FunctionView> functions = new ArrayList<FunctionView>();

	public boolean hasSubFunction(Map<Integer, List<FunctionView>> map) {
		if (map.containsKey(this.getFunctionLevel() + 1)) {
			return hasSubFunction(map.get(this.getFunctionLevel() + 1));
		}
		return false;
	}

	public boolean hasSubFunction(List<FunctionView> functions) {
		for (FunctionView f : functions) {
			if (f.getParentFunction().getId().equals(this.getId())) {
				return true;
			}
		}
		return false;
	}
}