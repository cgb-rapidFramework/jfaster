package com.abocode.jfaster.core.web.aop;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***
 * Ehcache
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.METHOD })
@Documented
public @interface Ehcache {
	//临时缓存还是永久缓存，默认为永久缓存
	boolean eternal() default true;
}
