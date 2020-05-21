package com.abocode.jfaster.admin.system.service.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)   
@Target(ElementType.METHOD)
/**
 * 菜单操作按钮注释标签
 */
public @interface AutoMenuOperation {
	
	/**
	 * 操作名称
	 * @return
	 */
	 String name();
	/**
	 * 操作码
	 * @return
	 */
	 String code();
	
	/**
	 * 操作码类型
	 * @return
	 */
	 MenuCodeType codeType() default MenuCodeType.TAG;
	
	/**
	 * 图标
	 * @return
	 */
	 String icon() default "";
	/**
	 * 状态
	 * @return
	 */
	 int status() default 0;
}

