package com.abocode.jfaster.core.persistence.datasource;


import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
/**
 *功能：动态数据源类
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
	protected Object determineCurrentLookupKey() {
		return DataSourceContextHolder.getDataSourceType();
	}
}
