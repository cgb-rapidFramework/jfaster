package com.abocode.jfaster.core.common.model.json;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 后台向前台返回JSON，用于easyui的datagrid
 * 
 * @author 
 * 
 */
@Getter
@Setter
public class DataGridReturn  extends  DataGrid{
	private int total;// 总记录数
	private List results;// 每行记录
	public DataGridReturn(int total, List results) {
		super(total,results);
		this.total = total;
		this.results = results;
	}
}
