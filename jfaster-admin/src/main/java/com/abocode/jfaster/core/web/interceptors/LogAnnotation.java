package com.abocode.jfaster.core.web.interceptors;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LogAnnotation {
    String operateModelNm();

    String operateFuncNm();

    String operateDescribe();
}