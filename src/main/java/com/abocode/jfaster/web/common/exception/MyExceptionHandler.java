package com.abocode.jfaster.web.common.exception;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.abocode.jfaster.core.common.util.ExceptionUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

/**
 * spring mvc异常捕获类
 * 
 */
@Component
public class MyExceptionHandler implements HandlerExceptionResolver {


	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		String exceptionMessage = ExceptionUtils.getExceptionMessage(ex);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("exceptionMessage", exceptionMessage);
		model.put("ex", ex);
		return new ModelAndView("common/error", model);
	}
}
