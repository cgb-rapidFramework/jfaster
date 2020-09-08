package com.abocode.jfaster.core.web.listener;

import com.abocode.jfaster.core.web.manager.ClientManager;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;


/**
 * 监听在线用户上线下线
 */
public class OnlineListener implements ServletContextListener, HttpSessionListener {

    private static ApplicationContext ctx = null;

    public OnlineListener() {
    }


    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
    }


    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        ClientManager.getInstance().removeClinet(httpSessionEvent.getSession().getId());
    }

    /**
     * 服务器初始化
     */

    public void contextInitialized(ServletContextEvent evt) {
        setCtx(WebApplicationContextUtils.getWebApplicationContext(evt.getServletContext()));
    }

    public static ApplicationContext getCtx() {
        return ctx;
    }


    public void contextDestroyed(ServletContextEvent paramServletContextEvent) {

    }

    public static void setCtx(ApplicationContext ctx) {
        OnlineListener.ctx = ctx;
    }
}
