package com.abocode.jfaster.admin.system.dto;

import com.abocode.jfaster.system.entity.DataRule;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DataRuleDto {
    private List<DataRule>  dataRules;
    private  String ruleSql;
}
