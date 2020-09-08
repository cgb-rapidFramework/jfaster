package com.abocode.jfaster.core.web.aop;

import java.lang.annotation.*;

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
