package com.abocode.jfaster.core.common.model.json;

import lombok.Data;

import java.util.List;
import java.util.Map;
@Data

public class ComboTree implements java.io.Serializable {

	private String id;
	private String text;// 树节点名称
	private String iconCls;// 前面的小图标样式
	private Boolean checked = false;// 是否勾选状态
	private Map<String, Object> attributes;// 其他参数
	private List<ComboTree> children;// 子节点
	private String state = "open";// 是否展开(open,closed)
}
