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
	/**
	 * 跳转到连接池监控页面
	 * @return
	 */
	@RequestMapping(params = "goDruid")
	public ModelAndView goDruid() {
		return new ModelAndView("/system/druid/index");
	}
		

}
