package com.abocode.jfaster.platform.util;

import javax.servlet.http.HttpServletRequest;

import com.abocode.jfaster.core.util.LogUtils;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;

import com.abocode.jfaster.platform.view.TemplateView;
import com.abocode.jfaster.platform.container.SystemContainer;

/**
 * 系统样式获取工具类
 *
 * @author Franky
 */
public class SysThemesUtils {
    /**
     * 获取系统风格
     *
     * @param request
     * @return
     */
    public static TemplateView getSysTheme(HttpServletRequest request) {
        TemplateView currentTemplate = null;
        try {
            String json = SystemContainer.TemplateContainer.template.get("SYSTEM-TEMPLATE");
            if (StringUtils.isNotEmpty(json)) {
                Gson gson = new Gson();
                currentTemplate = gson.fromJson(json, TemplateView.class);
            }
        } catch (Exception e) {
            LogUtils.error(e.getMessage());
        }
        if (currentTemplate == null) {
            currentTemplate = getDefaultTemplate();
        }
        return currentTemplate;
    }

    public static TemplateView getDefaultTemplate() {
        TemplateView template = new TemplateView();
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
    public static String getEasyUiTheme(TemplateView templateBean) {
        StringBuffer sb = new StringBuffer("");
        sb.append("<link rel=\"stylesheet\" href=\"template/" + templateBean.getTheme() + "/css/easyui.css\" type=\"text/css\"></link>");
        return sb.toString();
    }

    /**
     * easyui main.css
     *
     * @param templateBean
     * @return
     */
    public static String getEasyUiMainTheme(TemplateView templateBean) {
        StringBuffer sb = new StringBuffer("");
        sb.append("<link  rel=\"stylesheet\" href=\"template/" + templateBean.getTheme() + "/css/main.css\" type=\"text/css\"></link>");
        return sb.toString();
    }

    /**
     * easyui main.css
     *
     * @param templateBean
     * @return
     */
//	@Deprecated
    public static String getEasyUiIconTheme(TemplateView templateBean) {
        StringBuffer sb = new StringBuffer("");
        sb.append("<link  rel=\"stylesheet\" href=\"template/" + templateBean.getTheme() + "/css/icon.css\" type=\"text/css\"></link>");
        return sb.toString();
    }

    /**
     * tools common.css 样式
     *
     * @param templateBean
     * @return
     */
    public static String getCommonTheme(TemplateView templateBean) {
        StringBuffer sb = new StringBuffer("");
        sb.append("<link rel=\"stylesheet\" href=\"template/" + templateBean.getTheme() + "/css/common.css\" type=\"text/css\"></link>");
        return sb.toString();
    }

    /**
     * lhgdialog 样式
     *
     * @param templateBean
     * @return
     */
    public static String getLhgdialogTheme(TemplateView templateBean) {
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
    public static String getTabTheme(TemplateView templateBean) {
        StringBuffer sb = new StringBuffer("");
        sb.append("<script type=\"text/javascript\" src=\"template/" + templateBean.getTheme() + "/js/bootstrap-tab.js\"></script>");
        return sb.toString();
    }


    /**
     * graphreport report.css 样式
     *
     * @param templateBean
     * @return
     */
    @Deprecated
    public static String getReportTheme(TemplateView templateBean) {
        StringBuffer sb = new StringBuffer("");
        sb.append("<link rel=\"stylesheet\" href=\"template/" + templateBean.getTheme() + "/css/report.css\" type=\"text/css\"></link>");
        return sb.toString();
    }

    /**
     * Validform divfrom 样式
     *
     * @param templateBean
     * @return
     */
    public static String getValidformDivfromTheme(TemplateView templateBean) {
        StringBuffer sb = new StringBuffer("");
        sb.append("<link rel=\"stylesheet\" href=\"template/" + templateBean.getTheme() + "/css/divfrom.css\" type=\"text/css\"/>");
        return sb.toString();
    }

    /**
     * Validform style.css
     *
     * @param templateBean
     * @return
     */
    public static String getValidformStyleTheme(TemplateView templateBean) {
        StringBuffer sb = new StringBuffer("");
        sb.append("<link rel=\"stylesheet\" href=\"template/" + templateBean.getTheme() + "/css/style.css\" type=\"text/css\"/>");
        return sb.toString();
    }

    /**
     * Validform tablefrom.css
     *
     * @param templateBean
     * @return
     */
    public static String getValidformTablefrom(TemplateView templateBean) {
        StringBuffer sb = new StringBuffer("");
        sb.append("<link rel=\"stylesheet\" href=\"template/" + templateBean.getTheme() + "/css/tablefrom.css\" type=\"text/css\"/>");
        return sb.toString();
    }
}
