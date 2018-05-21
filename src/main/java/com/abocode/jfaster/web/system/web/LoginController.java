package com.abocode.jfaster.web.system.web;

import com.abocode.jfaster.core.common.model.json.AjaxJson;
import com.abocode.jfaster.core.extend.datasource.DataSourceType;
import com.abocode.jfaster.core.common.util.ContextHolderUtils;
import com.abocode.jfaster.core.common.util.ConvertUtils;
import com.abocode.jfaster.core.common.constants.Globals;
import com.abocode.jfaster.web.system.interfaces.bean.ClientBean;
import com.abocode.jfaster.web.system.interfaces.constant.TemplateConstant;
import com.abocode.jfaster.web.system.domain.entity.*;
import com.abocode.jfaster.web.common.manager.ClientManager;
import com.abocode.jfaster.core.common.util.BeanToTagUtils;
import com.abocode.jfaster.core.common.util.SessionShareCenter;
import com.abocode.jfaster.core.common.util.SessionUtils;
import com.google.gson.Gson;
import org.apache.log4j.Logger;
import com.abocode.jfaster.core.extend.datasource.DataSourceContextHolder;
import com.abocode.jfaster.web.system.interfaces.view.FunctionView;
import com.abocode.jfaster.web.system.interfaces.view.TemplateView;
import com.abocode.jfaster.core.common.container.SystemContainer;
import com.abocode.jfaster.core.common.util.SystemMenuUtils;
import com.abocode.jfaster.web.system.domain.repository.MutiLangService;
import com.abocode.jfaster.web.system.domain.repository.SystemService;
import com.abocode.jfaster.web.system.domain.repository.TemplateService;
import com.abocode.jfaster.web.system.domain.repository.UserService;
import com.abocode.jfaster.core.common.util.NumberComparator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * 登陆初始化控制器
 * @author 张代浩
 * 
 */
@Scope("prototype")
@Controller
@RequestMapping("/loginController")
public class LoginController extends BaseController {
	private Logger log = Logger.getLogger(LoginController.class);
	private SystemService systemService;
	private UserService userService;
	private String message = null;

	@Autowired
	private MutiLangService mutiLangService;
	@Autowired
	private TemplateService templateService;
	
	@Autowired
	public void setSystemService(SystemService systemService) {
		this.systemService = systemService;
	}

	@Autowired
	public void setUserService(UserService userService) {

		this.userService = userService;
	}

	@RequestMapping(params = "goPwdInit")
	public String goPwdInit() {
		return "login/pwd_init";
	}

	/**
	 * admin账户密码初始化
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "pwdInit")
	public ModelAndView pwdInit(HttpServletRequest request) {
		ModelAndView modelAndView = null;
		User user = new User();
		user.setUserName("admin");
		String newPwd = "123456";
		userService.pwdInit(user, newPwd);
		modelAndView = new ModelAndView(new RedirectView(
				"loginController.do?login"));
		return modelAndView;
	}
	
	/**
	 * 检查用户名称
	 * 
	 * @param user
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unused")
	@RequestMapping(params = "checkuser")
	@ResponseBody
	public AjaxJson checkuser(User user, HttpServletRequest request) {
		HttpSession session = ContextHolderUtils.getSession();
		DataSourceContextHolder
				.setDataSourceType(DataSourceType.dataSource_jeecg);
		AjaxJson j = new AjaxJson();
        if (request.getParameter("langCode")!=null) {
			request.getSession().setAttribute("lang", request.getParameter("langCode"));
        }
        String randCode = request.getParameter("randCode");
        if (StringUtils.isEmpty(randCode)) {
            j.setMsg(mutiLangService.getLang("common.enter.verifycode"));
            j.setSuccess(false);
        } else if (!randCode.equalsIgnoreCase(String.valueOf(session.getAttribute("randCode")))) {
            // todo "randCode"和验证码servlet中该变量一样，通过统一的系统常量配置比较好，暂时不知道系统常量放在什么地方合适
            j.setMsg(mutiLangService.getLang("common.verifycode.error"));
            j.setSuccess(false);
        } else {
			User u = userService.checkUserExits(user);
			if(u == null) {
				j.setMsg(mutiLangService.getLang("common.username.or.password.error"));
				j.setSuccess(false);
				return j;
			}
			if (u != null&&u.getStatus()!=0) {
				Map<String, Object> attrMap = new HashMap<String, Object>();
				j.setAttributes(attrMap);
				request.getSession().setAttribute("user",u); //用于切换部门时使用
				String orgId = request.getParameter("orgId");
				if (ConvertUtils.isEmpty(orgId)) { // 没有传组织机构参数，则获取当前用户的组织机构
					int orgNum=u.getUserOrgList().size();
					//获取默认部门
//					Long orgNum = systemService.queryForCount("select count(1) from t_s_user_org where user_id =?",new Object[]{u.getId()});
					if (orgNum > 1) {
						User res=new User();
						res.setId(u.getId());
						//暂时未处理多部门
						attrMap.put("orgNum", orgNum);
						attrMap.put("user", res);
					} else {
						Map<String, Object> userOrgMap = systemService.queryForMap("select org_id as orgId from t_s_user_org where user_id=?", u.getId());
						saveLoginSuccessInfo(request, u, (String) userOrgMap.get("orgId"));
					}
				} else {
					attrMap.put("orgNum", 1);
					saveLoginSuccessInfo(request, u, orgId);
				}
			} else {
				j.setMsg(mutiLangService.getLang("common.username.not.activation"));
				j.setSuccess(false);
			}
        }
		return j;
	}

    /**
     * 保存用户登录的信息，并将当前登录用户的组织机构赋值到用户实体中；
     * @param req request
     * @param user 当前登录用户
     * @param orgId 组织主键
     */
    private void saveLoginSuccessInfo(HttpServletRequest req, User user, String orgId) {
        Depart currentDepart = systemService.find(Depart.class, orgId);
        user.setCurrentDepart(currentDepart);

        HttpSession session = ContextHolderUtils.getSession();
        message = mutiLangService.getLang("common.user") + ": " + user.getUserName() + "["
                + currentDepart.getDepartname() + "]" + mutiLangService.getLang("common.login.success");

        ClientBean client = new ClientBean();
        client.setIp(com.abocode.jfaster.core.common.util.StringUtils.getIpAddr(req));
        client.setLogindatetime(new Date());
        client.setUser(user);
        SessionShareCenter.putUserId(client.getUser().getId());
        SessionShareCenter.putClient(client);
        ClientManager.getInstance().addClinet(session.getId(), client);
        // 添加登陆日志
        systemService.addLog(message, Globals.Log_Type_LOGIN, Globals.Log_Leavel_INFO);
    }

    /**
	 * 用户登录
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "login")
	public String login(ModelMap modelMap,HttpServletRequest request,HttpServletResponse response) {
		Template templateEntity=this.templateService.findUniqueByProperty(Template.class,"status", TemplateConstant.TEMPLATE_STATUS_IS_AVAILABLE);

		DataSourceContextHolder.setDataSourceType(DataSourceType.dataSource_jeecg);
		User user = SessionUtils.getCurrentUser();
		String roles = "";
		if (user != null) {
			List<Role> roleList=new ArrayList();
			List<RoleUser> rUsers = systemService.findAllByProperty(RoleUser.class, "TSUser.id", user.getId());
			for (RoleUser ru : rUsers) {
				Role role = ru.getTSRole();
				roles += role.getRoleName() + ",";
				roleList.add(role);
			}
			if (roles.length() > 0) {
				roles = roles.substring(0, roles.length() - 1);
			}
            modelMap.put("roleName", roles);
            modelMap.put("userName", user.getUserName());
            modelMap.put("currentOrgName", ClientManager.getInstance().getClient().getUser().getCurrentDepart().getDepartname());
            //将角色信息放入session
        	SessionShareCenter.putRoles(roleList);
            ClientManager.getInstance().getClient().setRoles(roleList);
			//request.getSession().setAttribute("lang", "en");
			Gson gson=new Gson();
			TemplateView templateBean=new TemplateView();
			BeanUtils.copyProperties(templateEntity,templateBean);
			//设置主题
			String systemTemplate=gson.toJson(templateBean);
			SystemContainer.TemplateContainer.template.put("SYSTEM-TEMPLATE",systemTemplate);

		    //放置语言
			/*String langCode= (String) request.getSession().getAttribute("lang");
			Cookie cookie=CacheUtils.putCookie("SYSTEM-LANGCODE",langCode);
			response.addCookie(cookie);*/
			//加载菜单
			request.setAttribute("menuMap", getFunctionMap(user));
			return templateEntity.getPageMain();
		} else {
			return  templateEntity.getPageLogin();
		}

	}

	/**
	 * 退出系统
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "logout")
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
		//退出权限登录
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null){
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}

		User user = SessionUtils.getCurrentUser();
		systemService.addLog("用户" + user.getUserName() + "已退出",
				Globals.Log_Type_EXIT, Globals.Log_Leavel_INFO);
		HttpSession session = ContextHolderUtils.getSession();
		//删除session
		SessionShareCenter.removeSession(session.getId());
		session.invalidate();
		ModelAndView modelAndView = new ModelAndView(new RedirectView(
				"loginController.do?login"));
		return modelAndView;
	}

	/**
	 * 菜单跳转
	 *
	 * @return
	 */
	@RequestMapping(params = "left")
	public ModelAndView left(HttpServletRequest request) {
		User user = SessionUtils.getCurrentUser();
		HttpSession session = ContextHolderUtils.getSession();
        ModelAndView modelAndView = new ModelAndView();
		// 登陆者的权限
		if (user.getId() == null) {
			session.removeAttribute(Globals.USER_SESSION);
            modelAndView.setView(new RedirectView("loginController.do?login"));
		}else{
            List<Config> configs = userService.findAll(Config.class);
            for (Config tsConfig : configs) {
                request.setAttribute(tsConfig.getCode(), tsConfig.getContents());
            }
            modelAndView.setViewName("main/left");
            request.setAttribute("menuMap",this.getFunctionMap(user));
        }
		return modelAndView;
	}

	/**
	 * 获取权限的map
	 *
	 * @param user
	 * @return
	 */
	private Map<Integer, List<FunctionView>> getFunctionMap(User user) {
		Map<Integer, List<FunctionView>> functionMap = new HashMap<Integer, List<FunctionView>>();
		Map<String, Function> loginActionlist = getUserFunction(user);
		if (loginActionlist.size() > 0) {
			Collection<Function> allFunctions = loginActionlist.values();
			for (Function function : allFunctions) {
	            /*if(function.getFunctionType().intValue()==Globals.Function_TYPE_FROM.intValue()){
					//如果为表单或者弹出 不显示在系统菜单里面
					continue;
				}*/
				if (!functionMap.containsKey(function.getFunctionLevel() + 0)) {
					functionMap.put(function.getFunctionLevel() + 0,
							new ArrayList<FunctionView>());
				}

				FunctionView functionBean= BeanToTagUtils.convertFunction(function);
				functionMap.get(function.getFunctionLevel() + 0).add(functionBean);
			}
			// 菜单栏排序
			Collection<List<FunctionView>> c = functionMap.values();
			for (List<FunctionView> list : c) {
				Collections.sort(list, new NumberComparator());
			}
		}
		return functionMap;
	}



	/**
	 * 获取用户菜单列表
	 *
	 * @param user
	 * @return
	 */
	private Map<String, Function> getUserFunction(User user) {
		HttpSession session = ContextHolderUtils.getSession();
		ClientBean client = ClientManager.getInstance().getClient(session.getId());
		if (client.getFunctions() == null || client.getFunctions().size() == 0) {
			Map<String, Function> loginActionlist = new HashMap<String, Function>();
			StringBuilder hqlsb1=new StringBuilder("select distinct f from Function f,RoleFunction rf,RoleUser ru  ")
					.append("where ru.TSRole.id=rf.TSRole.id and rf.TSFunction.id=f.id and ru.TSUser.id=? ");
			StringBuilder hqlsb2=new StringBuilder("select distinct c from Function c,RoleOrg b,UserOrg a ")
					.append("where a.tsDepart.id=b.tsDepart.id and b.tsRole.id=c.id and a.tsUser.id=?");
			 Object[] object=new Object[]{user.getId()};
	             List<Function> list1 = systemService.findByHql(hqlsb1.toString(),object);
	           List<Function> list2 = systemService.findByHql(hqlsb2.toString(),object);
	           for(Function function:list1){
		              loginActionlist.put(function.getId(),function);
		           }
	           for(Function function:list2){
		              loginActionlist.put(function.getId(),function);
		           }
            client.setFunctions(loginActionlist);
            //保存菜单到seesion中心
            session.setAttribute("functions"+session.getId(), loginActionlist);
		}
		return client.getFunctions();
	}

    /**
     * 根据 角色实体 组装 用户权限列表
     * @param loginActionlist 登录用户的权限列表
     * @param role 角色实体
     */
    private void assembleFunctionsByRole(Map<String, Function> loginActionlist, Role role) {
        List<RoleFunction> roleFunctionList = systemService.findAllByProperty(RoleFunction.class, "Role.id", role.getId());
        for (RoleFunction roleFunction : roleFunctionList) {
            Function function = roleFunction.getTSFunction();
            loginActionlist.put(function.getId(), function);
        }
    }


    /**
	 * 首页跳转
	 *
	 * @return
	 */
	@RequestMapping(params = "home")
	public ModelAndView home(HttpServletRequest request) {
		return new ModelAndView("main/home");
	}
	/**
	 * 无权限页面提示跳转
	 *
	 * @return
	 */
	@RequestMapping(params = "noAuth")
	public ModelAndView noAuth(HttpServletRequest request) {
		return new ModelAndView("common/noAuth");
	}
	/**
	 * @Title: top
	 * @Description: bootstrap头部菜单请求
	 * @param request
	 * @return ModelAndView
	 * @throws
	 */
	@RequestMapping(params = "top")
	public ModelAndView top(HttpServletRequest request) {
		User user = SessionUtils.getCurrentUser();
		HttpSession session = ContextHolderUtils.getSession();
		// 登陆者的权限
		if (user.getId() == null) {
			session.removeAttribute(Globals.USER_SESSION);
			return new ModelAndView(
					new RedirectView("loginController.do?login"));
		}
		request.setAttribute("menuMap", getFunctionMap(user));
		List<Config> configs = userService.findAll(Config.class);
		for (Config tsConfig : configs) {
			request.setAttribute(tsConfig.getCode(), tsConfig.getContents());
		}
		return new ModelAndView("main/bootstrap_top");
	}
	/**
	 * @Title: top
	 * @author gaofeng
	 * @Description: shortcut头部菜单请求
	 * @param request
	 * @return ModelAndView
	 * @throws
	 */
	@RequestMapping(params = "shortcut_top")
	public ModelAndView shortcut_top(HttpServletRequest request) {
		User user = SessionUtils.getCurrentUser();
		HttpSession session = ContextHolderUtils.getSession();
		// 登陆者的权限
		if (user.getId() == null) {
			session.removeAttribute(Globals.USER_SESSION);
			return new ModelAndView(
					new RedirectView("loginController.do?login"));
		}
		Map<Integer, List<FunctionView>>  menuMap=getFunctionMap(user);
		request.setAttribute("menuMap", menuMap);
		List<Config> configs = userService.findAll(Config.class);
		for (Config tsConfig : configs) {
			request.setAttribute(tsConfig.getCode(), tsConfig.getContents());
		}
		return new ModelAndView("main/shortcut_top");
	}
	
	/**
	 * @throws Exception 
	 * @Title: top
	 * @author:gaofeng
	 * @Description: shortcut头部菜单一级菜单列表，并将其用ajax传到页面，实现动态控制一级菜单列表
	 * @return AjaxJson
	 * @throws
	 */
    @RequestMapping(params = "primaryMenu")
    @ResponseBody
	public String getPrimaryMenu() throws Exception {
    	User user = SessionUtils.getCurrentUser();
    	HttpSession session = ContextHolderUtils.getSession();
    	// 登陆者的权限
		if (user.getId() == null) {
			session.removeAttribute(Globals.USER_SESSION);
			throw new Exception("用户不存在");
		}
		List<FunctionView> primaryMenu = getFunctionMap(user).get(0);
        String floor = "";
        if (primaryMenu == null) {
            return floor;
        }
        for (FunctionView function : primaryMenu) {
            if(function.getFunctionLevel() == 0) {

            	String lang_key = function.getFunctionName();
            	String lang_context = mutiLangService.getLang(lang_key);
            	
                if("Online 开发".equals(lang_context)){

                    floor += " <li><img class='imag1' src='plug-in/login/images/online.png' /> "
                            + " <img class='imag2' src='plug-in/login/images/online_up.png' style='display: none;' />" + " </li> ";
                }else if("统计查询".equals(lang_context)){

                    floor += " <li><img class='imag1' src='plug-in/login/images/guanli.png' /> "
                            + " <img class='imag2' src='plug-in/login/images/guanli_up.png' style='display: none;' />" + " </li> ";
                }else if("系统管理".equals(lang_context)){

                    floor += " <li><img class='imag1' src='plug-in/login/images/xtgl.png' /> "
                            + " <img class='imag2' src='plug-in/login/images/xtgl_up.png' style='display: none;' />" + " </li> ";
                }else if("常用示例".equals(lang_context)){

                    floor += " <li><img class='imag1' src='plug-in/login/images/cysl.png' /> "
                            + " <img class='imag2' src='plug-in/login/images/cysl_up.png' style='display: none;' />" + " </li> ";
                }else if("系统监控".equals(lang_context)){

                    floor += " <li><img class='imag1' src='plug-in/login/images/xtjk.png' /> "
                            + " <img class='imag2' src='plug-in/login/images/xtjk_up.png' style='display: none;' />" + " </li> ";
                }else if(lang_context.contains("消息推送")){
                	String s = "<div style='width:67px;position: absolute;top:40px;text-align:center;color:#909090;font-size:12px;'>消息推送</div>";
                    floor += " <li style='position: relative;'><img class='imag1' src='plug-in/login/images/msg.png' /> "
                            + " <img class='imag2' src='plug-in/login/images/msg_up.png' style='display: none;' />"
                            + s +"</li> ";
                }else{
                    //其他的为默认通用的图片模式
                    String s = "";
                    if(lang_context.length()>=5 && lang_context.length()<7){
                        s = "<div style='width:67px;position: absolute;top:40px;text-align:center;color:#909090;font-size:12px;'><span style='letter-spacing:-1px;'>"+ lang_context +"</span></div>";
                    }else if(lang_context.length()<5){
                        s = "<div style='width:67px;position: absolute;top:40px;text-align:center;color:#909090;font-size:12px;'>"+ lang_context +"</div>";
                    }else if(lang_context.length()>=7){
                        s = "<div style='width:67px;position: absolute;top:40px;text-align:center;color:#909090;font-size:12px;'><span style='letter-spacing:-1px;'>"+ lang_context.substring(0, 6) +"</span></div>";
                    }
                    floor += " <li style='position: relative;'><img class='imag1' src='plug-in/login/images/default.png' /> "
                            + " <img class='imag2' src='plug-in/login/images/default_up.png' style='display: none;' />"
                            + s +"</li> ";
                }
            }
        }
		
		return floor;
	}
	

	/**
	 * 返回数据
	 * @throws Exception 
	 */
	@RequestMapping(params = "getPrimaryMenuForWebos")
	@ResponseBody
	public AjaxJson getPrimaryMenuForWebos() throws Exception {
		AjaxJson j = new AjaxJson();
		//将菜单加载到Session，用户只在登录的时候加载一次
		Object getPrimaryMenuForWebos =  ContextHolderUtils.getSession().getAttribute("getPrimaryMenuForWebos");
		if(ConvertUtils.isNotEmpty(getPrimaryMenuForWebos)){
			j.setMsg(getPrimaryMenuForWebos.toString());
		}else{
			User user = SessionUtils.getCurrentUser();
	    	HttpSession session = ContextHolderUtils.getSession();
	    	// 登陆者的权限
			if (user.getId() == null) {
				session.removeAttribute(Globals.USER_SESSION);
				throw new Exception("用户不存在");
			}
			String PMenu = SystemMenuUtils.getWebosMenu(getFunctionMap(SessionUtils.getCurrentUser()));
			ContextHolderUtils.getSession().setAttribute("getPrimaryMenuForWebos", PMenu);
			j.setMsg(PMenu);
		}
		return j;
	}
}