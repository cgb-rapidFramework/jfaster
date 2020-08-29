package com.abocode.jfaster.system.entity;

import com.abocode.jfaster.core.AbstractIdEntity;
import lombok.Data;


import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "t_s_data_rule")
@Data
public class DataRule extends AbstractIdEntity implements Serializable {
    @Column(name = "rule_name", nullable = false, length = 32)
    private String ruleName;
    @Column(name = "rule_column", nullable = false, length = 100)
    private String ruleColumn;
    @Column(name = "rule_condition", nullable = false, length = 100)
    private String ruleCondition;
    @Column(name = "rule_value", nullable = false, length = 100)
    private String ruleValue;
    @Column(name = "create_by", nullable = false, length = 20)
    private String createBy;
    @Column(name = "create_by_id", nullable = false, length = 32)
    private String createById;
    @Column(name = "create_date", nullable = false)
    private Date createDate;
    @Column(name = "update_by", nullable = false, length = 20)
    private String updateBy;
    @Column(name = "update_by_id", nullable = false, length = 32)
    private String updateById;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "function_id")
    private Function function = new Function();
}
