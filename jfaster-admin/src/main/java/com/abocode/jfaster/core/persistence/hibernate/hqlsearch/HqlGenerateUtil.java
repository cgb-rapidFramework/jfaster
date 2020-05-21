package com.abocode.jfaster.core.persistence.hibernate.hqlsearch;

import com.abocode.jfaster.core.common.annotation.query.QueryTimeFormat;
import com.abocode.jfaster.core.common.util.DataRuleUtils;
import com.abocode.jfaster.core.common.util.LogUtils;
import com.abocode.jfaster.core.common.util.StringUtils;
import com.abocode.jfaster.core.persistence.hibernate.hqlsearch.vo.HqlRuleEnum;
import com.abocode.jfaster.core.persistence.hibernate.qbc.CriteriaQuery;
import com.abocode.jfaster.core.web.utils.SessionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;
import com.abocode.jfaster.system.entity.DataRule;
import org.springframework.util.NumberUtils;

import java.beans.PropertyDescriptor;
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
                                            Map<String, DataRule> ruleMap,
                                            Map<String, String[]> parameterMap, String alias) {
        PropertyDescriptor origDescriptors[] = PropertyUtils
                .getPropertyDescriptors(searchObj);
        String aliasName, name, type;
        for (int i = 0; i < origDescriptors.length; i++) {
            aliasName = (alias.equals("") ? "" : alias + ".")
                    + origDescriptors[i].getName();
            name = origDescriptors[i].getName();
            type = origDescriptors[i].getPropertyType().toString();
            try {
                if (judgedIsUselessField(name)
                        || !PropertyUtils.isReadable(searchObj, name)) {
                    continue;
                }
                // 如果规则包含这个属性
                if (ruleMap.containsKey(aliasName)) {
                    addRuleToCriteria(ruleMap.get(aliasName), aliasName,
                            origDescriptors[i].getPropertyType(), cq);
                }

                // 添加 判断是否有区间值
                String beginValue = null;
                String endValue = null;
                if (parameterMap != null
                        && parameterMap.containsKey(name + BEGIN)) {
                    beginValue = parameterMap.get(name + BEGIN)[0].trim();
                }
                if (parameterMap != null
                        && parameterMap.containsKey(name + END)) {
                    endValue = parameterMap.get(name + END)[0].trim();
                }

                Object value = PropertyUtils.getSimpleProperty(searchObj, name);
                // 根据类型分类处理
                if (type.contains("class java.lang")
                        || type.contains("class java.math")) {
                    // for：查询拼装的替换
                    if (value != null && !value.equals("")) {
                        HqlRuleEnum rule = PageValueConvertRuleEnum
                                .convert(value);
                        value = PageValueConvertRuleEnum.replaceValue(rule,
                                value);
                        ObjectParseUtil.addCriteria(cq, aliasName, rule, value);
                    } else if (parameterMap != null) {
                        ObjectParseUtil.addCriteria(cq, aliasName,
                                HqlRuleEnum.GE, beginValue);
                        ObjectParseUtil.addCriteria(cq, aliasName,
                                HqlRuleEnum.LE, endValue);
                    }
                    // for：查询拼装的替换
                } else if ("class java.util.Date".equals(type)) {
                    SimpleDateFormat time = new SimpleDateFormat(
                            "yyyy-MM-dd hh:mm:ss");
                    QueryTimeFormat format = origDescriptors[i].getReadMethod()
                            .getAnnotation(QueryTimeFormat.class);
                    SimpleDateFormat userDefined = null;
                    if (format != null) {
                        userDefined = new SimpleDateFormat(format.format());
                    }
                    if (!StringUtils.isEmpty(beginValue)) {
                        if (userDefined != null) {
                            cq.ge(aliasName, userDefined.parse(beginValue));
                        } else if (beginValue.length() == 19) {
                            cq.ge(aliasName, time.parse(beginValue));
                        } else if (beginValue.length() == 10) {
                            cq.ge(aliasName,
                                    time.parse(beginValue + " 00:00:00"));
                        }
                    }
                    if (!StringUtils.isEmpty(endValue)) {
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
                } else if (!StringUtils.isJDKClass(origDescriptors[i]
                        .getPropertyType())) {
                    Object param = PropertyUtils.getSimpleProperty(searchObj,
                            name);
                    if (isHaveRuleData(ruleMap, aliasName) || (isNotEmpty(param)
                            && itIsNotAllEmpty(param))) {
                        // 如果是实体类,创建别名,继续创建查询条件
                        // for：用户反馈
                        cq.createAlias(aliasName,
                                aliasName.replaceAll("\\.", "_"));
                        installHqlJoinAlias(cq, param, ruleMap, parameterMap,
                                aliasName);
                    }
                }
            } catch (Exception e) {
                LogUtils.error(e.getMessage());
            }
        }
    }

    /**
     * 判断数据规则是不是包含这个实体类
     *
     * @param ruleMap
     * @param aliasName
     * @return
     */
    private static boolean isHaveRuleData(Map<String, DataRule> ruleMap,
                                          String aliasName) {
        for (String key : ruleMap.keySet()) {
            if (key.contains(aliasName)) {
                return true;
            }
        }
        return false;
    }

    private static void addRuleToCriteria(DataRule tsDataRule,
                                          String aliasName, Class propertyType, CriteriaQuery cq) {
        HqlRuleEnum rule = HqlRuleEnum.getByValue(tsDataRule
                .getRuleCondition());
        if (rule.equals(HqlRuleEnum.IN)) {
            String[] values = tsDataRule.getRuleValue().split(",");
            Object[] objs = new Object[values.length];
            if (!propertyType.equals(String.class)) {
                for (int i = 0; i < values.length; i++) {
                    objs[i] = NumberUtils
                            .parseNumber(values[i], propertyType);
                }
            } else {
                objs = values;
            }
            ObjectParseUtil.addCriteria(cq, aliasName, rule, objs);
        } else {
            if (propertyType.equals(String.class)) {
                ObjectParseUtil.addCriteria(cq, aliasName, rule, converRuleValue(tsDataRule.getRuleValue()));
            } else {
                ObjectParseUtil.addCriteria(cq, aliasName, rule, NumberUtils
                        .parseNumber(tsDataRule.getRuleValue(), propertyType));
            }
        }
    }

    /***
     * SessionUtils 不应该使用SessionUtils
     * @param ruleValue
     * @return
     *    @Deprecated
     */
    @Deprecated
    private static String converRuleValue(String ruleValue) {
        log.warn("调用过时的方法,值为：", ruleValue);
        String value = SessionUtils.getUserSystemData(ruleValue);
        return value != null ? value : ruleValue;
    }

    private static boolean judgedIsUselessField(String name) {
        return "class".equals(name) || "ids".equals(name)
                || "page".equals(name) || "rows".equals(name)
                || "sort".equals(name) || "order".equals(name);
    }

    /**
     * 判断是不是空
     */
    public static boolean isNotEmpty(Object value) {
        return value != null && !"".equals(value);
    }

    /**
     * 判断这个类是不是所以属性都为空
     *
     * @param param
     * @return
     */
    private static boolean itIsNotAllEmpty(Object param) {
        boolean isNotEmpty = false;
        try {
            PropertyDescriptor origDescriptors[] = PropertyUtils.getPropertyDescriptors(param);
            String name;
            for (int i = 0; i < origDescriptors.length; i++) {
                name = origDescriptors[i].getName();
                if ("class".equals(name)
                        || !PropertyUtils.isReadable(param, name)) {
                    continue;
                }
                if (Map.class.isAssignableFrom(origDescriptors[i]
                        .getPropertyType())) {
                    Map<?, ?> map = (Map<?, ?>) PropertyUtils
                            .getSimpleProperty(param, name);
                    if (map != null && map.size() > 0) {
                        isNotEmpty = true;
                        break;
                    }
                } else if (Collection.class.isAssignableFrom(origDescriptors[i]
                        .getPropertyType())) {
                    Collection<?> c = (Collection<?>) PropertyUtils
                            .getSimpleProperty(param, name);
                    if (c != null && c.size() > 0) {
                        isNotEmpty = true;
                        break;
                    }
                } else if (!StringUtils.isEmpty(PropertyUtils
                        .getSimpleProperty(param, name))) {
                    isNotEmpty = true;
                    break;
                }
            }
        } catch (Exception e) {
            LogUtils.error(e.getMessage());
        }
        return isNotEmpty;
    }

    private static Map<String, DataRule> getRuleMap() {
        Map<String, DataRule> ruleMap = new HashMap<String, DataRule>();
        List<DataRule> list = DataRuleUtils.loadDataSearchConditonSQL(); //(List<TSDataRule>) ContextHolderUtils
        //	.getRequest().getAttribute(Globals.MENU_DATA_AUTHOR_RULES);
        if (list != null) {
            for (DataRule rule : list) {
                ruleMap.put(rule.getRuleColumn(), rule);
            }
        }
        return ruleMap;
    }

}
