package com.abocode.jfaster.core.web.interceptors;

import java.util.Date;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
public class MyWebBinding implements WebBindingInitializer {

	@Override
	public void initBinder(WebDataBinder webDataBinder) {
		webDataBinder.registerCustomEditor(Date.class, new DateConvertEditor());
	}

}
