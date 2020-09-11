package com.abocode.jfaster.core.platform.view.interactions.easyui;

import lombok.Data;
@Data
public class TreeGridModel implements java.io.Serializable {
	private String idField;
	private String textField;
	private String childList;
 	private String parentId;
 	private String parentText;
 	private String code;
 	private String src;
 	private String roleId;
 	private String icon;
 	private String order;
 	private String functionType;
}
