package com.abocode.jfaster.core.common.annotation.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)   
@Target(ElementType.TYPE)
/**
 * 菜单注释标签
 */
public @interface AutoMenu {
	
	/**
	 * 菜单名称
	 * @return
	 */
	 String name();
	/**
	 * 等级
	 * @return
	 */
	 String level() default "0";
	/**
	 * 菜单地址
	 * @return
	 */
	 String url();
	
	/**
	 * 图标
	 * @return
	 */
	 String icon();
	/**
	 * 顺序
	 * @return
	 */
	 int order() default 0;
}


