package com.abocode.jfaster.admin.system.web;
import com.abocode.jfaster.core.common.model.json.AjaxJson;
import com.abocode.jfaster.core.common.model.json.ComboTree;
import com.abocode.jfaster.core.common.model.json.DataGrid;
import com.abocode.jfaster.core.common.model.json.TreeGrid;
import com.abocode.jfaster.core.interfaces.BaseController;
import com.abocode.jfaster.core.platform.view.interactions.easyui.ComboTreeModel;
import com.abocode.jfaster.core.common.util.ConvertUtils;
import com.abocode.jfaster.core.common.constants.Globals;
import com.abocode.jfaster.core.common.util.MutiLangUtils;
import com.abocode.jfaster.core.web.hqlsearch.HqlGenerateUtil;
import com.abocode.jfaster.system.entity.Org;
import com.abocode.jfaster.system.entity.User;
import com.abocode.jfaster.system.entity.UserOrg;
import com.abocode.jfaster.admin.system.repository.DepartRepository;
import com.abocode.jfaster.admin.system.repository.ResourceRepository;
import com.abocode.jfaster.admin.system.repository.SystemRepository;
import com.abocode.jfaster.admin.system.repository.UserRepository;
import com.abocode.jfaster.core.common.util.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import com.abocode.jfaster.core.persistence.hibernate.qbc.CriteriaQuery;
import com.abocode.jfaster.core.platform.view.interactions.easyui.TreeGridModel;
import com.abocode.jfaster.core.platform.view.widgets.easyui.TagUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 部门信息处理类
 * 
 * @author 张代浩
 * 
 */
@Scope("prototype")
@Controller
@RequestMapping("/departController")
public class DepartController extends BaseController {

	@Autowired
	private DepartRepository departService;
	@Autowired
	private UserRepository userService;
	@Autowired
	private SystemRepository systemService;
	private String message;
	@Autowired
	private ResourceRepository resourceService;
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Autowired
	public void setSystemService(SystemRepository systemService) {
		this.systemService = systemService;
	}

	public UserRepository getUserService() {
		return userService;
	}

	@Autowired
	public void setUserService(UserRepository userService) {
		this.userService = userService;
	}

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
	 * @param request
	 * @param response
	 * @param dataGrid
	 */

	@RequestMapping(params = "datagrid")
	public void datagrid(HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(Org.class, dataGrid);
		this.systemService.findDataGridReturn(cq, true);
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
	 * @return 删除的结果信息
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(Org depart, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		depart = systemService.findEntity(Org.class, depart.getId());
        message = MutiLangUtils.paramDelSuccess("common.department");
        if (depart.getOrgs().size() == 0) {
			departService.deleteDepart(depart);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
        } else {
            message = MutiLangUtils.paramDelFail("common.department");
        }
        j.setMsg(message);
		return j;
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
		// 设置上级部门
		String pid = request.getParameter("PDepart.id");
		if (pid.equals("")) {
			depart.setParentOrg(null);
		}
		AjaxJson j = new AjaxJson();
		if (!StringUtils.isEmpty(depart.getId())) {
            message = MutiLangUtils.paramUpdSuccess("common.department");
			userService.saveOrUpdate(depart);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} else {
            message = MutiLangUtils.paramAddSuccess("common.department");
			userService.save(depart);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}

        j.setMsg(message);
		return j;
	}
	@RequestMapping(params = "add")
	public ModelAndView add(Org depart, HttpServletRequest req) {
		List<Org> departList = systemService.getList(Org.class);
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
		List<Org> departList = systemService.getList(Org.class);
		req.setAttribute("departList", departList);
		if (!StringUtils.isEmpty(depart.getId())) {
			depart = systemService.findEntity(Org.class, depart.getId());
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
		CriteriaQuery cq = new CriteriaQuery(Org.class);
		if(null != request.getParameter("selfId")){
			cq.notEq("id", request.getParameter("selfId"));
		}
		if (comboTree.getId() != null) {
			cq.eq("PDepart.id", comboTree.getId());
		}
		if (comboTree.getId() == null) {
			cq.isNull("PDepart");
		}
		cq.add();
		List<Org> departsList = systemService.findListByCq(cq, false);
		List<ComboTree> comboTrees = new ArrayList<ComboTree>();
		ComboTreeModel comboTreeModel = new ComboTreeModel("id", "departname", "Departs");
		comboTrees = resourceService.ComboTree(departsList, comboTreeModel, null, true);
		return comboTrees;

	}
	/**
	 * 部门列表，树形展示
	 * @param request
	 * @param response
	 * @param treegrid
	 * @return
	 */
	@RequestMapping(params = "departgrid")
	@ResponseBody
	public Object departgrid(Org tSDepart, HttpServletRequest request, HttpServletResponse response, TreeGrid treegrid) {
		CriteriaQuery cq = new CriteriaQuery(Org.class);
		if("yes".equals(request.getParameter("isSearch"))){
			treegrid.setId(null);
			tSDepart.setId(null);
		} 
		if(null != tSDepart.getOrgName()){
			HqlGenerateUtil.installHql(cq, tSDepart);
		}
		if (treegrid.getId() != null) {
			cq.eq("PDepart.id", treegrid.getId());
		}
		if (treegrid.getId() == null) {
			cq.isNull("PDepart");
		}
		cq.add();


		List<TreeGrid> departList =systemService.findListByCq(cq, false);
		if(departList.size()==0&&tSDepart.getOrgName()!=null){
			cq = new CriteriaQuery(Org.class);
			Org parDepart = new Org();
			tSDepart.setParentOrg(parDepart);
			HqlGenerateUtil.installHql(cq, tSDepart);
		    departList =systemService.findListByCq(cq, false);
		}

		TreeGridModel treeGridModel = new TreeGridModel();
		treeGridModel.setTextField("departname");
		treeGridModel.setParentText("PDepart_departname");
		treeGridModel.setParentId("PDepart_id");
		treeGridModel.setSrc("description");
		treeGridModel.setIdField("id");
		treeGridModel.setChildList("Departs");
        Map<String,Object> fieldMap = new HashMap<String, Object>();
        fieldMap.put("orgCode", "orgCode");
        fieldMap.put("orgType", "orgType");
        treeGridModel.setFieldMap(fieldMap);
		List<TreeGrid> treeGrids = resourceService.treegrid(departList, treeGridModel);
		return  treeGrids;
	}
	/**
	 * 方法描述:  查看成员列表
	 * 作    者： yiming.zhang
	 * 日    期： Dec 4, 2013-8:53:39 PM
	 * @param request
	 * @param departid
	 * @return 
	 * 返回类型： ModelAndView
	 */
	@RequestMapping(params = "userList")
	public ModelAndView userList(HttpServletRequest request, String departid) {
		request.setAttribute("departid", departid);
		return new ModelAndView("system/depart/departUserList");
	}
	
	/**
	 * 方法描述:  成员列表dataGrid
	 * 作    者： yiming.zhang
	 * 日    期： Dec 4, 2013-10:40:17 PM
	 * @param user
	 * @param request
	 * @param response
	 * @param dataGrid 
	 * 返回类型： void
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
			dcDepart.add(Restrictions.eq("tsDepart.id", departid));
            // 这种方式也是可以的
//            DetachedCriteria dcDepart = dc.createAlias("userOrgList", "userOrg");
//            dcDepart.add(Restrictions.eq("userOrg.tsDepart.id", departid));
		}
		Short[] userstate = new Short[] { Globals.User_Normal, Globals.User_ADMIN };
		cq.in("status", userstate);
		cq.add();
		this.systemService.findDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

    /**
     * 获取机构树-combotree
     * @param request
     * @return
     */
    @RequestMapping(params = "getOrgTree")
    @ResponseBody
    public List<ComboTree> getOrgTree(HttpServletRequest request) {
        List<Org> departsList = systemService.findByHql("from Depart where TSPDepart.id is null");
        ComboTreeModel comboTreeModel = new ComboTreeModel("id", "departname", "Departs");
		List<ComboTree> comboTrees = resourceService.ComboTree(departsList, comboTreeModel, null, true);
        return comboTrees;
    }
    /**
     * 添加 用户到组织机构 的页面  跳转
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
        subCq.setProjection(Property.forName("tsUser.id"));
        subCq.eq("tsDepart.id", orgId);
        subCq.add();

        cq.add(Property.forName("id").notIn(subCq.getDetachedCriteria()));
        cq.add();

        this.systemService.findDataGridReturn(cq, true);
        TagUtil.datagrid(response, dataGrid);
    }
    /**
     * 添加 用户到组织机构
     * @param req request
     * @return 处理结果信息
     */
    @RequestMapping(params = "doAddUserToOrg")
    @ResponseBody
    public AjaxJson doAddUserToOrg(HttpServletRequest req) {
        AjaxJson j = new AjaxJson();
        Org depart = systemService.findEntity(Org.class, req.getParameter("orgId"));
        saveOrgUserList(req, depart);
        message =  MutiLangUtils.paramAddSuccess("common.user");
        j.setMsg(message);
        return j;
    }
    /**
     * 保存 组织机构-用户 关系信息
     * @param request request
     * @param depart depart
     */
    private void saveOrgUserList(HttpServletRequest request, Org depart) {
        String orgIds = ConvertUtils.getString(request.getParameter("userIds"));

        List<UserOrg> userOrgList = new ArrayList<UserOrg>();
        List<String> userIdList = extractIdListByComma(orgIds);
        for (String userId : userIdList) {
            User user = new User();
            user.setId(userId);

            UserOrg userOrg = new UserOrg();
            userOrg.setUser(user);
            userOrg.setOrg(depart);

            userOrgList.add(userOrg);
        }
        if (!userOrgList.isEmpty()) {
            systemService.batchSave(userOrgList);
        }
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
        this.systemService.findDataGridReturn(cq, true);
        TagUtil.datagrid(response, dataGrid);
    }
}
