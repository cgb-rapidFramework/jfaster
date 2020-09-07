package com.abocode.jfaster.core.persistence.hibernate.hql.vo;

/**
 * HQL 规则 常量
 * Created by jue on 14-8-23.
 */
public enum HqlRuleEnum {

    GT(">","大于"),
    GE(">=","大于等于"),
    LT("<","小于"),
    LE("<=","小于等于"),
    EQ("=","等于"),
    NE("!=","不等于"),
    IN("IN","包含"),
    LIKE("LIKE","左右模糊"),
    LEFT_LIKE("LEFT_LIKE","左模糊"),
    RIGHT_LIKE("RIGHT_LIKE","右模糊");

    private String value;

    private String msg;

    HqlRuleEnum(String value, String msg){
        this.value = value;
        this.msg = msg;
    }

    public String getValue() {
        return value;
    }

    public String getMsg() {
        return msg;
    }


    public static HqlRuleEnum getByValue(String value){
        for(HqlRuleEnum val :values()){
            if (val.getValue().equals(value)){
                return val;
            }
        }
        return  null;
    }

    public static HqlRuleEnum convert(Object value) {
        // 避免空数据
        if (value == null) {
            return null;
        }
        String val = value.toString().trim();
        if (val.length() == 0) {
            return null;
        }
        return getRule(val);
    }

    private static HqlRuleEnum getRule(String val) {
        // step 1 .> <
        HqlRuleEnum rule = HqlRuleEnum.getByValue(val.substring(0, 1));
        // step 2 .>= =<
        if (rule == null && val.length() >= 2) {
            rule = HqlRuleEnum.getByValue(val.substring(0, 2));
        }
        // step 3 like
        if (rule == null && val.contains(HqlParseEnum.SUFFIX_ASTERISK.getValue())) {
            if (val.startsWith(HqlParseEnum.SUFFIX_ASTERISK.getValue()) && val.endsWith(HqlParseEnum.SUFFIX_ASTERISK.getValue())) {
                rule = HqlRuleEnum.LIKE;
            } else if (val.startsWith(HqlParseEnum.SUFFIX_ASTERISK.getValue())) {
                rule = HqlRuleEnum.LEFT_LIKE;
            } else {
                rule = HqlRuleEnum.RIGHT_LIKE;
            }
        }
        // step 4 in
        if (rule == null && val.contains(HqlParseEnum.SUFFIX_COMMA.getValue())) {
            rule = HqlRuleEnum.IN;
        }
        // step 5 !=
        if (rule == null && val.startsWith(HqlParseEnum.SUFFIX_NOT_EQUAL.getValue())) {
            rule = HqlRuleEnum.NE;
        }
        return rule != null ? rule : HqlRuleEnum.EQ;
    }

    /**
     * 替换掉关键字字符
     *
     * @param rule
     * @param value
     * @return
     */
    public static Object replaceValue(HqlRuleEnum rule, Object value) {
        if (rule == null) {
            return null;
        }
        if (!(value instanceof String)) {
            return value;
        }
        String val = (value + "").trim();
        if (rule == HqlRuleEnum.LIKE) {
            value = val.substring(1, val.length() - 1);
        } else if (rule == HqlRuleEnum.LEFT_LIKE || rule == HqlRuleEnum.NE) {
            value = val.substring(1);
        } else if (rule == HqlRuleEnum.RIGHT_LIKE) {
            value = val.substring(0, val.length() - 1);
        } else if (rule != HqlRuleEnum.IN) {
            value = val.replace(rule.getValue(),
                    HqlParseEnum.SUFFIX_NULL_STRING.getValue());
        } else {
            value = val.split(",");
        }
        return value;
    }
}
