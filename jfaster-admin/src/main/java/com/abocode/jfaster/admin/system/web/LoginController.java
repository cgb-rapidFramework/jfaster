package com.abocode.jfaster.admin.system.web;

import com.abocode.jfaster.api.core.AvailableEnum;
import com.abocode.jfaster.core.common.constants.Globals;
import com.abocode.jfaster.core.common.container.SystemContainer;
import com.abocode.jfaster.core.common.model.json.AjaxJson;
import com.abocode.jfaster.core.common.util.*;
import com.abocode.jfaster.core.extend.datasource.DataSourceContextHolder;
import com.abocode.jfaster.core.extend.datasource.DataSourceType;
import com.abocode.jfaster.core.interfaces.BaseController;
import com.abocode.jfaster.core.web.manager.ClientManager;
import com.abocode.jfaster.admin.system.repository.MutiLangRepository;
import com.abocode.jfaster.admin.system.repository.SystemRepository;
import com.abocode.jfaster.admin.system.repository.TemplateRepository;
import com.abocode.jfaster.admin.system.repository.UserRepository;
import com.abocode.jfaster.admin.system.service.FunctionService;
import com.abocode.jfaster.admin.system.dto.bean.ClientBean;
import com.abocode.jfaster.admin.system.dto.view.FunctionView;
import com.abocode.jfaster.admin.system.dto.view.TemplateView;
import com.abocode.jfaster.system.entity.*;
import com.google.gson.Gson;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
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
 */
@Scope("prototype")
@Controller
@RequestMapping("/loginController")
public class LoginController extends BaseController {
    private SystemRepository systemService;
    private UserRepository userService;
    private String message = null;

    @Autowired
    private MutiLangRepository mutiLangService;
    @Autowired
    private TemplateRepository templateService;
    @Autowired
    private FunctionService functionService;

    @Autowired
    public void setSystemService(SystemRepository systemService) {
        this.systemService = systemService;
    }

    @Autowired
    public void setUserService(UserRepository userService) {
        this.userService = userService;
    }

    @RequestMapping(params = "goPwdInit")
    public String goPwdInit() {
        return "login/pwd_init";
    }

    /**
     * admin账户密码初始化
     *
     * @return
     */
    @RequestMapping(params = "pwdInit")
    public ModelAndView pwdInit() {
        User user = new User();
        user.setUsername("admin");
        String newPwd = "123456";
        userService.pwdInit(user, newPwd);
        ModelAndView modelAndView = new ModelAndView(new RedirectView(
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
    @RequestMapping(params = "checkuser")
    @ResponseBody
    public AjaxJson checkuser(User user, HttpServletRequest request) {
        HttpSession session = ContextHolderUtils.getSession();
        DataSourceContextHolder
                .setDataSourceType(DataSourceType.dataSource_jeecg);
        AjaxJson j = new AjaxJson();
        if (request.getParameter("langCode") != null) {
            request.getSession().setAttribute("lang", request.getParameter("langCode"));
        }
        String randCode = request.getParameter("randCode");
        if (StringUtils.isEmpty(randCode)) {
            j.setMsg(mutiLangService.getLang("common.enter.verifycode"));
            j.setSuccess(false);
        } else if (!randCode.equalsIgnoreCase(String.valueOf(session.getAttribute("randCode")))) {
            j.setMsg(mutiLangService.getLang("common.verifycode.error"));
            j.setSuccess(false);
        } else {
            User u = userService.checkUserExits(user);
            if (u == null) {
                j.setMsg(mutiLangService.getLang("common.username.or.password.error"));
                j.setSuccess(false);
                return j;
            }
            if (u != null && u.getStatus() != 0) {
                Map<String, Object> attrMap = new HashMap<String, Object>();
                j.setAttributes(attrMap);
                request.getSession().setAttribute("user", u); //用于切换部门时使用
                String orgId = request.getParameter("orgId");
                if (ConvertUtils.isEmpty(orgId)) { // 没有传组织机构参数，则获取当前用户的组织机构
//					int orgNum=u.getUserOrgList().size();
                    //获取默认部门
                    Long orgNum = systemService.queryForCount("select count(1) from t_s_user_org where user_id =?", new Object[]{u.getId()});
                    if (orgNum > 1) {
                        User res = new User();
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
     *
     * @param req   request
     * @param user  当前登录用户
     * @param orgId 组织主键
     */
    private void saveLoginSuccessInfo(HttpServletRequest req, User user, String orgId) {
        Org currentDepart = systemService.find(Org.class, orgId);
        user.setCurrentDepart(currentDepart);

        HttpSession session = ContextHolderUtils.getSession();
        message = mutiLangService.getLang("common.user") + ": " + user.getUsername() + "["
                + currentDepart.getOrgName() + "]" + mutiLangService.getLang("common.login.success");

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
    public String login(ModelMap modelMap, HttpServletRequest request) {
        Template templateEntity = this.templateService.findUniqueByProperty(Template.class, "status", AvailableEnum.AVAILABLE.getValue());

        DataSourceContextHolder.setDataSourceType(DataSourceType.dataSource_jeecg);
        User user = SessionUtils.getCurrentUser();
        String roles = "";
        if (user != null) {
            List<Role> roleList = new ArrayList();
            List<RoleUser> rUsers = systemService.findAllByProperty(RoleUser.class, "user.id", user.getId());
            for (RoleUser ru : rUsers) {
                Role role = ru.getRole();
                roles += role.getRoleName() + ",";
                roleList.add(role);
            }
            if (roles.length() > 0) {
                roles = roles.substring(0, roles.length() - 1);
            }
            modelMap.put("roleName", roles);
            modelMap.put("username", user.getUsername());
            modelMap.put("currentOrgName", ClientManager.getInstance().getClient().getUser().getCurrentDepart().getOrgName());
            //将角色信息放入session
            SessionShareCenter.putRoles(roleList);
            ClientManager.getInstance().getClient().setRoles(roleList);
            //request.getSession().setAttribute("lang", "en");
            Gson gson = new Gson();
            TemplateView templateBean = new TemplateView();
            BeanUtils.copyProperties(templateEntity, templateBean);
            //设置主题
            String systemTemplate = gson.toJson(templateBean);
            SystemContainer.TemplateContainer.template.put("SYSTEM-TEMPLATE", systemTemplate);

            //放置语言
			/*String langCode= (String) request.getSession().getAttribute("lang");
			Cookie cookie=CacheUtils.putCookie("SYSTEM-LANGCODE",langCode);
			response.addCookie(cookie);*/
            //加载菜单
            request.setAttribute("menuMap", functionService.getFunctionMap(user));
            return templateEntity.getPageMain();
        } else {
            return templateEntity.getPageLogin();
        }

    }

    /**
     * 退出系统
     * @return
     */
    @RequestMapping(params = "logout")
    public ModelAndView logout() {
        User user = SessionUtils.getCurrentUser();
        systemService.addLog("用户" + user.getUsername() + "已退出",
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
        } else {
            List<Config> configs = userService.findAll(Config.class);
            for (Config tsConfig : configs) {
                request.setAttribute(tsConfig.getCode(), tsConfig.getContent());
            }
            modelAndView.setViewName("main/left");
            request.setAttribute("menuMap", functionService.getFunctionMap(user));
        }
        return modelAndView;
    }


    /**
     * 根据 角色实体 组装 用户权限列表
     *
     * @param loginActionlist 登录用户的权限列表
     * @param role            角色实体
     */
    private void assembleFunctionsByRole(Map<String, Function> loginActionlist, Role role) {
        List<RoleFunction> roleFunctionList = systemService.findAllByProperty(RoleFunction.class, "role.id", role.getId());
        for (RoleFunction roleFunction : roleFunctionList) {
            Function function = roleFunction.getFunction();
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
     * @param request
     * @return ModelAndView
     * @throws
     * @Title: top
     * @Description: bootstrap头部菜单请求
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
        request.setAttribute("menuMap", functionService.getFunctionMap(user));
        List<Config> configs = userService.findAll(Config.class);
        for (Config tsConfig : configs) {
            request.setAttribute(tsConfig.getCode(), tsConfig.getContent());
        }
        return new ModelAndView("main/bootstrap_top");
    }

    /**
     * @param request
     * @return ModelAndView
     * @throws
     * @Title: top
     * @author gaofeng
     * @Description: shortcut头部菜单请求
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
        Map<Integer, List<FunctionView>> menuMap = functionService.getFunctionMap(user);
        request.setAttribute("menuMap", menuMap);
        List<Config> configs = userService.findAll(Config.class);
        for (Config tsConfig : configs) {
            request.setAttribute(tsConfig.getCode(), tsConfig.getContent());
        }
        return new ModelAndView("main/shortcut_top");
    }

    /**
     * @return AjaxJson
     * @throws Exception
     * @throws
     * @Title: top
     * @author:gaofeng
     * @Description: shortcut头部菜单一级菜单列表，并将其用ajax传到页面，实现动态控制一级菜单列表
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
        List<FunctionView> primaryMenu = functionService.getFunctionMap(user).get(0);
        String floor = "";
        if (primaryMenu == null) {
            return floor;
        }
        for (FunctionView function : primaryMenu) {
            if (function.getFunctionLevel() == 0) {

                String lang_key = function.getFunctionName();
                String lang_context = mutiLangService.getLang(lang_key);

                if ("Online 开发".equals(lang_context)) {

                    floor += " <li><img class='imag1' src='plug-in/login/images/online.png' /> "
                            + " <img class='imag2' src='plug-in/login/images/online_up.png' style='display: none;' />" + " </li> ";
                } else if ("统计查询".equals(lang_context)) {

                    floor += " <li><img class='imag1' src='plug-in/login/images/guanli.png' /> "
                            + " <img class='imag2' src='plug-in/login/images/guanli_up.png' style='display: none;' />" + " </li> ";
                } else if ("系统管理".equals(lang_context)) {

                    floor += " <li><img class='imag1' src='plug-in/login/images/xtgl.png' /> "
                            + " <img class='imag2' src='plug-in/login/images/xtgl_up.png' style='display: none;' />" + " </li> ";
                } else if ("常用示例".equals(lang_context)) {

                    floor += " <li><img class='imag1' src='plug-in/login/images/cysl.png' /> "
                            + " <img class='imag2' src='plug-in/login/images/cysl_up.png' style='display: none;' />" + " </li> ";
                } else if ("系统监控".equals(lang_context)) {

                    floor += " <li><img class='imag1' src='plug-in/login/images/xtjk.png' /> "
                            + " <img class='imag2' src='plug-in/login/images/xtjk_up.png' style='display: none;' />" + " </li> ";
                } else if (lang_context.contains("消息推送")) {
                    String s = "<div style='width:67px;position: absolute;top:40px;text-align:center;color:#909090;font-size:12px;'>消息推送</div>";
                    floor += " <li style='position: relative;'><img class='imag1' src='plug-in/login/images/msg.png' /> "
                            + " <img class='imag2' src='plug-in/login/images/msg_up.png' style='display: none;' />"
                            + s + "</li> ";
                } else {
                    //其他的为默认通用的图片模式
                    String s = "";
                    if (lang_context.length() >= 5 && lang_context.length() < 7) {
                        s = "<div style='width:67px;position: absolute;top:40px;text-align:center;color:#909090;font-size:12px;'><span style='letter-spacing:-1px;'>" + lang_context + "</span></div>";
                    } else if (lang_context.length() < 5) {
                        s = "<div style='width:67px;position: absolute;top:40px;text-align:center;color:#909090;font-size:12px;'>" + lang_context + "</div>";
                    } else if (lang_context.length() >= 7) {
                        s = "<div style='width:67px;position: absolute;top:40px;text-align:center;color:#909090;font-size:12px;'><span style='letter-spacing:-1px;'>" + lang_context.substring(0, 6) + "</span></div>";
                    }
                    floor += " <li style='position: relative;'><img class='imag1' src='plug-in/login/images/default.png' /> "
                            + " <img class='imag2' src='plug-in/login/images/default_up.png' style='display: none;' />"
                            + s + "</li> ";
                }
            }
        }

        return floor;
    }


    /**
     * 返回数据
     *
     * @throws Exception
     */
    @RequestMapping(params = "getPrimaryMenuForWebos")
    @ResponseBody
    public AjaxJson getPrimaryMenuForWebos() throws Exception {
        AjaxJson j = new AjaxJson();
        //将菜单加载到Session，用户只在登录的时候加载一次
        Object getPrimaryMenuForWebos = ContextHolderUtils.getSession().getAttribute("getPrimaryMenuForWebos");
        if (ConvertUtils.isNotEmpty(getPrimaryMenuForWebos)) {
            j.setMsg(getPrimaryMenuForWebos.toString());
        } else {
            User user = SessionUtils.getCurrentUser();
            HttpSession session = ContextHolderUtils.getSession();
            // 登陆者的权限
            if (user.getId() == null) {
                session.removeAttribute(Globals.USER_SESSION);
                throw new Exception("用户不存在");
            }
            String PMenu = SystemMenuUtils.getWebosMenu(functionService.getFunctionMap(SessionUtils.getCurrentUser()));
            ContextHolderUtils.getSession().setAttribute("getPrimaryMenuForWebos", PMenu);
            j.setMsg(PMenu);
        }
        return j;
    }
}