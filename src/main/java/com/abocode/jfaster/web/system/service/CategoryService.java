package com.abocode.jfaster.web.system.service;

import com.abocode.jfaster.core.common.service.CommonService;
import com.abocode.jfaster.web.system.entity.TSCategoryEntity;

public interface CategoryService extends CommonService{
	/**
	 * 保存分类管理
	 * @param category
	 */
	void saveCategory(TSCategoryEntity category);
}
