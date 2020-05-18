package com.abocode.jfaster.admin.system.web;

import com.abocode.jfaster.api.core.AvailableEnum;
import com.abocode.jfaster.admin.system.dto.UploadFileDto;
import com.abocode.jfaster.core.common.model.json.*;
import com.abocode.jfaster.core.common.util.*;
import com.abocode.jfaster.admin.system.service.SystemService;
import com.abocode.jfaster.admin.system.service.UserService;
import com.abocode.jfaster.core.platform.poi.excel.ExcelExportUtil;
import com.abocode.jfaster.core.platform.poi.excel.ExcelImportUtil;
import com.abocode.jfaster.core.platform.poi.excel.entity.ExcelTitle;
import com.abocode.jfaster.core.platform.poi.excel.entity.ImportParams;
import com.abocode.jfaster.core.common.constants.Globals;
import com.abocode.jfaster.core.web.hqlsearch.HqlGenerateUtil;
import com.abocode.jfaster.admin.system.dto.bean.DuplicateBean;
import com.abocode.jfaster.admin.system.repository.ResourceRepository;
import com.abocode.jfaster.admin.system.repository.UserRepository;
import com.abocode.jfaster.admin.system.dto.bean.ExlUserBean;
import com.abocode.jfaster.system.entity.*;
import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.hibernate.criterion.Property;
import com.abocode.jfaster.core.persistence.hibernate.qbc.CriteriaQuery;
import com.abocode.jfaster.core.platform.view.widgets.easyui.TagUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;


/**
 * @author 张代浩
 * @ClassName: UserController
 * @Description: 用户管理处理类
 */
@Scope("prototype")
@Controller
@RequestMapping("/userController")
public class UserController{
    @Autowired
    private ResourceRepository resourceService;
    @Resource
    private UserRepository userService;
    @Resource
    private UserService userApplicationService;
    @Resource
    private SystemService systemApplicationService;

    /**
     * 菜单列表
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "menu")
    public void menu(HttpServletRequest request, HttpServletResponse response) {
        User u = SessionUtils.getCurrentUser();
        String logString = userApplicationService.getMenus(u);
        try {
            response.getWriter().write(logString);
        } catch (IOException e) {
            LogUtils.error(e.getMessage());
        }
    }

    /**
     * 用户列表页面跳转
     *
     * @return
     */
    @RequestMapping(params = "user")
    public String user(HttpServletRequest request) {
        // 给部门查询条件中的下拉框准备数据
        List<Org> departList = userService.getList(Org.class);
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
        User user = SessionUtils.getCurrentUser();
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
        User user = SessionUtils.getCurrentUser();
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
        User user = SessionUtils.getCurrentUser();
        String password = ConvertUtils.getString(request.getParameter("password"));
        String newPassword = ConvertUtils.getString(request.getParameter("newpassword"));
        String pString = PasswordUtils.encrypt(user.getUsername(), password, PasswordUtils.getStaticSalt());
        Assert.isTrue(!pString.equals(user.getPassword()), "原密码不正确");
        try {
            user.setPassword(PasswordUtils.encrypt(user.getUsername(), newPassword, PasswordUtils.getStaticSalt()));
        } catch (Exception e) {
            LogUtils.error(e.getMessage());
        }
        userService.update(user);
        return AjaxJsonBuilder.success("修改成功");
    }


    /**
     * 修改用户密码
     *
     * @author
     */

    @RequestMapping(params = "changepasswordforuser")
    public ModelAndView changepasswordforuser(User user, HttpServletRequest req) {
        if (StringUtils.isNotEmpty(user.getId())) {
            user = userService.findEntity(User.class, user.getId());
            req.setAttribute("userView", user);
            idandname(req, user);
        }
        return new ModelAndView("system/user/adminchangepwd");
    }


    @RequestMapping(params = "savenewpwdforuser")
    @ResponseBody
    public AjaxJson savenewpwdforuser(HttpServletRequest req) {
        String id = ConvertUtils.getString(req.getParameter("id"));
        String password = ConvertUtils.getString(req.getParameter("password"));
        userApplicationService.restPassword(id, password);
        return AjaxJsonBuilder.success();
    }


    /**
     * 锁定账户
     */
    @RequestMapping(params = "lock")
    @ResponseBody
    public AjaxJson lock(String id) {
        User user = userService.findEntity(User.class, id);
        Assert.isTrue("admin".equals(user.getUsername()), "超级管理员[admin]不可锁定");
        Assert.isTrue(Globals.User_Forbidden.equals(user.getStatus()), "锁定账户已经锁定");
        user.setStatus(Globals.User_Forbidden);
        userService.update(user);
        return AjaxJsonBuilder.success( "用户：" + user.getUsername() + "锁定成功");
    }


    /**
     * 得到角色列表
     * @return
     */
    @RequestMapping(params = "role")
    @ResponseBody
    public List<ComboBox> role(HttpServletRequest request, ComboBox comboBox) {
        String id = request.getParameter("id");
        List<ComboBox> comboBoxs = new ArrayList<ComboBox>();
		List<Role> roles = userService.findRoleById(id);
        List<Role> roleList = userService.getList(Role.class);
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
        List<Org> departs = new ArrayList();
        if (StringUtils.isNotEmpty(id)) {
            Object[] object = new Object[]{id};
            List<Org[]> resultList = userService.findByHql("from Depart d,TSUserOrg uo where d.id=uo.orgId and uo.id=?0", object);
            for (Org[] departArr : resultList) {
                departs.add(departArr[0]);
            }
        }
        List<Org> departList = userService.getList(Org.class);
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
    public void datagrid(User user, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
        String orgIds = request.getParameter("orgIds");
        CriteriaQuery cq = this.buildCq(user, dataGrid, orgIds);
        this.userService.findDataGridReturn(cq, true);
        TagUtil.datagrid(response, dataGrid);
    }

    /***
     * build 查询条件
     * @param user
     * @param dataGrid
     * @param orgIds
     */
    private CriteriaQuery buildCq(User user, DataGrid dataGrid, String orgIds) {
        CriteriaQuery cq = new CriteriaQuery(User.class, dataGrid);
        //查询条件组装器
        HqlGenerateUtil.installHql(cq, user);

        Short[] userstate = new Short[]{Globals.User_Normal, Globals.User_ADMIN, Globals.User_Forbidden};
        cq.in("status", userstate);

        List<String> orgIdList = IdUtils.extractIdListByComma(orgIds);
        // 获取 当前组织机构的用户信息
        if (!CollectionUtils.isEmpty(orgIdList)) {
            CriteriaQuery subCq = new CriteriaQuery(UserOrg.class);
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
    public AjaxJson del(User user, HttpServletRequest req) {
        AjaxJson j = new AjaxJson();
        String message;
        if ("admin".equals(user.getUsername())) {
            message = "超级管理员[admin]不可删除";
            j.setMsg(message);
            return j;
        }
        user = userService.findEntity(User.class, user.getId());
        List<RoleUser> roleUser = userService.findAllByProperty(RoleUser.class, "user.id", user.getId());
        if (!user.getStatus().equals(Globals.User_ADMIN)) {
            if (roleUser.size() > 0) {
                // 删除用户时先删除用户和角色关系表
                delRoleUser(user);
                userService.executeSql("delete from t_s_user_org where user_id=?", user.getId()); // 删除 用户-机构 数据
                userService.delete(user);
                message = "用户：" + user.getUsername() + "删除成功";
            } else {
                userService.delete(user);
                message = "用户：" + user.getUsername() + "删除成功";
            }
        } else {
            message = "超级管理员不可删除";
        }

        j.setMsg(message);
        return j;
    }

    public void delRoleUser(User user) {
        // 同步删除用户角色关联表
        List<RoleUser> roleUserList = userService.findAllByProperty(RoleUser.class, "user.id", user.getId());
        if (roleUserList.size() >= 1) {
            for (RoleUser tRoleUser : roleUserList) {
                userService.delete(tRoleUser);
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
        String username = ConvertUtils.getString(request.getParameter("param"));
        String code = ConvertUtils.getString(request.getParameter("code"));
        List<User> roles = userService.findAllByProperty(User.class, "username", username);
        if (roles.size() > 0 && !code.equals(username)) {
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
    public AjaxJson saveUser(HttpServletRequest req, User user) {
        AjaxJson j = new AjaxJson();
        // 得到用户的角色
        String message;
        String roleid = ConvertUtils.getString(req.getParameter("roleid"));
        String password = ConvertUtils.getString(req.getParameter("password"));
        if (StringUtils.isNotEmpty(user.getId())) {
            User users = userService.findEntity(User.class, user.getId());
            users.setEmail(user.getEmail());
            users.setOfficePhone(user.getOfficePhone());
            users.setMobilePhone(user.getMobilePhone());
            userService.executeSql("delete from t_s_user_org where user_id=?", user.getId());
            saveUserOrgList(req, user);
//            users.setOrg(user.getDepart());
            users.setRealName(user.getRealName());
            users.setStatus(Globals.User_Normal);
            userService.update(users);
            List<RoleUser> ru = userService.findAllByProperty(RoleUser.class, "user.id", user.getId());
            userService.deleteEntities(ru);
            message = "用户: " + users.getUsername() + "更新成功";
            if (StringUtils.isNotEmpty(roleid)) {
                saveRoleUser(users, roleid);
            }
        } else {
            User users = userService.findUniqueByProperty(User.class, "username", user.getUsername());
            if (users != null) {
                message = "用户: " + users.getUsername() + "已经存在";
            } else {
                user.setPassword(PasswordUtils.encrypt(user.getUsername(), password, PasswordUtils.getStaticSalt()));
                user.setStatus(Globals.User_Normal);
                userService.save(user);
                saveUserOrgList(req, user);
                message = "用户: " + user.getUsername() + "添加成功";
                if (StringUtils.isNotEmpty(roleid)) {
                    saveRoleUser(user, roleid);
                }
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
    private void saveUserOrgList(HttpServletRequest request, User user) {
        String orgIds = ConvertUtils.getString(request.getParameter("orgIds"));

        List<UserOrg> userOrgList = new ArrayList<UserOrg>();
        List<String> orgIdList = IdUtils.extractIdListByComma(orgIds);
        for (String orgId : orgIdList) {
            Org depart = new Org();
            depart.setId(orgId);

            UserOrg userOrg = new UserOrg();
            userOrg.setUser(user);
            userOrg.setOrg(depart);

            userOrgList.add(userOrg);
        }
        if (!userOrgList.isEmpty()) {
            userService.batchSave(userOrgList);
        }
    }

    protected void saveRoleUser(User user, String roleidstr) {
        String[] roleids = roleidstr.split(",");
        for (int i = 0; i < roleids.length; i++) {
            RoleUser rUser = new RoleUser();
            Role role = userService.findEntity(Role.class, roleids[i]);
            rUser.setRole(role);
            rUser.setUser(user);
            userService.save(rUser);

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
        CriteriaQuery cq = new CriteriaQuery(Role.class, dataGrid);
        this.userService.findDataGridReturn(cq, true);
        TagUtil.datagrid(response, dataGrid);
    }

    /**
     * easyuiAJAX请求数据： 用户选择角色列表
     *
     * @param user
     */
    @RequestMapping(params = "addorupdate")
    public ModelAndView addorupdate(User user, HttpServletRequest req) {
        List<Org> departList = new ArrayList<Org>();
        String departid = ConvertUtils.getString(req.getParameter("departid"));
        if (!StringUtils.isEmpty(departid)) {
            departList.add((Org) userService.findEntity(Org.class, departid));
        } else {
            departList.addAll((List) userService.getList(Org.class));
        }
        req.setAttribute("departList", departList);
        List<String> orgIdList = new ArrayList<String>();
        if (StringUtils.isNotEmpty(user.getId())) {
            user = userService.findEntity(User.class, user.getId());
            req.setAttribute("userView", user);
            idandname(req, user);
            orgIdList = userService.findByHql("select d.id from Depart d,UserOrg uo where d.id=uo.tsDepart.id and uo.tsUser.id=?0", new String[]{user.getId()});
        }
        req.setAttribute("orgIdList", new Gson().toJson(orgIdList));
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
        User u = (User) request.getSession().getAttribute("user");
        String userId = u != null ? u.getId() : null;
        if (StringUtils.isEmpty(userId)) {
            userId = ConvertUtils.getString(request.getParameter("userId"));
        }
        List<Org> orgList = new ArrayList<Org>();
        List<Object[]> orgArrList = userService.findByHql("from Depart d,UserOrg uo where d.id=uo.tsDepart.id and uo.tsUser.id=?0", new String[]{userId});
        for (Object[] departs : orgArrList) {
            orgList.add((Org) departs[0]);
        }
        request.setAttribute("orgList", orgList);
        User user = userService.findEntity(User.class, userId);
        request.setAttribute("user", user);
        return new ModelAndView("system/user/userOrgSelect");
    }

    public void idandname(HttpServletRequest req, User user) {
        List<RoleUser> roleUsers = userService.findAllByProperty(RoleUser.class, "user.id", user.getId());
        String roleId = "";
        String roleName = "";
        if (roleUsers.size() > 0) {
            for (RoleUser tRoleUser : roleUsers) {
                roleId += tRoleUser.getRole().getId() + ",";
                roleName += tRoleUser.getRole().getRoleName() + ",";
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
        List<Role> roles = userService.findAll(Role.class);
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
        CriteriaQuery cq = new CriteriaQuery(User.class, dataGrid);
        if (departid.length() > 0) {
            cq.eq("TDepart.departid", ConvertUtils.getInt(departid, 0));
            cq.add();
        }
        String userid = "";
        if (roleid.length() > 0) {
            List<RoleUser> roleUsers = userService.findAllByProperty(RoleUser.class, "TRole.roleid", ConvertUtils.getInt(roleid, 0));
            if (roleUsers.size() > 0) {
                for (RoleUser tRoleUser : roleUsers) {
                    userid += tRoleUser.getUser().getId() + ",";
                }
            }
            cq.in("userid", ConvertUtils.getInts(userid.split(",")));
            cq.add();
        }
        this.userService.findDataGridReturn(cq, true);
        TagUtil.datagrid(response, dataGrid);
    }

    /**
     * 根据部门和角色选择用户跳转页面
     */
    @RequestMapping(params = "roleDepart")
    public String roleDepart(HttpServletRequest request) {
        List<Role> roles = userService.findAll(Role.class);
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
        CriteriaQuery cq = new CriteriaQuery(Org.class, dataGrid);
        userService.findDataGridReturn(cq, true);
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
        UploadFileDto uploadFile = new UploadFileDto(req);
        String id = uploadFile.get("id");
        User user = userService.findEntity(User.class, id);
        uploadFile.setRealPath("signatureFile");
        uploadFile.setCusPath("signature");
        uploadFile.setByteField("signature");
        uploadFile.setBasePath("resources");
        uploadFile.setRename(false);
        uploadFile.setObject(user);
        AjaxJson j = new AjaxJson();
        String message = user.getUsername() + "设置签名成功";
        resourceService.uploadFile(uploadFile);
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
    public void testSearch(User user, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
        CriteriaQuery cq = new CriteriaQuery(User.class, dataGrid);
        if (user.getUsername() != null) {
            cq.like("username", user.getUsername());
        }
        if (user.getRealName() != null) {
            cq.like("realName", user.getRealName());
        }
        cq.add();
        this.userService.findDataGridReturn(cq, true);
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
    public ModelAndView importUser(User user, HttpServletRequest request) {
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
            LogUtils.error(e.getMessage());
        } finally {
            try {
                fOut.flush();
                fOut.close();
            } catch (IOException e) {
                LogUtils.error(e.getMessage());
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
        List<ExlUserBean> userList;
        List<User> userEntities = new ArrayList<User>();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            try {
                //解析文件
                file = entity.getValue();
                userList = (List<ExlUserBean>) ExcelImportUtil
                        .importExcelByIs(file.getInputStream(),
                                ExlUserBean.class, new ImportParams());
                //验证文件
                if (null == userList || userList.size() == 0) {
                    j.setMsg("<font color='red'>失败!</font> Excel中没有可以导入的数据");
                    return j;
                }

                for (ExlUserBean exlUserVo : userList) {
                    j = ValidateUtils.volatileBean(exlUserVo);
                    if (!j.isSuccess()) {
                        j.setSuccess(true);
                        return j;
                    }
                    //判断帐号是否存在
                    User u = this.userService.findUniqueByProperty(User.class, "username", exlUserVo.getUsername());
                    if (StringUtils.isNotEmpty(u)) {
                        j.setMsg("<font color='red'>失败!</font>" + exlUserVo.getUsername() + " 帐号已经存在");
                        return j;
                    }

                    //判断组织机构是否存在
                    List<Org> exlDeparts = new ArrayList<Org>();
                    String[] departNames = exlUserVo.getOrgName().split(",");
                    for (int i = 0; i < departNames.length; i++) {
                        List<Org> departs = userService.findAllByProperty(Org.class, "departname", departNames[i]);
                        if (departs.size() == 0) {
                            j.setMsg("<font color='red'>失败!</font>" + exlUserVo.getOrgName() + " 组织机构不存在");
                            return j;
                        }
                        exlDeparts.add(departs.get(0));
                    }


                    List<Role> exlRoles = new ArrayList<Role>();
                    String[] roleNames = exlUserVo.getRoleName().split(",");
                    for (int i = 0; i < roleNames.length; i++) {
                        //判断角色是否存在
                        List<Role> roles = userService.findAllByProperty(Role.class, "roleName", roleNames[i]);
                        if (roles.size() == 0) {
                            j.setMsg("<font color='red'>失败!</font>" + exlUserVo.getRoleName() + " 角色不存在");
                            return j;
                        }
                        exlRoles.add(roles.get(0));
                    }

                    User userEntity = new User();
                    BeanUtils.copyProperties(exlUserVo, userEntity);
                    userEntity.setOrgs(exlDeparts);
                    userEntity.setRoles(exlRoles);
                    userEntity.setStatus(AvailableEnum.AVAILABLE.getShortValue());
                    userEntities.add(userEntity);
                }

                for (User userEntity : userEntities) {
                    String pwd = userEntity.getPassword();
                    userEntity.setPassword(null);
                    String uid = (String) this.userService.save(userEntity);
                    userEntity = this.userService.find(User.class, uid);
                    userEntity.setPassword(PasswordUtils.encrypt(userEntity.getUsername(), pwd, PasswordUtils.getStaticSalt()));
                    userService.update(userEntity);

                    //保存组织机构
                    for (Org depart : userEntity.getOrgs()) {
                        UserOrg userOrg = new UserOrg();
                        userOrg.setUser(userEntity);
                        userOrg.setOrg(depart);
                        this.userService.save(userOrg);
                    }

                    //保存角色
                    for (Role role : userEntity.getRoles()) {
                        RoleUser roleUser = new RoleUser();
                        roleUser.setRole(role);
                        roleUser.setUser(userEntity);
                        this.userService.save(roleUser);
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
    public void exportUser(User user, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
        dataGrid.setPage(0);
        dataGrid.setRows(1000000);
        String orgIds = request.getParameter("orgIds");
        CriteriaQuery cq = this.buildCq(user, dataGrid, orgIds);
        // 生成提示信息，
        response.setContentType("application/vnd.ms-excel");
        OutputStream fOut = null;
        try {
            String exportFileName = "用户列表" + DateUtils.formatDate(new Date(), DateUtils.YYYY_MM_DD_HH_MM_SS);
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
            List<ExlUserBean> exlUserList = this.userService.getExlUserList(dataGrid, user, cq);
            HSSFWorkbook workbook = ExcelExportUtil.exportExcel(new ExcelTitle(null, null, exportFileName), ExlUserBean.class, exlUserList);
            fOut = response.getOutputStream();
            workbook.write(fOut);
        } catch (Exception e) {
            LogUtils.error(e.getMessage());
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


    /**
     * 校验数据是否在系统中是否存在
     *
     * @return
     */
    @RequestMapping(params = "doDuplicateCheck")
    @ResponseBody
    public AjaxJson doDuplicateCheck(DuplicateBean duplicateCheckPage) {
        AjaxJson j = new AjaxJson();
        Long num = systemApplicationService.findCountByTable(duplicateCheckPage);
        if (num == null || num == 0) {
            //该值可用
            j.setSuccess(true);
            j.setMsg("该值可用！");
        } else {
            //该值不可用
            j.setSuccess(false);
            j.setMsg("该值不可用，系统中已存在！");
        }
        return j;
    }
}