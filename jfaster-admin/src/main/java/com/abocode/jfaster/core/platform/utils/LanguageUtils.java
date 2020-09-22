package com.abocode.jfaster.core.platform.utils;

import com.abocode.jfaster.core.persistence.hibernate.qbc.CriteriaQuery;
import com.abocode.jfaster.core.platform.LanguageContainer;
import com.abocode.jfaster.core.platform.view.ReflectHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

@Slf4j
public class LanguageUtils {
    public final static String ZH_CN = "zh-cn";

    private LanguageUtils() {
    }

    /**
     * 组装查询条件：对多语言字段进行 查询条件的组装
     *
     * @param fieldLangKeyList 待查询字段对应的语言key列表
     * @param cq               查询条件实体 - CriteriaQuery
     * @param fieldName        待查询字段名称
     * @param fieldValue       待查询字段值 - 页面传入
     */
    public static void assembleCondition(List<String> fieldLangKeyList, CriteriaQuery cq, String fieldName, String fieldValue) {
        List<String> paramValueList = buildParamValues(fieldValue, fieldLangKeyList);
        if (CollectionUtils.isEmpty(paramValueList)) {
            paramValueList.add("~!@#$%_()*&^"); // 设置一个错误的key值。
        }
        cq.in(fieldName, paramValueList.toArray());
        cq.add();
    }



    /**
     * 通用删除消息方法
     *
     * @param paramLangKey 如：common.delete.success.param
     * @return XXX删除成功，如多语言删除成功
     */
    public static String paramDelSuccess(String paramLangKey) {
        return getLang("common.delete.success.param", paramLangKey);
    }

    /**
     * 通用删除消息方法
     *
     * @param paramLangKey 如：common.delete.fail.param
     * @return XXX删除失败，如系统图标失败，正在使用的图标，不允许删除。
     */
    public static String paramDelFail(String paramLangKey) {
        return getLang("common.delete.fail.param", paramLangKey);
    }

    /**
     * 通用更新成功消息方法
     *
     * @param paramLangKey 如：common.edit.success.param
     * @return XXX更新成功，如多语言删除成功
     */
    public static String paramUpdSuccess(String paramLangKey) {
        return getLang("common.edit.success.param", paramLangKey);
    }

    /**
     * 通用添加消息方法
     *
     * @param paramLangKey 如：common.edit.success.param
     * @return XXX录入成功，如多语言录入成功
     */
    public static String paramAddSuccess(String paramLangKey) {
        return getLang("common.add.success.param", paramLangKey);
    }

    /**
     * 通用国际化tree方法
     *
     * @param treeList, mutiLangService
     */
    public static void setLanguageTree(List<?> treeList) {
        if (StringUtils.isEmpty(treeList)) return;

        for (Object treeItem : treeList) {
            ReflectHelper reflectHelper = new ReflectHelper(treeItem);
            Object langKey = reflectHelper.getMethodValue("text");
            if (langKey != null) {
                String langContext = getLang((String) langKey);
                reflectHelper.setMethodValue("text", langContext);
            }

        }
    }


    /**
     * 通用得到MutiLangService方法
     *
     * @return mutiLangService实例
     */


    public static String doLang(String title, String langArg) {
        return getLang(title, langArg);
    }

    /**
     * 获取语言
     *
     * @param title
     * @param langArg
     * @return
     */
    public static String getLang(String title, String langArg) {
        return LanguageContainer.getLang(title, langArg);
    }

    /****
     * 获取多语言
     *
     * @param langKey
     * @return
     */
    public static String getLang(String langKey) {
        return LanguageContainer.getLang(langKey);
    }

    public static boolean existLangContext(String langContext) {
        return LanguageContainer.existLangContext(langContext);
    }

    private static List<String> buildParamValues(String fieldValue, List<String> fieldLangKeyList) {
        if ("*".equals(fieldValue)) {
            fieldValue = "**";
        }
        List<String> paramValueList = new ArrayList<>();
        for (String fieldLangKey : fieldLangKeyList) {
            String fieldLangValue = LanguageUtils.getLang(fieldLangKey);
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
        return paramValueList;

    }

}
