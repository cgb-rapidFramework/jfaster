package com.abocode.jfaster.core.common.model.json;


import lombok.Data;

/**
 * 后台向前台返回JSON，用于easyui的datagrid
 *
 * @author
 */
@Data
public class ComboBox {
	private String id;
	private String text;
	private boolean selected;
}
