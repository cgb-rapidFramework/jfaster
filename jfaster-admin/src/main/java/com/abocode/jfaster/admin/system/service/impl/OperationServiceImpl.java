package com.abocode.jfaster.admin.system.service.impl;

import com.abocode.jfaster.admin.system.repository.SystemRepository;
import com.abocode.jfaster.admin.system.repository.UserRepository;
import com.abocode.jfaster.admin.system.service.OperationService;
import com.abocode.jfaster.core.common.constants.Globals;
import com.abocode.jfaster.core.platform.utils.MutiLangUtils;
import com.abocode.jfaster.core.common.util.StrUtils;
import com.abocode.jfaster.system.entity.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Description:
 *
 * @author: guanxf
 * @date: 2020/5/18
 */
@Service
public class OperationServiceImpl implements OperationService {
    @Autowired
    private UserRepository userService;
    @Autowired
    private SystemRepository systemService;
    @Override
    public void save(Operation operation) {
        String message;
        if (StrUtils.isNotEmpty(operation.getId())) {
            message = MutiLangUtils.paramUpdSuccess("common.operation");
            userService.saveOrUpdate(operation);
            systemService.addLog(message, Globals.Log_Type_UPDATE,
                    Globals.Log_Leavel_INFO);
        } else {
            message = MutiLangUtils.paramAddSuccess("common.operation");
            userService.save(operation);
            systemService.addLog(message, Globals.Log_Type_INSERT,
                    Globals.Log_Leavel_INFO);
        }

    }
}
