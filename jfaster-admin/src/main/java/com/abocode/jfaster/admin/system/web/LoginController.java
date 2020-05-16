package com.abocode.jfaster.admin.system.web;

import com.abocode.jfaster.admin.system.service.UserLoginService;
import com.abocode.jfaster.api.core.AvailableEnum;
import com.abocode.jfaster.core.common.constants.Globals;
import com.abocode.jfaster.core.common.container.SystemContainer;
import com.abocode.jfaster.core.common.model.json.AjaxJson;
import com.abocode.jfaster.core.common.model.json.AjaxJsonBuilder;
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
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * 登陆初始化控制器
 */
@Scope("prototype")
@Controller
@RequestMapping("/loginController")
public class LoginController extends BaseController {
    @Autowired
    private SystemRepository systemService;
    @Autowired
    private UserRepository userService;
    @Autowired
    private MutiLangRepository mutiLangService;
    @Autowired
    private TemplateRepository templateService;
    @Autowired
    private FunctionService functionService;
    @Autowired
    private UserLoginService userLoginService;

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
        if (request.getParameter("langCode") != null) {
            request.getSession().setAttribute("lang", request.getParameter("langCode"));
        }
        HttpSession session = ContextHolderUtils.getSession();
        DataSourceContextHolder.setDataSourceType(DataSourceType.dataSource_jeecg);
        String randCode = request.getParameter("randCode");
        Assert.hasText(randCode, mutiLangService.getLang("common.enter.verifycode"));
        Assert.isTrue(!randCode.equalsIgnoreCase(String.valueOf(session.getAttribute("randCode"))), mutiLangService.getLang("common.verifycode.error"));
        User u = userService.checkUserExits(user.getUsername(), user.getPassword());
        Assert.isTrue(u != null, mutiLangService.getLang("common.username.or.password.error"));
        Assert.isTrue(u.getStatus() != 0, mutiLangService.getLang("common.username.not.activation"));
        request.getSession().setAttribute("user", u); //用于切换部门时使用
        String orgId = request.getParameter("orgId");
        String ip = com.abocode.jfaster.core.common.util.StringUtils.getIpAddr(request);
        Map<String, Object> attrMap = userLoginService.getLoginMap(u, orgId, ip);
        return AjaxJsonBuilder.success().setAttributes(attrMap);
    }


    /**
     * 用户登录
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "login")
    public String login(ModelMap modelMap, HttpServletRequest request) {
        DataSourceContextHolder.setDataSourceType(DataSourceType.dataSource_jeecg);
        Template templateEntity = this.templateService.findUniqueByProperty(Template.class, "status", AvailableEnum.AVAILABLE.getValue());
        Assert.isTrue(templateEntity!=null,"未找到相关的模版");

        User user = SessionUtils.getCurrentUser();
        String roles = "";
        if (user != null) {
            List<Role> roleList=userLoginService.cahModelMap(modelMap,user.getId());
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
     *
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