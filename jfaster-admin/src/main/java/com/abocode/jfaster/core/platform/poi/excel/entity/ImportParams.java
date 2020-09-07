package com.abocode.jfaster.core.platform.poi.excel.entity;

import lombok.Data;

/**
 * 导入参数设置
 */
@Data
public class ImportParams {
	/**
	 * 表格标题行数,默认0
	 */
	private int titleRows = 0;
	/**
	 * 表格列标题行数,默认1
	 */
	private int secondTitleRows = 1;
	/**
	 * 字段真正值和列标题之间的距离 默认0
	 */
	private int startRows = 0;
	/**
	 * 主键设置,如何这个cell没有值,就跳过
	 * 或者认为这个是list的下面的值
	 */
	private int keyIndex = 0;
	/**
	 * 上传表格需要读取的sheet 数量,默认为1
	 */
	private int sheetNum = 1;
	/**
	 * 是否需要保存上传的Excel,默认为false
	 */
	private boolean needSave = false;
	/**
	 * 保存上传的Excel目录,默认是
	 * 如 TestEntity这个类保存路径就是
	 * upload/excelUpload/Test/yyyyMMddHHmss_*****
	 * 保存名称上传时间_五位随机数
	 */
	private String saveUrl = "upload/excelUpload";
}
