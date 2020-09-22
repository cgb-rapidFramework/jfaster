package com.abocode.jfaster.admin.system.service.impl;

import com.abocode.jfaster.admin.system.repository.SystemRepository;
import com.abocode.jfaster.admin.system.repository.UserRepository;
import com.abocode.jfaster.admin.system.service.OperationService;
import com.abocode.jfaster.core.common.constants.Globals;
import com.abocode.jfaster.core.common.util.StrUtils;
import com.abocode.jfaster.core.platform.utils.LanguageUtils;
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
    public static final String COMMON_OPERATION = "common.operation";
    @Autowired
    private UserRepository userService;
    @Autowired
    private SystemRepository systemService;
    @Override
    public void save(Operation operation) {
        String message;
        if (StrUtils.isNotEmpty(operation.getId())) {
            message = LanguageUtils.paramUpdSuccess(COMMON_OPERATION);
            userService.saveOrUpdate(operation);
            systemService.addLog(message, Globals.LOG_TYPE_UPDATE,
                    Globals.LOG_LEVEL);
        } else {
            message = LanguageUtils.paramAddSuccess(COMMON_OPERATION);
            userService.save(operation);
            systemService.addLog(message, Globals.LOG_TYPE_INSERT,
                    Globals.LOG_LEVEL);
        }

    }
}
