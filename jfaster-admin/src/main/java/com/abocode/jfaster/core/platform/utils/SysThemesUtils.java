package com.abocode.jfaster.core.platform.utils;

import com.abocode.jfaster.core.common.util.FileUtils;
import com.abocode.jfaster.core.platform.SystemContainer;
import com.abocode.jfaster.core.platform.view.TemplateView;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.io.File;

/**
 * 系统样式获取工具类
 * @author Franky
 */
@Slf4j
public class SysThemesUtils {

    public static final String STYLESHEET_BASE_HREF = "<link rel=\"stylesheet\" href=\"template/";

    private SysThemesUtils() {
    }

    /**
     * 获取系统风格
     *
     * @return
     */
    public static TemplateView getSysTheme() {
        TemplateView currentTemplate = null;
        try {
            String json = SystemContainer.TemplateContainer.getTemplate();
            if (StringUtils.isNotEmpty(json)) {
                Gson gson = new Gson();
                currentTemplate = gson.fromJson(json, TemplateView.class);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
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
        StringBuilder sb = new StringBuilder();
        sb.append(STYLESHEET_BASE_HREF).append( templateBean.getTheme()).append("/css/easyui.css\" type=\"text/css\"></link>");
        return sb.toString();
    }

    /**
     * easyui main.css
     *
     * @param templateBean
     * @return
     */
    public static String getEasyUiMainTheme(TemplateView templateBean) {
        StringBuilder sb = new StringBuilder();
        sb.append("<link  rel=\"stylesheet\" href=\"template/" + templateBean.getTheme() + "/css/main.css\" type=\"text/css\"></link>");
        return sb.toString();
    }

    /**
     * easyui main.css
     *
     * @param templateBean
     * @return
     */
    public static String getEasyUiIconTheme(TemplateView templateBean) {
        StringBuilder sb = new StringBuilder();
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
        StringBuilder sb = new StringBuilder();
        sb.append( templateBean.getTheme()).append("/css/common.css\" type=\"text/css\"></link>");
        return sb.toString();
    }

    /**
     * lhgdialog 样式
     *
     * @param templateBean
     * @return
     */
    public static String getLhgdialogTheme(TemplateView templateBean) {
        StringBuilder sb = new StringBuilder();
        sb.append("<script type=\"text/javascript\" src=\"template/" + templateBean.getTheme() + "/js/lhgdialog.min.js?skin=" + templateBean.getTheme() + "\"></script>");
        return sb.toString();
    }

    /**
     * lhgdialog 样式
     *
     * @param templateBean
     * @return
     */
    public static String getTabTheme(TemplateView templateBean) {
        StringBuilder sb = new StringBuilder();
        sb.append("<script type=\"text/javascript\" src=\"template/" + templateBean.getTheme() + "/js/bootstrap-tab.js\"></script>");
        return sb.toString();
    }

    /**
     * Validform divfrom 样式
     *
     * @param templateBean
     * @return
     */
    public static String getValidformDivfromTheme(TemplateView templateBean) {
        StringBuilder sb = new StringBuilder();
        sb.append(templateBean.getTheme()).append("/css/divfrom.css\" type=\"text/css\"/>");
        return sb.toString();
    }

    /**
     * Validform style.css
     *
     * @param templateBean
     * @return
     */
    public static String getValidformStyleTheme(TemplateView templateBean) {
        StringBuilder sb = new StringBuilder();
        sb.append(templateBean.getTheme()).append("/css/style.css\" type=\"text/css\"/>");
        return sb.toString();
    }

    /**
     * Validform tablefrom.css
     *
     * @param templateBean
     * @return
     */
    public static String getValidformTablefrom(TemplateView templateBean) {
        StringBuilder sb = new StringBuilder();
        sb.append(templateBean.getTheme()).append("/css/tablefrom.css\" type=\"text/css\"/>");
        return sb.toString();
    }


    /**
     * 添加图标样式
     *
     * @param css
     */
    public static void write(String path, String css) {
        File file = new File(path);
        FileUtils.createFileIfNotExists(file);
        FileUtils.writeToFile(file,"\r\n".concat(css),true);
    }

    /**
     * 清空文件内容
     *
     * @param path
     */
    public static void clearFile(String path) {
        File file = new File(path);
        FileUtils.createFileIfNotExists(file);
        FileUtils.writeToFile(file,"",false);
    }
}
