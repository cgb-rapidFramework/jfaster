package com.abocode.jfaster.core.web.listener;

import com.abocode.jfaster.admin.system.repository.MenuInitRepository;
import com.abocode.jfaster.admin.system.repository.SystemRepository;
import com.abocode.jfaster.admin.system.service.SystemService;
import com.abocode.jfaster.core.common.util.ConfigUtils;
import com.abocode.jfaster.admin.system.repository.MutiLangRepository;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


/**
 * 系统初始化监听器,在系统启动时运行,进行一些初始化工作
 *
 * @author laien
 */
public class InitListener implements ServletContextListener {


    @Override
    public void contextDestroyed(ServletContextEvent arg0) {

    }


    @Override
    public void contextInitialized(ServletContextEvent event) {
        WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());
        SystemRepository systemService = webApplicationContext.getBean(SystemRepository.class);
        MenuInitRepository menuInitService = webApplicationContext.getBean(MenuInitRepository.class);
        MutiLangRepository mutiLangService = webApplicationContext.getBean(MutiLangRepository.class);
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
        if ("true".equals(ConfigUtils.getConfigByName("auto.scan.menu.flag").toLowerCase())) {
            menuInitService.initMenu();
        }

        /**
         * 第三部分：加载多语言内容
         */
        mutiLangService.initAllMutiLang();
    }

}
