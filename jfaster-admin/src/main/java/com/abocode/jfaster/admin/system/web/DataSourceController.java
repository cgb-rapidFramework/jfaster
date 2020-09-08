package com.abocode.jfaster.admin.system.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
/**
 * 数据库管理
 */
@Controller
@RequestMapping("/dataSourceController")
public class DataSourceController{
	@RequestMapping(params = "druid")
	public ModelAndView druid() {
		return new ModelAndView("/system/druid/index");
	}

}
