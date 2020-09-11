package com.abocode.jfaster.core.platform.view.interactions.easyui;

import lombok.Data;

@Data
public class Carousel {
	private String path; //图片路径
	private String message; //图片信息描述
	private boolean active; //当前显示项
}
