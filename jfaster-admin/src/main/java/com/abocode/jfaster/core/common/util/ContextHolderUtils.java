package com.abocode.jfaster.core.common.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
public class ContextHolderUtils {
	private ContextHolderUtils() {
	}

	/**
	 * SpringMvc下获取request
	 * 
	 * @return
	 */
	public static HttpServletRequest getRequest() {
 		return  ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

	}
	/**
	 * SpringMvc下获取session
	 * 
	 * @return
	 */
	public static HttpSession getSession() {
		return  getRequest().getSession();

	}

}
