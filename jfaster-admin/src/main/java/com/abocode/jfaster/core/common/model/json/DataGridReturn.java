package com.abocode.jfaster.core.common.model.json;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		DataGridReturn that = (DataGridReturn) o;
		return total == that.total &&
				Objects.equals(results, that.results);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), total, results);
	}
}
