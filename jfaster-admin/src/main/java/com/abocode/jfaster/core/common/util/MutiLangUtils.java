package com.abocode.jfaster.core.common.util;

import com.abocode.jfaster.admin.system.dto.view.ReflectHelper;
import com.abocode.jfaster.core.common.container.MutilangContainer;
import com.abocode.jfaster.core.persistence.hibernate.qbc.CriteriaQuery;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 字符串处理及转换工具类
 *
 * @author 张代浩
 */
public class MutiLangUtils {
    public final static String DEFUALT_LANG = "zh-cn";

    private static Log logger = LogFactory.getLog(MutiLangUtils.class);


    /**
     * 组装查询条件：对多语言字段进行 查询条件的组装
     *
     * @param fieldLangKeyList 待查询字段对应的语言key列表
     * @param cq               查询条件实体 - CriteriaQuery
     * @param fieldName        待查询字段名称
     * @param fieldValue       待查询字段值 - 页面传入
     */
    public static void assembleCondition(List<String> fieldLangKeyList, CriteriaQuery cq, String fieldName, String fieldValue) {
        Map<String, String> fieldLangMap = new HashMap<String, String>();
        for (String nameKey : fieldLangKeyList) {
            String name = MutiLangUtils.getLang(nameKey);
            fieldLangMap.put(nameKey, name);
        }

        if ("*".equals(fieldValue)) {
            fieldValue = "**";
        }
        List<String> paramValueList = new ArrayList<String>();
        for (Map.Entry<String, String> entry : fieldLangMap.entrySet()) {
            String fieldLangKey = entry.getKey();
            String fieldLangValue = entry.getValue();
            if (fieldValue.startsWith("*") && fieldValue.endsWith("*")) {
                if (fieldLangValue.contains(fieldValue.substring(1, fieldValue.length() - 1))) {
                    paramValueList.add(fieldLangKey);
                }
            } else if (fieldValue.startsWith("*")) {
                if (fieldLangValue.endsWith(fieldValue.substring(1))) {
                    paramValueList.add(fieldLangKey);
                }
            } else if (fieldValue.endsWith("*")) {
                if (fieldLangValue.startsWith(fieldValue.substring(0, fieldValue.length() - 1))) {
                    paramValueList.add(fieldLangKey);
                }
            } else {
                if (fieldLangValue.equals(fieldValue)) {
                    paramValueList.add(fieldLangKey);
                }
            }
        }

        if (paramValueList.size() == 0) {
            paramValueList.add("~!@#$%_()*&^"); // 设置一个错误的key值。
        }
        cq.in(fieldName, paramValueList.toArray());
        cq.add();
    }


    /**
     * 通用删除消息方法
     *
     * @param param_lang_key 如：common.delete.success.param
     * @return XXX删除成功，如多语言删除成功
     */
    public static String paramDelSuccess(String param_lang_key) {
        String message = getLang("common.delete.success.param", param_lang_key);
        return message;
    }

    // add-begin--Author:zhangguoming  Date:20140727 for：通用删除消息方法

    /**
     * 通用删除消息方法
     *
     * @param param_lang_key 如：common.delete.fail.param
     * @return XXX删除失败，如系统图标失败，正在使用的图标，不允许删除。
     */
    public static String paramDelFail(String param_lang_key) {
        String message = getLang("common.delete.fail.param", param_lang_key);
        return message;
    }
    // add-end--Author:zhangguoming  Date:20140727 for：通用删除消息方法

    /**
     * 通用更新成功消息方法
     *
     * @param param_lang_key 如：common.edit.success.param
     * @return XXX更新成功，如多语言删除成功
     */
    public static String paramUpdSuccess(String param_lang_key) {
        String message = getLang("common.edit.success.param", param_lang_key);
        return message;
    }

    /**
     * 通用更新失败消息方法
     *
     * @param param_lang_key 如：common.edit.success.param
     * @return XXX更新失败，如多语言更新失败
     */
    public static String paramUpdFail(String param_lang_key) {
        String message = getLang("common.edit.fail.param", param_lang_key);
        return message;
    }

    /**
     * 通用添加消息方法
     *
     * @param param_lang_key 如：common.edit.success.param
     * @return XXX录入成功，如多语言录入成功
     */
    public static String paramAddSuccess(String param_lang_key) {
        String message = getLang("common.add.success.param", param_lang_key);
        return message;
    }

    /**
     * 通用国际化tree方法
     *
     * @param treeList, mutiLangService
     */
    public static void setMutiTree(List<?> treeList) {
        if (StringUtils.isEmpty(treeList)) return;

        for (Object treeItem : treeList) {
            ReflectHelper reflectHelper = new ReflectHelper(treeItem);
            String lang_key = (String) reflectHelper.getMethodValue("text"); //treeItem.getText();
            String lang_context = getLang(lang_key);
            reflectHelper.setMethodValue("text", lang_context);
        }
    }


    /**
     * 通用得到MutiLangService方法
     *
     * @return mutiLangService实例
     */


    public static String doMutiLang(String title, String langArg) {
        String context = getLang(title, langArg);
        return context;
    }


    // add-begin--Author:zhangguoming  Date:20140928 for：多语言

    /**
     * 处理列表中对象的多语言属性值，即为列表中实体对象的属性值替换为多语言所对应的值
     *
     * @param list       对象列表
     * @param attributes 多语言属性名列表
     */
    public static void setMutiLangValueForList(List<Object> list, String... attributes) {
        if (StringUtils.isEmpty(list)) {
            return;
        }
        if (StringUtils.isEmpty(attributes)) {
            return;
        }
        List<Object> newList = new ArrayList<Object>();
        for (Object obj : list) {
            // 如果直接操作列表中的原始对象，则会触发Hibernate的update操作，所以使用类似克隆的方式进行处理；
            Object cloneObj = null;
            try {
                cloneObj = Class.forName(obj.getClass().getName()).newInstance();
                BeanPropertyUtils.copyBean2Bean(cloneObj, obj);
            } catch (Exception e) {
                LogUtils.error(e.getMessage());
                continue;
            }
            ReflectHelper reflectHelper = new ReflectHelper(cloneObj);
            for (String attribute : attributes) {
                String lang_key = (String) reflectHelper.getMethodValue(attribute);
                String lang_context = getLang(lang_key);
                reflectHelper.setMethodValue(attribute, lang_context);
            }
            newList.add(cloneObj);
        }
        list.clear();
        list.addAll(newList);
    }

    /**
     * 获取语言
     *
     * @param title
     * @param langArg
     * @return
     */
    public static String getLang(String title, String langArg) {
        return MutilangContainer.getLang(title, langArg);
    }

    /****
     * 获取多语言
     *
     * @param lang_key
     * @return
     */
    public static String getLang(String lang_key) {
        return MutilangContainer.getLang(lang_key);
    }

    public static boolean existLangContext(String langContext) {
        return MutilangContainer.existLangContext(langContext);
    }
}
