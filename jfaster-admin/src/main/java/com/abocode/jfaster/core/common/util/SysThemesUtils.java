package com.abocode.jfaster.core.common.util;

import javax.servlet.http.HttpServletRequest;

import com.abocode.jfaster.core.common.container.SystemContainer;
import com.abocode.jfaster.core.common.exception.BusinessException;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;

import com.abocode.jfaster.admin.system.dto.view.TemplateView;

import java.io.*;

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


    /**
     * 添加图标样式
     * @param css
     */
    public static void write(String  path, String css) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter out = new FileWriter(file, true);
            out.write("\r\n");
            out.write(css);
            out.close();
        } catch (Exception e) {
            throw  new BusinessException("写主题失败",e);
        }
    }

    /**
     * 清空文件内容
     * @param path
     */
    public  static void clearFile(String path) {
        try {
            FileOutputStream fos = new FileOutputStream(new File(path));
            fos.write("".getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            LogUtils.error(e.getMessage());
        } catch (IOException e) {
            LogUtils.error(e.getMessage());
        }
    }
}
