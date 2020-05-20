package com.abocode.jfaster.admin.system.service.impl;

import com.abocode.jfaster.admin.system.dto.bean.ExlUserBean;
import com.abocode.jfaster.admin.system.repository.UserRepository;
import com.abocode.jfaster.api.core.AvailableEnum;
import com.abocode.jfaster.core.common.constants.Globals;
import com.abocode.jfaster.admin.system.service.BeanToTagConverter;
import com.abocode.jfaster.core.common.exception.BusinessException;
import com.abocode.jfaster.core.common.model.json.AjaxJson;
import com.abocode.jfaster.core.common.model.json.ComboBox;
import com.abocode.jfaster.core.common.model.json.DataGrid;
import com.abocode.jfaster.core.common.util.*;
import com.abocode.jfaster.admin.system.service.UserService;
import com.abocode.jfaster.admin.system.repository.SystemRepository;
import com.abocode.jfaster.core.platform.utils.SystemMenuUtils;
import com.abocode.jfaster.core.platform.view.FunctionView;
import com.abocode.jfaster.core.persistence.hibernate.qbc.CriteriaQuery;
import com.abocode.jfaster.core.platform.poi.excel.ExcelImportUtil;
import com.abocode.jfaster.core.platform.poi.excel.entity.ImportParams;
import com.abocode.jfaster.core.platform.view.widgets.easyui.TagUtil;
import com.abocode.jfaster.core.web.hqlsearch.HqlGenerateUtil;
import com.abocode.jfaster.system.entity.*;
import org.hibernate.criterion.Property;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private SystemRepository systemRepository;
    @Resource
    private UserRepository userRepository;

    public String getMenus(User u) {
        FunctionComparator sort = new FunctionComparator();
        // 登陆者的权限
        Set<Function> loginActionlist = new HashSet();// 已有权限菜单
        List<RoleUser> rUsers = systemRepository.findAllByProperty(RoleUser.class, "user.id", u.getId());
        for (RoleUser ru : rUsers) {
            Role role = ru.getRole();
            List<RoleFunction> roleFunctionList = systemRepository.findAllByProperty(RoleFunction.class, "role.id", role.getId());
            if (roleFunctionList.size() > 0) {
                for (RoleFunction roleFunction : roleFunctionList) {
                    Function function = roleFunction.getFunction();
                    loginActionlist.add(function);
                }
            }
        }
        List<FunctionView> bigActionlist = new ArrayList();// 一级权限菜单
        List<FunctionView> smailActionlist = new ArrayList();// 二级权限菜单
        if (loginActionlist.size() > 0) {
            for (Function function : loginActionlist) {
                FunctionView functionBean = BeanToTagConverter.convertFunction(function);
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
        return logString;
    }

    @Override
    public Object getAll() {
        return systemRepository.findAll(User.class);
    }

    @Override
    public void restPassword(String id, String password) {
        User users = systemRepository.findEntity(User.class, id);
        users.setPassword(PasswordUtils.encrypt(users.getUsername(), password, PasswordUtils.getStaticSalt()));
        users.setStatus(Globals.User_Normal);
        systemRepository.update(users);
    }

    @Override
    public void updatePwd(User user, String password, String newPassword) {
        String pString = PasswordUtils.encrypt(user.getUsername(), password, PasswordUtils.getStaticSalt());
        Assert.isTrue(!pString.equals(user.getPassword()), "原密码不正确");
        user.setPassword(PasswordUtils.encrypt(user.getUsername(), newPassword, PasswordUtils.getStaticSalt()));
        systemRepository.update(user);
    }

    @Override
    public void lockById(String id) {
        User user = userRepository.findEntity(User.class, id);
        Assert.isTrue("admin".equals(user.getUsername()), "超级管理员[admin]不可锁定");
        Assert.isTrue(Globals.User_Forbidden.equals(user.getStatus()), "锁定账户已经锁定");
        user.setStatus(Globals.User_Forbidden);
        userRepository.update(user);
    }

    @Override
    public List<ComboBox> findComboBox(String id, String[] fields) {
        List<Org> departs = new ArrayList();
        if (StringUtils.isNotEmpty(id)) {
            Object[] object = new Object[]{id};
            List<Org[]> resultList = userRepository.findByHql("from Org d,UserOrg uo where d.id=uo.orgId and uo.id=?0", object);
            for (Org[] departArr : resultList) {
                departs.add(departArr[0]);
            }
        }
        List<Org> departList = userRepository.getList(Org.class);
        List<ComboBox> comboBoxes = TagUtil.getComboBox(departList, departs, fields);
        return comboBoxes;
    }

    @Override
    public CriteriaQuery buildCq(User user, DataGrid dataGrid, String orgIds) {
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

    @Override
    public void del(String id) {
        User user = userRepository.findEntity(User.class, id);
        List<RoleUser> roleUser = userRepository.findAllByProperty(RoleUser.class, "user.id", id);
        Assert.isTrue(!user.getStatus().equals(Globals.User_ADMIN), "超级管理员不可删除");
        if (roleUser.size() > 0) {
            // 删除用户时先删除用户和角色关系表
            delRoleUser(user);
            userRepository.executeSql("delete from t_s_user_org where user_id=?", user.getId()); // 删除 用户-机构 数据
            userRepository.delete(user);
        } else {
            userRepository.delete(user);
        }
    }

    @Override
    public void saveUser(User user, String roleId, String password, String orgIds) {
        if (StringUtils.isNotEmpty(user.getId())) {
            User users = userRepository.findEntity(User.class, user.getId());
            users.setEmail(user.getEmail());
            users.setOfficePhone(user.getOfficePhone());
            users.setMobilePhone(user.getMobilePhone());
            userRepository.executeSql("delete from t_s_user_org where user_id=?", user.getId());
            saveUserOrgList(orgIds, user);
//            users.setOrg(user.getDepart());
            users.setRealName(user.getRealName());
            users.setStatus(Globals.User_Normal);
            userRepository.update(users);
            List<RoleUser> ru = userRepository.findAllByProperty(RoleUser.class, "user.id", user.getId());
            userRepository.deleteEntities(ru);
            if (StringUtils.isNotEmpty(roleId)) {
                saveRoleUser(users, roleId);
            }
        } else {
            User users = userRepository.findUniqueByProperty(User.class, "username", user.getUsername());
            if (users == null) {
                user.setPassword(PasswordUtils.encrypt(user.getUsername(), password, PasswordUtils.getStaticSalt()));
                user.setStatus(Globals.User_Normal);
                userRepository.save(user);
                saveUserOrgList(orgIds, user);
                if (StringUtils.isNotEmpty(roleId)) {
                    saveRoleUser(user, roleId);
                }
            }
        }
    }

    /**
     * 保存 用户-组织机构 关系信息
     *
     * @param orgIds
     * @param user   user
     */
    private void saveUserOrgList(String orgIds, User user) {
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
            userRepository.batchSave(userOrgList);
        }
    }

    private void saveRoleUser(User user, String roleidstr) {
        String[] roleids = roleidstr.split(",");
        for (int i = 0; i < roleids.length; i++) {
            RoleUser rUser = new RoleUser();
            Role role = userRepository.findEntity(Role.class, roleids[i]);
            rUser.setRole(role);
            rUser.setUser(user);
            userRepository.save(rUser);

        }
    }

    private void delRoleUser(User user) {
        // 同步删除用户角色关联表
        List<RoleUser> roleUserList = userRepository.findAllByProperty(RoleUser.class, "user.id", user.getId());
        if (roleUserList.size() >= 1) {
            for (RoleUser tRoleUser : roleUserList) {
                userRepository.delete(tRoleUser);
            }
        }
    }

    @Override
    public void importFile(Map<String, MultipartFile> fileMap) {
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
                Assert.isTrue(!(null == userList || userList.size() == 0), "<font color='red'>失败!</font> Excel中没有可以导入的数据");
                for (ExlUserBean exlUserVo : userList) {
                    AjaxJson j = ValidateUtils.volatileBean(exlUserVo);
                    Assert.isTrue(j.isSuccess(), "数据验证失败");

                    //判断帐号是否存在
                    User u = this.userRepository.findUniqueByProperty(User.class, "username", exlUserVo.getUsername());
                    Assert.isTrue(!StringUtils.isNotEmpty(u), "<font color='red'>失败!</font>" + exlUserVo.getUsername() + " 帐号已经存在");
                    //判断组织机构是否存在
                    List<Org> exlDeparts = new ArrayList<Org>();
                    String[] departNames = exlUserVo.getOrgName().split(",");
                    for (int i = 0; i < departNames.length; i++) {
                        List<Org> departs = userRepository.findAllByProperty(Org.class, "departname", departNames[i]);
                        Assert.isTrue(departs.size() > 0, "<font color='red'>失败!</font>" + exlUserVo.getOrgName() + " 组织机构不存在");
                        exlDeparts.add(departs.get(0));
                    }


                    List<Role> exlRoles = new ArrayList<Role>();
                    String[] roleNames = exlUserVo.getRoleName().split(",");
                    for (int i = 0; i < roleNames.length; i++) {
                        //判断角色是否存在
                        List<Role> roles = userRepository.findAllByProperty(Role.class, "roleName", roleNames[i]);
                        Assert.isTrue(roles.size() > 0, "<font color='red'>失败!</font>" + exlUserVo.getRoleName() + " 角色不存在");
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
                    String uid = (String) this.userRepository.save(userEntity);
                    userEntity = this.userRepository.find(User.class, uid);
                    userEntity.setPassword(PasswordUtils.encrypt(userEntity.getUsername(), pwd, PasswordUtils.getStaticSalt()));
                    userRepository.update(userEntity);

                    //保存组织机构
                    for (Org depart : userEntity.getOrgs()) {
                        UserOrg userOrg = new UserOrg();
                        userOrg.setUser(userEntity);
                        userOrg.setOrg(depart);
                        this.userRepository.save(userOrg);
                    }

                    //保存角色
                    for (Role role : userEntity.getRoles()) {
                        RoleUser roleUser = new RoleUser();
                        roleUser.setRole(role);
                        roleUser.setUser(userEntity);
                        this.userRepository.save(roleUser);
                    }
                }
            } catch (IOException e) {
                throw  new BusinessException("<font color='red'>失败!</font> 检查文件数据、格式等是否正确！详细信息：",e);
            } catch (Exception e) {
                throw  new BusinessException("<font color='red'>失败!</font> 检查文件数据、格式等是否正确！详细信息：",e);
            }
        }
    }

    @Override
    public List<ExlUserBean> findExportUserList(User user, String orgIds, DataGrid dataGrid) {
        dataGrid.setPage(0);
        dataGrid.setRows(1000000);
        CriteriaQuery cq = buildCq(user, dataGrid, orgIds);
        List<ExlUserBean> exlUserList = this.userRepository.getExlUserList(dataGrid, user, cq);
        return  exlUserList;
    }
}
