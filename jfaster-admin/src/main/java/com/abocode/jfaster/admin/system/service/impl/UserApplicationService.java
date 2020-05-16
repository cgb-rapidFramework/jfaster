package com.abocode.jfaster.admin.system.service.impl;

import com.abocode.jfaster.core.common.constants.Globals;
import com.abocode.jfaster.admin.system.service.BeanToTagConverter;
import com.abocode.jfaster.core.common.util.FunctionComparator;
import com.abocode.jfaster.core.common.util.PasswordUtils;
import com.abocode.jfaster.core.common.util.SystemMenuUtils;
import com.abocode.jfaster.admin.system.service.UserService;
import com.abocode.jfaster.admin.system.repository.SystemRepository;
import com.abocode.jfaster.admin.system.dto.view.FunctionView;
import com.abocode.jfaster.system.entity.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class UserApplicationService implements UserService {
    @Resource
    private SystemRepository systemService;
    public String getMenus(User u) {
        FunctionComparator sort = new FunctionComparator();
        // 登陆者的权限
        Set<Function> loginActionlist = new HashSet();// 已有权限菜单
        List<RoleUser> rUsers = systemService.findAllByProperty(RoleUser.class, "user.id", u.getId());
        for (RoleUser ru : rUsers) {
            Role role = ru.getRole();
            List<RoleFunction> roleFunctionList = systemService.findAllByProperty(RoleFunction.class, "role.id", role.getId());
            if (roleFunctionList.size() > 0) {
                for (RoleFunction roleFunction : roleFunctionList) {
                    Function function = roleFunction.getFunction();
                    loginActionlist.add(function);
                }
            }
        }
        List<FunctionView> bigActionlist = new ArrayList();// 一级权限菜单
        List<FunctionView> smailActionlist = new ArrayList();// 二级权限菜单
        if (loginActionlist.size() > 0) {
            for (Function function : loginActionlist) {
                FunctionView functionBean = BeanToTagConverter.convertFunction(function);
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

    @Override
    public void restPassword(String id, String password) {
        User users = systemService.findEntity(User.class, id);
        users.setPassword(PasswordUtils.encrypt(users.getUsername(), password, PasswordUtils.getStaticSalt()));
        users.setStatus(Globals.User_Normal);
        systemService.update(users);
    }
}
