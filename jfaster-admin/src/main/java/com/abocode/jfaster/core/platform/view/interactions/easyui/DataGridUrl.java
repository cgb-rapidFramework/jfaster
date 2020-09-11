package com.abocode.jfaster.core.platform.view.interactions.easyui;

import lombok.Data;

@Data
public class DataGridUrl {
	private String url;//操作链接地址
	private String title;//按钮名称
	private String icon;//按钮图标
	private String value;//传入参数
	private String width;//弹出窗宽度
	private String height;//弹出窗高度
	private OptTypeDirection type;//按钮类型
	private String isbtn;//是否是操作选项以外的链接
	private String message;//询问链接的提示语
	private String exp;//判断链接是否显示的表达式
	private String function;//自定义函数名称
	private boolean isRadio;//是否是单选框
	private String onclick;//选项单击事件
}
