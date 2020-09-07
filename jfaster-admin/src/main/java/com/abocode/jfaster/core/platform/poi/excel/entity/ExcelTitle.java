package com.abocode.jfaster.core.platform.poi.excel.entity;

import lombok.Data;
import org.apache.poi.hssf.util.HSSFColor;

/**
 * 表格属性
 */
@Data
public class ExcelTitle {
	public ExcelTitle(String title,String secondTitle,String sheetName){
		this.title = title;
		this.secondTitle = secondTitle;
		this.sheetName = sheetName;
	}

	/**
	 * 表格名称
	 */
	private String title;
	/**
	 * 第二行名称
	 */
	private String secondTitle;
	/**
	 * sheetName
	 */
	private String sheetName;
	/**
	 * 表头颜色
	 */
	private short color = HSSFColor.WHITE.index;
	
	/**
	 * 属性说明行的颜色
	 * 例如:HSSFColor.SKY_BLUE.index  默认
	 */
	private short headerColor = HSSFColor.SKY_BLUE.index;
}
