package com.abocode.jfaster.admin.system.web;

import com.abocode.jfaster.admin.system.repository.DepartRepository;
import com.abocode.jfaster.admin.system.repository.ResourceRepository;
import com.abocode.jfaster.admin.system.repository.SystemRepository;
import com.abocode.jfaster.admin.system.service.OrgService;
import com.abocode.jfaster.admin.system.service.UserService;
import com.abocode.jfaster.api.system.OrgDto;
import com.abocode.jfaster.api.system.UserDto;
import com.abocode.jfaster.core.common.constants.Globals;
import com.abocode.jfaster.core.common.model.json.AjaxJson;
import com.abocode.jfaster.core.common.model.json.AjaxJsonBuilder;
import com.abocode.jfaster.core.common.model.json.ComboTree;
import com.abocode.jfaster.core.common.model.json.TreeGrid;
import com.abocode.jfaster.core.common.util.ConvertUtils;
import com.abocode.jfaster.core.common.util.StrUtils;
import com.abocode.jfaster.core.persistence.hibernate.qbc.CriteriaQuery;
import com.abocode.jfaster.core.platform.utils.LanguageUtils;
import com.abocode.jfaster.core.platform.view.interactions.easyui.ComboTreeModel;
import com.abocode.jfaster.core.repository.DataGridData;
import com.abocode.jfaster.core.repository.DataGridParam;
import com.abocode.jfaster.system.entity.Org;
import com.abocode.jfaster.system.entity.User;
import com.abocode.jfaster.system.entity.UserOrg;
import org.hibernate.criterion.Property;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
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
    @Autowired
    private UserService userService;

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
     * @param dataGridParam
     * @return
     */

    @RequestMapping(params = "findDataGridData")
    @ResponseBody
    public DataGridData findDataGridData(DataGridParam dataGridParam) {
        CriteriaQuery cq = new CriteriaQuery(Org.class).buildDataGrid(dataGridParam);
        return this.departRepository.findDataGridData(cq);
    }

    /**
     * @return 删除的结果信息
     */
    @RequestMapping(params = "del")
    @ResponseBody
    public AjaxJson del(@RequestParam String id) {
        Org depart = departRepository.find(Org.class, id);
        String paramLangKey = "common.department";
        Assert.isTrue(depart != null, LanguageUtils.paramDelFail(paramLangKey));
        Assert.isTrue(!CollectionUtils.isEmpty(depart.getOrgs()), LanguageUtils.paramDelFail(paramLangKey));
        departRepository.deleteDepart(depart);
        String message = LanguageUtils.paramDelSuccess(paramLangKey);
        systemRepository.addLog(message, Globals.LOG_TYPE_DEL, Globals.LOG_LEVEL);
        return AjaxJsonBuilder.success(message);
    }

    /**
     * 添加部门
     *
     * @param orgDto
     * @return
     */
    @RequestMapping(params = "save")
    @ResponseBody
    public AjaxJson save(OrgDto orgDto, HttpServletRequest request) {
        String pid = request.getParameter("parentOrg.id");
        // 设置上级部门
        if (pid.equals("")) {
            orgDto.setParentOrg(null);
        }
        Org org = new Org();
        BeanUtils.copyProperties(orgDto, org);
        orgService.save(org);
        return AjaxJsonBuilder.success();
    }

    @RequestMapping(params = "add")
    public ModelAndView add(@RequestParam String  id, HttpServletRequest request) {
        List<Org> departList = departRepository.findAll(Org.class);
        request.setAttribute("departList", departList);
        request.setAttribute("pid",id);
        return new ModelAndView("system/depart/depart");
    }

    /**
     * 部门列表页面跳转
     *
     * @return
     */
    @RequestMapping(params = "update")
    public ModelAndView update(@RequestParam String  id, HttpServletRequest request) {
        List<Org> departList = departRepository.findAll(Org.class);
        request.setAttribute("departList", departList);
        if (!StrUtils.isEmpty(id)) {
            Org depart = departRepository.find(Org.class, id);
            request.setAttribute("departView", depart);
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
    public List<ComboTree> setPFunction(@RequestParam String  selfId, ComboTree comboTree,HttpServletRequest request) {
        List<Org> departsList = orgService.findAll(selfId, comboTree.getId());
        ComboTreeModel comboTreeModel = new ComboTreeModel("id", "departname", "Departs");
        return resourceService.buildComboTree(departsList, comboTreeModel, null, true);

    }

    /**
     * 部门列表，树形展示
     *
     * @param treegrid
     * @return
     */
    @RequestMapping(params = "departgrid")
    @ResponseBody
    public Object departgrid(@RequestParam String  isSearch,  OrgDto orgDto, TreeGrid treegrid) {
        return orgService.findTreeGrid(isSearch, orgDto,  treegrid);
    }

    /**
     * 方法描述:  查看成员列表
     *
     * @param request
     * @param departid
     * @return 返回类型： ModelAndView
     */
    @RequestMapping(params = "userList")
    public ModelAndView userList(@RequestParam String  departid,HttpServletRequest request) {
        request.setAttribute("departid", departid);
        return new ModelAndView("system/depart/departUserList");
    }

    /**
     * 方法描述:  成员列表dataGrid
     *
     * @param departid
     * @param dataGridParam 返回类型： void
     * @return
     */
    @RequestMapping(params = "userDatagrid")
    @ResponseBody
    public DataGridData userDatagrid(UserDto userDto, @RequestParam String departid, DataGridParam dataGridParam) {
        return  userService.findDataGridData(userDto,departid,dataGridParam);
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
     * @param request request
     * @return 处理结果信息
     */
    @RequestMapping(params = "goAddUserToOrg")
    public ModelAndView goAddUserToOrg(@RequestParam String orgId, HttpServletRequest request) {
        request.setAttribute("orgId",orgId);
        return new ModelAndView("system/depart/noCurDepartUserList");
    }

    /**
     * 获取 除当前 组织之外的用户信息列表
     *
     * @param orgId
     * @return 处理结果信息
     */
    @RequestMapping(params = "addUserToOrgList")
    @ResponseBody
    public DataGridData addUserToOrgList(UserDto userDto,@RequestParam String  orgId, DataGridParam dataGridParam) {
        CriteriaQuery cq = new CriteriaQuery(User.class).buildParameters(userDto, dataGridParam);
        // 获取 当前组织机构的用户信息
        CriteriaQuery subCq = new CriteriaQuery(UserOrg.class);
        subCq.setProjection(Property.forName("user.id"));
        subCq.eq("parentOrg.id", orgId);
        subCq.add();
        cq.add(Property.forName("id").notIn(subCq.getDetachedCriteria()));
        cq.add();
        return this.departRepository.findDataGridData(cq);
    }

    /**
     * 添加 用户到组织机构
     *
     * @return 处理结果信息
     */
    @RequestMapping(params = "doAddUserToOrg")
    @ResponseBody
    public AjaxJson doAddUserToOrg(@RequestParam String  orgId,@RequestParam String userIds) {
        Org depart = departRepository.find(Org.class, orgId);
        String orgIds = ConvertUtils.getString(userIds);
        orgService.saveOrgUserList(depart, orgIds);
        return AjaxJsonBuilder.success(LanguageUtils.paramAddSuccess("common.user"));
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
     * @param dataGridParam dataGrid
     * @return
     */
    @RequestMapping(params = "departSelectDataGrid")
    @ResponseBody
    public DataGridData findDataGridDataRole(DataGridParam dataGridParam) {
        CriteriaQuery cq = new CriteriaQuery(Org.class).buildDataGrid(dataGridParam);
        return this.departRepository.findDataGridData(cq);
    }
}
