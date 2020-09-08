package com.abocode.jfaster.admin.system.web;

import com.abocode.jfaster.admin.system.dto.ExlUserDto;
import com.abocode.jfaster.admin.system.dto.FileUploadDto;
import com.abocode.jfaster.admin.system.dto.RoleIdAndNameDto;
import com.abocode.jfaster.admin.system.repository.ResourceRepository;
import com.abocode.jfaster.admin.system.repository.UserRepository;
import com.abocode.jfaster.admin.system.service.OrgService;
import com.abocode.jfaster.admin.system.service.RoleService;
import com.abocode.jfaster.admin.system.service.UserService;
import com.abocode.jfaster.core.common.model.json.AjaxJson;
import com.abocode.jfaster.core.common.model.json.AjaxJsonBuilder;
import com.abocode.jfaster.core.common.model.json.ComboBox;
import com.abocode.jfaster.core.common.model.json.ValidForm;
import com.abocode.jfaster.core.common.util.*;
import com.abocode.jfaster.core.persistence.hibernate.qbc.CriteriaQuery;
import com.abocode.jfaster.core.platform.poi.excel.ExcelExportUtil;
import com.abocode.jfaster.core.platform.poi.excel.entity.ExcelTitle;
import com.abocode.jfaster.core.repository.DataGridData;
import com.abocode.jfaster.core.repository.DataGridParam;
import com.abocode.jfaster.core.repository.TagUtil;
import com.abocode.jfaster.core.web.manager.SessionHolder;
import com.abocode.jfaster.system.entity.Org;
import com.abocode.jfaster.system.entity.Role;
import com.abocode.jfaster.system.entity.RoleUser;
import com.abocode.jfaster.system.entity.User;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
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
import java.util.Date;
import java.util.List;
import java.util.Map;

@Scope("prototype")
@Controller
@RequestMapping("/userController")
@Slf4j
public class UserController {
    @Autowired
    private ResourceRepository resourceRepository;
    @Resource
    private UserRepository userRepository;
    @Resource
    private UserService userService;
    @Resource
    private OrgService orgService;
    @Resource
    private RoleService roleService;

    /**
     * 菜单列表
     * @return
     */
    @RequestMapping(params = "menu")
    @ResponseBody
    public String menu() {
        User u = SessionHolder.getCurrentUser();
        return userService.getMenus(u);
    }

    /**
     * 用户列表页面跳转-给部门查询条件中的下拉框准备数据
     *
     * @return
     */
    @RequestMapping(params = "user")
    public String user(HttpServletRequest request) {
        List<Org> departList = userRepository.findAll(Org.class);
        request.setAttribute("departsReplace", SystemJsonUtils.listToReplaceStr(departList, "orgName", "id"));
        return "system/user/userList";
    }

    /**
     * 用户信息
     *
     * @return
     */
    @RequestMapping(params = "userinfo")
    public String userinfo(HttpServletRequest request) {
        User user = SessionHolder.getCurrentUser();
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
        User user = SessionHolder.getCurrentUser();
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
        User user = SessionHolder.getCurrentUser();
        String password = ConvertUtils.getString(request.getParameter("password"));
        String newPassword = ConvertUtils.getString(request.getParameter("newpassword"));
        userService.updatePwd(user, password, newPassword);
        return AjaxJsonBuilder.success("修改密码成功");
    }


    /**
     * 修改用户密码
     *
     * @author
     */

    @RequestMapping(params = "changepasswordforuser")
    public ModelAndView changepasswordforuser(User user, HttpServletRequest request) {
        if (StrUtils.isNotEmpty(user.getId())) {
            user = userRepository.findEntity(User.class, user.getId());
            request.setAttribute("userView", user);
            RoleIdAndNameDto roleIdAndNameDto = roleService.findByUserId(user.getId());
            request.setAttribute("id", roleIdAndNameDto.getRoleId());
            request.setAttribute("roleName", roleIdAndNameDto.getRoleName());
        }
        return new ModelAndView("system/user/adminchangepwd");
    }


    @RequestMapping(params = "savenewpwdforuser")
    @ResponseBody
    public AjaxJson savenewpwdforuser(HttpServletRequest request) {
        String id = ConvertUtils.getString(request.getParameter("id"));
        String password = ConvertUtils.getString(request.getParameter("password"));
        userService.restPassword(id, password);
        return AjaxJsonBuilder.success();
    }


    /**
     * 锁定账户
     */
    @RequestMapping(params = "lock")
    @ResponseBody
    public AjaxJson lock(String id) {
        userService.lockById(id);
        return AjaxJsonBuilder.success("用户锁定成功");
    }


    /**
     * 得到角色列表
     *
     * @return
     */
    @RequestMapping(params = "role")
    @ResponseBody
    public List<ComboBox> role(HttpServletRequest request, ComboBox comboBox) {
        String id = request.getParameter("id");
        List<Role> roles = userRepository.findRoleById(id);
        List<Role> roleList = userRepository.findAll(Role.class);
        String[] fields = new String[]{comboBox.getId(), comboBox.getText()};
        return TagUtil.getComboBox(roleList, roles, fields);
    }

    /**
     * 得到部门列表
     * @return
     */
    @RequestMapping(params = "depart")
    @ResponseBody
    public List<ComboBox> depart(HttpServletRequest request, ComboBox comboBox) {
        String id = request.getParameter("id");
        String[] fields = new String[]{comboBox.getId(), comboBox.getText()};
        return userService.findComboBox(id, fields);
    }

    /**
     * easyuiAJAX用户列表请求数据
     *  @param request
     * @param response
     * @param dataGridParam
     * @return
     */
    @RequestMapping(params = "findDataGridData")
    @ResponseBody
    public DataGridData findDataGridData(User user, HttpServletRequest request, HttpServletResponse response, DataGridParam dataGridParam) {
        String orgIds = request.getParameter("orgIds");
        CriteriaQuery cq = userService.buildCq(user, dataGridParam, orgIds);
     return   this.userRepository.findDataGridData(cq, true);
    }

    /**
     * 用户信息录入和更新
     *
     * @param user
     * @return
     */
    @RequestMapping(params = "del")
    @ResponseBody
    public AjaxJson del(User user) {
        Assert.isTrue(!"admin".equals(user.getUsername()), "超级管理员[admin]不可删除");
        userService.del(user.getId());
        return AjaxJsonBuilder.success();
    }

    /**
     * 检查用户名
     * @param request
     * @return
     */
    @RequestMapping(params = "checkUser")
    @ResponseBody
    public ValidForm checkUser(HttpServletRequest request) {
        ValidForm v = new ValidForm();
        String username = ConvertUtils.getString(request.getParameter("param"));
        String code = ConvertUtils.getString(request.getParameter("code"));
        List<User> roles = userRepository.findAllByProperty(User.class, "username", username);
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
     * @param request
     * @return
     */

    @RequestMapping(params = "saveUser")
    @ResponseBody
    public AjaxJson saveUser(HttpServletRequest request, User user) {
        String roleId = ConvertUtils.getString(request.getParameter("roleid"));
        String password = ConvertUtils.getString(request.getParameter("password"));
        String orgIds = ConvertUtils.getString(request.getParameter("orgIds"));
        userService.saveUser(user, roleId, password, orgIds);
        return AjaxJsonBuilder.success();
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
     * @param dataGridParam
     * @return
     */
    @RequestMapping(params = "findDataGridDataRole")
    @ResponseBody
    public DataGridData findDataGridDataRole(DataGridParam dataGridParam) {
        CriteriaQuery cq = new CriteriaQuery(Role.class).buildDataGrid(dataGridParam);
       return this.userRepository.findDataGridData(cq, true);
    }

    /**
     * easyuiAJAX请求数据： 用户选择角色列表
     *
     * @param user
     */
    @RequestMapping(params = "detail")
    public ModelAndView detail(User user, HttpServletRequest request) {
        String orgId = ConvertUtils.getString(request.getParameter("departid"));
        List<Org> departList = orgService.find(orgId);
        request.setAttribute("departList", departList);
        if (StrUtils.isNotEmpty(user.getId())) {
            user = userRepository.findEntity(User.class, user.getId());
            request.setAttribute("userView", user);
            RoleIdAndNameDto roleIdAndNameDto = roleService.findByUserId(user.getId());
            request.setAttribute("id", roleIdAndNameDto.getRoleId());
            request.setAttribute("roleName", roleIdAndNameDto.getRoleName());
            List<String> orgIds = orgService.findIdByUserId(user.getId());
            request.setAttribute("orgIdList", new Gson().toJson(orgIds));
        }
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
        String userId = u != null ? u.getId() : ConvertUtils.getString(request.getParameter("userId"));
        List<Org> orgList = orgService.findOrgByUserId(userId);
        request.setAttribute("orgList", orgList);
        User user = userRepository.findEntity(User.class, userId);
        request.setAttribute("user", user);
        return new ModelAndView("system/user/userOrgSelect");
    }

    /**
     * 根据部门和角色选择用户跳转页面
     */
    @RequestMapping(params = "choose")
    public String choose(HttpServletRequest request) {
        List<Role> roles = userRepository.findAll(Role.class);
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
        String orgId = request.getParameter("departid");
        String roleId = request.getParameter("roleid");
        request.setAttribute("roleid", roleId);
        request.setAttribute("departid", orgId);
        return "system/membership/userlist";
    }

    /**
     * 部门和角色选择用户的用户显示列表
     *  @param request
     * @param response
     * @param dataGridParam
     * @return
     */
    @RequestMapping(params = "findDataGridDataUser")
    @ResponseBody
    public DataGridData findDataGridDataUser(HttpServletRequest request, HttpServletResponse response, DataGridParam dataGridParam) {
        String departid = request.getParameter("departid");
        String roleid = request.getParameter("roleid");
        CriteriaQuery cq = new CriteriaQuery(User.class).buildDataGrid(dataGridParam);
        if (departid.length() > 0) {
            cq.eq("currentDepart.id", ConvertUtils.getInt(departid, 0));
            cq.add();
        }
        StringBuilder uIds =new StringBuilder();
        if (roleid.length() > 0) {
            List<RoleUser> roleUsers = userRepository.findAllByProperty(RoleUser.class, "role.roleId", ConvertUtils.getInt(roleid, 0));
            if (roleUsers.size() > 0) {
                for (RoleUser tRoleUser : roleUsers) {
                    uIds.append( tRoleUser.getUser().getId()).append( ",");
                }
            }
            cq.in("userid", ConvertUtils.getInts(uIds.toString().split(",")));
            cq.add();
        }
       return this.userRepository.findDataGridData(cq, true);
    }

    /**
     * 根据部门和角色选择用户跳转页面
     */
    @RequestMapping(params = "roleDepart")
    public String roleDepart(HttpServletRequest request) {
        List<Role> roles = userRepository.findAll(Role.class);
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
        if (nodeid.equals("role")) {
            return new ModelAndView("system/membership/users");
        } else {
            return new ModelAndView("system/membership/departList");
        }
    }

    /**
     * 部门和角色选择用户的用户显示列表
     * @param dataGridParam
     * @return
     */
    @RequestMapping(params = "findDataGridDataDepart")
    @ResponseBody
    public DataGridData findDataGridDataDepart(DataGridParam dataGridParam) {
        CriteriaQuery cq = new CriteriaQuery(Org.class).buildDataGrid(dataGridParam);
       return  userRepository.findDataGridData(cq, true);
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
     * @param request
     * @param request
     * @return
     */

    @RequestMapping(params = "savesign", method = RequestMethod.POST)
    @ResponseBody
    public AjaxJson savesign(HttpServletRequest request) {
        FileUploadDto uploadFile = new FileUploadDto(request);
        String id = uploadFile.get("id");
        User user = userRepository.findEntity(User.class, id);
        uploadFile.setRealPath("signatureFile");
        uploadFile.setFolderPath("signature");
        uploadFile.setByteField("signature");
        uploadFile.setBasePath("resources");
        uploadFile.setRename(false);
        uploadFile.setObject(user);
        String message = user.getUsername() + "设置签名成功";
        resourceRepository.uploadFile(uploadFile);
        return AjaxJsonBuilder.success(message);
    }

    /**
     * 测试组合查询功能
     *  @param user
     * @param request
     * @param response
     * @param dataGridParam
     * @return
     */
    @RequestMapping(params = "testSearch")
    @ResponseBody
    public DataGridData testSearch(User user, HttpServletRequest request, HttpServletResponse response, DataGridParam dataGridParam) {
        CriteriaQuery cq = new CriteriaQuery(User.class).buildDataGrid(dataGridParam);
        if (user.getUsername() != null) {
            cq.like("username", user.getUsername());
        }
        if (user.getRealName() != null) {
            cq.like("realName", user.getRealName());
        }
        cq.add();
      return  this.userRepository.findDataGridData(cq, true);
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
            log.error(e.getMessage());
        } finally {
            try {
                fOut.flush();
                fOut.close();
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
    }


    /**
     * 保存用户数据
     */
    @RequestMapping(params = "saveImportUser")
    @ResponseBody
    public AjaxJson saveImportUser(HttpServletRequest request) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        Assert.isTrue(fileMap.size() == 1, "<font color='red'>失败!</font> 每次只能导入一个文件");
        userService.importFile(fileMap);
        return AjaxJsonBuilder.success();
    }


    /***
     *导出用户
     * @param user
     * @param request
     * @return
     */
    @RequestMapping(params = "exportUser")
    public void exportUser(User user, HttpServletRequest request, HttpServletResponse response, DataGridParam dataGridParam) {
        String orgIds = request.getParameter("orgIds");
        List<ExlUserDto> exlUserList = userService.findExportUserList(user, orgIds, dataGridParam);
        download(request, response, exlUserList);
    }

    private void download(HttpServletRequest request, HttpServletResponse response, List<ExlUserDto> exlUserList) {
        // 生成提示信息，
        response.setContentType("application/vnd.ms-excel");
        OutputStream fOut = null;
        try {
            String exportFileName = "用户列表" + DateUtils.formatDate(new Date(), "YYYY_MM_DD_HH_MM_SS");
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
            HSSFWorkbook workbook = ExcelExportUtil.exportExcel(new ExcelTitle(null, null, exportFileName), ExlUserDto.class, exlUserList);
            fOut = response.getOutputStream();
            workbook.write(fOut);
        } catch (Exception e) {
            log.error(e.getMessage());
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