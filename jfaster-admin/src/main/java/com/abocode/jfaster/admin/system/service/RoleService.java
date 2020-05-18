package com.abocode.jfaster.admin.system.service;

import com.abocode.jfaster.system.entity.Role;

import java.util.List;

public interface RoleService{
    void del(Role role);

    void saveRole(Role role);

    void updateOrgRole(String orgId, List<String> roleIdList);

}
