package com.abocode.jfaster.core.common.util;

import com.abocode.jfaster.core.common.constants.Globals;
import com.abocode.jfaster.system.entity.DataRule;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Franky on 2016/3/15.
 */
public class DataRuleUtils {

    /**
     * 往链接请求里面，传入数据查询条件
     *
     * @param MENU_DATA_AUTHOR_RULES
     */
    public static synchronized List<DataRule> installDataSearchConditon(List<DataRule> MENU_DATA_AUTHOR_RULES) {
        List<DataRule> list = loadDataSearchConditonSQL();// 1.先从request获取MENU_DATA_AUTHOR_RULES，如果存则获取到LIST
        if (list == null) { // 2.如果不存在，则new一个list
            list = new ArrayList<DataRule>();
        }
        for (DataRule tsDataRule : MENU_DATA_AUTHOR_RULES) {
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
     * @param MENU_DATA_AUTHOR_RULE_SQL
     */
    public static synchronized String  installDataSearchConditon(String MENU_DATA_AUTHOR_RULE_SQL) {
        // 1.先从request获取MENU_DATA_AUTHOR_RULE_SQL，如果存则获取到sql串
        String ruleSql = loadDataSearchConditonSQLString();
        if (!StringUtils.hasText(ruleSql)) {
            // 2.如果不存在，则new一个sql串
            ruleSql += MENU_DATA_AUTHOR_RULE_SQL;
        }
         return ruleSql;

    }
}
