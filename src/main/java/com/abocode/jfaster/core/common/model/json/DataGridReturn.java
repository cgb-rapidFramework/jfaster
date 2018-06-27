package com.abocode.jfaster.core.common.model.json;

import java.util.List;

/**
 * 后台向前台返回JSON，用于easyui的datagrid
 * 
 * @author 
 * 
 */
public class DataGridReturn  extends  DataGrid{
	public DataGridReturn(Integer total, List rows) {
		this.total = total;
		this.rows = rows;
	}

	private Integer total;// 总记录数
	private List rows;// 每行记录
	private List footer;


}
