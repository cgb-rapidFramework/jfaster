package com.abocode.jfaster.admin.system.service.impl;

import com.abocode.jfaster.admin.system.repository.SystemRepository;
import com.abocode.jfaster.admin.system.repository.UserRepository;
import com.abocode.jfaster.admin.system.service.RuleService;
import com.abocode.jfaster.core.common.constants.Globals;
import com.abocode.jfaster.core.common.exception.BusinessException;
import com.abocode.jfaster.core.common.util.StrUtils;
import com.abocode.jfaster.core.platform.utils.LanguageUtils;
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
    public static final String COMMON_OPERATION = "common.operation";
    @Autowired
    private SystemRepository systemService;
    @Autowired
    private UserRepository userService;

    @Override
    public void del(DataRule operation) {
        operation = systemService
                .find(DataRule.class, operation.getId());
        String message = LanguageUtils.paramDelSuccess(COMMON_OPERATION);
        userService.delete(operation);
        systemService.addLog(message, Globals.LOG_TYPE_DEL,
                Globals.LOG_LEVEL);
    }

    @Override
    public void save(DataRule operation) {
        String message;
        if (StrUtils.isNotEmpty(operation.getId())) {
            message = LanguageUtils.paramUpdSuccess(COMMON_OPERATION);
            userService.saveOrUpdate(operation);
            systemService.addLog(message, Globals.LOG_TYPE_UPDATE,
                    Globals.LOG_LEVEL);
        } else {
            if (justHaveDataRule(operation) == 0) {
                message = LanguageUtils.paramAddSuccess(COMMON_OPERATION);
                userService.save(operation);
                systemService.addLog(message, Globals.LOG_TYPE_INSERT,
                        Globals.LOG_LEVEL);
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
