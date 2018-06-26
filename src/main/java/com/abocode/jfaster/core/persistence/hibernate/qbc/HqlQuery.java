package com.abocode.jfaster.core.persistence.hibernate.qbc;

import java.util.List;
import java.util.Map;

import com.abocode.jfaster.core.common.model.json.DataGrid;
import lombok.Data;
import org.hibernate.type.Type;

@Data
public class HqlQuery {
	private int curPage =1;
	private int pageSize = 10;
	private String myaction;
	private String myform;
	private String queryString;
	private Object[] param;
	private Type[] types;
	private Map<String, Object> map;
	private DataGrid dataGrid;
	private String field="";//查询需要显示的字段
	private Class class1;
	private List results;// 结果集
	private int total;
}
