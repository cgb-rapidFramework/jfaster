package com.abocode.jfaster.web.system.service;

import java.util.List;

import com.abocode.jfaster.core.common.service.CommonService;
import com.abocode.jfaster.web.system.entity.DynamicDataSourceEntity;

public interface DynamicDataSourceService extends CommonService{
	
	public List<DynamicDataSourceEntity> initDynamicDataSource();
	
	public void refleshCache();
}
