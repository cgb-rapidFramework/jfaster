package com.abocode.jfaster.platform.util;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;

import com.abocode.jfaster.platform.bean.TemplateBean;
import com.abocode.jfaster.platform.container.SystemContainer;

/**
 * 系统样式获取工具类
 *
 * @author Administrator
 */
public class SysThemesUtils {
    /**
     * 获取系统风格
     *
     * @param request
     * @return
     */
    public static TemplateBean getSysTheme(HttpServletRequest request) {
        TemplateBean currentTemplate = null;
        try {
            String json = SystemContainer.TemplateContainer.template.get("SYSTEM-TEMPLATE");
            if (StringUtils.isNotEmpty(json)) {
                Gson gson = new Gson();
                currentTemplate = gson.fromJson(json, TemplateBean.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (currentTemplate == null) {
            currentTemplate = getDefaultTemplate();
        }
        return currentTemplate;
    }

    public static TemplateBean getDefaultTemplate() {
        TemplateBean template = new TemplateBean();
        template.setName("经典风格");
        template.setPageMain("main/main");
        template.setStyle("main/main");
        template.setTheme("default");
        return template;
    }

    /**
     * easyui.css 样式
     *
     * @param templateBean
     * @return
     */
    public static String getEasyUiTheme(TemplateBean templateBean) {
        StringBuffer sb = new StringBuffer("");
        sb.append("<link rel=\"stylesheet\" href=\"plug-in/easyui/themes/" + templateBean.getTheme() + "/easyui.css\" type=\"text/css\"></link>");
        return sb.toString();
    }

    /**
     * easyui main.css
     *
     * @param templateBean
     * @return
     */
    public static String getEasyUiMainTheme(TemplateBean templateBean) {
        StringBuffer sb = new StringBuffer("");
        sb.append("<link  rel=\"stylesheet\" href=\"plug-in/easyui/themes/" + templateBean.getTheme() + "/main.css\" type=\"text/css\"></link>");
        return sb.toString();
    }

    /**
     * easyui main.css
     *
     * @param templateBean
     * @return
     */
//	@Deprecated
    public static String getEasyUiIconTheme(TemplateBean templateBean) {
        StringBuffer sb = new StringBuffer("");
        sb.append("<link  rel=\"stylesheet\" href=\"plug-in/easyui/themes/" + templateBean.getTheme() + "/icon.css\" type=\"text/css\"></link>");
        return sb.toString();
    }

    /**
     * tools common.css 样式
     *
     * @param templateBean
     * @return
     */
    public static String getCommonTheme(TemplateBean templateBean) {
        StringBuffer sb = new StringBuffer("");
        sb.append("<link rel=\"stylesheet\" href=\"plug-in/tools/css/" + templateBean.getTheme() + "/common.css\" type=\"text/css\"></link>");
        return sb.toString();
    }

    /**
     * lhgdialog 样式
     *
     * @param templateBean
     * @return
     */
    public static String getLhgdialogTheme(TemplateBean templateBean) {
        StringBuffer sb = new StringBuffer("");
        sb.append("<script type=\"text/javascript\" src=\"plug-in/lhgDialog/lhgdialog.min.js?skin=" + templateBean.getTheme() + "\"></script>");
        return sb.toString();
    }

    /**
     * lhgdialog 样式
     *
     * @param templateBean
     * @return
     */
    public static String getTabTheme(TemplateBean templateBean) {
        StringBuffer sb = new StringBuffer("");
        sb.append("<script type=\"text/javascript\" src=\"plug-in/" + templateBean.getTheme() + "/js/bootstrap-tab.js\"></script>");
        return sb.toString();
    }


    /**
     * graphreport report.css 样式
     *
     * @param templateBean
     * @return
     */
    @Deprecated
    public static String getReportTheme(TemplateBean templateBean) {
        StringBuffer sb = new StringBuffer("");
        sb.append("<link rel=\"stylesheet\" href=\"plug-in/graphreport/css/" + templateBean.getTheme() + "/report.css\" type=\"text/css\"></link>");
        return sb.toString();
    }

    /**
     * Validform divfrom 样式
     *
     * @param templateBean
     * @return
     */
    public static String getValidformDivfromTheme(TemplateBean templateBean) {
        StringBuffer sb = new StringBuffer("");
        sb.append("<link rel=\"stylesheet\" href=\"plug-in/Validform/css/" + templateBean.getTheme() + "/divfrom.css\" type=\"text/css\"/>");
        return sb.toString();
    }

    /**
     * Validform style.css
     *
     * @param templateBean
     * @return
     */
    public static String getValidformStyleTheme(TemplateBean templateBean) {
        StringBuffer sb = new StringBuffer("");
        sb.append("<link rel=\"stylesheet\" href=\"plug-in/Validform/css/" + templateBean.getTheme() + "/style.css\" type=\"text/css\"/>");
        return sb.toString();
    }

    /**
     * Validform tablefrom.css
     *
     * @param templateBean
     * @return
     */
    public static String getValidformTablefrom(TemplateBean templateBean) {
        StringBuffer sb = new StringBuffer("");
        sb.append("<link rel=\"stylesheet\" href=\"plug-in/Validform/css/" + templateBean.getTheme() + "/tablefrom.css\" type=\"text/css\"/>");
        return sb.toString();
    }
}
