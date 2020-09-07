package com.abocode.jfaster.admin.system.web;

import com.abocode.jfaster.admin.system.service.UserLoginService;
import com.abocode.jfaster.api.core.AvailableEnum;
import com.abocode.jfaster.core.common.constants.Globals;
import com.abocode.jfaster.core.persistence.datasource.DataSourceType;
import com.abocode.jfaster.core.platform.SystemContainer;
import com.abocode.jfaster.core.common.model.json.AjaxJson;
import com.abocode.jfaster.core.common.model.json.AjaxJsonBuilder;
import com.abocode.jfaster.core.common.util.*;
import com.abocode.jfaster.core.persistence.datasource.DataSourceContextHolder;
import com.abocode.jfaster.core.platform.utils.SystemMenuUtils;
import com.abocode.jfaster.admin.system.service.manager.ClientManager;
import com.abocode.jfaster.admin.system.repository.LanguageRepository;
import com.abocode.jfaster.admin.system.repository.SystemRepository;
import com.abocode.jfaster.admin.system.repository.TemplateRepository;
import com.abocode.jfaster.admin.system.repository.UserRepository;
import com.abocode.jfaster.admin.system.service.FunctionService;
import com.abocode.jfaster.core.platform.view.FunctionView;
import com.abocode.jfaster.core.platform.view.TemplateView;
import com.abocode.jfaster.admin.system.service.manager.SessionShareCenter;
import com.abocode.jfaster.admin.system.service.manager.SessionHolder;
import com.abocode.jfaster.system.entity.*;
import com.google.gson.Gson;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
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
public class LoginController {
    @Autowired
    private SystemRepository systemService;
    @Autowired
    private UserRepository userService;
    @Autowired
    private LanguageRepository languageRepository;
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
        DataSourceContextHolder.setDataSourceType(DataSourceType.DEFAULT);
        String randCode = request.getParameter("randCode");
        Assert.hasText(randCode, languageRepository.getLang("common.enter.verifycode"));
        Assert.isTrue(randCode.equalsIgnoreCase(String.valueOf(session.getAttribute("randCode"))), languageRepository.getLang("common.verifycode.error"));
        User u = userService.checkUserExits(user.getUsername(), user.getPassword());
        Assert.isTrue(u != null, languageRepository.getLang("common.username.or.password.error"));
        Assert.isTrue(u.getStatus() != 0, languageRepository.getLang("common.username.not.activation"));
        request.getSession().setAttribute("user", u); //用于切换部门时使用
        String orgId = request.getParameter("orgId");
        String ip = StrUtils.getIpAddr(request);
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
        DataSourceContextHolder.setDataSourceType(DataSourceType.DEFAULT);
        Template templateEntity = this.templateService.findUniqueByProperty(Template.class, "status", AvailableEnum.AVAILABLE.getValue());
        Assert.isTrue(templateEntity != null, "未找到相关的模版");

        User user = SessionHolder.getCurrentUser();
        if (user != null) {
            List<Role> roleList = userLoginService.cahModelMap(modelMap, user.getId());
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
            SystemContainer.TemplateContainer.putTemplate( gson.toJson(templateBean));
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
        User user = SessionHolder.getCurrentUser();
        systemService.addLog("用户" + user.getUsername() + "已退出",
                Globals.LOG_TYPE_EXIT, Globals.LOG_LEVEL);
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
        User user = SessionHolder.getCurrentUser();
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
     * 首页跳转
     *
     * @return
     */
    @RequestMapping(params = "home")
    public ModelAndView home() {
        return new ModelAndView("main/home");
    }

    /**
     * 无权限页面提示跳转
     *
     * @return
     */
    @RequestMapping(params = "noAuth")
    public ModelAndView noAuth() {
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
        User user = SessionHolder.getCurrentUser();
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
     * @Description: shortcut头部菜单请求
     */
    @RequestMapping(params = "shortcut_top")
    public ModelAndView shortcut_top(HttpServletRequest request) {
        User user = SessionHolder.getCurrentUser();
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
     * @Description: shortcut头部菜单一级菜单列表，并将其用ajax传到页面，实现动态控制一级菜单列表
     */
    @RequestMapping(params = "primaryMenu")
    @ResponseBody
    public String getPrimaryMenu() throws Exception {
        User user = SessionHolder.getCurrentUser();
        HttpSession session = ContextHolderUtils.getSession();
        // 登陆者的权限
        if (user.getId() == null) {
            session.removeAttribute(Globals.USER_SESSION);
            throw new Exception("用户不存在");
        }
        return functionService.getPrimaryMenu(user);
    }


    /**
     * 返回数据
     * @throws Exception
     */
    @RequestMapping(params = "getPrimaryMenuForWebos")
    @ResponseBody
    public AjaxJson getPrimaryMenuForWebos() throws Exception {
        //将菜单加载到Session，用户只在登录的时候加载一次
        Object getPrimaryMenuForWebos = ContextHolderUtils.getSession().getAttribute("getPrimaryMenuForWebos");
        String message;
        if (ConvertUtils.isEmpty(getPrimaryMenuForWebos)) {
            User user = SessionHolder.getCurrentUser();
            HttpSession session = ContextHolderUtils.getSession();
            // 登陆者的权限
            if (user.getId() == null) {
                session.removeAttribute(Globals.USER_SESSION);
                throw new Exception("用户不存在");
            }
            message = SystemMenuUtils.getWebosMenu(functionService.getFunctionMap(SessionHolder.getCurrentUser()));
            ContextHolderUtils.getSession().setAttribute("getPrimaryMenuForWebos", message);
        }else {
            message=getPrimaryMenuForWebos.toString();
        }
        return AjaxJsonBuilder.success(message);
    }
}