package com.abocode.jfaster.core.web.exception;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

/**
 * spring mvc异常捕获类
 * 
 */
@Component
@Slf4j
public class MyExceptionHandler implements HandlerExceptionResolver {

	@Override
	public ModelAndView resolveException(HttpServletRequest request,
										 HttpServletResponse response, Object handler, Exception ex) {
		log.error("服务器异常，异常信息为：",ex);
		Map<String, Object> model = new HashMap<>();
		model.put("exceptionMessage", "服务器异常");
		model.put("ex", ex);
		return new ModelAndView("common/error", model);
	}
}
