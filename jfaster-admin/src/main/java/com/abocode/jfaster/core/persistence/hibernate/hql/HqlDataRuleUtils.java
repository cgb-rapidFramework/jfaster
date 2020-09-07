package com.abocode.jfaster.core.persistence.hibernate.hql;

import com.abocode.jfaster.core.common.constants.Globals;
import com.abocode.jfaster.core.common.util.ContextHolderUtils;
import com.abocode.jfaster.core.persistence.hibernate.hql.vo.HqlDataRule;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Franky on 2016/3/15.
 */
public class HqlDataRuleUtils {
    private HqlDataRuleUtils() {
    }

    public static synchronized List<HqlDataRule> installDataSearchCondition(List<HqlDataRule> hqlDataRules) {
        // 1.先从request获取MENU_DATA_AUTHOR_RULES，如果存则获取到LIST
        List<HqlDataRule> list = loadDataSearchConditonSQL();
        if (list == null) { // 2.如果不存在，则new一个list
            list = new ArrayList<>();
        }
        if (!CollectionUtils.isEmpty(hqlDataRules)){
            list.addAll(hqlDataRules);
        }
        return list;
    }

    /**
     * 获取查询条件方法
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public static synchronized List<HqlDataRule> loadDataSearchConditonSQL() {
        return (List<HqlDataRule>) ContextHolderUtils.getRequest().getAttribute(
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
