package com.abocode.jfaster.core.platform.view.interactions.easyui;

import lombok.Data;

@Data
public class DataGridColumn {
	protected String title;//表格列名
	protected String field;//数据库对应字段
	protected Integer width;//宽度
	protected String rowspan;//跨列
	protected String colspan;//跨行
	protected String align;//对齐方式
	protected boolean sortable;//是否排序
	protected boolean checkbox;//是否显示复选框
	protected String formatter;//格式化函数
	protected boolean hidden;//是否隐藏
	protected String treeField;//
	protected boolean image;//是否是图片
	protected boolean query;//是否查询
	protected String queryMode = "single";//字段查询模式：single单字段查询；group范围查询
	
	protected boolean autoLoadData = true; // 列表是否自动加载数据
	private boolean frozenColumn=false; // 是否是冰冻列    默认不是
	protected String url;//自定义链接
	protected String function="openwindow";//自定义函数名称
	protected String arg;
	protected String dictionary;
	protected String replace;
	protected String extend;
	protected String style; //列的颜色值
	protected String imageSize;//自定义图片显示大小
	protected String downloadName;//附件下载
	protected boolean autocomplete;//自动补全
	protected String extendParams;//扩展参数,easyui有的,但是jeecg没有的参数进行扩展
	protected boolean popup;
}
