/**===========================================
 *        Copyright (C) 2014 Tempus
 *           All rights reserved
 *
 *  项 目 名： jeecg-framework
 *  文 件 名： DictEntity.java
 *  版本信息： V1.0.0 
 *  作    者： Administrator
 *  日    期： 2014年5月11日-上午1:57:29
 * 
 ============================================*/

package com.abocode.jfaster.admin.system.dto.bean;
/**
 * Created by Franky on 2016/3/15.
 */
public class DictBean {
	private String typecode;
	private String typename;
	
	public String getTypeCode() {
		return typecode;
	}
	public void setTypeCode(String typecode) {
		this.typecode = typecode;
	}
	public String getTypeName() {
		return typename;
	}
	public void setTypeName(String typename) {
		this.typename = typename;
	}
}
