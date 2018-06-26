package com.abocode.jfaster.web.system.interfaces.web;
import com.abocode.jfaster.core.common.model.json.AjaxJson;
import com.abocode.jfaster.core.common.model.json.ComboTree;
import com.abocode.jfaster.core.common.model.json.DataGrid;
import com.abocode.jfaster.core.common.model.json.TreeGrid;
import com.abocode.jfaster.core.platform.view.interactions.easyui.ComboTreeModel;
import com.abocode.jfaster.core.common.util.ConvertUtils;
import com.abocode.jfaster.core.common.constants.Globals;
import com.abocode.jfaster.core.common.util.MutiLangUtils;
import com.abocode.jfaster.web.common.hqlsearch.HqlGenerateUtil;
import com.abocode.jfaster.web.system.domain.entity.Depart;
import com.abocode.jfaster.web.system.domain.entity.User;
import com.abocode.jfaster.web.system.domain.entity.UserOrg;
import com.abocode.jfaster.web.system.domain.repository.DepartService;
import com.abocode.jfaster.web.system.domain.repository.ResourceService;
import com.abocode.jfaster.web.system.domain.repository.SystemService;
import com.abocode.jfaster.web.system.domain.repository.UserService;
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
	private DepartService departService;
	@Autowired
	private UserService userService;
	@Autowired
	private SystemService systemService;
	private String message;
	@Autowired
	private ResourceService resourceService;
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Autowired
	public void setSystemService(SystemService systemService) {
		this.systemService = systemService;
	}

	public UserService getUserService() {
		return userService;
	}

	@Autowired
	public void setUserService(UserService userService) {
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
		CriteriaQuery cq = new CriteriaQuery(Depart.class, dataGrid);
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
	public AjaxJson del(Depart depart, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		depart = systemService.findEntity(Depart.class, depart.getId());
        message = MutiLangUtils.paramDelSuccess("common.department");
        if (depart.getTSDeparts().size() == 0) {
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
	public AjaxJson save(Depart depart, HttpServletRequest request) {
		// 设置上级部门
		String pid = request.getParameter("TSPDepart.id");
		if (pid.equals("")) {
			depart.setTSPDepart(null);
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
	public ModelAndView add(Depart depart, HttpServletRequest req) {
		List<Depart> departList = systemService.getList(Depart.class);
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
	public ModelAndView update(Depart depart, HttpServletRequest req) {
		List<Depart> departList = systemService.getList(Depart.class);
		req.setAttribute("departList", departList);
		if (!StringUtils.isEmpty(depart.getId())) {
			depart = systemService.findEntity(Depart.class, depart.getId());
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
		CriteriaQuery cq = new CriteriaQuery(Depart.class);
		if(null != request.getParameter("selfId")){
			cq.notEq("id", request.getParameter("selfId"));
		}
		if (comboTree.getId() != null) {
			cq.eq("TSPDepart.id", comboTree.getId());
		}
		if (comboTree.getId() == null) {
			cq.isNull("TSPDepart");
		}
		cq.add();
		List<Depart> departsList = systemService.findListByCq(cq, false);
		List<ComboTree> comboTrees = new ArrayList<ComboTree>();
		ComboTreeModel comboTreeModel = new ComboTreeModel("id", "departname", "TSDeparts");
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
	public Object departgrid(Depart tSDepart, HttpServletRequest request, HttpServletResponse response, TreeGrid treegrid) {
		CriteriaQuery cq = new CriteriaQuery(Depart.class);
		if("yes".equals(request.getParameter("isSearch"))){
			treegrid.setId(null);
			tSDepart.setId(null);
		} 
		if(null != tSDepart.getDepartname()){
			HqlGenerateUtil.installHql(cq, tSDepart);
		}
		if (treegrid.getId() != null) {
			cq.eq("TSPDepart.id", treegrid.getId());
		}
		if (treegrid.getId() == null) {
			cq.isNull("TSPDepart");
		}
		cq.add();


		List<TreeGrid> departList =systemService.findListByCq(cq, false);
		if(departList.size()==0&&tSDepart.getDepartname()!=null){ 
			cq = new CriteriaQuery(Depart.class);
			Depart parDepart = new Depart();
			tSDepart.setTSPDepart(parDepart);
			HqlGenerateUtil.installHql(cq, tSDepart);
		    departList =systemService.findListByCq(cq, false);
		}

		TreeGridModel treeGridModel = new TreeGridModel();
		treeGridModel.setTextField("departname");
		treeGridModel.setParentText("TSPDepart_departname");
		treeGridModel.setParentId("TSPDepart_id");
		treeGridModel.setSrc("description");
		treeGridModel.setIdField("id");
		treeGridModel.setChildList("TSDeparts");
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
        List<Depart> departsList = systemService.findByHql("from Depart where TSPDepart.id is null");
        ComboTreeModel comboTreeModel = new ComboTreeModel("id", "departname", "TSDeparts");
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
        Depart depart = systemService.findEntity(Depart.class, req.getParameter("orgId"));
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
    private void saveOrgUserList(HttpServletRequest request, Depart depart) {
        String orgIds = ConvertUtils.getString(request.getParameter("userIds"));

        List<UserOrg> userOrgList = new ArrayList<UserOrg>();
        List<String> userIdList = extractIdListByComma(orgIds);
        for (String userId : userIdList) {
            User user = new User();
            user.setId(userId);

            UserOrg userOrg = new UserOrg();
            userOrg.setTsUser(user);
            userOrg.setTsDepart(depart);

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
        CriteriaQuery cq = new CriteriaQuery(Depart.class, dataGrid);
        this.systemService.findDataGridReturn(cq, true);
        TagUtil.datagrid(response, dataGrid);
    }
}
