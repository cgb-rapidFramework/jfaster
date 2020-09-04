package com.abocode.jfaster.core.persistence.hibernate.hqlsearch;

import com.abocode.jfaster.core.persistence.hibernate.qbc.CriteriaQuery;
import com.abocode.jfaster.core.persistence.hibernate.hqlsearch.vo.HqlParseEnum;
import com.abocode.jfaster.core.persistence.hibernate.hqlsearch.vo.HqlRuleEnum;
import com.abocode.jfaster.core.web.utils.SessionUtils;
import com.abocode.jfaster.system.entity.DataRule;

/**
 * 判断类型,拼接字符串
 */
public class ObjectParseUtil {

    public static String setSqlModel(DataRule dataRule) {
        if (dataRule == null)
            return "";

        HqlRuleEnum ruleEnum = HqlRuleEnum.getByValue(dataRule.getRuleCondition());
        String valueTemp ;

        //针对特殊标示处理#{sysOrgCode}，判断替换
        if (dataRule.getRuleValue().contains("{")) {
            valueTemp = dataRule.getRuleValue().substring(2, dataRule.getRuleValue().length() - 1);
        } else {
            valueTemp = dataRule.getRuleValue();
        }
        String TempValue = SessionUtils.getUserSystemData(valueTemp) == null ? valueTemp : SessionUtils.getUserSystemData(valueTemp);//将系统变量
        String sqlValue = "";
        switch (ruleEnum) {
            case GT:
                sqlValue += " and " + dataRule.getRuleColumn() + " >'" + TempValue + "'";
                break;
            case GE:
                sqlValue += " and " + dataRule.getRuleColumn() + " >='" + TempValue + "'";
                break;
            case LT:
                sqlValue += " and " + dataRule.getRuleColumn() + " <'" + TempValue + "'";
                break;
            case LE:
                sqlValue += " and " + dataRule.getRuleColumn() + " =>'" + TempValue + "'";
                break;
            case EQ:
                sqlValue += " and " + dataRule.getRuleColumn() + " ='" + TempValue + "'";
                break;
            case LIKE:
                sqlValue += " and " + dataRule.getRuleColumn() + " like %'" + TempValue + "'%";
                break;
            case NE:
                sqlValue += " and " + dataRule.getRuleColumn() + " !='" + TempValue + "'";
                break;
            case IN:
                sqlValue += " and " + dataRule.getRuleColumn() + " IN('" + TempValue + "')";
            default:
                break;
        }
        return sqlValue;
    }

    public static void addCriteria(CriteriaQuery cq, String name,
                                   HqlRuleEnum rule, Object value) {
        if (value == null || rule == null) {
            return;
        }
        switch (rule) {
            case GT:
                cq.gt(name, value);
                break;
            case GE:
                cq.ge(name, value);
                break;
            case LT:
                cq.lt(name, value);
                break;
            case LE:
                cq.le(name, value);
                break;
            case EQ:
                cq.eq(name, value);
                break;
            case NE:
                cq.notEq(name, value);
                break;
            case IN:
                cq.in(name, (Object[]) value);
                break;
            case LIKE:
                cq.like(name, HqlParseEnum.SUFFIX_ASTERISK_VAGUE.getValue() + value
                        + HqlParseEnum.SUFFIX_ASTERISK_VAGUE.getValue());
                break;
            case LEFT_LIKE:
                cq.like(name, HqlParseEnum.SUFFIX_ASTERISK_VAGUE.getValue() + value);
                break;
            case RIGHT_LIKE:
                cq.like(name, value + HqlParseEnum.SUFFIX_ASTERISK_VAGUE.getValue());
                break;
            default:
                break;
        }
    }

}
