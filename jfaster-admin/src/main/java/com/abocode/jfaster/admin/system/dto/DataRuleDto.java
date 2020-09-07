package com.abocode.jfaster.admin.system.dto;

import com.abocode.jfaster.core.persistence.hibernate.hql.vo.HqlDataRule;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DataRuleDto {
    private List<HqlDataRule> hqlDataRules;
    private  String ruleSql;
}
