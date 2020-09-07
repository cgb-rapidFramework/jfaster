package com.abocode.jfaster.core.persistence.hibernate.hql;

import com.abocode.jfaster.core.common.util.StrUtils;
import com.abocode.jfaster.core.persistence.hibernate.hql.vo.HqlDataRule;
import com.abocode.jfaster.core.persistence.hibernate.hql.vo.HqlParseEnum;
import com.abocode.jfaster.core.persistence.hibernate.hql.vo.HqlRuleEnum;
import com.abocode.jfaster.core.persistence.hibernate.qbc.CriteriaQuery;
import com.abocode.jfaster.core.web.manager.SessionHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.NumberUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author guanxf
 * @de
 */
@Slf4j
public class HqlGenerateUtil {
    private HqlGenerateUtil() {
    }

    /**
     * 时间查询符号
     */
    private static final String END = "_end";
    private static final String BEGIN = "_begin";


    /**
     * 自动生成查询条件HQL 模糊查询 不带有日期组合
     *
     * @param cq
     * @param searchObj
     * @throws Exception
     */
    public static void installHql(CriteriaQuery cq, Object searchObj) {
        installHqlJoinAlias(cq, searchObj, getRuleMap(), null, "");
        cq.add();
    }

    /**
     * 自动生成查询条件HQL（扩展区间查询功能）
     *
     * @param cq
     * @param searchObj
     * @param parameterMap request参数集合，封装了所有查询信息
     */
    public static void installHql(CriteriaQuery cq, Object searchObj,
                                  Map<String, String[]> parameterMap) {
        installHqlJoinAlias(cq, searchObj, getRuleMap(), parameterMap, "");
        cq.add();
    }

    /**
     * 添加Alias别名的查询
     *
     * @param cq
     * @param searchObj
     * @param parameterMap
     * @param alias
     * @date 2014年1月19日
     */
    private static void installHqlJoinAlias(CriteriaQuery cq, Object searchObj,
                                            Map<String, HqlDataRule> ruleMap,
                                            Map<String, String[]> parameterMap, String alias) {
        PropertyDescriptor[] origDescriptors = PropertyUtils
                .getPropertyDescriptors(searchObj);
        for (int i = 0; i < origDescriptors.length; i++) {
            String aliasName = (alias.equals("") ? "" : alias + ".") + origDescriptors[i].getName();
            String name = origDescriptors[i].getName();
            String type = origDescriptors[i].getPropertyType().toString();
            try {
                if (judgedIsUselessField(name) || !PropertyUtils.isReadable(searchObj, name)) {
                    continue;
                }
                // 如果规则包含这个属性
                if (ruleMap.containsKey(aliasName)) {
                    addRuleToCriteria(ruleMap.get(aliasName).getRuleCondition(),ruleMap.get(aliasName).getRuleValue(), aliasName, origDescriptors[i].getPropertyType(), cq);
                }
                // 添加 判断是否有区间值
                String beginValue = getBeginValue(parameterMap, name);
                String endValue = getEndValue(parameterMap, name);
                Object value = PropertyUtils.getSimpleProperty(searchObj, name);
                // 根据类型分类处理
                if (type.contains("class java.lang")
                        || type.contains("class java.math")) {
                    addCriteriaBasic(value, cq, aliasName, parameterMap, beginValue, endValue);
                } else if ("class java.util.Date".equals(type)) {
                    addCriteriaDate(value, cq, aliasName, origDescriptors[i], beginValue, endValue);
                } else if (!StrUtils.isJDKClass(origDescriptors[i].getPropertyType())) {
                    addCriteriaJdk(value, ruleMap, aliasName, cq, parameterMap);
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }


    /**
     * 判断是不是空
     */
    public static boolean isNotEmpty(Object value) {
        return value != null && !"".equals(value);
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

    
    private static String getEndValue(Map<String, String[]> parameterMap, String name) {
        if (parameterMap != null
                && parameterMap.containsKey(name + END)) {
            return parameterMap.get(name + END)[0].trim();
        }
        return null;
    }

    private static String getBeginValue(Map<String, String[]> parameterMap, String name) {
        if (parameterMap != null && parameterMap.containsKey(name + BEGIN)) {
            return parameterMap.get(name + BEGIN)[0].trim();
        }
        return null;
    }

    private static void addCriteriaJdk(Object value, Map<String,HqlDataRule> ruleMap, String aliasName, CriteriaQuery cq, Map<String, String[]> parameterMap) {
        if (isHaveRuleData(ruleMap, aliasName) || (isNotEmpty(value) && isNotAllEmpty(value))) {
            // 如果是实体类,创建别名,继续创建查询条件
            // for：用户反馈
            cq.createAlias(aliasName,
                    aliasName.replaceAll("\\.", "_"));
            installHqlJoinAlias(cq, value, ruleMap, parameterMap,
                    aliasName);
        }
    }

    private static void addCriteriaDate(Object value, CriteriaQuery cq, String aliasName, PropertyDescriptor origDescriptor, String beginValue, String endValue) throws ParseException {
        SimpleDateFormat time = new SimpleDateFormat(
                "yyyy-MM-dd hh:mm:ss");
        TimeFormat format = origDescriptor.getReadMethod().getAnnotation(TimeFormat.class);
        SimpleDateFormat userDefined = null;
        if (format != null) {
            userDefined = new SimpleDateFormat(format.format());
        }
        if (!StrUtils.isEmpty(beginValue)) {
            if (userDefined != null) {
                cq.ge(aliasName, userDefined.parse(beginValue));
            } else if (beginValue.length() == 19) {
                cq.ge(aliasName, time.parse(beginValue));
            } else if (beginValue.length() == 10) {
                cq.ge(aliasName,
                        time.parse(beginValue + " 00:00:00"));
            }
        }
        if (!StrUtils.isEmpty(endValue)) {
            if (userDefined != null) {
                cq.ge(aliasName, userDefined.parse(beginValue));
            } else if (endValue.length() == 19) {
                cq.le(aliasName, time.parse(endValue));
            } else if (endValue.length() == 10) {
                // 对于"yyyy-MM-dd"格式日期，因时间默认为0，故此添加" 23:23:59"并使用time解析，以方便查询日期时间数据
                cq.le(aliasName, time.parse(endValue + " 23:23:59"));
            }
        }
        if (isNotEmpty(value)) {
            cq.eq(aliasName, value);
        }
    }

    private static void addCriteriaBasic(Object value, CriteriaQuery cq, String aliasName, Map<String, String[]> parameterMap, String beginValue, String endValue) {

        // for：查询拼装的替换
        if (value != null && !value.equals("")) {
            HqlRuleEnum rule =HqlRuleEnum.convert(value);
            value =HqlRuleEnum.replaceValue(rule,
                    value);
            addCriteria(cq, aliasName, rule, value);
        } else if (parameterMap != null) {
            addCriteria(cq, aliasName,
                    HqlRuleEnum.GE, beginValue);
            addCriteria(cq, aliasName,
                    HqlRuleEnum.LE, endValue);
        }
        // for：查询拼装的替换
    }


    /**
     * 判断数据规则是不是包含这个实体类
     *
     * @param ruleMap
     * @param aliasName
     * @return
     */
    private static boolean isHaveRuleData(Map<String,HqlDataRule> ruleMap,
                                          String aliasName) {
        for (String key : ruleMap.keySet()) {
            if (key.contains(aliasName)) {
                return true;
            }
        }
        return false;
    }

    private static void addRuleToCriteria(String  condition,String value,
                                          String aliasName, Class propertyType, CriteriaQuery cq) {
        HqlRuleEnum rule = HqlRuleEnum.getByValue(condition);
        if (rule.equals(HqlRuleEnum.IN)) {
            String[] values = value.split(",");
            Object[] objs = new Object[values.length];
            if (!propertyType.equals(String.class)) {
                for (int i = 0; i < values.length; i++) {
                    objs[i] = NumberUtils.parseNumber(values[i], propertyType);
                }
            } else {
                objs = values;
            }
            addCriteria(cq, aliasName, rule, objs);
        } else {
            if (propertyType.equals(String.class)) {
                addCriteria(cq, aliasName, rule, convertRuleValue(value));
            } else {
                addCriteria(cq, aliasName, rule, NumberUtils.parseNumber(value, propertyType));
            }
        }
    }

    /***
     * SessionUtils 不应该使用SessionUtils
     * @param ruleValue
     * @return
     *    @Deprecated
     */
    private static String convertRuleValue(String ruleValue) {
        String value = SessionHolder.getUserSystemData(ruleValue);
        return value != null ? value : ruleValue;
    }

    private static boolean judgedIsUselessField(String name) {
        return "class".equals(name) || "ids".equals(name)
                || "page".equals(name) || "rows".equals(name)
                || "sort".equals(name) || "order".equals(name);
    }

    /**
     * 判断这个类是不是所以属性都为空
     *
     * @param param
     * @return
     */
    private static boolean isNotAllEmpty(Object param) {
        try {
            PropertyDescriptor[] origDescriptors = PropertyUtils.getPropertyDescriptors(param);
            for (int i = 0; i < origDescriptors.length; i++) {
                String name = origDescriptors[i].getName();
                if ("class".equals(name) || !PropertyUtils.isReadable(param, name)) {
                    continue;
                }
                boolean isNotEmpty = checkEmpty(origDescriptors[i], param, name);
                if (isNotEmpty) {
                    return true;
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

    private static boolean checkEmpty(PropertyDescriptor origDescriptor, Object param, String name) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        if (Map.class.isAssignableFrom(origDescriptor.getPropertyType())) {
            Map<?, ?> map = (Map<?, ?>) PropertyUtils.getSimpleProperty(param, name);
            if (map != null && map.size() > 0) {
                return true;
            }
        } else if (Collection.class.isAssignableFrom(origDescriptor.getPropertyType())) {
            Collection<?> properties = (Collection) PropertyUtils.getSimpleProperty(param, name);
            if (!CollectionUtils.isEmpty(properties)) {
                return true;
            }
        } else if (!StrUtils.isEmpty(PropertyUtils.getSimpleProperty(param, name))) {
            return true;
        }
        return false;
    }

    private static Map<String, HqlDataRule> getRuleMap() {
        Map<String, HqlDataRule> ruleMap = new HashMap<>();
        List<HqlDataRule> list = HqlDataRuleUtils.loadDataSearchConditonSQL();
        for (HqlDataRule rule : list) {
            ruleMap.put(rule.getRuleColumn(), rule);
        }
        return ruleMap;
    }

}
