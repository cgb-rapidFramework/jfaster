package com.abocode.jfaster.core.persistence.hibernate.hql.vo;

import lombok.Data;

@Data
public class HqlDataRule {
    private String ruleColumn;
    private String ruleCondition;
    private String ruleValue;
}
