package org.jeecgframework.web.system.service.impl;

import org.apache.commons.lang.StringUtils;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.web.system.entity.base.TSCategoryEntity;
import org.jeecgframework.web.system.service.CategoryServiceI;
import org.jeecgframework.web.utils.SystemConfigUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("tSCategoryService")
@Transactional
public class CategoryServiceImpl extends CommonServiceImpl implements
		CategoryServiceI {

	private static final String MAX_SQL = "SELECT MAX(code) FROM t_s_category WHERE parent_id";

	public void saveCategory(TSCategoryEntity category) {
		category.setCode(getCategoryCoade(category));
		this.save(category);
	}

	/**
	 * 获取类型编码 加锁防止并发问题
	 * 
	 * @param category
	 * @return
	 */
	private synchronized String getCategoryCoade(TSCategoryEntity category) {
		Long maxCode = null;
		//step 1 顶级code只按照序列增长
		if (category.getParent() == null
				|| StringUtils.isEmpty(category.getParent().getId())) {
			category.setParent(null);
			maxCode = this.queryCount(MAX_SQL + " IS NULL");
			maxCode = maxCode == 0 ? 1 : maxCode + 1;
			return String.format(
					"%0"
							+ Integer.valueOf(SystemConfigUtil
									.getConfigByName("categoryCodeLengthType"))
							+ "d", maxCode);
		}
		//step 2按照下级序列向上排序
		TSCategoryEntity parent = this.find(TSCategoryEntity.class, category
				.getParent().getId());
		//防止hibernate缓存持久化异常
		category.setParent(parent);
		maxCode = this.queryCount(MAX_SQL + " = '"
				+ category.getParent().getId()+"'");
		maxCode = maxCode == 0 ? 1 : Long.valueOf(maxCode.toString()
				.substring(parent.getCode().length())) + 1;
		return parent.getCode()
				+ String.format(
						"%0"
								+ Integer.valueOf(SystemConfigUtil
										.getConfigByName("categoryCodeLengthType"))
								+ "d", maxCode);
	}
}