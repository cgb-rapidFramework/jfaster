package com.abocode.jfaster.admin.system.service.impl;

import com.abocode.jfaster.admin.system.dto.RoleIdAndNameDto;
import com.abocode.jfaster.admin.system.repository.ResourceRepository;
import com.abocode.jfaster.admin.system.repository.SystemRepository;
import com.abocode.jfaster.admin.system.repository.UserRepository;
import com.abocode.jfaster.admin.system.service.BeanToTagConverter;
import com.abocode.jfaster.admin.system.service.RoleService;
import com.abocode.jfaster.core.common.constants.Globals;
import com.abocode.jfaster.core.common.model.json.ComboTree;
import com.abocode.jfaster.core.common.model.json.TreeGrid;
import com.abocode.jfaster.core.common.util.IdUtils;
import com.abocode.jfaster.core.common.util.StrUtils;
import com.abocode.jfaster.core.persistence.hibernate.qbc.CriteriaQuery;
import com.abocode.jfaster.core.platform.utils.FunctionSortUtils;
import com.abocode.jfaster.core.platform.utils.LanguageUtils;
import com.abocode.jfaster.core.platform.view.FunctionView;
import com.abocode.jfaster.core.platform.view.interactions.easyui.ComboTreeModel;
import com.abocode.jfaster.core.platform.view.interactions.easyui.TreeGridModel;
import com.abocode.jfaster.system.entity.*;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RoleServiceImpl implements RoleService {
    public static final String ROLE_ID = "role.id";
    @Autowired
    private ResourceRepository resourceRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SystemRepository systemRepository;

    @Override
    public void del(Role role) {
        // 删除角色之前先删除角色权限关系
        delRoleFunction(role);
        systemRepository.executeSql("delete from t_s_role_org where role_id=?", role.getId()); // 删除 角色-机构 关系信息
        role = systemRepository.find(Role.class, role.getId());
        userRepository.delete(role);
        String message = "角色: " + role.getRoleName() + "被删除成功";
        systemRepository.addLog(message, Globals.LOG_TYPE_DEL, Globals.LOG_LEVEL);
    }

    @Override
    public void saveRole(Role role) {
        String message;
        if (StrUtils.isNotEmpty(role.getId())) {
            message = "角色: " + role.getRoleName() + "被更新成功";
            userRepository.saveOrUpdate(role);
            systemRepository.addLog(message, Globals.LOG_TYPE_UPDATE,
                    Globals.LOG_LEVEL);
        } else {
            message = "角色: " + role.getRoleName() + "被添加成功";
            userRepository.save(role);
            systemRepository.addLog(message, Globals.LOG_TYPE_INSERT,
                    Globals.LOG_LEVEL);
        }
    }

    @Override
    public void updateOrgRole(String orgId, List<String> roleIdList) {
        systemRepository.executeSql("delete from t_s_role_org where org_id=?", orgId);
        if (!roleIdList.isEmpty()) {
            List<RoleOrg> roleOrgList = new ArrayList<>();
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

    @Override
    public Criterion buildCriterion(String roleId) {
        List<RoleUser> roleUser = systemRepository.findAllByProperty(RoleUser.class, ROLE_ID, roleId);
        Criterion cc = null;
        if (!CollectionUtils.isEmpty(roleUser)) {
            for (int i = 0; i < roleUser.size(); i++) {
                if (i == 0) {
                    cc = Restrictions.eq("id", roleUser.get(i).getUser().getId());
                } else {
                    cc = Restrictions.or(cc, Restrictions.eq("id", roleUser.get(i).getUser().getId()));
                }
            }
        } else {
            cc = Restrictions.eq("id", "-1");
        }
        return cc;
    }

    @Override
    public List<ComboTree> findComboTree(String roleId, User user) {
        List<User> loginActionlist = new ArrayList<>();
        if (user != null) {
            List<RoleUser> roleUser = systemRepository.findAllByProperty(RoleUser.class, ROLE_ID, roleId);
            for (RoleUser ru : roleUser) {
                loginActionlist.add(ru.getUser());
            }
        }
        ComboTreeModel comboTreeModel = new ComboTreeModel("id", "username", "user");
        return resourceRepository.buildComboTree(loginActionlist, comboTreeModel, loginActionlist, false);
    }

    @Override
    public List<ComboTree> findComboTree(String roleId, ComboTree comboTree) {
        CriteriaQuery cq = new CriteriaQuery(Function.class);
        if (comboTree.getId() != null) {
            cq.eq("parentFunction.id", comboTree.getId());
        }
        if (comboTree.getId() == null) {
            cq.isNull("parentFunction");
        }
        cq.notEq("functionLevel", Short.parseShort("-1"));
        cq.add();
        List<Function> functionList = systemRepository.findListByCq(cq, false);
        List<FunctionView> functionBeanList = BeanToTagConverter.convertFunctions(functionList);
        FunctionSortUtils.sortView(functionBeanList);
        List<Function> loginActionList = this.systemRepository.getFucntionList(roleId);
        ComboTreeModel comboTreeModel = new ComboTreeModel("id",
                "functionName", "Functions");
        List<ComboTree> comboTrees = resourceRepository.buildComboTree(functionBeanList, comboTreeModel,
                BeanToTagConverter.convertFunctions(loginActionList), false);
        LanguageUtils.setLanguageTree(comboTrees);
        return comboTrees;
    }

    @Override
    public void updateAuthority(String roleId, String roleFunction) {
        Role role = this.systemRepository.find(Role.class, roleId);
        List<RoleFunction> roleFunctionList = systemRepository
                .findAllByProperty(RoleFunction.class, ROLE_ID,
                        role.getId());
        Map<String, RoleFunction> map = new HashMap<>();
        for (RoleFunction functionOfRole : roleFunctionList) {
            map.put(functionOfRole.getFunction().getId(), functionOfRole);
        }
        String[] roleFunctions = roleFunction.split(",");
        updateCompare(roleFunctions, role, map);
    }

    @Override
    public List<TreeGrid> setOperate(String roleId, String id) {
        CriteriaQuery cq = new CriteriaQuery(Function.class);
        if (id != null) {
            cq.eq("parentFunction.id", id);
        }
        if (id == null) {
            cq.isNull("parentFunction");
        }
        cq.add();
        List<Function> functionList = systemRepository.findListByCq(
                cq, false);
        FunctionSortUtils.sort(functionList);
        TreeGridModel treeGridModel = new TreeGridModel();
        treeGridModel.setRoleId(roleId);
        return resourceRepository.getTreeGrid(functionList, treeGridModel);
    }

    @Override
    public void saveOperate(String roleId, String functionOperate) {
        // 录入操作前清空上一次的操作数据
        clearp(roleId);
        String[] operates = functionOperate.split(",");
        // 只有一个被选中
        if (operates.length == 1) {
            savep(roleId, operates[0].split("_")[1], operates[0].split("_")[0]);
        } else {
            saveAll(operates, roleId);
        }
    }

    private void saveAll(String[] funOps, String roleId) {
        // 至少2个被选中
        String bb = "";
        for (int i = 0; i < funOps.length; i++) {
            String cc = funOps[i].split("_")[0]; // 操作id
            if (i > 0 && bb.equals(funOps[i].split("_")[1])) {
                StringBuilder aa = new StringBuilder();
                aa.append("," + cc);
                if (i == (funOps.length - 1)) {
                    savep(roleId, bb, aa.toString());
                }
            } else if (i > 0) {
                StringBuilder aa = new StringBuilder();
                savep(roleId, bb, aa.toString());
                aa.append(funOps[i].split("_")[0]); // 操作ID
                if (i == (funOps.length - 1)) {
                    bb = funOps[i].split("_")[1]; // 权限id
                    savep(roleId, bb, aa.toString());
                }

            } else {
                StringBuilder aa = new StringBuilder();
                aa.append(funOps[i].split("_")[0]); // 操作ID
            }
            bb = funOps[i].split("_")[1]; // 权限id
        }
    }

    @Override
    public void updateOperation(String roleId, String functionId, String operationcodes) {
        CriteriaQuery cq1 = new CriteriaQuery(RoleFunction.class);
        cq1.eq(ROLE_ID, roleId);
        cq1.eq("function.id", functionId);
        cq1.add();
        List<RoleFunction> rFunctions = systemRepository.findListByCq(
                cq1, false);
        if (!CollectionUtils.isEmpty(rFunctions)) {
            RoleFunction tsRoleFunction = rFunctions.get(0);
            tsRoleFunction.setOperation(operationcodes);
            systemRepository.saveOrUpdate(tsRoleFunction);
        }
    }

    @Override
    public void updateDataRule(String roleId, String functionId, String dataRulecodes) {
        CriteriaQuery cq1 = new CriteriaQuery(RoleFunction.class);
        cq1.eq(ROLE_ID, roleId);
        cq1.eq("function.id", functionId);
        cq1.add();
        List<RoleFunction> rFunctions = systemRepository.findListByCq(
                cq1, false);
        if (!CollectionUtils.isEmpty(rFunctions)) {
            RoleFunction tsRoleFunction = rFunctions.get(0);
            tsRoleFunction.setDataRule(dataRulecodes);
            systemRepository.saveOrUpdate(tsRoleFunction);
        }
    }

    @Override
    public void doAddUserToOrg(String roleId, String userIds) {
        Role role = systemRepository.find(Role.class, roleId);
        List<RoleUser> roleUserList = new ArrayList<>();
        List<String> userIdList = IdUtils.extractIdListByComma(userIds);
        for (String userId : userIdList) {
            User user = new User();
            user.setId(userId);

            RoleUser roleUser = new RoleUser();
            roleUser.setUser(user);
            roleUser.setRole(role);

            roleUserList.add(roleUser);
        }
        if (!roleUserList.isEmpty()) {
            systemRepository.batchSave(roleUserList);
        }
        String message = LanguageUtils.paramAddSuccess("common.user");
        systemRepository.addLog(message, Globals.LOG_TYPE_UPDATE, Globals.LOG_LEVEL);
    }

    /**
     * 更新操作
     *
     * @param roleId
     * @param functionid
     * @param ids
     */
    private void savep(String roleId, String functionid, String ids) {
        StringBuilder hql = new StringBuilder();
        hql.append(" from RoleFunction t where ");
        hql.append(" t.org.id=" + roleId);
        hql.append(" and t.function.function_id=" + functionid);
        RoleFunction rFunction = systemRepository.findUniqueByHql(hql.toString());
        if (rFunction != null) {
            rFunction.setOperation(ids);
            systemRepository.saveOrUpdate(rFunction);
        }
    }

    /**
     * 清空操作
     *
     * @param roleId
     */
    private void clearp(String roleId) {
        List<RoleFunction> rFunctions = systemRepository.findAllByProperty(
                RoleFunction.class, ROLE_ID, roleId);
        for (RoleFunction tRoleFunction : rFunctions) {
            tRoleFunction.setOperation(null);
            systemRepository.saveOrUpdate(tRoleFunction);
        }
    }

    /**
     * 权限比较
     *
     * @param set  最新的权限列表
     * @param role 当前角色
     * @param map  旧的权限列表
     */
    private void updateCompare(String[] set, Role role,
                               Map<String, RoleFunction> map) {
        List<RoleFunction> entitys = new ArrayList<>();
        List<RoleFunction> deleteEntitys = new ArrayList<>();
        for (String s : set) {
            if (map.containsKey(s)) {
                map.remove(s);
            } else {
                RoleFunction rf = new RoleFunction();
                Function f = this.systemRepository.find(Function.class, s);
                rf.setFunction(f);
                rf.setRole(role);
                entitys.add(rf);
            }
        }
        Collection<RoleFunction> collection = map.values();
        Iterator<RoleFunction> it = collection.iterator();
        while (it.hasNext()) {
            deleteEntitys.add(it.next());
        }
        systemRepository.batchSave(entitys);
        systemRepository.deleteEntities(deleteEntitys);

    }

    /**
     * 删除角色权限
     *
     * @param role
     */
    private void delRoleFunction(Role role) {
        List<RoleFunction> roleFunctions = systemRepository.findAllByProperty(
                RoleFunction.class, ROLE_ID, role.getId());
        for (RoleFunction tsRoleFunction : roleFunctions) {
            systemRepository.delete(tsRoleFunction);
        }
        List<RoleUser> roleUsers = systemRepository.findAllByProperty(
                RoleUser.class, ROLE_ID, role.getId());
        for (RoleUser tsRoleUser : roleUsers) {
            systemRepository.delete(tsRoleUser);
        }
    }

    @Override
    public RoleIdAndNameDto findByUserId(String id) {
        List<RoleUser> roleUsers = userRepository.findAllByProperty(RoleUser.class, "user.id", id);
        StringBuilder roleId = new StringBuilder();
        StringBuilder roleName = new StringBuilder();
        for (RoleUser tRoleUser : roleUsers) {
            roleId.append(tRoleUser.getRole().getId()).append(",");
            roleName.append(tRoleUser.getRole().getRoleName()).append(",");
        }
        return new RoleIdAndNameDto(roleId.toString(), roleName.toString());
    }
}
