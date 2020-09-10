package com.abocode.jfaster.api.system;

import lombok.Data;

/**
 * Description:
 *
 * @author: guanxf
 * @date: 2020/9/10
 */
@Data
public class DataRuleDTO {
    private String id;
    private String ruleName;
    private String ruleColumn;
    private String ruleCondition;
    private String ruleValue;
}
