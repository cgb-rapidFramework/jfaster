package com.abocode.jfaster.core.platform.poi.excel.entity;

import lombok.Data;

import java.util.Map;
@Data
public class ExcelCollectionParams {
	/**
	 * 集合对应的名称
	 */
	private String name;
	/**
	 * 实体对象
	 */
	private Class<?> type;
	/**
	 * 这个list下面的参数集合实体对象
	 */
	private Map<String, ExcelImportEntity> excelParams;
}
