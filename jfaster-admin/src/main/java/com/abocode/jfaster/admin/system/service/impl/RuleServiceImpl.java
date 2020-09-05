package com.abocode.jfaster.admin.system.service.impl;

import com.abocode.jfaster.admin.system.repository.SystemRepository;
import com.abocode.jfaster.admin.system.repository.UserRepository;
import com.abocode.jfaster.admin.system.service.RuleService;
import com.abocode.jfaster.core.common.constants.Globals;
import com.abocode.jfaster.core.common.exception.BusinessException;
import com.abocode.jfaster.core.platform.utils.MutiLangUtils;
import com.abocode.jfaster.core.common.util.StrUtils;
import com.abocode.jfaster.system.entity.DataRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description:
 *
 * @author: guanxf
 * @date: 2020/5/18
 */
@Service
public class RuleServiceImpl implements RuleService {
    @Autowired
    private SystemRepository systemService;
    @Autowired
    private UserRepository userService;

    @Override
    public void del(DataRule operation) {
        operation = systemService
                .findEntity(DataRule.class, operation.getId());
        String message = MutiLangUtils.paramDelSuccess("common.operation");
        userService.delete(operation);
        systemService.addLog(message, Globals.Log_Type_DEL,
                Globals.Log_Leavel_INFO);
    }

    @Override
    public void save(DataRule operation) {
        String message;
        if (StrUtils.isNotEmpty(operation.getId())) {
            message = MutiLangUtils.paramUpdSuccess("common.operation");
            userService.saveOrUpdate(operation);
            systemService.addLog(message, Globals.Log_Type_UPDATE,
                    Globals.Log_Leavel_INFO);
        } else {
            if (justHaveDataRule(operation) == 0) {
                message = MutiLangUtils.paramAddSuccess("common.operation");
                userService.save(operation);
                systemService.addLog(message, Globals.Log_Type_INSERT,
                        Globals.Log_Leavel_INFO);
            } else {
                throw new BusinessException("操作 字段规则已存在");
            }
        }
    }

    private int justHaveDataRule(DataRule dataRule) {
        String sql = "SELECT id FROM t_s_data_rule WHERE functionId='" + dataRule.getFunction()
                .getId() + "' AND rule_column='" + dataRule.getRuleColumn() + "' AND rule_conditions='" + dataRule
                .getRuleCondition() + "'";

        List<String> hasOperList = this.systemService.findListBySql(sql);
        return hasOperList.size();
    }
}
