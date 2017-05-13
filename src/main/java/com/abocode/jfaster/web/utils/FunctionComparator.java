package com.abocode.jfaster.web.utils;

import com.abocode.jfaster.core.util.ConvertUtils;
import com.abocode.jfaster.web.system.entity.Function;

import java.util.Comparator;


/**
 * Created by Franky on 2016/3/15.
 */
public class FunctionComparator implements Comparator {
	/**
	 * 菜单排序比较器
	 */
	public int compare(Object o1, Object o2) {
		Function c1 = (Function) o1;
		Function c2 = (Function) o2;
		if (c1.getFunctionOrder() != null && c2.getFunctionOrder() != null) {
			int c1order = ConvertUtils.getInt(c1.getFunctionOrder().substring(c1.getFunctionOrder().indexOf("fun") + 3));
			int c2order = ConvertUtils.getInt(c2.getFunctionOrder().substring(c2.getFunctionOrder().indexOf("fun")) + 3);
			if (c1order > c2order) {
				return 1;
			} else {
				return -1;
			}
		} else {
			return 1;
		}

	}
}