package com.abocode.jfaster.admin.system.web;

import com.abocode.jfaster.admin.system.service.OrgService;
import com.abocode.jfaster.core.common.model.json.*;
import com.abocode.jfaster.core.platform.view.interactions.easyui.ComboTreeModel;
import com.abocode.jfaster.core.common.util.ConvertUtils;
import com.abocode.jfaster.core.common.constants.Globals;
import com.abocode.jfaster.core.platform.utils.MutiLangUtils;
import com.abocode.jfaster.core.web.hqlsearch.HqlGenerateUtil;
import com.abocode.jfaster.system.entity.Org;
import com.abocode.jfaster.system.entity.User;
import com.abocode.jfaster.system.entity.UserOrg;
import com.abocode.jfaster.admin.system.repository.DepartRepository;
import com.abocode.jfaster.admin.system.repository.ResourceRepository;
import com.abocode.jfaster.admin.system.repository.SystemRepository;
import com.abocode.jfaster.core.common.util.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import com.abocode.jfaster.core.persistence.hibernate.qbc.CriteriaQuery;
import com.abocode.jfaster.core.platform.view.widgets.easyui.TagUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * 部门信息处理类
 */
@Scope("prototype")
@Controller
@RequestMapping("/departController")
public class DepartController {
    @Autowired
    private DepartRepository departRepository;
    @Autowired
    private SystemRepository systemRepository;
    @Autowired
    private ResourceRepository resourceService;
    @Autowired
    private OrgService orgService;
    /**
     * 部门列表页面跳转
     *
     * @return
     */
    @RequestMapping(params = "depart")
    public ModelAndView depart() {
        return new ModelAndView("system/depart/departList");
    }

    /**
     * easyuiAJAX请求数据
     *
     * @param response
     * @param dataGrid
     */

    @RequestMapping(params = "datagrid")
    public void datagrid(HttpServletResponse response, DataGrid dataGrid) {
        CriteriaQuery cq = new CriteriaQuery(Org.class, dataGrid);
        this.systemRepository.findDataGridReturn(cq, true);
        TagUtil.datagrid(response, dataGrid);
    }

    /**
     * 删除部门：
     * <ul>
     *     组织机构下存在子机构时
     *     <li>不允许删除 组织机构</li>
     * </ul>
     * <ul>
     *     组织机构下存在用户时
     *     <li>不允许删除 组织机构</li>
     * </ul>
     * <ul>
     *     组织机构下 不存在子机构 且 不存在用户时
     *     <li>删除 组织机构-角色 信息</li>
     *     <li>删除 组织机构 信息</li>
     * </ul>
     *
     * @return 删除的结果信息
     */
    @RequestMapping(params = "del")
    @ResponseBody
    public AjaxJson del(Org depart, HttpServletRequest request) {
        depart = systemRepository.findEntity(Org.class, depart.getId());
        Assert.isTrue(depart != null, MutiLangUtils.paramDelFail("common.department"));
        Assert.isTrue(depart.getOrgs().size() == 0, MutiLangUtils.paramDelFail("common.department"));
        departRepository.deleteDepart(depart);
        String message = MutiLangUtils.paramDelSuccess("common.department");
        systemRepository.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
        return AjaxJsonBuilder.success(message);
    }

    /**
     * 添加部门
     *
     * @param depart
     * @return
     */
    @RequestMapping(params = "save")
    @ResponseBody
    public AjaxJson save(Org depart, HttpServletRequest request) {
        String pid = request.getParameter("parentOrg.id");
        // 设置上级部门
        if (pid.equals("")) {
            depart.setParentOrg(null);
        }
        orgService.save(depart);
        return AjaxJsonBuilder.success();
    }

    @RequestMapping(params = "add")
    public ModelAndView add(Org depart, HttpServletRequest req) {
        List<Org> departList = systemRepository.getList(Org.class);
        req.setAttribute("departList", departList);
        req.setAttribute("pid", depart.getId());
        return new ModelAndView("system/depart/depart");
    }

    /**
     * 部门列表页面跳转
     *
     * @return
     */
    @RequestMapping(params = "update")
    public ModelAndView update(Org depart, HttpServletRequest req) {
        List<Org> departList = systemRepository.getList(Org.class);
        req.setAttribute("departList", departList);
        if (!StringUtils.isEmpty(depart.getId())) {
            depart = systemRepository.findEntity(Org.class, depart.getId());
            req.setAttribute("departView", depart);
        }
        return new ModelAndView("system/depart/depart");
    }

    /**
     * 父级权限列表
     *
     * @param request
     * @param comboTree
     * @return
     */
    @RequestMapping(params = "setPFunction")
    @ResponseBody
    public List<ComboTree> setPFunction(HttpServletRequest request, ComboTree comboTree) {
        String selfId = request.getParameter("selfId");
        List<Org> departsList = orgService.findAll(selfId, comboTree.getId());
        ComboTreeModel comboTreeModel = new ComboTreeModel("id", "departname", "Departs");
        return resourceService.ComboTree(departsList, comboTreeModel, null, true);

    }

    /**
     * 部门列表，树形展示
     *
     * @param request
     * @param treegrid
     * @return
     */
    @RequestMapping(params = "departgrid")
    @ResponseBody
    public Object departgrid(Org tSDepart, HttpServletRequest request, TreeGrid treegrid) {
        String isSearch = request.getParameter("isSearch");
        return orgService.findTreeGrid(isSearch, tSDepart, treegrid);
    }

    /**
     * 方法描述:  查看成员列表
     *
     * @param request
     * @param departid
     * @return 返回类型： ModelAndView
     */
    @RequestMapping(params = "userList")
    public ModelAndView userList(HttpServletRequest request, String departid) {
        request.setAttribute("departid", departid);
        return new ModelAndView("system/depart/departUserList");
    }

    /**
     * 方法描述:  成员列表dataGrid
     *
     * @param user
     * @param request
     * @param response
     * @param dataGrid 返回类型： void
     */
    @RequestMapping(params = "userDatagrid")
    public void userDatagrid(User user, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
        CriteriaQuery cq = new CriteriaQuery(User.class, dataGrid);
        //查询条件组装器
        HqlGenerateUtil.installHql(cq, user);
        String departid = ConvertUtils.getString(request.getParameter("departid"));
        if (!StringUtils.isEmpty(departid)) {
            DetachedCriteria dc = cq.getDetachedCriteria();
            DetachedCriteria dcDepart = dc.createCriteria("userOrgList");
            dcDepart.add(Restrictions.eq("parentOrg.id", departid));
        }
        Short[] userstate = new Short[]{Globals.User_Normal, Globals.User_ADMIN};
        cq.in("status", userstate);
        cq.add();
        this.systemRepository.findDataGridReturn(cq, true);
        TagUtil.datagrid(response, dataGrid);
    }

    /**
     * 获取机构树-combotree
     *
     * @return
     */
    @RequestMapping(params = "getOrgTree")
    @ResponseBody
    public List<ComboTree> getOrgTree() {
        return orgService.buildComboTree();
    }

    /**
     * 添加 用户到组织机构 的页面  跳转
     *
     * @param req request
     * @return 处理结果信息
     */
    @RequestMapping(params = "goAddUserToOrg")
    public ModelAndView goAddUserToOrg(HttpServletRequest req) {
        req.setAttribute("orgId", req.getParameter("orgId"));
        return new ModelAndView("system/depart/noCurDepartUserList");
    }

    /**
     * 获取 除当前 组织之外的用户信息列表
     *
     * @param request request
     * @return 处理结果信息
     */
    @RequestMapping(params = "addUserToOrgList")
    public void addUserToOrgList(User user, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
        String orgId = request.getParameter("orgId");
        CriteriaQuery cq = new CriteriaQuery(User.class, dataGrid);
        HqlGenerateUtil.installHql(cq, user);

        // 获取 当前组织机构的用户信息
        CriteriaQuery subCq = new CriteriaQuery(UserOrg.class);
        subCq.setProjection(Property.forName("user.id"));
        subCq.eq("parentOrg.id", orgId);
        subCq.add();
        cq.add(Property.forName("id").notIn(subCq.getDetachedCriteria()));
        cq.add();
        this.systemRepository.findDataGridReturn(cq, true);
        TagUtil.datagrid(response, dataGrid);
    }

    /**
     * 添加 用户到组织机构
     *
     * @param request
     * @return 处理结果信息
     */
    @RequestMapping(params = "doAddUserToOrg")
    @ResponseBody
    public AjaxJson doAddUserToOrg(HttpServletRequest request) {
        Org depart = systemRepository.findEntity(Org.class, request.getParameter("orgId"));
        String orgIds = ConvertUtils.getString(request.getParameter("userIds"));
        orgService.saveOrgUserList(depart, orgIds);
        return AjaxJsonBuilder.success(MutiLangUtils.paramAddSuccess("common.user"));
    }

    /**
     * 用户选择机构列表跳转页面
     *
     * @return
     */
    @RequestMapping(params = "departSelect")
    public String departSelect() {
        return "system/depart/departSelect";
    }

    /**
     * 角色显示列表
     *
     * @param response response
     * @param dataGrid dataGrid
     */
    @RequestMapping(params = "departSelectDataGrid")
    public void datagridRole(HttpServletResponse response, DataGrid dataGrid) {
        CriteriaQuery cq = new CriteriaQuery(Org.class, dataGrid);
        this.systemRepository.findDataGridReturn(cq, true);
        TagUtil.datagrid(response, dataGrid);
    }
}
