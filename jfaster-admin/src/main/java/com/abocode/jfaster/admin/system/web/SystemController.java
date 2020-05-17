package com.abocode.jfaster.admin.system.web;

import com.abocode.jfaster.core.common.model.json.*;
import com.abocode.jfaster.core.extend.hqlsearch.parse.ObjectParseUtil;
import com.abocode.jfaster.admin.system.dto.bean.ClientBean;
import com.abocode.jfaster.core.persistence.hibernate.qbc.CriteriaQuery;
import com.abocode.jfaster.core.extend.hqlsearch.parse.PageValueConvertRuleEnum;
import com.abocode.jfaster.core.extend.hqlsearch.parse.vo.HqlRuleEnum;
import com.abocode.jfaster.core.platform.view.interactions.easyui.ComboTreeModel;
import com.abocode.jfaster.core.platform.view.interactions.easyui.TreeGridModel;
import com.abocode.jfaster.core.common.util.ConvertUtils;
import com.abocode.jfaster.core.platform.view.widgets.easyui.TagUtil;
import com.abocode.jfaster.core.common.constants.Globals;
import com.abocode.jfaster.core.common.util.MutiLangUtils;
import com.abocode.jfaster.core.web.manager.ClientManager;
import com.abocode.jfaster.core.web.manager.ClientSort;
import com.abocode.jfaster.admin.system.repository.MutiLangRepository;
import com.abocode.jfaster.admin.system.repository.ResourceRepository;
import com.abocode.jfaster.admin.system.repository.SystemRepository;
import com.abocode.jfaster.admin.system.repository.UserRepository;
import com.abocode.jfaster.core.common.util.FunctionComparator;
import com.abocode.jfaster.core.common.util.StringUtils;
import com.abocode.jfaster.system.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 类型字段处理类
 * 
 * @author 张代浩
 * 
 */
@Scope("prototype")
@Controller
@RequestMapping("/systemController")
public class SystemController{
	private UserRepository userService;
	@Autowired
	private ResourceRepository resourceService;
	private SystemRepository systemService;
	private MutiLangRepository mutiLangService;
	
	private String message;

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

	@Autowired
	public void setMutiLangService(MutiLangRepository mutiLangService) {
		this.mutiLangService = mutiLangService;
	}

	public UserRepository getUserService() {
		return userService;
	}

	@Autowired
	public void setUserService(UserRepository userService) {
		this.userService = userService;
	}
	@RequestMapping(params = "druid")
	public ModelAndView druid() {
		return new ModelAndView(new RedirectView("druid/index.html"));
	}
	/**
	 * 类型字典列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "typeGroupTabs")
	public ModelAndView typeGroupTabs(HttpServletRequest request) {
		List<TypeGroup> typegroupList = systemService.findAll(TypeGroup.class);
		request.setAttribute("typegroupList", typegroupList);
		return new ModelAndView("system/type/typeGroupTabs");
	}

	/**
	 * 类型分组列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "typeGroupList")
	public ModelAndView typeGroupList(HttpServletRequest request) {
		return new ModelAndView("system/type/typeGroupList");
	}

	/**
	 * 类型列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "typeList")
	public ModelAndView typeList(HttpServletRequest request) {
		String typegroupid = request.getParameter("typegroupid");
		TypeGroup typegroup = systemService.findEntity(TypeGroup.class, typegroupid);
		request.setAttribute("typegroup", typegroup);
		return new ModelAndView("system/type/typeList");
	}

	/**
	 * easyuiAJAX请求数据
	 */

	@RequestMapping(params = "typeGroupGrid")
	public void typeGroupGrid(HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid, TypeGroup typegroup) {
		CriteriaQuery cq = new CriteriaQuery(TypeGroup.class, dataGrid);
        String typegroupname = request.getParameter("typegroupname");
        if(typegroupname != null && typegroupname.trim().length() > 0) {
            typegroupname = typegroupname.trim();
            List<String> typegroupnameKeyList = systemService.findByHql("select typegroupname from TypeGroup");
            MutiLangUtils.assembleCondition(typegroupnameKeyList, cq, "typegroupname", typegroupname);
        }
		this.systemService.findDataGridReturn(cq, true);
        MutiLangUtils.setMutiLangValueForList(dataGrid.getResults(), "typegroupname");
		TagUtil.datagrid(response, dataGrid);
	}


	/**
	 * easyuiAJAX请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 */

	@RequestMapping(params = "typeGrid")
	public void typeGrid(HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		String typegroupid = request.getParameter("typegroupid");
		String typename = request.getParameter("typename");
		CriteriaQuery cq = new CriteriaQuery(Type.class, dataGrid);
		cq.eq("typeGroup.id", typegroupid);
		cq.like("typename", typename);
		cq.add();
		this.systemService.findDataGridReturn(cq, true);
        MutiLangUtils.setMutiLangValueForList(dataGrid.getResults(), "typename");
		TagUtil.datagrid(response, dataGrid);
	}

    /**
     * 跳转到类型页面
     * @param request request
     * @return
     */
	@RequestMapping(params = "goTypeGrid")
	public ModelAndView goTypeGrid(HttpServletRequest request) {
		String typegroupid = request.getParameter("typegroupid");
        request.setAttribute("typegroupid", typegroupid);
		return new ModelAndView("system/type/typeListForTypegroup");
	}

	@RequestMapping(params = "typeGridTree")
	@ResponseBody
    @Deprecated
	public List<TreeGrid> typeGridTree(HttpServletRequest request, TreeGrid treegrid) {
		CriteriaQuery cq;
		List<TreeGrid> treeGrids = new ArrayList<TreeGrid>();
		if (treegrid.getId() != null) {
			cq = new CriteriaQuery(Type.class);
			cq.eq("typeGroup.id", treegrid.getId().substring(1));
			cq.add();
			List<Type> typeList = systemService.findListByCq(cq, false);
			for (Type obj : typeList) {
				TreeGrid treeNode = new TreeGrid();
				treeNode.setId("T"+obj.getId());
				treeNode.setText(obj.getTypeName());
				treeNode.setCode(obj.getTypeCode());
				treeGrids.add(treeNode);
			}
		} else {
			cq = new CriteriaQuery(TypeGroup.class);
            String typegroupcode = request.getParameter("typegroupcode");
            if(typegroupcode != null ) {
                HqlRuleEnum rule = PageValueConvertRuleEnum
						.convert(typegroupcode);
                Object value = PageValueConvertRuleEnum.replaceValue(rule,
                		typegroupcode);
				ObjectParseUtil.addCriteria(cq, "typegroupcode", rule, value);
                cq.add();
            }
            String typegroupname = request.getParameter("typegroupname");
            if(typegroupname != null && typegroupname.trim().length() > 0) {
                typegroupname = typegroupname.trim();
                List<String> typegroupnameKeyList = systemService.findByHql("select typegroupname from TypeGroup");
                MutiLangUtils.assembleCondition(typegroupnameKeyList, cq, "typegroupname", typegroupname);
            }
            List<TypeGroup> typeGroupList = systemService.findListByCq(cq, false);
			for (TypeGroup obj : typeGroupList) {
				TreeGrid treeNode = new TreeGrid();
				treeNode.setId("G"+obj.getId());
				treeNode.setText(obj.getTypeGroupName());
				treeNode.setCode(obj.getTypeGroupCode());
				treeNode.setState("closed");
				treeGrids.add(treeNode);
			}
		}
		MutiLangUtils.setMutiTree(treeGrids);
		return treeGrids;
	}

    /**
	 * 删除类型分组或者类型（ID以G开头的是分组）
	 * 
	 * @return
	 */
	@RequestMapping(params = "delTypeGridTree")
	@ResponseBody
	public AjaxJson delTypeGridTree(String id, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		if (id.startsWith("G")) {//分组
			TypeGroup typegroup = systemService.findEntity(TypeGroup.class, id.substring(1));
			message = "数据字典分组: " + mutiLangService.getLang(typegroup.getTypeGroupName()) + "被删除 成功";
			systemService.delete(typegroup);
		} else {
			Type type = systemService.findEntity(Type.class, id.substring(1));
			message = "数据字典类型: " + mutiLangService.getLang(type.getTypeName()) + "被删除 成功";
			systemService.delete(type);
		}
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		//刷新缓存
		systemService.refleshTypeGroupCach();
		j.setMsg(message);
		return j;
	}

	/**
	 * 删除类型分组
	 * 
	 * @return
	 */
	@RequestMapping(params = "delTypeGroup")
	@ResponseBody
	public AjaxJson delTypeGroup(TypeGroup typegroup, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		typegroup = systemService.findEntity(TypeGroup.class, typegroup.getId());
		message = "类型分组: " + mutiLangService.getLang(typegroup.getTypeGroupName()) + " 被删除 成功";
        if (StringUtils.isEmpty(typegroup.getTypes())) {
            systemService.delete(typegroup);
            systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
            //刷新缓存
            systemService.refleshTypeGroupCach();
        } else {
            message = "类型分组: " + mutiLangService.getLang(typegroup.getTypeGroupName()) + " 下有类型信息，不能删除！";
        }
		j.setMsg(message);
		return j;
	}

	/**
	 * 删除类型
	 * 
	 * @return
	 */
	@RequestMapping(params = "delType")
	@ResponseBody
	public AjaxJson delType(Type type, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		type = systemService.findEntity(Type.class, type.getId());
		if(!StringUtils.isNotEmpty(type)){
			message="已经被删除了";
			j.setMsg(message);
			j.setSuccess(false);
			return  j;
		}
		message = "类型: " + mutiLangService.getLang(type.getTypeName()) + "被删除 成功";
		systemService.delete(type);
		//刷新缓存
		systemService.refleshTypesCach(type);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		j.setMsg(message);
		return j;
	}

	/**
	 * 检查分组代码
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "checkTypeGroup")
	@ResponseBody
	public ValidForm checkTypeGroup(HttpServletRequest request) {
		ValidForm v = new ValidForm();
		String typegroupcode=ConvertUtils.getString(request.getParameter("param"));
		String code=ConvertUtils.getString(request.getParameter("code"));
		List<TypeGroup> typegroups=systemService.findAllByProperty(TypeGroup.class,"typegroupcode",typegroupcode);
		if(typegroups.size()>0&&!code.equals(typegroupcode))
		{
			v.setInfo("分组已存在");
			v.setStatus("n");
		}
		return v;
	}
	/**
	 * 添加类型分组
	 * 
	 * @param typegroup
	 * @return
	 */
	@RequestMapping(params = "saveTypeGroup")
	@ResponseBody
	public AjaxJson saveTypeGroup(TypeGroup typegroup, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		if (StringUtils.isNotEmpty(typegroup.getId())) {
			message = "类型分组: " + mutiLangService.getLang(typegroup.getTypeGroupName()) + "被更新成功";
			userService.saveOrUpdate(typegroup);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} else {
			message = "类型分组: " + mutiLangService.getLang(typegroup.getTypeGroupName()) + "被添加成功";
			userService.save(typegroup);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		//刷新缓存
		systemService.refleshTypeGroupCach();
		j.setMsg(message);
		return j;
	}
	/**
	 * 检查类型代码
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "checkType")
	@ResponseBody
	public ValidForm checkType(HttpServletRequest request) {
		ValidForm v = new ValidForm();
		String typecode=ConvertUtils.getString(request.getParameter("param"));
		String code=ConvertUtils.getString(request.getParameter("code"));
		String typeGroupCode=ConvertUtils.getString(request.getParameter("typeGroupCode"));
		StringBuilder hql = new StringBuilder("FROM ").append(Type.class.getName()).append(" AS entity WHERE 1=1 ");
		hql.append(" AND entity.TSTypegroup.typegroupcode =  '").append(typeGroupCode).append("'");
		hql.append(" AND entity.typecode =  '").append(typecode).append("'");
		List<Object> types = this.systemService.findByHql(hql.toString());
		if(types.size()>0&&!code.equals(typecode))
		{
			v.setInfo("类型已存在");
			v.setStatus("n");
		}
		return v;
	}
	/**
	 * 添加类型
	 * 
	 * @param type
	 * @return
	 */
	@RequestMapping(params = "saveType")
	@ResponseBody
	public AjaxJson saveType(Type type, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		if (StringUtils.isNotEmpty(type.getId())) {
			message = "类型: " + mutiLangService.getLang(type.getTypeName()) + "被更新成功";
			userService.saveOrUpdate(type);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} else {
			message = "类型: " + mutiLangService.getLang(type.getTypeName()) + "被添加成功";
			userService.save(type);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		//刷新缓存
		systemService.refleshTypesCach(type);
		j.setMsg(message);
		return j;
	}

	

	/**
	 * 类型分组列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "aouTypeGroup")
	public ModelAndView aouTypeGroup(TypeGroup typegroup, HttpServletRequest req) {
		if (typegroup.getId() != null) {
			typegroup = systemService.findEntity(TypeGroup.class, typegroup.getId());
			req.setAttribute("typeGroupView", typegroup);
		}
		return new ModelAndView("system/type/typegroup");
	}

	/**
	 * 类型列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "addorupdateType")
	public ModelAndView addorupdateType(Type type, HttpServletRequest req) {
		String typegroupid = req.getParameter("typegroupid");
		req.setAttribute("typegroupid", typegroupid);
        TypeGroup typegroup = systemService.findUniqueByProperty(TypeGroup.class, "id", typegroupid);
        String typegroupname = typegroup.getTypeGroupName();
        req.setAttribute("typegroupname", mutiLangService.getLang(typegroupname));
		if (StringUtils.isNotEmpty(type.getId())) {
			type = systemService.findEntity(Type.class, type.getId());
			req.setAttribute("typeView", type);
		}
		return new ModelAndView("system/type/type");
	}

	/*
	 * *****************部门管理操作****************************
	 */

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

	@RequestMapping(params = "datagridDepart")
	public void datagridDepart(HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(Org.class, dataGrid);
		this.systemService.findDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
		;
	}

	/**
	 * 删除部门
	 * 
	 * @return
	 */
	@RequestMapping(params = "delDepart")
	@ResponseBody
	public AjaxJson delDepart(Org depart, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		depart = systemService.findEntity(Org.class, depart.getId());
		message = "部门: " + depart.getOrgName() + "被删除 成功";
		systemService.delete(depart);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);

		return j;
	}

	/**
	 * 添加部门
	 * 
	 * @param depart
	 * @return
	 */
	@RequestMapping(params = "saveDepart")
	@ResponseBody
	public AjaxJson saveDepart(Org depart, HttpServletRequest request) {
		// 设置上级部门
		String pid = request.getParameter("parentOrg.id");
		if (pid.equals("")) {
			depart.setParentOrg(null);
		}
		AjaxJson j = new AjaxJson();
		if (StringUtils.isNotEmpty(depart.getId())) {
			userService.saveOrUpdate(depart);
            message = MutiLangUtils.paramUpdSuccess("common.department");
            systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);

		} else {
            String orgCode = systemService.generateOrgCode(depart.getId(), pid);
            depart.setOrgCode(orgCode);
			userService.save(depart);
            message = MutiLangUtils.paramAddSuccess("common.department");
            systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);

        }
		j.setMsg(message);
		return j;
	}

	/**
	 * 部门列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "addorupdateDepart")
	public ModelAndView addorupdateDepart(Org depart, HttpServletRequest req) {
		List<Org> departList = systemService.getList(Org.class);
		req.setAttribute("departList", departList);
		if (depart.getId() != null) {
			depart = systemService.findEntity(Org.class, depart.getId());
			req.setAttribute("departView", depart);
		}
		return new ModelAndView("system/depart/depart");
	}

	/**
	 * 父级权限列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "setPFunction")
	@ResponseBody
	public List<ComboTree> setPFunction(HttpServletRequest request, ComboTree comboTree) {
		CriteriaQuery cq = new CriteriaQuery(Org.class);
		if (StringUtils.isNotEmpty(comboTree.getId())) {
			cq.eq("parentOrg.id", comboTree.getId());
		}
		if (StringUtils.isEmpty(comboTree.getId())) {
			cq.isNull("parentOrg.id");
		}
		cq.add();
		List<Org> departsList = systemService.findListByCq(cq, false);
		List<ComboTree> comboTrees = resourceService.comTree(departsList, comboTree);
		return comboTrees;

	}

	/*
	 * *****************角色管理操作****************************
	 */
	/**
	 * 角色列表页面跳转
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

	@RequestMapping(params = "datagridRole")
	public void datagridRole(HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(Role.class, dataGrid);
		this.systemService.findDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除角色
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "delRole")
	@ResponseBody
	public AjaxJson delRole(Role role, String ids, HttpServletRequest request) {
		message = "角色: " + role.getRoleName() + "被删除成功";
		AjaxJson j = new AjaxJson();
		role = systemService.findEntity(Role.class, role.getId());
		userService.delete(role);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		j.setMsg(message);
		return j;
	}

	/**
	 * 角色录入
	 * 
	 * @param role
	 * @return
	 */
	@RequestMapping(params = "saveRole")
	@ResponseBody
	public AjaxJson saveRole(Role role, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		if (role.getId() != null) {
			message = "角色: " + role.getRoleName() + "被更新成功";
			userService.saveOrUpdate(role);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} else {
			message = "角色: " + role.getRoleName() + "被添加成功";
			userService.saveOrUpdate(role);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 角色列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "fun")
	public ModelAndView fun(HttpServletRequest request) {
		Integer roleid = ConvertUtils.getInt(request.getParameter("roleid"), 0);
		request.setAttribute("roleid", roleid);
		return new ModelAndView("system/role/roleList");
	}

	/**
	 * 设置权限
	 * 
	 * @param role
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "setAuthority")
	@ResponseBody
	public List<ComboTree> setAuthority(Role role, HttpServletRequest request, ComboTree comboTree) {
		CriteriaQuery cq = new CriteriaQuery(Function.class);
		if (comboTree.getId() != null) {
			cq.eq("TFunction.functionid", ConvertUtils.getInt(comboTree.getId(), 0));
		}
		if (comboTree.getId() == null) {
			cq.isNull("TFunction");
		}
		cq.add();
		List<Function> functionList = systemService.findListByCq(cq, false);
		List<ComboTree> comboTrees = new ArrayList<ComboTree>();
		String  roleId =request.getParameter("roleid");
		List<Function> loginActionList=this.systemService.getFucntionList(roleId);
		ComboTreeModel comboTreeModel = new ComboTreeModel("id", "functionName", "Functions");
		comboTrees = resourceService.ComboTree(functionList, comboTreeModel, loginActionList, false);
		return comboTrees;
	}

	/**
	 * 更新权限
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "updateAuthority")
	public String updateAuthority(HttpServletRequest request) {
		Integer roleid = ConvertUtils.getInt(request.getParameter("roleid"), 0);
		String rolefunction = request.getParameter("rolefunctions");
		Role role = this.systemService.find(Role.class, roleid);
		List<RoleFunction> roleFunctionList = systemService.findAllByProperty(RoleFunction.class, "role.id", role.getId());
		systemService.deleteEntities(roleFunctionList);
		if (!StringUtils.isEmpty(rolefunction)) {
			String[] roleFunctions  = rolefunction.split(",");
			for (String s : roleFunctions) {
				RoleFunction rf = new RoleFunction();
				Function f = this.systemService.find(Function.class, Integer.valueOf(s));
				rf.setFunction(f);
				rf.setRole(role);
				this.systemService.save(rf);
			}
		}
		return "system/role/roleList";
	}

	/**
	 * 角色页面跳转
	 * 
	 * @param role
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "addorupdateRole")
	public ModelAndView addorupdateRole(Role role, HttpServletRequest req) {
		if (role.getId() != null) {
			role = systemService.findEntity(Role.class, role.getId());
			req.setAttribute("roleView", role);
		}
		return new ModelAndView("system/role/role");
	}

	/**
	 * 操作列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "operate")
	public ModelAndView operate(HttpServletRequest request) {
		String roleid = request.getParameter("roleid");
		request.setAttribute("roleid", roleid);
		return new ModelAndView("system/role/functionList");
	}

	/**
	 * 权限操作列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "setOperate")
	@ResponseBody
	public List<TreeGrid> setOperate(HttpServletRequest request, TreeGrid treegrid) {
		String roleid = request.getParameter("roleid");
		CriteriaQuery cq = new CriteriaQuery(Function.class);
		if (treegrid.getId() != null) {
			cq.eq("TFunction.functionid", ConvertUtils.getInt(treegrid.getId(), 0));
		}
		if (treegrid.getId() == null) {
			cq.isNull("TFunction");
		}
		cq.add();
		List<Function> functionList = systemService.findListByCq(cq, false);
		List<TreeGrid> treeGrids = new ArrayList<TreeGrid>();
		Collections.sort(functionList, new FunctionComparator());
		TreeGridModel treeGridModel = new TreeGridModel();
		treeGridModel.setRoleid(roleid);
		treeGrids = resourceService.treegrid(functionList, treeGridModel);
		return treeGrids;
	}
	/**
	 * 在线用户列表
	 * @param request
	 * @param response
	 * @param dataGrid
	 */

	@RequestMapping(params = "datagridOnline")
	public void datagridOnline(ClientBean tSOnline, HttpServletRequest request,
							   HttpServletResponse response, DataGrid dataGrid) {
		List<ClientBean> onlines = new ArrayList<ClientBean>();
		onlines.addAll(ClientManager.getInstance().getAllClient());
		dataGrid.setTotal(onlines.size());
		dataGrid.setResults(getClinetList(onlines,dataGrid));
		TagUtil.datagrid(response, dataGrid);
	}
	/**
	 * 获取当前页面的用户列表
	 * @param onlines
	 * @param dataGrid
	 * @return
	 */
	private List<ClientBean> getClinetList(List<ClientBean> onlines, DataGrid dataGrid) {
		Collections.sort(onlines, new ClientSort());
		List<ClientBean> result = new ArrayList<ClientBean>();
		for(int i = (dataGrid.getPage()-1)*dataGrid.getRows();
				i<onlines.size()&&i<dataGrid.getPage()*dataGrid.getRows();i++){
			result.add(onlines.get(i));
		}
		return result;
	}


}
