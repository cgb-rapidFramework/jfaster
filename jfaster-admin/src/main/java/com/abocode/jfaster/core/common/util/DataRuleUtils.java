package com.abocode.jfaster.core.common.util;

import com.abocode.jfaster.core.common.constants.Globals;
import com.abocode.jfaster.system.entity.DataRule;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Franky on 2016/3/15.
 */
public class DataRuleUtils {
    private DataRuleUtils() {
    }

    /**
     * 往链接请求里面，传入数据查询条件
     *
     * @param dataRules
     */
    public static synchronized List<DataRule> installDataSearchCondition(List<DataRule> dataRules) {
        List<DataRule> list = loadDataSearchConditonSQL();// 1.先从request获取MENU_DATA_AUTHOR_RULES，如果存则获取到LIST
        if (list == null) { // 2.如果不存在，则new一个list
            list = new ArrayList<>();
        }
        for (DataRule tsDataRule : dataRules) {
            list.add(tsDataRule);
        }
        return list;
    }

    /**
     * 获取查询条件方法
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public static synchronized List<DataRule> loadDataSearchConditonSQL() {
        return (List<DataRule>) ContextHolderUtils.getRequest().getAttribute(
                Globals.MENU_DATA_AUTHOR_RULES);
    }

    /**
     * 获取查询条件方法
     *
     * @return
     */
    public static synchronized String loadDataSearchConditonSQLString() {
        return (String) ContextHolderUtils.getRequest().getAttribute(
                Globals.MENU_DATA_AUTHOR_RULE_SQL);
    }

    /**
     * 往链接请求里面，传入数据查询条件
     * @param s
     */
    public static synchronized String installDataSearchCondition(String s) {
        String ruleSql = loadDataSearchConditonSQLString();
        if (!StringUtils.hasText(ruleSql)) {
            // 2.如果不存在，则new一个sql串
            ruleSql += s;
        }
         return ruleSql;

    }
}
