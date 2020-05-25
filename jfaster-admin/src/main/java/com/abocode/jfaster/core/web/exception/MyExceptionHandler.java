package com.abocode.jfaster.core.web.exception;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

/**
 * spring mvc异常捕获类
 * 
 */
@Component
public class MyExceptionHandler implements HandlerExceptionResolver {

	@Override
	public ModelAndView resolveException(HttpServletRequest request,
										 HttpServletResponse response, Object handler, Exception ex) {
		String exceptionMessage = getExceptionMessage(ex);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("exceptionMessage", exceptionMessage);
		model.put("ex", ex);
		return new ModelAndView("common/error", model);
	}

	public static String getExceptionMessage(Exception ex) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		ex.printStackTrace(pw);
		return sw.toString();
	}

}
