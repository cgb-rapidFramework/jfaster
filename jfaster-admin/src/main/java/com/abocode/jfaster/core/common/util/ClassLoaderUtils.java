package com.abocode.jfaster.core.common.util;

import com.abocode.jfaster.core.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClassLoaderUtils extends ClassLoader {
	public static Class<?> getClassByScn(String className)  {
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw  new BusinessException("class gets exception",e);
		}
	}
}
