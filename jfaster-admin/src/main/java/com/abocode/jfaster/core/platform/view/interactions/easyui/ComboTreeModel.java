package com.abocode.jfaster.core.platform.view.interactions.easyui;

import lombok.Data;

@Data
public class ComboTreeModel implements java.io.Serializable {
	private String idField;
	private String textField;
	private String iconCls;// 前面的小图标样式
	private String childField;// 子节点
	private String srcField;//地址字段
	
	public ComboTreeModel(String idField, String textField, String childField) {
		this.idField = idField;
		this.textField = textField;
		this.childField = childField;
	}
	public ComboTreeModel(String idField, String textField, String childField, String srcField) {
		this.idField = idField;
		this.textField = textField;
		this.childField = childField;
		this.srcField = srcField;
	}

}
