package com.abocode.jfaster.core.web.listener;

import com.abocode.jfaster.admin.system.service.InitService;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


/**
 * 系统初始化监听器,在系统启动时运行,进行一些初始化工作
 */
public class InitListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {

        WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());
        InitService initService = webApplicationContext.getBean(InitService.class);
        initService.contextInitialized();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }

}
