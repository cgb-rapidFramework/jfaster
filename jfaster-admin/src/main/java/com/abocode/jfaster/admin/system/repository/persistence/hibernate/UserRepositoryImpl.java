package com.abocode.jfaster.admin.system.repository.persistence.hibernate;

import com.abocode.jfaster.admin.system.dto.ExlUserDto;
import com.abocode.jfaster.admin.system.repository.UserRepository;
import com.abocode.jfaster.core.common.util.PasswordUtils;
import com.abocode.jfaster.core.common.util.StrUtils;
import com.abocode.jfaster.core.persistence.hibernate.qbc.CriteriaQuery;
import com.abocode.jfaster.core.repository.DataGridParam;
import com.abocode.jfaster.core.repository.persistence.hibernate.CommonRepositoryImpl;
import com.abocode.jfaster.system.entity.Role;
import com.abocode.jfaster.system.entity.RoleUser;
import com.abocode.jfaster.system.entity.User;
import com.abocode.jfaster.system.entity.UserOrg;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author guanxf
 */
@Service
@Transactional
public class UserRepositoryImpl extends CommonRepositoryImpl implements UserRepository {

    /**
     * 检查用户是否存在
     */
    public User checkUserExits(String username,String password) {
        password = PasswordUtils.encrypt(username, password, PasswordUtils.getStaticSalt());
        String query = "from User u where u.username = :username and u.password=:passowrd";
        Query<User> queryObject = getSession().createQuery(query);
        queryObject.setParameter("username", username);
        queryObject.setParameter("passowrd", password);
        List<User> users = queryObject.list();
        if (!CollectionUtils.isEmpty(users)) {
            return users.get(0);
        }
        return null;
    }

    /**
     * admin账户初始化
     */
    public void pwdInit(User user, String newPwd) {
        String query = "from User u where u.username = :username ";
        Query<User> queryObject = getSession().createQuery(query);
        queryObject.setParameter("username", user.getUsername());
        List<User> users = queryObject.list();
        if (!CollectionUtils.isEmpty(users)) {
            user = users.get(0);
            String pwd = PasswordUtils.encrypt(user.getUsername(), newPwd, PasswordUtils.getStaticSalt());
            user.setPassword(pwd);
            save(user);
        }

    }


    public String getUserRole(User user) {
        StringBuilder userRole = new StringBuilder();
        List<RoleUser> sRoleUser = findAllByProperty(RoleUser.class, "user.id", user.getId());
        for (RoleUser tsRoleUser : sRoleUser) {
            userRole.append(tsRoleUser.getRole().getRoleCode()).append(",") ;
        }
        return userRole.toString();
    }


    public int getUsersOfThisRole(String id) {
        Criteria criteria = getSession().createCriteria(RoleUser.class);
        criteria.add(Restrictions.eq("role.id", id));
        return ((Long) criteria.setProjection(
                Projections.rowCount()).uniqueResult()).intValue();
    }

    @Override
    public List<ExlUserDto> getExlUserList(DataGridParam dataGridParam, User user, CriteriaQuery cq) {
        List<User> users = this.findListByCq(cq, true);
        List<ExlUserDto> exlUserList = new ArrayList<>();
        // 参数组装
        for (User model : users) {
            ExlUserDto exlUserVo = new ExlUserDto();
            StringBuilder sb = new StringBuilder();
            for (UserOrg org : model.getUserOrgList()) {
                sb.append(org.getOrg().getOrgName()).append(",");
            }
            exlUserVo.setOrgName(sb.toString());
            exlUserVo.setEmail(model.getEmail());
            exlUserVo.setMobilePhone(model.getMobilePhone());
            exlUserVo.setOfficePhone(model.getOfficePhone());
            exlUserVo.setRealName(model.getRealName());
            StringBuilder roleName = new StringBuilder();
            for (RoleUser role : model.getRoleUserList()) {
                roleName.append(role.getRole().getRoleName()).append(",");
            }
            exlUserVo.setRoleName(roleName.toString());
            exlUserVo.setUsername(model.getUsername());
            exlUserList.add(exlUserVo);
        }
        return exlUserList;
    }

    @Override
    public List<Role> findRoleById(String id) {
        List<Role> roles = new ArrayList<>();
        if (StrUtils.isNotEmpty(id)) {
            List<RoleUser> roleUser = findAllByProperty(RoleUser.class, "user.id", id);
            for (RoleUser ru : roleUser) {
                roles.add(ru.getRole());
            }
        }
        return roles;
    }
}
