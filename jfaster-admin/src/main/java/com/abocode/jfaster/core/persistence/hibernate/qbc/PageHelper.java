package com.abocode.jfaster.core.persistence.hibernate.qbc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
/**
 *
 *分页查询结果封装类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageHelper {
	private int page;
	private int offset;
	//分页工具条
	private String toolBar;
	private int count;
	private List rows;//结果集
	/**
	 * 不使用分页标签的初始化构造方法
	 * @param rows
	 * @param toolBar
	 * @param offset
	 * @param page
	 * @param count
	 */
	public PageHelper(List rows, String toolBar, int offset, int page, int count) {
		this.page = page;
		this.offset = offset;
		this.toolBar = toolBar;
		this.rows = rows;
		this.count = count;
	}

	public PageHelper(List rows, int offset, int page, int count) {
		this.page = page;
		this.offset = offset;
		this.rows = rows;
		this.count = count;
	}
}
