package com.abocode.jfaster.web.system.web;

import com.abocode.jfaster.core.common.model.json.AjaxJson;
import com.abocode.jfaster.core.common.model.json.DataGrid;
import com.abocode.jfaster.platform.view.interactions.datatable.SortDirection;
import com.abocode.jfaster.platform.view.interactions.easyui.ComboTreeModel;
import com.abocode.jfaster.core.common.util.ConvertUtils;
import com.abocode.jfaster.core.common.constants.Globals;
import com.abocode.jfaster.core.common.util.MutiLangUtils;
import com.abocode.jfaster.web.system.domain.repository.ResourceService;
import com.abocode.jfaster.web.system.domain.repository.SystemService;
import com.abocode.jfaster.core.common.util.BeanToTagUtils;
import com.abocode.jfaster.web.system.domain.entity.*;
import com.abocode.jfaster.core.persistence.hibernate.qbc.CriteriaQuery;
import com.abocode.jfaster.core.common.model.json.ComboTree;
import com.abocode.jfaster.core.common.model.json.TreeGrid;
import com.abocode.jfaster.platform.view.interactions.easyui.TreeGridModel;
import com.abocode.jfaster.web.system.interfaces.view.FunctionView;
import com.abocode.jfaster.platform.view.widgets.easyui.TagUtil;
import com.abocode.jfaster.web.system.domain.repository.UserService;
import com.abocode.jfaster.core.common.util.NumberComparator;
import com.abocode.jfaster.core.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;

/**
 * 菜单权限处理类
 * 
 * @author 张代浩
 * 
 */
@Scope("prototype")
@Controller
@RequestMapping("/functionController")
public class FunctionController extends BaseController {
	@Autowired
	private ResourceService resourceService;
	private UserService userService;
	private SystemService systemService;
	private String message = null;

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
	 * 权限列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "function")
	public ModelAndView function() {
		return new ModelAndView("system/function/functionList");
	}

	/**
	 * 操作列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "operation")
	public ModelAndView operation(HttpServletRequest request, String functionId) {
		request.setAttribute("functionId", functionId);
		return new ModelAndView("system/operation/operationList");
	}

	/**
	 * 数据规则列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "dataRule")
	public ModelAndView operationData(HttpServletRequest request,
			String functionId) {
		request.setAttribute("functionId", functionId);
		return new ModelAndView("system/dataRule/ruleDataList");
	}

	/**
	 * easyuiAJAX请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 */

	@RequestMapping(params = "datagrid")
	public void datagrid(HttpServletRequest request,
			HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(Function.class, dataGrid);
		this.systemService.findDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * easyuiAJAX请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 */

	@RequestMapping(params = "opdategrid")
	public void opdategrid(HttpServletRequest request,
			HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(Operation.class, dataGrid);
		String functionId = ConvertUtils.getString(request
				.getParameter("functionId"));
		cq.eq("TSFunction.id", functionId);
		cq.add();
		this.systemService.findDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除权限
	 * 
	 * @param function
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(Function function, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		// // 删除权限时先删除权限与角色之间关联表信息
		List<RoleFunction> roleFunctions = systemService.findAllByProperty(RoleFunction.class, "TSFunction.id", function.getId());
		if (roleFunctions.size() > 0) {
			message="菜单已分配无法删除";
		} else {
			function = systemService.findEntity(Function.class, function.getId());
			message = MutiLangUtils.paramDelSuccess("common.menu");
			systemService.updateBySql("delete from t_s_role_function where functionid='"
					+ function.getId() + "'");
			systemService.delete(function);
			userService.delete(function);
			systemService.addLog(message, Globals.Log_Type_DEL,
					Globals.Log_Leavel_INFO);
			systemService.addLog(message, Globals.Log_Type_DEL,
					Globals.Log_Leavel_INFO);
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 删除操作
	 * 
	 * @param operation
	 * @return
	 */
	@RequestMapping(params = "delop")
	@ResponseBody
	public AjaxJson delop(Operation operation, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		operation = systemService.findEntity(Operation.class,
				operation.getId());
		message = MutiLangUtils.paramDelSuccess("common.operation");
		userService.delete(operation);
		systemService.addLog(message, Globals.Log_Type_DEL,
				Globals.Log_Leavel_INFO);
		j.setMsg(message);
		return j;
	}

	/**
	 * 递归更新子菜单的FunctionLevel
	 * @param subFunction
	 * @param parent
	 */
	private void updateSubFunction(List<Function> subFunction, Function parent) {
		if(subFunction.size() ==0){//没有子菜单是结束
              return;
       }else{
    	   for (Function tsFunction : subFunction) {
				tsFunction.setFunctionLevel(Short.valueOf(parent.getFunctionLevel()
						+ 1 + ""));
				systemService.saveOrUpdate(tsFunction);
				List<Function> subFunction1 = systemService.findAllByProperty(Function.class, "TSFunction.id", tsFunction.getId());
				updateSubFunction(subFunction1,tsFunction);
		   }
       }
	}
	
	/**
	 * 权限录入
	 * 
	 * @param function
	 * @return
	 */
	@RequestMapping(params = "saveFunction")
	@ResponseBody
	public AjaxJson saveFunction(Function function, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		String functionOrder = function.getFunctionOrder();
		if (StringUtils.isEmpty(functionOrder)) {
			function.setFunctionOrder("0");
		}
		
		if (function.getTSFunction().getId().equals("")) {
			function.setTSFunction(null);
		} else {
			Function parent = systemService.findEntity(Function.class,
					function.getTSFunction().getId());
			function.setFunctionLevel(Short.valueOf(parent.getFunctionLevel()
					+ 1 + ""));
		}
		if (StringUtils.isNotEmpty(function.getId())) {
			message = MutiLangUtils.paramUpdSuccess("common.menu");
			userService.saveOrUpdate(function);
			systemService.addLog(message, Globals.Log_Type_UPDATE,
					Globals.Log_Leavel_INFO);
			List<Function> subFunction = systemService.findAllByProperty(Function.class, "TSFunction.id", function.getId());
			updateSubFunction(subFunction,function);
			systemService.flushRoleFunciton(function.getId(), function);
		} else {
			if (function.getFunctionLevel().equals(Globals.Function_Leave_ONE)) {
			/*	List<TSFunction> functionList = systemService.findAllByProperty(
						TSFunction.class, "functionLevel",
						Globals.Function_Leave_ONE);
				 int ordre=functionList.size()+1;
				 function.setFunctionOrder(Globals.Function_Order_ONE+ordre);*/
				function.setFunctionOrder(function.getFunctionOrder());
			} else {
			/*	List<TSFunction> functionList = systemService.findAllByProperty(
						TSFunction.class, "functionLevel",
						Globals.Function_Leave_TWO);
				 int ordre=functionList.size()+1;
				 function.setFunctionOrder(Globals.Function_Order_TWO+ordre);*/
				function.setFunctionOrder(function.getFunctionOrder());
			}
			message = MutiLangUtils.paramAddSuccess("common.menu");
			systemService.save(function);
			systemService.addLog(message, Globals.Log_Type_INSERT,
					Globals.Log_Leavel_INFO);
		}

		j.setMsg(message);
		return j;
	}

	/**
	 * 操作录入
	 * 
	 * @param operation
	 * @return
	 */
	@RequestMapping(params = "saveop")
	@ResponseBody
	public AjaxJson saveop(Operation operation, HttpServletRequest request) {
		String pid = request.getParameter("TSFunction.id");
		if (pid.equals("")) {
			operation.setTSFunction(null);
		}
		AjaxJson j = new AjaxJson();
		if (StringUtils.isNotEmpty(operation.getId())) {
			message = MutiLangUtils.paramUpdSuccess("common.operation");
			userService.saveOrUpdate(operation);
			systemService.addLog(message, Globals.Log_Type_UPDATE,
					Globals.Log_Leavel_INFO);
		} else {
			message = MutiLangUtils.paramAddSuccess("common.operation");
			userService.save(operation);
			systemService.addLog(message, Globals.Log_Type_INSERT,
					Globals.Log_Leavel_INFO);
		}

		j.setMsg(message);
		return j;
	}

	/**
	 * 权限列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(Function function, HttpServletRequest req) {
		String functionId = req.getParameter("id");
		List<Function> functionList = systemService
				.getList(Function.class);
		req.setAttribute("flist", functionList);
		List<Icon> iconList = systemService
				.findByHql("from Icon where iconType != 3");
		req.setAttribute("iconlist", iconList);
		List<Icon> iconDeskList = systemService
				.findByHql("from Icon where iconType = 3");
		req.setAttribute("iconDeskList", iconDeskList);
		if (functionId != null) {
			function = systemService.findEntity(Function.class, functionId);
			req.setAttribute("functionView", function);
		}
		if (function.getTSFunction() != null
				&& function.getTSFunction().getId() != null) {
			function.setFunctionLevel((short) 1);
			function.setTSFunction((Function) systemService.findEntity(
					Function.class, function.getTSFunction().getId()));
			req.setAttribute("functionView", function);
		}
		return new ModelAndView("system/function/function");
	}

	/**
	 * 操作列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "addorupdateop")
	public ModelAndView addorupdateop(Operation operation,
			HttpServletRequest req) {
		List<Icon> iconlist = systemService.getList(Icon.class);
		req.setAttribute("iconlist", iconlist);
		if (operation.getId() != null) {
			operation = systemService.findEntity(Operation.class,
					operation.getId());
			req.setAttribute("operation", operation);
		}
		String functionId = ConvertUtils.getString(req
				.getParameter("functionId"));
		req.setAttribute("functionId", functionId);
		return new ModelAndView("system/operation/operation");
	}

	/**
	 * 权限列表
	 */
	@RequestMapping(params = "functionGrid")
	@ResponseBody
	public List<TreeGrid> functionGrid(HttpServletRequest request,
			TreeGrid treegrid) {
		CriteriaQuery cq = new CriteriaQuery(Function.class);
		String selfId = request.getParameter("selfId");
		if (selfId != null) {
			cq.notEq("id", selfId);
		}
		if (treegrid.getId() != null) {
			cq.eq("TSFunction.id", treegrid.getId());
		}
		if (treegrid.getId() == null) {
			cq.isNull("TSFunction");
		}
		cq.addOrder("functionOrder", SortDirection.asc);
		cq.add();
		List<Function> functionList = systemService.findListByCq(cq, false);
		
		List<FunctionView> functionBeanList= BeanToTagUtils.convertFunctions(functionList);
        Collections.sort(functionBeanList, new NumberComparator());
		TreeGridModel treeGridModel = new TreeGridModel();
		treeGridModel.setIcon("TSIcon_iconPath");
		treeGridModel.setTextField("functionName");
		treeGridModel.setParentText("TSFunction_functionName");
		treeGridModel.setParentId("TSFunction_id");
		treeGridModel.setSrc("functionUrl");
		treeGridModel.setIdField("id");
		treeGridModel.setChildList("TSFunctions");
		// 添加排序字段
		treeGridModel.setOrder("functionOrder");
		treeGridModel.setFunctionType("functionType");

		List<TreeGrid> treeGrids  = resourceService.treegrid(functionList, treeGridModel);
		MutiLangUtils.setMutiTree(treeGrids);
		return treeGrids;
	}

	/**
	 * 权限列表
	 */
	@RequestMapping(params = "functionList")
	@ResponseBody
	public void functionList(HttpServletRequest request,
			HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(Function.class, dataGrid);
		String id = ConvertUtils.getString(request.getParameter("id"));
		cq.isNull("TSFunction");
		if (id != null) {
			cq.eq("TSFunction.id", id);
		}
		cq.add();
		this.systemService.findDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 父级权限下拉菜单
	 */
	@RequestMapping(params = "setPFunction")
	@ResponseBody
	public List<ComboTree> setPFunction(HttpServletRequest request,
			ComboTree comboTree) {
		CriteriaQuery cq = new CriteriaQuery(Function.class);
		if (null != request.getParameter("selfId")) {
			cq.notEq("id", request.getParameter("selfId"));
		}
		if (comboTree.getId() != null) {
			cq.eq("TSFunction.id", comboTree.getId());
		}
		if (comboTree.getId() == null) {
			cq.isNull("TSFunction");
		}
		cq.add();
		List<Function> functionList = systemService.findListByCq(
				cq, false);
		ComboTreeModel comboTreeModel = new ComboTreeModel("id",
				"functionName", "TSFunctions");
		List<ComboTree> comboTrees = resourceService.ComboTree(functionList, comboTreeModel,
				null, false);
		MutiLangUtils.setMutiTree(comboTrees);
		return comboTrees;
	}

	/**
	 * 菜单模糊检索功能
	 * 
	 * @return
	 */
	@RequestMapping(params = "searchApp")
	public ModelAndView searchApp(Function function, HttpServletRequest req) {

		String name = req.getParameter("name");
		String menuListMap = "";
		CriteriaQuery cq = new CriteriaQuery(Function.class);

		cq.notEq("functionLevel", Short.valueOf("0"));
		if (name == null || "".equals(name)) {
			cq.isNull("TSFunction");
		} else {
			String name1 = "%" + name + "%";
			cq.like("functionName", name1);
		}
		cq.add();
		List<Function> functionList = systemService.findListByCq(
				cq, false);
		if (functionList.size() > 0 && functionList != null) {
			for (int i = 0; i < functionList.size(); i++) {
				String icon = "";
				if (!"".equals(functionList.get(i).getTSIconDesk())
						&& functionList.get(i).getTSIconDesk() != null) {
					icon = functionList.get(i).getTSIconDesk().getIconPath();
				} else {
					icon = "plug-in/sliding/icon/default.png";
				}
				menuListMap = menuListMap
						+ "<div type='"
						+ i
						+ 1
						+ "' class='menuSearch_Info' id='"
						+ functionList.get(i).getId()
						+ "' title='"
						+ functionList.get(i).getFunctionName()
						+ "' url='"
						+ functionList.get(i).getFunctionUrl()
						+ "' icon='"
						+ icon
						+ "' style='float:left;left: 0px; top: 0px;margin-left: 30px;margin-top: 20px;'>"
						+ "<div ><img alt='"
						+ functionList.get(i).getFunctionName()
						+ "' src='"
						+ icon
						+ "'></div>"
						+ "<div class='appButton_appName_inner1' style='color:#333333;'>"
						+ functionList.get(i).getFunctionName() + "</div>"
						+ "<div class='appButton_appName_inner_right1'></div>" +
						// "</div>" +
						"</div>";
			}
		} else {
			menuListMap = menuListMap + "很遗憾，在系统中没有检索到与“" + name + "”相关的信息！";
		}
		req.setAttribute("menuListMap", menuListMap);
		return new ModelAndView("system/function/menuAppList");
	}


	/**
	 * 
	 * addorupdaterule 数据规则权限的编辑和新增
	 * 
	 * @Title: addorupdaterule
	 * @param @param operation
	 * @param @param req
	 * @param @return 设定文件
	 * @return ModelAndView 返回类型
	 * @throws
	 */
	@RequestMapping(params = "addorupdaterule")
	public ModelAndView addorupdaterule(DataRule operation,
			HttpServletRequest req) {
		List<Icon> iconlist = systemService.getList(Icon.class);
		req.setAttribute("iconlist", iconlist);
		if (operation.getId() != null) {
			operation = systemService.findEntity(DataRule.class,
					operation.getId());
			req.setAttribute("operationView", operation);
		}
		String functionId = ConvertUtils.getString(req
				.getParameter("functionId"));
		req.setAttribute("functionId", functionId);
		return new ModelAndView("system/dataRule/ruleData");
	}

	/**
	 * 
	 * opdategrid 数据规则的列表界面
	 * 
	 * @Title: opdategrid
	 * @param @param request
	 * @param @param response
	 * @param @param dataGrid 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	@RequestMapping(params = "ruledategrid")
	public void ruledategrid(HttpServletRequest request,
			HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(DataRule.class, dataGrid);
		String functionId = ConvertUtils.getString(request
				.getParameter("functionId"));
		cq.eq("TSFunction.id", functionId);
		cq.add();
		this.systemService.findDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 
	 * delrule 删除数据权限规则
	 * 
	 * @Title: delrule
	 * @Description: TODO
	 * @param @param operation
	 * @param @param request
	 * @param @return 设定文件
	 * @return AjaxJson 返回类型
	 * @throws
	 */
	@RequestMapping(params = "delrule")
	@ResponseBody
	public AjaxJson delrule(DataRule operation, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		operation = systemService
				.findEntity(DataRule.class, operation.getId());
		message = MutiLangUtils.paramDelSuccess("common.operation");
		userService.delete(operation);
		systemService.addLog(message, Globals.Log_Type_DEL,
				Globals.Log_Leavel_INFO);

		j.setMsg(message);

		return j;
	}

	/**
	 * 
	 * saverule保存规则权限值
	 * 
	 * @Title: saverule
	 * @Description: TODO
	 * @param @param operation
	 * @param @param request
	 * @param @return 设定文件
	 * @return AjaxJson 返回类型
	 * @throws
	 */
	@RequestMapping(params = "saverule")
	@ResponseBody
	public AjaxJson saverule(DataRule operation, HttpServletRequest request) {

		AjaxJson j = new AjaxJson();
		if (StringUtils.isNotEmpty(operation.getId())) {
			message = MutiLangUtils.paramUpdSuccess("common.operation");
			userService.saveOrUpdate(operation);
			systemService.addLog(message, Globals.Log_Type_UPDATE,
					Globals.Log_Leavel_INFO);
		} else {
			if (justHaveDataRule(operation) == 0) {
				message = MutiLangUtils.paramAddSuccess("common.operation");
				userService.save(operation);
				systemService.addLog(message, Globals.Log_Type_INSERT,
						Globals.Log_Leavel_INFO);
			} else {

				message = "操作 字段规则已存在";
			}
		}
		j.setMsg(message);
		return j;
	}

	public int justHaveDataRule(DataRule dataRule) {
		String sql = "SELECT id FROM t_s_data_rule WHERE functionId='"+dataRule.getTSFunction()
				.getId()+"' AND rule_column='"+dataRule.getRuleColumn()+"' AND rule_conditions='"+dataRule
				.getRuleConditions()+"'";

		List<String> hasOperList = this.systemService.findListBySql(sql);
		return hasOperList.size();
	}
}
