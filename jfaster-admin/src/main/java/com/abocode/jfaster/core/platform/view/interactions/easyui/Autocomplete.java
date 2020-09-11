package com.abocode.jfaster.core.platform.view.interactions.easyui;

import lombok.Data;

@Data
public class Autocomplete {
	private String entityName;//实体名称
	private String labelField;//提示显示的字段
	private String valueField;//传递后台的字段
	private String searchField;//查询关键字字段
	private String trem;//查询传递的值
	private Integer maxRows;
	private Integer curPage;
	public Integer getCurPage() {
		if(curPage==null||curPage<1){
			curPage = 1;
		}
		return curPage;
	}

}
