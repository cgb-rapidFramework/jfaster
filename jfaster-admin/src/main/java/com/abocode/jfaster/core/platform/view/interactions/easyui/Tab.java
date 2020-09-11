package com.abocode.jfaster.core.platform.view.interactions.easyui;

import lombok.Data;

@Data
public class Tab {
	private String href;
	private String iframe;
	private String id;
	private String title;
	private String icon = "'icon-default'";
	private String width;// 宽度
	private String height;// 高度
	private boolean cache;
	private String content;
	private boolean closable=true;
}
