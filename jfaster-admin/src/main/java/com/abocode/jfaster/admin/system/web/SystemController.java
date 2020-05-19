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
 * @Deprecated 该控制类职责不明确
 */
@Scope("prototype")
@Controller
@RequestMapping("/systemController")
@Deprecated
public class SystemController{
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ResourceRepository resourceRepository;
	@Autowired
	private SystemRepository systemRepository;
	@Autowired
	private MutiLangRepository mutiLangRepository;

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
		List<TypeGroup> typeGroupList = systemRepository.findAll(TypeGroup.class);
		request.setAttribute("typegroupList", typeGroupList);
		return new ModelAndView("system/type/typeGroupTabs");
	}

	/**
	 * 类型分组列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "typeGroupList")
	public ModelAndView typeGroupList() {
		return new ModelAndView("system/type/typeGroupList");
	}

	/**
	 * 类型列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "typeList")
	public ModelAndView typeList(HttpServletRequest request) {
		String typeGroupId = request.getParameter("typegroupid");
		TypeGroup typegroup = systemRepository.findEntity(TypeGroup.class, typeGroupId);
		request.setAttribute("typegroup", typegroup);
		return new ModelAndView("system/type/typeList");
	}

	/**
	 * easyuiAJAX请求数据
	 */
	@RequestMapping(params = "typeGroupGrid")
	public void typeGroupGrid(HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid, TypeGroup typegroup) {
		CriteriaQuery cq = new CriteriaQuery(TypeGroup.class, dataGrid);
        String typeGroupName = request.getParameter("typegroupname");
        if(typeGroupName != null && typeGroupName.trim().length() > 0) {
            typeGroupName = typeGroupName.trim();
            List<String> typegroupnameKeyList = systemRepository.findByHql("select typeGroupName from TypeGroup");
            MutiLangUtils.assembleCondition(typegroupnameKeyList, cq, "typeGroupName", typeGroupName);
        }
		this.systemRepository.findDataGridReturn(cq, true);
        MutiLangUtils.setMutiLangValueForList(dataGrid.getResults(), "typeGroupName");
		TagUtil.datagrid(response, dataGrid);
	}


	/**
	 * easyuiAJAX请求数据
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
		cq.like("typeName", typename);
		cq.add();
		this.systemRepository.findDataGridReturn(cq, true);
        MutiLangUtils.setMutiLangValueForList(dataGrid.getResults(), "typeName");
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
			List<Type> typeList = systemRepository.findListByCq(cq, false);
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
                List<String> typegroupnameKeyList = systemRepository.findByHql("select typegroupname from TypeGroup");
                MutiLangUtils.assembleCondition(typegroupnameKeyList, cq, "typegroupname", typegroupname);
            }
            List<TypeGroup> typeGroupList = systemRepository.findListByCq(cq, false);
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
		String message;
		if (id.startsWith("G")) {//分组
			TypeGroup typegroup = systemRepository.findEntity(TypeGroup.class, id.substring(1));
			message = "数据字典分组: " + mutiLangRepository.getLang(typegroup.getTypeGroupName()) + "被删除 成功";
			systemRepository.delete(typegroup);
		} else {
			Type type = systemRepository.findEntity(Type.class, id.substring(1));
			message = "数据字典类型: " + mutiLangRepository.getLang(type.getTypeName()) + "被删除 成功";
			systemRepository.delete(type);
		}
		systemRepository.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		//刷新缓存
		systemRepository.refleshTypeGroupCach();
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
		typegroup = systemRepository.findEntity(TypeGroup.class, typegroup.getId());
		String message = "类型分组: " + mutiLangRepository.getLang(typegroup.getTypeGroupName()) + " 被删除 成功";
        if (StringUtils.isEmpty(typegroup.getTypes())) {
            systemRepository.delete(typegroup);
            systemRepository.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
            //刷新缓存
            systemRepository.refleshTypeGroupCach();
        } else {
            message = "类型分组: " + mutiLangRepository.getLang(typegroup.getTypeGroupName()) + " 下有类型信息，不能删除！";
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
		type = systemRepository.findEntity(Type.class, type.getId());
		String message;
		if(!StringUtils.isNotEmpty(type)){
			message="已经被删除了";
			j.setMsg(message);
			j.setSuccess(false);
			return  j;
		}
		message = "类型: " + mutiLangRepository.getLang(type.getTypeName()) + "被删除 成功";
		systemRepository.delete(type);
		//刷新缓存
		systemRepository.refleshTypesCach(type);
		systemRepository.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
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
		List<TypeGroup> typegroups= systemRepository.findAllByProperty(TypeGroup.class,"typegroupcode",typegroupcode);
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
		String message;
		if (StringUtils.isNotEmpty(typegroup.getId())) {
			message = "类型分组: " + mutiLangRepository.getLang(typegroup.getTypeGroupName()) + "被更新成功";
			userRepository.saveOrUpdate(typegroup);
			systemRepository.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} else {
			message = "类型分组: " + mutiLangRepository.getLang(typegroup.getTypeGroupName()) + "被添加成功";
			userRepository.save(typegroup);
			systemRepository.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		//刷新缓存
		systemRepository.refleshTypeGroupCach();
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
		List<Object> types = this.systemRepository.findByHql(hql.toString());
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
		String message;
		if (StringUtils.isNotEmpty(type.getId())) {
			message = "类型: " + mutiLangRepository.getLang(type.getTypeName()) + "被更新成功";
			userRepository.saveOrUpdate(type);
			systemRepository.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} else {
			message = "类型: " + mutiLangRepository.getLang(type.getTypeName()) + "被添加成功";
			userRepository.save(type);
			systemRepository.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		//刷新缓存
		systemRepository.refleshTypesCach(type);
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
			typegroup = systemRepository.findEntity(TypeGroup.class, typegroup.getId());
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
        TypeGroup typegroup = systemRepository.findUniqueByProperty(TypeGroup.class, "id", typegroupid);
        String typegroupname = typegroup.getTypeGroupName();
        req.setAttribute("typegroupname", mutiLangRepository.getLang(typegroupname));
		if (StringUtils.isNotEmpty(type.getId())) {
			type = systemRepository.findEntity(Type.class, type.getId());
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
		this.systemRepository.findDataGridReturn(cq, true);
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
		depart = systemRepository.findEntity(Org.class, depart.getId());
		String message = "部门: " + depart.getOrgName() + "被删除 成功";
		systemRepository.delete(depart);
		systemRepository.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);

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
		String message;
		if (StringUtils.isNotEmpty(depart.getId())) {
			userRepository.saveOrUpdate(depart);
            message = MutiLangUtils.paramUpdSuccess("common.department");
            systemRepository.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);

		} else {
            String orgCode = systemRepository.generateOrgCode(depart.getId(), pid);
            depart.setOrgCode(orgCode);
			userRepository.save(depart);
            message = MutiLangUtils.paramAddSuccess("common.department");
            systemRepository.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);

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
		List<Org> departList = systemRepository.getList(Org.class);
		req.setAttribute("departList", departList);
		if (depart.getId() != null) {
			depart = systemRepository.findEntity(Org.class, depart.getId());
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
		List<Org> departsList = systemRepository.findListByCq(cq, false);
		List<ComboTree> comboTrees = resourceRepository.comTree(departsList, comboTree);
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
		this.systemRepository.findDataGridReturn(cq, true);
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
		String message = "角色: " + role.getRoleName() + "被删除成功";
		AjaxJson j = new AjaxJson();
		role = systemRepository.findEntity(Role.class, role.getId());
		userRepository.delete(role);
		systemRepository.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
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
		String message;
		if (role.getId() != null) {
			message = "角色: " + role.getRoleName() + "被更新成功";
			userRepository.saveOrUpdate(role);
			systemRepository.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} else {
			message = "角色: " + role.getRoleName() + "被添加成功";
			userRepository.saveOrUpdate(role);
			systemRepository.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
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
		List<Function> functionList = systemRepository.findListByCq(cq, false);
		List<ComboTree> comboTrees = new ArrayList<ComboTree>();
		String  roleId =request.getParameter("roleid");
		List<Function> loginActionList=this.systemRepository.getFucntionList(roleId);
		ComboTreeModel comboTreeModel = new ComboTreeModel("id", "functionName", "Functions");
		comboTrees = resourceRepository.ComboTree(functionList, comboTreeModel, loginActionList, false);
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
		Role role = this.systemRepository.find(Role.class, roleid);
		List<RoleFunction> roleFunctionList = systemRepository.findAllByProperty(RoleFunction.class, "role.id", role.getId());
		systemRepository.deleteEntities(roleFunctionList);
		if (!StringUtils.isEmpty(rolefunction)) {
			String[] roleFunctions  = rolefunction.split(",");
			for (String s : roleFunctions) {
				RoleFunction rf = new RoleFunction();
				Function f = this.systemRepository.find(Function.class, Integer.valueOf(s));
				rf.setFunction(f);
				rf.setRole(role);
				this.systemRepository.save(rf);
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
			role = systemRepository.findEntity(Role.class, role.getId());
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
		List<Function> functionList = systemRepository.findListByCq(cq, false);
		List<TreeGrid> treeGrids = new ArrayList<TreeGrid>();
		Collections.sort(functionList, new FunctionComparator());
		TreeGridModel treeGridModel = new TreeGridModel();
		treeGridModel.setRoleid(roleid);
		treeGrids = resourceRepository.treegrid(functionList, treeGridModel);
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
