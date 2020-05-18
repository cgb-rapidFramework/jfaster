package com.abocode.jfaster.admin.system.service.impl;

import com.abocode.jfaster.admin.system.repository.SystemRepository;
import com.abocode.jfaster.admin.system.repository.UserRepository;
import com.abocode.jfaster.admin.system.service.RoleService;
import com.abocode.jfaster.core.common.constants.Globals;
import com.abocode.jfaster.core.common.util.StringUtils;
import com.abocode.jfaster.system.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SystemRepository systemRepository;

    @Override
    public void del(Role role) {
        // 删除角色之前先删除角色权限关系
        delRoleFunction(role);
        systemRepository.executeSql("delete from t_s_role_org where role_id=?", role.getId()); // 删除 角色-机构 关系信息
        role = systemRepository.findEntity(Role.class, role.getId());
        userRepository.delete(role);
        String message = "角色: " + role.getRoleName() + "被删除成功";
        systemRepository.addLog(message, Globals.Log_Type_DEL,Globals.Log_Leavel_INFO);
    }

    @Override
    public void saveRole(Role role) {
        String message;
        if (StringUtils.isNotEmpty(role.getId())) {
            message = "角色: " + role.getRoleName() + "被更新成功";
            userRepository.saveOrUpdate(role);
            systemRepository.addLog(message, Globals.Log_Type_UPDATE,
                    Globals.Log_Leavel_INFO);
        } else {
            message = "角色: " + role.getRoleName() + "被添加成功";
            userRepository.save(role);
            systemRepository.addLog(message, Globals.Log_Type_INSERT,
                    Globals.Log_Leavel_INFO);
        }
    }

    @Override
    public void updateOrgRole(String orgId, List<String> roleIdList) {
        systemRepository.executeSql("delete from t_s_role_org where org_id=?",  orgId);
        if (!roleIdList.isEmpty()) {
            List<RoleOrg> roleOrgList = new ArrayList<RoleOrg>();
            Org depart = new Org();
            depart.setId(orgId);
            for (String roleId : roleIdList) {
                Role role = new Role();
                role.setId(roleId);

                RoleOrg roleOrg = new RoleOrg();
                roleOrg.setRole(role);
                roleOrg.setOrg(depart);
                roleOrgList.add(roleOrg);
            }
            systemRepository.batchSave(roleOrgList);
        }
    }

    /**
     * 删除角色权限
     *
     * @param role
     */
    private void delRoleFunction(Role role) {
        List<RoleFunction> roleFunctions = systemRepository.findAllByProperty(
                RoleFunction.class, "role.id", role.getId());
        if (roleFunctions.size() > 0) {
            for (RoleFunction tsRoleFunction : roleFunctions) {
                systemRepository.delete(tsRoleFunction);
            }
        }
        List<RoleUser> roleUsers = systemRepository.findAllByProperty(
                RoleUser.class, "role.id", role.getId());
        for (RoleUser tsRoleUser : roleUsers) {
            systemRepository.delete(tsRoleUser);
        }
    }
}
