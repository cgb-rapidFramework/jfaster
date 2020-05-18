package com.abocode.jfaster.admin.system.web;

import com.abocode.jfaster.admin.system.service.BeanToTagConverter;
import com.abocode.jfaster.admin.system.service.RoleService;
import com.abocode.jfaster.core.common.model.json.*;
import com.abocode.jfaster.core.common.util.*;
import com.abocode.jfaster.core.platform.view.interactions.easyui.ComboTreeModel;
import com.abocode.jfaster.core.common.constants.Globals;
import com.abocode.jfaster.admin.system.repository.ResourceRepository;
import com.abocode.jfaster.system.entity.*;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import com.abocode.jfaster.core.persistence.hibernate.qbc.CriteriaQuery;
import com.abocode.jfaster.core.platform.view.interactions.easyui.TreeGridModel;
import com.abocode.jfaster.admin.system.dto.view.FunctionView;
import com.abocode.jfaster.core.platform.view.widgets.easyui.TagUtil;
import com.abocode.jfaster.core.web.hqlsearch.HqlGenerateUtil;
import com.abocode.jfaster.admin.system.repository.SystemRepository;
import com.abocode.jfaster.admin.system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

/**
 * 角色
 */
@Scope("prototype")
@Controller
@RequestMapping("/roleController")
public class RoleController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SystemRepository systemRepository;
    @Autowired
    private ResourceRepository resourceRepository;
    @Autowired
    private RoleService roleService;

    /**
     * 角色列表页
     *
     * @return
     */
    @RequestMapping(params = "role")
    public ModelAndView role() {
        return new ModelAndView("system/role/roleList");
    }

    /**
     * easyuiAJAX请求数据
     *
     * @param request
     * @param response
     * @param dataGrid
     */

    @RequestMapping(params = "roleGrid")
    public void roleGrid(Role role, HttpServletRequest request,
                         HttpServletResponse response, DataGrid dataGrid) {
        CriteriaQuery cq = new CriteriaQuery(Role.class, dataGrid);
        HqlGenerateUtil.installHql(cq,
                role);
        cq.add();
        this.systemRepository.findDataGridReturn(cq, true);
        TagUtil.datagrid(response, dataGrid);
        ;
    }

    /**
     * 删除角色
     *
     * @param role
     * @return
     */
    @RequestMapping(params = "delRole")
    @ResponseBody
    public AjaxJson delRole(Role role) {
        AjaxJson j = new AjaxJson();
        int count = userRepository.getUsersOfThisRole(role.getId());
        Assert.isTrue(count == 0, "角色: 仍被用户使用，请先删除关联关系");
        roleService.del(role);
        return AjaxJsonBuilder.success();
    }

    /**
     * 检查角色
     *
     * @param role
     * @return
     */
    @RequestMapping(params = "checkRole")
    @ResponseBody
    public ValidForm checkRole(Role role, HttpServletRequest request,
                               HttpServletResponse response) {
        ValidForm v = new ValidForm();
        String roleCode = ConvertUtils
                .getString(request.getParameter("param"));
        String code = ConvertUtils.getString(request.getParameter("code"));
        List<Role> roles = systemRepository.findAllByProperty(Role.class,
                "roleCode", roleCode);
        if (roles.size() > 0 && !code.equals(roleCode)) {
            v.setInfo("角色编码已存在");
            v.setStatus("n");
        }
        return v;
    }

    /**
     * 角色录入
     *
     * @param role
     * @return
     */
    @RequestMapping(params = "saveRole")
    @ResponseBody
    public AjaxJson saveRole(Role role) {
        roleService.saveRole(role);
        return AjaxJsonBuilder.success();
    }

    /**
     * 角色列表页面跳转
     *
     * @return
     */
    @RequestMapping(params = "fun")
    public ModelAndView fun(HttpServletRequest request) {
        String roleId = request.getParameter("roleId");
        request.setAttribute("roleId", roleId);
        return new ModelAndView("system/role/roleSet");
    }

    /**
     * 角色所有用户信息列表页面跳转
     *
     * @return
     */
    @RequestMapping(params = "userList")
    public ModelAndView userList(HttpServletRequest request) {
        request.setAttribute("roleId", request.getParameter("roleId"));
        return new ModelAndView("system/role/roleUserList");
    }

    /**
     * 用户列表查询
     *
     * @param request
     * @param response
     * @param dataGrid
     */
    @RequestMapping(params = "roleUserDatagrid")
    public void roleUserDatagrid(User user, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
        CriteriaQuery cq = new CriteriaQuery(User.class, dataGrid);
        //查询条件组装器
        String roleId = request.getParameter("roleId");
        List<RoleUser> roleUser = systemRepository.findAllByProperty(RoleUser.class, "role.id", roleId);
        Criterion cc = null;
        if (roleUser.size() > 0) {
            for (int i = 0; i < roleUser.size(); i++) {
                if (i == 0) {
                    cc = Restrictions.eq("id", roleUser.get(i).getUser().getId());
                } else {
                    cc = cq.getor(cc, Restrictions.eq("id", roleUser.get(i).getUser().getId()));
                }
            }
        } else {
            cc = Restrictions.eq("id", "-1");
        }
        cq.add(cc);
        HqlGenerateUtil.installHql(cq, user);
        this.systemRepository.findDataGridReturn(cq, true);
        TagUtil.datagrid(response, dataGrid);
    }

    /**
     * 获取用户列表
     *
     * @param user
     * @param request
     * @return
     */
    @RequestMapping(params = "getUserList")
    @ResponseBody
    public List<ComboTree> getUserList(User user, HttpServletRequest request,
                                       ComboTree comboTree) {
        String roleId = request.getParameter("roleId");
        List<User> loginActionlist = new ArrayList<User>();
        if (user != null) {
            List<RoleUser> roleUser = systemRepository.findAllByProperty(RoleUser.class, "role.id", roleId);
            if (roleUser.size() > 0) {
                for (RoleUser ru : roleUser) {
                    loginActionlist.add(ru.getUser());
                }
            }
        }
        ComboTreeModel comboTreeModel = new ComboTreeModel("id", "username", "user");
        List<ComboTree> comboTrees = resourceRepository.ComboTree(loginActionlist, comboTreeModel, loginActionlist, false);
        return comboTrees;
    }

    /**
     * 角色树列表页面跳转
     *
     * @return
     */
    @RequestMapping(params = "roleTree")
    public ModelAndView roleTree(HttpServletRequest request) {
        request.setAttribute("orgId", request.getParameter("orgId"));
        return new ModelAndView("system/role/roleTree");
    }

    /**
     * 获取 组织机构的角色树
     *
     * @param request
     * @return 组织机构的角色树
     */
    @RequestMapping(params = "getRoleTree")
    @ResponseBody
    public List<ComboTree> getRoleTree(HttpServletRequest request) {
        String orgId = request.getParameter("orgId");
        return resourceRepository.findComboTree(orgId);
    }

    /**
     * 更新 组织机构的角色列表
     *
     * @param request request
     * @return 操作结果
     */
    @RequestMapping(params = "updateOrgRole")
    @ResponseBody
    public AjaxJson updateOrgRole(HttpServletRequest request) {
        String orgId = request.getParameter("orgId");
        String roleIds = request.getParameter("roleIds");
        List<String> roleIdList = IdUtils.extractIdListByComma(roleIds);
        roleService.updateOrgRole(orgId, roleIdList);
        return AjaxJsonBuilder.success();
    }


    /**
     * 设置权限
     * @param request
     * @param comboTree
     * @return
     */
    @RequestMapping(params = "setAuthority")
    @ResponseBody
    public List<ComboTree> setAuthority(HttpServletRequest request, ComboTree comboTree) {
        CriteriaQuery cq = new CriteriaQuery(Function.class);
        if (comboTree.getId() != null) {
            cq.eq("Function.id", comboTree.getId());
        }
        if (comboTree.getId() == null) {
            cq.isNull("Function");
        }
        cq.notEq("functionLevel", Short.parseShort("-1"));
        cq.add();
        List<Function> functionList = systemRepository.findListByCq( cq, false);
        List<FunctionView> functionBeanList = BeanToTagConverter.convertFunctions(functionList);
        Collections.sort(functionBeanList, new NumberComparator());
        List<ComboTree> comboTrees = new ArrayList<ComboTree>();
        String roleId = request.getParameter("roleId");
        List<Function> loginActionList = this.systemRepository.getFucntionList(roleId);
        ComboTreeModel comboTreeModel = new ComboTreeModel("id",
                "functionName", "Functions");
        comboTrees = resourceRepository.ComboTree(functionBeanList, comboTreeModel,
                BeanToTagConverter.convertFunctions(loginActionList), false);
        MutiLangUtils.setMutiTree(comboTrees);
        return comboTrees;
    }

    /**
     * 更新权限
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "updateAuthority")
    @ResponseBody
    public AjaxJson updateAuthority(HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        try {
            String roleId = request.getParameter("roleId");
            String rolefunction = request.getParameter("rolefunctions");
            Role role = this.systemRepository.find(Role.class, roleId);
            List<RoleFunction> roleFunctionList = systemRepository
                    .findAllByProperty(RoleFunction.class, "role.id",
                            role.getId());
            Map<String, RoleFunction> map = new HashMap<String, RoleFunction>();
            for (RoleFunction functionOfRole : roleFunctionList) {
                map.put(functionOfRole.getFunction().getId(), functionOfRole);
            }
            String[] roleFunctions = rolefunction.split(",");
            Set<String> set = new HashSet<String>();
            for (String s : roleFunctions) {
                set.add(s);
            }
            updateCompare(set, role, map);
            j.setMsg("权限更新成功");
        } catch (Exception e) {
            j.setMsg("权限更新失败");
        }
        return j;
    }

    /**
     * 权限比较
     *
     * @param set  最新的权限列表
     * @param role 当前角色
     * @param map  旧的权限列表
     */
    private void updateCompare(Set<String> set, Role role,
                               Map<String, RoleFunction> map) {
        List<RoleFunction> entitys = new ArrayList<RoleFunction>();
        List<RoleFunction> deleteEntitys = new ArrayList<RoleFunction>();
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
        for (; it.hasNext(); ) {
            deleteEntitys.add(it.next());
        }
        systemRepository.batchSave(entitys);
        systemRepository.deleteEntities(deleteEntitys);

    }

    /**
     * 角色页面跳转
     *
     * @param role
     * @param req
     * @return
     */
    @RequestMapping(params = "addorupdate")
    public ModelAndView addorupdate(Role role, HttpServletRequest req) {
        if (role.getId() != null) {
            role = systemRepository.findEntity(Role.class, role.getId());
            req.setAttribute("roleView", role);
        }
        return new ModelAndView("system/role/role");
    }

    /**
     * 权限操作列表
     *
     * @param treegrid
     * @param request
     * @return
     */
    @RequestMapping(params = "setOperate")
    @ResponseBody
    public List<TreeGrid> setOperate(HttpServletRequest request,
                                     TreeGrid treegrid) {
        String roleId = request.getParameter("roleId");
        CriteriaQuery cq = new CriteriaQuery(Function.class);
        if (treegrid.getId() != null) {
            cq.eq("Function.id", treegrid.getId());
        }
        if (treegrid.getId() == null) {
            cq.isNull("Function");
        }
        cq.add();
        List<Function> functionList = systemRepository.findListByCq(
                cq, false);
        Collections.sort(functionList, new FunctionComparator());
        TreeGridModel treeGridModel = new TreeGridModel();
        treeGridModel.setRoleid(roleId);
        List<TreeGrid> treeGrids = resourceRepository.treegrid(functionList, treeGridModel);
        return treeGrids;

    }

    /**
     * 操作录入
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "saveOperate")
    @ResponseBody
    public AjaxJson saveOperate(HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        String fop = request.getParameter("fp");
        String roleId = request.getParameter("roleId");
        // 录入操作前清空上一次的操作数据
        clearp(roleId);
        String[] fun_op = fop.split(",");
        String aa = "";
        String bb = "";
        // 只有一个被选中
        if (fun_op.length == 1) {
            bb = fun_op[0].split("_")[1];
            aa = fun_op[0].split("_")[0];
            savep(roleId, bb, aa);
        } else {
            // 至少2个被选中
            for (int i = 0; i < fun_op.length; i++) {
                String cc = fun_op[i].split("_")[0]; // 操作id
                if (i > 0 && bb.equals(fun_op[i].split("_")[1])) {
                    aa += "," + cc;
                    if (i == (fun_op.length - 1)) {
                        savep(roleId, bb, aa);
                    }
                } else if (i > 0) {
                    savep(roleId, bb, aa);
                    aa = fun_op[i].split("_")[0]; // 操作ID
                    if (i == (fun_op.length - 1)) {
                        bb = fun_op[i].split("_")[1]; // 权限id
                        savep(roleId, bb, aa);
                    }

                } else {
                    aa = fun_op[i].split("_")[0]; // 操作ID
                }
                bb = fun_op[i].split("_")[1]; // 权限id

            }
        }

        return j;
    }

    /**
     * 更新操作
     *
     * @param roleId
     * @param functionid
     * @param ids
     */
    public void savep(String roleId, String functionid, String ids) {
        StringBuffer hql = new StringBuffer();
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
    public void clearp(String roleId) {
        List<RoleFunction> rFunctions = systemRepository.findAllByProperty(
                RoleFunction.class, "role.id", roleId);
        if (rFunctions.size() > 0) {
            for (RoleFunction tRoleFunction : rFunctions) {
                tRoleFunction.setOperation(null);
                systemRepository.saveOrUpdate(tRoleFunction);
            }
        }
    }

    /**
     * 按钮权限展示
     *
     * @param request
     * @param functionId
     * @param roleId
     * @return
     */
    @RequestMapping(params = "operationListForFunction")
    public ModelAndView operationListForFunction(HttpServletRequest request,
                                                 String functionId, String roleId) {
        CriteriaQuery cq = new CriteriaQuery(Operation.class);
        cq.eq("Function.id", functionId);
        cq.eq("status", Short.valueOf("0"));
        cq.add();
        List<Operation> operationList = this.systemRepository
                .findListByCq(cq, false);
        Set<String> operationCodes = systemRepository
                .getOperationCodesByRoleIdAndFunctionId(roleId, functionId);
        request.setAttribute("operationList", operationList);
        request.setAttribute("operationcodes", operationCodes);
        request.setAttribute("functionId", functionId);
        return new ModelAndView("system/role/operationListForFunction");
    }

    /**
     * 更新按钮权限
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "updateOperation")
    @ResponseBody
    public AjaxJson updateOperation(HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        String roleId = request.getParameter("roleId");
        String functionId = request.getParameter("functionId");
        String operationcodes = null;
        try {
            operationcodes = URLDecoder.decode(
                    request.getParameter("operationcodes"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            LogUtils.error(e.getMessage());
        }
        CriteriaQuery cq1 = new CriteriaQuery(RoleFunction.class);
        cq1.eq("role.id", roleId);
        cq1.eq("Function.id", functionId);
        cq1.add();
        List<RoleFunction> rFunctions = systemRepository.findListByCq(
                cq1, false);
        if (null != rFunctions && rFunctions.size() > 0) {
            RoleFunction tsRoleFunction = rFunctions.get(0);
            tsRoleFunction.setOperation(operationcodes);
            systemRepository.saveOrUpdate(tsRoleFunction);
        }
        j.setMsg("按钮权限更新成功");
        return j;
    }

    /**
     * 按钮权限展示
     *
     * @param request
     * @param functionId
     * @param roleId
     * @return
     */
    @RequestMapping(params = "dataRuleListForFunction")
    public ModelAndView dataRuleListForFunction(HttpServletRequest request,
                                                String functionId, String roleId) {
        CriteriaQuery cq = new CriteriaQuery(DataRule.class);
        cq.eq("Function.id", functionId);
        cq.add();
        List<DataRule> dataRuleList = this.systemRepository
                .findListByCq(cq, false);
        Set<String> dataRulecodes = systemRepository
                .getOperationCodesByRoleIdAndruleDataId(roleId, functionId);
        request.setAttribute("dataRuleList", dataRuleList);
        request.setAttribute("dataRulecodes", dataRulecodes);
        request.setAttribute("functionId", functionId);
        return new ModelAndView("system/role/dataRuleListForFunction");
    }


    /**
     * 更新按钮权限
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "updateDataRule")
    @ResponseBody
    public AjaxJson updateDataRule(HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        String roleId = request.getParameter("roleId");
        String functionId = request.getParameter("functionId");
        String dataRulecodes = null;
        try {
            dataRulecodes = URLDecoder.decode(
                    request.getParameter("dataRulecodes"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            LogUtils.error(e.getMessage());
        }
        CriteriaQuery cq1 = new CriteriaQuery(RoleFunction.class);
        cq1.eq("role.id", roleId);
        cq1.eq("Function.id", functionId);
        cq1.add();
        List<RoleFunction> rFunctions = systemRepository.findListByCq(
                cq1, false);
        if (null != rFunctions && rFunctions.size() > 0) {
            RoleFunction tsRoleFunction = rFunctions.get(0);
            tsRoleFunction.setDataRule(dataRulecodes);
            systemRepository.saveOrUpdate(tsRoleFunction);
        }
        j.setMsg("数据权限更新成功");
        return j;
    }


    /**
     * 添加 用户到角色 的页面  跳转
     *
     * @param req request
     * @return 处理结果信息
     */
    @RequestMapping(params = "goAddUserToRole")
    public ModelAndView goAddUserToOrg(HttpServletRequest req) {
        return new ModelAndView("system/role/noCurRoleUserList");
    }

    /**
     * 获取 除当前 角色之外的用户信息列表
     *
     * @param request request
     * @return 处理结果信息
     */
    @RequestMapping(params = "addUserToRoleList")
    public void addUserToOrgList(User user, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
        String roleId = request.getParameter("roleId");

        CriteriaQuery cq = new CriteriaQuery(User.class, dataGrid);
        HqlGenerateUtil.installHql(cq, user);

        // 获取 当前组织机构的用户信息
        CriteriaQuery subCq = new CriteriaQuery(RoleUser.class);
        subCq.setProjection(Property.forName("user.id"));
        subCq.eq("role.id", roleId);
        subCq.add();


        cq.add(Property.forName("id").notIn(subCq.getDetachedCriteria()));
        cq.add();

        this.systemRepository.findDataGridReturn(cq, true);
        TagUtil.datagrid(response, dataGrid);
    }

    /**
     * 添加 用户到角色
     *
     * @param req request
     * @return 处理结果信息
     */
    @RequestMapping(params = "doAddUserToRole")
    @ResponseBody
    public AjaxJson doAddUserToOrg(HttpServletRequest req) {
        AjaxJson j = new AjaxJson();
        Role role = systemRepository.findEntity(Role.class, req.getParameter("roleId"));
        saveRoleUserList(req, role);
		String message = MutiLangUtils.paramAddSuccess("common.user");
        systemRepository.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
        j.setMsg(message);
        return j;
    }

    /**
     * 保存 角色-用户 关系信息
     *
     * @param request request
     */
    private void saveRoleUserList(HttpServletRequest request, Role role) {
        String userIds = ConvertUtils.getString(request.getParameter("userIds"));

        List<RoleUser> roleUserList = new ArrayList<RoleUser>();
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
    }
}
