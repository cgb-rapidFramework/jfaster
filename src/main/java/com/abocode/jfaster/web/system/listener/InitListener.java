package com.abocode.jfaster.web.system.listener;

import com.abocode.jfaster.web.system.service.DynamicDataSourceService;
import com.abocode.jfaster.web.system.service.JobService;
import com.abocode.jfaster.web.system.service.MenuInitService;
import com.abocode.jfaster.web.system.service.SystemService;
import com.abocode.jfaster.web.utils.ConfigUtils;
import com.abocode.jfaster.web.system.service.MutiLangService;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;


/**
 * 系统初始化监听器,在系统启动时运行,进行一些初始化工作
 * @author laien
 *
 */
public class InitListener  implements javax.servlet.ServletContextListener {

	
	public void contextDestroyed(ServletContextEvent arg0) {
		
	}

	
	public void contextInitialized(ServletContextEvent event) {
		WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());
		SystemService systemService = (SystemService) webApplicationContext.getBean("systemService");
		MenuInitService menuInitService = (MenuInitService) webApplicationContext.getBean("menuInitService");
		MutiLangService mutiLangService = (MutiLangService) webApplicationContext.getBean("mutiLangService");
		DynamicDataSourceService dynamicDataSourceService = (DynamicDataSourceService) webApplicationContext.getBean("dynamicDataSourceService");
		JobService jobService =(JobService) webApplicationContext.getBean("jobService");
		/**
		 * 第一部分：对数据字典进行缓存
		 */
		systemService.initAllTypeGroups();
		//初始化图标
		systemService.initAllTSIcons();
		systemService.initOperations();
		
		/**
		 * 第二部分：自动加载新增菜单和菜单操作权限
		 * 说明：只会添加，不会删除（添加在代码层配置，但是在数据库层未配置的）
		 */
		if("true".equals(ConfigUtils.getConfigByName("auto.scan.menu.flag").toLowerCase())){
			menuInitService.initMenu();
		}
		
		/**
		 * 第三部分：加载多语言内容
		 */
		mutiLangService.initAllMutiLang();
		
		/**
		 * 第四部分：加载配置的数据源信息
		 */
		dynamicDataSourceService.initDynamicDataSource();

		/**
		 * 初始化任务调度
		 */
		try {
			jobService.initJob();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
