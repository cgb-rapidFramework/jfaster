package com.abocode.jfaster.core.platform.view.interactions.easyui;

import lombok.Data;

@Data
public class TabMenu {
	private String classLi ;//li上样式
	private String url;    //url地址
 	private String title;//名称
	private String icon;//标签图标
	private String iconColor;//标签图标颜色
	private boolean tab=true;//是否为可切换标签页
}
