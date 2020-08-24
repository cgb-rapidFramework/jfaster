package com.abocode.jfaster.core.common.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClassLoaderUtils extends ClassLoader {
	public static Class getClassByScn(String className) {
		Class myclass = null;
		try {
			myclass = Class.forName(className);
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage());
		}
		return myclass;
	}
}
