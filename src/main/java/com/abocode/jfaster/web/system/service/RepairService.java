package com.abocode.jfaster.web.system.service;

import com.abocode.jfaster.core.common.service.CommonService;

/** 
 * @Description 修复数据库Service
 * @ClassName: RepairService
 * @author tanghan
 * @date 2013-7-19 下午01:31:00  
 */ 
public interface RepairService  extends CommonService{
	/**
	 * @Description  先清空数据库，然后再修复数据库
	 * @author tanghan 2013-7-19  
	 */
	  	
	void deleteAndRepair();

}
