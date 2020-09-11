package com.abocode.jfaster.core.persistence;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
@Data
public class DBTable<T> implements Serializable {
	public String tableName;
	public String entityName;
	public String tableTitle;
	public Class<T> tableEntityClass;
	public List<T> tableData;
}
