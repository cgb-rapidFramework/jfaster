package com.abocode.jfaster.web.system.application;

import com.abocode.jfaster.core.common.util.BeanToTagUtils;
import com.abocode.jfaster.core.common.util.FunctionComparator;
import com.abocode.jfaster.core.common.util.SystemMenuUtils;
import com.abocode.jfaster.web.system.domain.entity.*;
import com.abocode.jfaster.web.system.domain.repository.SystemService;
import com.abocode.jfaster.web.system.interfaces.IUserService;
import com.abocode.jfaster.web.system.interfaces.view.FunctionView;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class UserApplicationService implements IUserService {
    @Resource
    private SystemService systemService;
    public String getMenus(User u) {
        FunctionComparator sort = new FunctionComparator();
        // 登陆者的权限
        Set<Function> loginActionlist = new HashSet();// 已有权限菜单
        List<RoleUser> rUsers = systemService.findAllByProperty(RoleUser.class, "TSUser.id", u.getId());
        for (RoleUser ru : rUsers) {
            Role role = ru.getTSRole();
            List<RoleFunction> roleFunctionList = systemService.findAllByProperty(RoleFunction.class, "TSRole.id", role.getId());
            if (roleFunctionList.size() > 0) {
                for (RoleFunction roleFunction : roleFunctionList) {
                    Function function = roleFunction.getTSFunction();
                    loginActionlist.add(function);
                }
            }
        }
        List<FunctionView> bigActionlist = new ArrayList();// 一级权限菜单
        List<FunctionView> smailActionlist = new ArrayList();// 二级权限菜单
        if (loginActionlist.size() > 0) {
            for (Function function : loginActionlist) {
                FunctionView functionBean = BeanToTagUtils.convertFunction(function);
                if (function.getFunctionLevel() == 0) {
                    bigActionlist.add(functionBean);
                } else if (function.getFunctionLevel() == 1) {
                    smailActionlist.add(functionBean);
                }
            }
        }
        // 菜单栏排序
        Collections.sort(bigActionlist, sort);
        Collections.sort(smailActionlist, sort);
        String logString = SystemMenuUtils.getMenu(bigActionlist, smailActionlist);
         return logString;
    }

    @Override
    public Object getAll() {
        return systemService.findAll(User.class);
    }
}
