package com.abocode.jfaster.admin.system.dto;

import lombok.Data;

/**
 * Created by Franky on 2016/3/15.
 */
@Data
public class DuplicateBean implements java.io.Serializable {
	private String tableName;
	private String fieldName;
	private String fieldValue;
	private String rowId;
}