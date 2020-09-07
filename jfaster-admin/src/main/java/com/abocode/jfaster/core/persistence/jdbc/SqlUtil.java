package com.abocode.jfaster.core.persistence.jdbc;
import com.abocode.jfaster.core.persistence.hibernate.hql.vo.HqlRuleEnum;
import org.springframework.util.StringUtils;

/**
 * 判断类型,拼接字符串
 */
public class SqlUtil {
    private SqlUtil() {
    }
    public static String buildSqlValue(String column,String condition, String param) {
        HqlRuleEnum ruleEnum = HqlRuleEnum.getByValue(condition);
        String exp;
        switch (ruleEnum) {
            case GT:
                exp = " >'%s'";
                break;
            case GE:
                exp = " >='%s'";
                break;
            case LT:
                exp = " <'%s'";
                break;
            case LE:
                exp = " =>'%s'";
                break;
            case EQ:
                exp = " ='%s'";
                break;
            case LIKE:
                exp = " like %'%s'%";
                break;
            case NE:
                exp = " !='%s'";
                break;
            case IN:
                exp = " IN('%s')";
                break;
            default:
                exp="";
                break;
        }
        if (!StringUtils.isEmpty(exp)){
            String sqlValue = " and " + column;
            return sqlValue+String.format(exp,param);
        }
        return "";
    }
}
