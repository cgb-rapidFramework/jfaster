package com.abocode.jfaster.core.persistence.datasource;
import com.abocode.jfaster.core.common.util.LogUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class DataSourceMap {
	private static Map<Object,Object> dsm;
	
	static{
		InputStream is=Thread.currentThread().getContextClassLoader().getResourceAsStream("dataSourceMap.properties");
	    Properties p=new Properties();
	    try {
			p.load(is);
			dsm=new HashMap<Object,Object>();
			Set ps=p.entrySet();
			for (Iterator iterator = ps.iterator(); iterator.hasNext();) {
				Map.Entry<Object, Object> entry = (Map.Entry<Object, Object>) iterator.next();
				dsm.put(entry.getKey().toString(), entry.getValue()==null?"":entry.getValue().toString().trim());
			}
			is.close();//关闭
		} catch (IOException e) {
			LogUtils.error(e.getMessage());
		}
	}
	public static Map<Object,Object> getDataSourceMap() {
		Map<Object,Object> datadsm=new HashMap<Object,Object>();
		datadsm.putAll(dsm);
		return datadsm;
	}
	
}