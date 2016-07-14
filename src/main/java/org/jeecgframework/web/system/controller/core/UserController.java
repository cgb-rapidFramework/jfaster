package org.jeecgframework.web.system.controller.core;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.hibernate.criterion.Property;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.common.UploadFile;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.ComboBox;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.common.model.json.ValidForm;
import org.jeecgframework.core.util.ConvertUtils;
import org.jeecgframework.platform.bean.FunctionBean;
import org.jeecgframework.platform.common.poi.excel.ExcelExportUtil;
import org.jeecgframework.platform.common.poi.excel.ExcelImportUtil;
import org.jeecgframework.platform.common.poi.excel.entity.ExcelTitle;
import org.jeecgframework.platform.common.poi.excel.entity.ImportParams;
import org.jeecgframework.platform.common.tag.easyui.TagUtil;
import org.jeecgframework.platform.constant.Globals;
import org.jeecgframework.platform.util.BrowserUtils;
import org.jeecgframework.platform.util.SystemMenuUtils;
import org.jeecgframework.web.common.hqlsearch.HqlGenerateUtil;
import org.jeecgframework.web.system.constant.core.UserConstant;
import org.jeecgframework.web.system.controller.BaseController;
import org.jeecgframework.web.system.entity.base.*;
import org.jeecgframework.web.system.service.DepartService;
import org.jeecgframework.web.system.service.ResourceService;
import org.jeecgframework.web.system.service.SystemService;
import org.jeecgframework.web.system.service.UserService;
import org.jeecgframework.web.system.vo.base.ExlUserVo;
import org.jeecgframework.web.utils.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;


/**
 * @ClassName: UserController
 * @Description: TODO(用户管理处理类)
 * @author 张代浩
 */
@Scope("prototype")
@Controller
@RequestMapping("/userController")
public class UserController extends BaseController {
	private static final Logger logger = Logger.getLogger(UserController.class);
	@Autowired
	private ResourceService resourceService;
	private UserService userService;
	private SystemService systemService;
	private String message = null;

	@Autowired
	public void setSystemService(SystemService systemService) {
		this.systemService = systemService;
	}

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}


	/**
	 * 菜单列表
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "menu")
	public void menu(HttpServletRequest request, HttpServletResponse response) {
		FunctionComparator sort = new FunctionComparator();
		TSUser u = SessionUtils.getCurrentUser();
		// 登陆者的权限
		Set<TSFunction> loginActionlist = new HashSet();// 已有权限菜单
		List<TSRoleUser> rUsers = systemService.findAllByProperty(TSRoleUser.class, "TSUser.id", u.getId());
		for (TSRoleUser ru : rUsers) {
			TSRole role = ru.getTSRole();
			List<TSRoleFunction> roleFunctionList = systemService.findAllByProperty(TSRoleFunction.class, "TSRole.id", role.getId());
			if (roleFunctionList.size() > 0) {
				for (TSRoleFunction roleFunction : roleFunctionList) {
					TSFunction function = roleFunction.getTSFunction();
					loginActionlist.add(function);
				}
			}
		}
		List<FunctionBean> bigActionlist = new ArrayList();// 一级权限菜单
		List<FunctionBean> smailActionlist = new ArrayList();// 二级权限菜单
		if (loginActionlist.size() > 0) {
			for (TSFunction function : loginActionlist) {
				FunctionBean functionBean = BeanToTagUtils.convertFunction(function);
				if (function.getFunctionLevel() == 0) {
					bigActionlist.add(functionBean);
				} else if (function.getFunctionLevel() == 1) {
					smailActionlist.add(functionBean);
				}
			}
		}
		// 菜单栏排序
		Collections.sort(bigActionlist, sort);
		Collections.sort(smailActionlist, sort);
		String logString = SystemMenuUtils.getMenu(bigActionlist, smailActionlist);
		// request.setAttribute("loginMenu",logString);
		try {
			response.getWriter().write(logString);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 用户列表页面跳转[跳转到标签和手工结合的html页面]
	 *
	 * @return
	 */
	@RequestMapping(params = "userDemo")
	public String userDemo(HttpServletRequest request) {
		// 给部门查询条件中的下拉框准备数据
		List<TSDepart> departList = systemService.getList(TSDepart.class);
		request.setAttribute("departsReplace", SystemJsonUtils.listToReplaceStr(departList, "departname", "id"));
		return "system/user/userList2";
	}


	/**
	 * 用户列表页面跳转
	 *
	 * @return
	 */
	@RequestMapping(params = "user")
	public String user(HttpServletRequest request) {
		// 给部门查询条件中的下拉框准备数据
		List<TSDepart> departList = systemService.getList(TSDepart.class);
		request.setAttribute("departsReplace", SystemJsonUtils.listToReplaceStr(departList, "departname", "id"));
		return "system/user/userList";
	}

	/**
	 * 用户信息
	 *
	 * @return
	 */
	@RequestMapping(params = "userinfo")
	public String userinfo(HttpServletRequest request) {
		TSUser user = SessionUtils.getCurrentUser();
		request.setAttribute("user", user);
		return "system/user/userinfo";
	}

	/**
	 * 修改密码
	 *
	 * @return
	 */
	@RequestMapping(params = "changepassword")
	public String changepassword(HttpServletRequest request) {
		TSUser user = SessionUtils.getCurrentUser();
		request.setAttribute("user", user);
		return "system/user/changepassword";
	}


	/**
	 * 修改密码
	 *
	 * @return
	 */
	@RequestMapping(params = "savenewpwd")
	@ResponseBody
	public AjaxJson savenewpwd(HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		TSUser user = SessionUtils.getCurrentUser();
		String password = ConvertUtils.getString(request.getParameter("password"));
		String newpassword = ConvertUtils.getString(request.getParameter("newpassword"));
		String pString = PasswordUtils.encrypt(user.getUserName(), password, PasswordUtils.getStaticSalt());
		if (!pString.equals(user.getPassword())) {
			j.setMsg("原密码不正确");
			j.setSuccess(false);
		} else {
			try {
				user.setPassword(PasswordUtils.encrypt(user.getUserName(), newpassword, PasswordUtils.getStaticSalt()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			systemService.update(user);
			j.setMsg("修改成功");

		}
		return j;
	}


	/**
	 * 修改用户密码
	 *
	 * @author Chj
	 */

	@RequestMapping(params = "changepasswordforuser")
	public ModelAndView changepasswordforuser(TSUser user, HttpServletRequest req) {
		if (StringUtils.isNotEmpty(user.getId())) {
			user = systemService.findEntity(TSUser.class, user.getId());
			req.setAttribute("user", user);
			idandname(req, user);
		}
		return new ModelAndView("system/user/adminchangepwd");
	}


	@RequestMapping(params = "savenewpwdforuser")
	@ResponseBody
	public AjaxJson savenewpwdforuser(HttpServletRequest req) {
		AjaxJson j = new AjaxJson();
		String id = ConvertUtils.getString(req.getParameter("id"));
		String password = ConvertUtils.getString(req.getParameter("password"));
		if (StringUtils.isNotEmpty(id)) {
			TSUser users = systemService.findEntity(TSUser.class, id);
			System.out.println(users.getUserName());
			users.setPassword(PasswordUtils.encrypt(users.getUserName(), password, PasswordUtils.getStaticSalt()));
			users.setStatus(Globals.User_Normal);
			users.setActivitiSync(users.getActivitiSync());
			systemService.update(users);
			message = "用户: " + users.getUserName() + "密码重置成功";
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		}

		j.setMsg(message);

		return j;
	}


	/**
	 * 锁定账户
	 *
	 * @author Chj
	 */
	@RequestMapping(params = "lock")
	@ResponseBody
	public AjaxJson lock(String id, HttpServletRequest req) {
		AjaxJson j = new AjaxJson();

		TSUser user = systemService.findEntity(TSUser.class, id);
		if ("admin".equals(user.getUserName())) {
			message = "超级管理员[admin]不可锁定";
			j.setMsg(message);
			return j;
		}
		if (user.getStatus() != Globals.User_Forbidden) {
			user.setStatus(Globals.User_Forbidden);
			userService.update(user);
			message = "用户：" + user.getUserName() + "锁定成功";
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);

		} else {
			message = "锁定账户失败";
		}

		j.setMsg(message);
		return j;
	}


	/**
	 * 得到角色列表
	 *
	 * @return
	 */
	@RequestMapping(params = "role")
	@ResponseBody
	public List<ComboBox> role(HttpServletResponse response, HttpServletRequest request, ComboBox comboBox) {
		String id = request.getParameter("id");
		List<ComboBox> comboBoxs = new ArrayList<ComboBox>();
		List<TSRole> roles = new ArrayList();
		if (StringUtils.isNotEmpty(id)) {
			List<TSRoleUser> roleUser = systemService.findAllByProperty(TSRoleUser.class, "TSUser.id", id);
			if (roleUser.size() > 0) {
				for (TSRoleUser ru : roleUser) {
					roles.add(ru.getTSRole());
				}
			}
		}
		List<TSRole> roleList = systemService.getList(TSRole.class);
		comboBoxs = TagUtil.getComboBox(roleList, roles, comboBox);
		return comboBoxs;
	}

	/**
	 * 得到部门列表
	 *
	 * @return
	 */
	@RequestMapping(params = "depart")
	@ResponseBody
	public List<ComboBox> depart(HttpServletResponse response, HttpServletRequest request, ComboBox comboBox) {
		String id = request.getParameter("id");
		List<ComboBox> comboBoxs = new ArrayList<ComboBox>();
		List<TSDepart> departs = new ArrayList();
		if (StringUtils.isNotEmpty(id)) {
			TSUser user = systemService.find(TSUser.class, id);
//			if (user.getTSDepart() != null) {
//				TSDepart depart = systemService.get(TSDepart.class, user.getTSDepart().getId());
//				departs.add(depart);
//			}
			// todo zhanggm 获取指定用户的组织机构列表
			List<TSDepart[]> resultList = systemService.findByHql("from TSDepart d,TSUserOrg uo where d.id=uo.orgId and uo.id=?", id);
			for (TSDepart[] departArr : resultList) {
				departs.add(departArr[0]);
			}
		}
		List<TSDepart> departList = systemService.getList(TSDepart.class);
		comboBoxs = TagUtil.getComboBox(departList, departs, comboBox);
		return comboBoxs;
	}

	/**
	 * easyuiAJAX用户列表请求数据
	 *
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "datagrid")
	public void datagrid(TSUser user, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		String orgIds = request.getParameter("orgIds");
		CriteriaQuery cq =this.buildCq(user,dataGrid,orgIds);
		this.systemService.findDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/***
	 * build 查询条件
	 * @param user
	 * @param dataGrid
	 * @param orgIds
	 */
	private CriteriaQuery buildCq(TSUser user, DataGrid dataGrid, String orgIds) {
		CriteriaQuery cq = new CriteriaQuery(TSUser.class, dataGrid);
		//查询条件组装器
		HqlGenerateUtil.installHql(cq, user);

		Short[] userstate = new Short[]{Globals.User_Normal, Globals.User_ADMIN, Globals.User_Forbidden};
		cq.in("status", userstate);

		List<String> orgIdList = extractIdListByComma(orgIds);
		// 获取 当前组织机构的用户信息
		if (!CollectionUtils.isEmpty(orgIdList)) {
			CriteriaQuery subCq = new CriteriaQuery(TSUserOrg.class);
			subCq.setProjection(Property.forName("tsUser.id"));
			subCq.in("tsDepart.id", orgIdList.toArray());
			subCq.add();

			cq.add(Property.forName("id").in(subCq.getDetachedCriteria()));
		}
		cq.add();
		return cq;
	}




	/**
	 * 用户信息录入和更新
	 *
	 * @param user
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(TSUser user, HttpServletRequest req) {
		AjaxJson j = new AjaxJson();
		if ("admin".equals(user.getUserName())) {
			message = "超级管理员[admin]不可删除";
			j.setMsg(message);
			return j;
		}
		user = systemService.findEntity(TSUser.class, user.getId());
		List<TSRoleUser> roleUser = systemService.findAllByProperty(TSRoleUser.class, "TSUser.id", user.getId());
		if (!user.getStatus().equals(Globals.User_ADMIN)) {
			if (roleUser.size() > 0) {
				// 删除用户时先删除用户和角色关系表
				delRoleUser(user);
				systemService.executeSql("delete from t_s_user_org where user_id=?", user.getId()); // 删除 用户-机构 数据
				userService.delete(user);
				message = "用户：" + user.getUserName() + "删除成功";
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			} else {
				userService.delete(user);
				message = "用户：" + user.getUserName() + "删除成功";
			}
		} else {
			message = "超级管理员不可删除";
		}

		j.setMsg(message);
		return j;
	}

	public void delRoleUser(TSUser user) {
		// 同步删除用户角色关联表
		List<TSRoleUser> roleUserList = systemService.findAllByProperty(TSRoleUser.class, "TSUser.id", user.getId());
		if (roleUserList.size() >= 1) {
			for (TSRoleUser tRoleUser : roleUserList) {
				systemService.delete(tRoleUser);
			}
		}
	}

	/**
	 * 检查用户名
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "checkUser")
	@ResponseBody
	public ValidForm checkUser(HttpServletRequest request) {
		ValidForm v = new ValidForm();
		String userName = ConvertUtils.getString(request.getParameter("param"));
		String code = ConvertUtils.getString(request.getParameter("code"));
		List<TSUser> roles = systemService.findAllByProperty(TSUser.class, "userName", userName);
		if (roles.size() > 0 && !code.equals(userName)) {
			v.setInfo("用户名已存在");
			v.setStatus("n");
		}
		return v;
	}

	/**
	 * 用户录入
	 *
	 * @param user
	 * @param req
	 * @return
	 */

	@RequestMapping(params = "saveUser")
	@ResponseBody
	public AjaxJson saveUser(HttpServletRequest req, TSUser user) {
		AjaxJson j = new AjaxJson();
		// 得到用户的角色
		String roleid = ConvertUtils.getString(req.getParameter("roleid"));
		String password = ConvertUtils.getString(req.getParameter("password"));
		if (StringUtils.isNotEmpty(user.getId())) {
			TSUser users = systemService.findEntity(TSUser.class, user.getId());
			users.setEmail(user.getEmail());
			users.setOfficePhone(user.getOfficePhone());
			users.setMobilePhone(user.getMobilePhone());
			systemService.executeSql("delete from t_s_user_org where user_id=?", user.getId());
			saveUserOrgList(req, user);
//            users.setTSDepart(user.getTSDepart());
			users.setRealName(user.getRealName());
			users.setStatus(Globals.User_Normal);
			users.setActivitiSync(user.getActivitiSync());
			systemService.update(users);
			List<TSRoleUser> ru = systemService.findAllByProperty(TSRoleUser.class, "TSUser.id", user.getId());
			systemService.deleteEntities(ru);
			message = "用户: " + users.getUserName() + "更新成功";
			if (StringUtils.isNotEmpty(roleid)) {
				saveRoleUser(users, roleid);
			}
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} else {
			TSUser users = systemService.findUniqueByProperty(TSUser.class, "userName", user.getUserName());
			if (users != null) {
				message = "用户: " + users.getUserName() + "已经存在";
			} else {
				user.setPassword(PasswordUtils.encrypt(user.getUserName(), password, PasswordUtils.getStaticSalt()));
//				if (user.getTSDepart().equals("")) {
//					user.setTSDepart(null);
//				}
				user.setStatus(Globals.User_Normal);
				systemService.save(user);
				// todo zhanggm 保存多个组织机构
				saveUserOrgList(req, user);
				message = "用户: " + user.getUserName() + "添加成功";
				if (StringUtils.isNotEmpty(roleid)) {
					saveRoleUser(user, roleid);
				}
				systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
			}

		}
		j.setMsg(message);

		return j;
	}

	/**
	 * 保存 用户-组织机构 关系信息
	 *
	 * @param request request
	 * @param user    user
	 */
	private void saveUserOrgList(HttpServletRequest request, TSUser user) {
		String orgIds = ConvertUtils.getString(request.getParameter("orgIds"));

		List<TSUserOrg> userOrgList = new ArrayList<TSUserOrg>();
		List<String> orgIdList = extractIdListByComma(orgIds);
		for (String orgId : orgIdList) {
			TSDepart depart = new TSDepart();
			depart.setId(orgId);

			TSUserOrg userOrg = new TSUserOrg();
			userOrg.setTsUser(user);
			userOrg.setTsDepart(depart);

			userOrgList.add(userOrg);
		}
		if (!userOrgList.isEmpty()) {
			systemService.batchSave(userOrgList);
		}
	}

	protected void saveRoleUser(TSUser user, String roleidstr) {
		String[] roleids = roleidstr.split(",");
		for (int i = 0; i < roleids.length; i++) {
			TSRoleUser rUser = new TSRoleUser();
			TSRole role = systemService.findEntity(TSRole.class, roleids[i]);
			rUser.setTSRole(role);
			rUser.setTSUser(user);
			systemService.save(rUser);

		}
	}

	/**
	 * 用户选择角色跳转页面
	 *
	 * @return
	 */
	@RequestMapping(params = "roles")
	public String roles() {
		return "system/user/users";
	}

	/**
	 * 角色显示列表
	 *
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "datagridRole")
	public void datagridRole(HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(TSRole.class, dataGrid);
		this.systemService.findDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * easyuiAJAX请求数据： 用户选择角色列表
	 *
	 * @param user
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(TSUser user, HttpServletRequest req) {
		List<TSDepart> departList = new ArrayList<TSDepart>();
		String departid = ConvertUtils.getString(req.getParameter("departid"));
		if (!StringUtils.isEmpty(departid)) {
			departList.add((TSDepart) systemService.findEntity(TSDepart.class, departid));
		} else {
			departList.addAll((List) systemService.getList(TSDepart.class));
		}
		req.setAttribute("departList", departList);
		List<String> orgIdList = new ArrayList<String>();
		if (StringUtils.isNotEmpty(user.getId())) {
			user = systemService.findEntity(TSUser.class, user.getId());

			req.setAttribute("user", user);
			idandname(req, user);
			orgIdList = systemService.findByHql("select d.id from TSDepart d,TSUserOrg uo where d.id=uo.tsDepart.id and uo.tsUser.id=?", new String[]{user.getId()});
		}
		req.setAttribute("orgIdList", JSON.toJSON(orgIdList));

		return new ModelAndView("system/user/user");
	}

	/**
	 * 用户的登录后的组织机构选择页面
	 *
	 * @param request request
	 * @return 用户选择组织机构页面
	 */
	@RequestMapping(params = "userOrgSelect")
	public ModelAndView userOrgSelect(HttpServletRequest request) {
		List<TSDepart> orgList = new ArrayList<TSDepart>();
		String userId = ConvertUtils.getString(request.getParameter("userId"));

		List<Object[]> orgArrList = systemService.findByHql("from TSDepart d,TSUserOrg uo where d.id=uo.tsDepart.id and uo.tsUser.id=?", new String[]{userId});
		for (Object[] departs : orgArrList) {
			orgList.add((TSDepart) departs[0]);
		}
		request.setAttribute("orgList", orgList);

		TSUser user = systemService.findEntity(TSUser.class, userId);
		request.setAttribute("user", user);

		return new ModelAndView("system/user/userOrgSelect");
	}

	public void idandname(HttpServletRequest req, TSUser user) {
		List<TSRoleUser> roleUsers = systemService.findAllByProperty(TSRoleUser.class, "TSUser.id", user.getId());
		String roleId = "";
		String roleName = "";
		if (roleUsers.size() > 0) {
			for (TSRoleUser tRoleUser : roleUsers) {
				roleId += tRoleUser.getTSRole().getId() + ",";
				roleName += tRoleUser.getTSRole().getRoleName() + ",";
			}
		}
		req.setAttribute("id", roleId);
		req.setAttribute("roleName", roleName);

	}

	/**
	 * 根据部门和角色选择用户跳转页面
	 */
	@RequestMapping(params = "choose")
	public String choose(HttpServletRequest request) {
		List<TSRole> roles = systemService.findAll(TSRole.class);
		request.setAttribute("roleList", roles);
		return "system/membership/checkuser";
	}

	/**
	 * 部门和角色选择用户的panel跳转页面
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "chooseUser")
	public String chooseUser(HttpServletRequest request) {
		String departid = request.getParameter("departid");
		String roleid = request.getParameter("roleid");
		request.setAttribute("roleid", roleid);
		request.setAttribute("departid", departid);
		return "system/membership/userlist";
	}

	/**
	 * 部门和角色选择用户的用户显示列表
	 *
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "datagridUser")
	public void datagridUser(HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		String departid = request.getParameter("departid");
		String roleid = request.getParameter("roleid");
		CriteriaQuery cq = new CriteriaQuery(TSUser.class, dataGrid);
		if (departid.length() > 0) {
			cq.eq("TDepart.departid", ConvertUtils.getInt(departid, 0));
			cq.add();
		}
		String userid = "";
		if (roleid.length() > 0) {
			List<TSRoleUser> roleUsers = systemService.findAllByProperty(TSRoleUser.class, "TRole.roleid", ConvertUtils.getInt(roleid, 0));
			if (roleUsers.size() > 0) {
				for (TSRoleUser tRoleUser : roleUsers) {
					userid += tRoleUser.getTSUser().getId() + ",";
				}
			}
			cq.in("userid", ConvertUtils.getInts(userid.split(",")));
			cq.add();
		}
		this.systemService.findDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 根据部门和角色选择用户跳转页面
	 */
	@RequestMapping(params = "roleDepart")
	public String roleDepart(HttpServletRequest request) {
		List<TSRole> roles = systemService.findAll(TSRole.class);
		request.setAttribute("roleList", roles);
		return "system/membership/roledepart";
	}

	/**
	 * 部门和角色选择用户的panel跳转页面
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "chooseDepart")
	public ModelAndView chooseDepart(HttpServletRequest request) {
		String nodeid = request.getParameter("nodeid");
		ModelAndView modelAndView = null;
		if (nodeid.equals("role")) {
			modelAndView = new ModelAndView("system/membership/users");
		} else {
			modelAndView = new ModelAndView("system/membership/departList");
		}
		return modelAndView;
	}

	/**
	 * 部门和角色选择用户的用户显示列表
	 *
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "datagridDepart")
	public void datagridDepart(HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(TSDepart.class, dataGrid);
		systemService.findDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 用户列表页面跳转
	 *
	 * @return
	 */
	@RequestMapping(params = "index")
	public String index() {
		return "bootstrap/main";
	}

	/**
	 * 用户列表页面跳转
	 *
	 * @return
	 */
	@RequestMapping(params = "main")
	public String main() {
		return "bootstrap/test";
	}

	/**
	 * 测试
	 *
	 * @return
	 */
	@RequestMapping(params = "testpage")
	public String testpage(HttpServletRequest request) {
		return "test/test";
	}

	/**
	 * 设置签名跳转页面
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "addsign")
	public ModelAndView addsign(HttpServletRequest request) {
		String id = request.getParameter("id");
		request.setAttribute("id", id);
		return new ModelAndView("system/user/usersign");
	}

	/**
	 * 用户录入
	 *
	 * @param req
	 * @param req
	 * @return
	 */

	@RequestMapping(params = "savesign", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson savesign(HttpServletRequest req) {
		UploadFile uploadFile = new UploadFile(req);
		String id = uploadFile.get("id");
		TSUser user = systemService.findEntity(TSUser.class, id);
		uploadFile.setRealPath("signatureFile");
		uploadFile.setCusPath("signature");
		uploadFile.setByteField("signature");
		uploadFile.setBasePath("resources");
		uploadFile.setRename(false);
		uploadFile.setObject(user);
		AjaxJson j = new AjaxJson();
		message = user.getUserName() + "设置签名成功";
		resourceService.uploadFile(uploadFile);
		systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		j.setMsg(message);

		return j;
	}

	/**
	 * 测试组合查询功能
	 *
	 * @param user
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "testSearch")
	public void testSearch(TSUser user, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(TSUser.class, dataGrid);
		if (user.getUserName() != null) {
			cq.like("userName", user.getUserName());
		}
		if (user.getRealName() != null) {
			cq.like("realName", user.getRealName());
		}
		cq.add();
		this.systemService.findDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/***
	 * 导入用户
	 *
	 * @param user
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "importUser")
	public ModelAndView importUser(TSUser user, HttpServletRequest request) {
		return new ModelAndView("system/user/importUser");
	}

	/**
	 * 下载模版
	 */

	@RequestMapping(params = "downloadUserTemplate")
	public void downloadUserTemplate(HttpServletRequest request, HttpServletResponse response) {
		// 生成提示信息，
		response.setContentType("application/vnd.ms-excel");
		String codedFileName;
		String codedFileShowName;
		OutputStream fOut = null;
		try {
			codedFileName = "ImportUserTemplate";
			codedFileShowName = "用户信息导入模板";
			// 根据浏览器进行转码，使其支持中文文件名
			String browse = BrowserUtils.checkBrowse(request);
			if ("MSIE".equalsIgnoreCase(browse.substring(0, 4))) {
				response.setHeader("content-disposition",
						"attachment;filename="
								+ java.net.URLEncoder.encode(codedFileShowName,
								"UTF-8") + ".xls");
			} else {
				String newtitle = new String(codedFileShowName
						.getBytes("UTF-8"), "ISO8859-1");
				response.setHeader("content-disposition",
						"attachment;filename=" + newtitle + ".xls");
			}
			String path = ConfigUtils.getConfigByName("template.file.path");
			File f = new File(path + "/" + codedFileName + ".xls");
			fOut = new BufferedOutputStream(response.getOutputStream());
			byte[] readFileToByteArray = FileUtils.readFileToByteArray(f);
			// 返回客户端
			fOut.write(readFileToByteArray);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fOut.flush();
				fOut.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	/**
	 * 保存用户数据
	 */
	@RequestMapping(params = "saveImportUser")
	@ResponseBody
	public AjaxJson saveImportUser(HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		if (fileMap.size() > 1) {
			j.setSuccess(true);
			j.setMsg("<font color='red'>失败!</font> 每次只能导入一个文件");
			return j;
		}
		MultipartFile file;
		List<ExlUserVo> userList;
		List<TSUser> userEntities = new ArrayList<TSUser>();
		for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
			try {
				//解析文件
				file = entity.getValue();
				userList = (List<ExlUserVo>) ExcelImportUtil
						.importExcelByIs(file.getInputStream(),
								ExlUserVo.class, new ImportParams());
				//验证文件
				if (null == userList || userList.size() == 0) {
					j.setMsg("<font color='red'>失败!</font> Excel中没有可以导入的数据");
					return j;
				}

				for (ExlUserVo exlUserVo : userList) {
					j = ValidateUtils.volatileBean(exlUserVo);
					if (!j.isSuccess()) {
						j.setSuccess(true);
						return j;
					}
					//判断帐号是否存在
					TSUser u = this.systemService.findUniqueByProperty(TSUser.class, "userName", exlUserVo.getUserName());
					if (StringUtils.isNotEmpty(u)) {
						j.setMsg("<font color='red'>失败!</font>" + exlUserVo.getUserName() + " 帐号已经存在");
						return j;
					}

					//判断组织机构是否存在
					List<TSDepart> exlDeparts = new ArrayList<TSDepart>();
					String[] departNames = exlUserVo.getDepartName().split(",");
					for (int i = 0; i < departNames.length; i++) {
						List<TSDepart> departs = systemService.findAllByProperty(TSDepart.class, "departname", departNames[i]);
						if (departs.size() == 0) {
							j.setMsg("<font color='red'>失败!</font>" + exlUserVo.getDepartName() + " 组织机构不存在");
							return j;
						}
						exlDeparts.add(departs.get(0));
					}


					List<TSRole> exlRoles = new ArrayList<TSRole>();
					String[] roleNames = exlUserVo.getRoleName().split(",");
					for (int i = 0; i < roleNames.length; i++) {
						//判断角色是否存在
						List<TSRole> roles = systemService.findAllByProperty(TSRole.class, "roleName", roleNames[i]);
						if (roles.size() == 0) {
							j.setMsg("<font color='red'>失败!</font>" + exlUserVo.getRoleName() + " 角色不存在");
							return j;
						}
						exlRoles.add(roles.get(0));
					}

					TSUser userEntity = new TSUser();
					BeanUtils.copyProperties(exlUserVo, userEntity);
					userEntity.setDeparts(exlDeparts);
					userEntity.setRoles(exlRoles);
					userEntity.setStatus(UserConstant.USER_STATUS_IS_AVAILABLE);
					userEntities.add(userEntity);
				}

				for (TSUser userEntity : userEntities) {
					String pwd = userEntity.getPassword();
					userEntity.setPassword(null);
					String uid = (String) this.userService.save(userEntity);
					userEntity = this.userService.find(TSUser.class, uid);
					userEntity.setPassword(PasswordUtils.encrypt(userEntity.getUserName(), pwd, PasswordUtils.getStaticSalt()));
					userService.update(userEntity);

					//保存组织机构
					for (TSDepart depart : userEntity.getDeparts()) {
						TSUserOrg userOrg = new TSUserOrg();
						userOrg.setTsUser(userEntity);
						userOrg.setTsDepart(depart);
						this.systemService.save(userOrg);
					}

					//保存角色
					for (TSRole role : userEntity.getRoles()) {
						TSRoleUser roleUser = new TSRoleUser();
						roleUser.setTSRole(role);
						roleUser.setTSUser(userEntity);
						this.systemService.save(roleUser);
					}
				}
				j.setMsg("<font color='green'> 文件导入成功！</font>");
			} catch (IOException e) {
				j.setMsg("<font color='red'>失败!</font> 检查文件数据、格式等是否正确！详细信息："
						+ e.getMessage());

			} catch (Exception e) {
				j.setMsg("<font color='red'>失败!</font> 检查文件数据、格式等是否正确！详细信息："
						+ e.getMessage());
			}
		}
		return j;
	}


	/***
	 *导出用户
	 * @param user
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "exportUser")
	public void exportUser(TSUser user, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		//TODO 暂时未加入组织id过滤
		dataGrid.setPage(0);
		dataGrid.setRows(1000000);
		String orgIds = request.getParameter("orgIds");
		CriteriaQuery cq =this.buildCq(user,dataGrid,orgIds);
		// 生成提示信息，
		response.setContentType("application/vnd.ms-excel");
		OutputStream fOut = null;
		try {
			String exportFileName = "用户列表" + DateUtils.formatDate(new Date(),DateUtils.YYYY_MM_DD_HH_MM_SS);
			// 根据浏览器进行转码，使其支持中文文件名
			String browse = BrowserUtils.checkBrowse(request);
			if ("MSIE".equalsIgnoreCase(browse.substring(0, 4))) {
				response.setHeader("content-disposition", "attachment;filename=" + java.net.URLEncoder.encode(exportFileName, "UTF-8") + ".xls");
			} else {
				String exportFileTitle = new String(exportFileName.getBytes("UTF-8"), "ISO8859-1");
				response.setHeader("content-disposition", "attachment;filename=" + exportFileTitle + ".xls");
			}
			// 进行转码，使其支持中文文件名
			// 产生工作簿对象
			List<ExlUserVo> exlUserList = this.userService.getExlUserList(dataGrid, user,cq);
			HSSFWorkbook workbook = ExcelExportUtil.exportExcel(new ExcelTitle(null, null, exportFileName), ExlUserVo.class, exlUserList);
			fOut = response.getOutputStream();
			workbook.write(fOut);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fOut != null) {
					fOut.flush();
					fOut.close();
				}
			} catch (IOException e) {

			}
		}
	}
}