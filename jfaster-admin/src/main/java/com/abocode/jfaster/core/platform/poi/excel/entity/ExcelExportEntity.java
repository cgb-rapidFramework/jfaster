package com.abocode.jfaster.core.platform.poi.excel.entity;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Method;
import java.util.List;
@Getter
@Setter
public class ExcelExportEntity  {
	
	private int width;
	private int height;
	/**
	 * 对应exportName
	 */
	private String name;
	/**
	 * 对应exportType
	 */
	private int type;
	/**
	 * 图片的类型,1是文件,2是数据库
	 */
	private int  exportImageType;
	/**
	 * 排序顺序
	 */
	private int orderNum;
	/**
	 * 是否支持换行
	 */
	private boolean isWrap;
	/**
	 * 是否需要合并
	 */
	private boolean  needMerge;
	
	/**
	 * 数据库格式
	 */
	private String databaseFormat;
	/**
	 * 导出日期格式
	 */
	private String exportFormat;
	/**
	 * cell 函数
	 */
	private String cellFormula;
	/**
	 * get 和convert 合并
	 */
	private Method getMethod;
	
	private List<Method> getMethods;
	
	private List<ExcelExportEntity> rows;
}
