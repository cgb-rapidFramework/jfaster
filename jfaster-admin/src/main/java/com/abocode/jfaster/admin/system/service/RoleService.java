package com.abocode.jfaster.admin.system.service;

import com.abocode.jfaster.core.common.model.json.ComboTree;
import com.abocode.jfaster.core.common.model.json.TreeGrid;
import com.abocode.jfaster.system.entity.Role;
import com.abocode.jfaster.system.entity.User;
import org.hibernate.criterion.Criterion;

import java.util.List;

public interface RoleService{
    void del(Role role);

    void saveRole(Role role);

    void updateOrgRole(String orgId, List<String> roleIdList);

    Criterion buildCriterion(String roleId);

    List<ComboTree> findComboTree(String roleId, User user);

    List<ComboTree> findComboTree(String roleId,ComboTree comboTree);

    void updateAuthority(String roleId, String roleFunction);

    List<TreeGrid> setOperate(String roleId, String id);

    void saveOperate(String roleId, String fop);

    void updateOperation(String roleId, String functionId,String operationcodes);

    void updateDataRule(String roleId, String functionId, String dataRulecodes);

    void doAddUserToOrg(String roleId, String userIds);
}
