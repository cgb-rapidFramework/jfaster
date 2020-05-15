package com.abocode.jfaster.core.common.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
@Retention(RetentionPolicy.RUNTIME)
public @interface EntityTitle {
	  String name();
}
