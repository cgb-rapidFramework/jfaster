package com.abocode.jfaster.admin.system.repository.persistence.hibernate;

import java.util.ArrayList;
import java.util.List;

import com.abocode.jfaster.admin.system.repository.UserRepository;
import com.abocode.jfaster.core.common.model.json.DataGrid;
import com.abocode.jfaster.core.common.util.StringUtils;
import com.abocode.jfaster.core.repository.persistence.hibernate.CommonRepositoryImpl;
import com.abocode.jfaster.system.entity.Role;
import com.abocode.jfaster.system.entity.RoleUser;
import com.abocode.jfaster.system.entity.UserOrg;
import com.abocode.jfaster.admin.system.dto.bean.ExlUserBean;
import com.abocode.jfaster.core.common.util.PasswordUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import com.abocode.jfaster.core.persistence.hibernate.qbc.CriteriaQuery;
import com.abocode.jfaster.system.entity.User;
import org.hibernate.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 张代浩
 */
@Service
@Transactional
public class UserRepositoryImpl extends CommonRepositoryImpl implements UserRepository {

    /**
     * 检查用户是否存在
     */
    public User checkUserExits(User user) {
        String password = PasswordUtils.encrypt(user.getUsername(), user.getPassword(), PasswordUtils.getStaticSalt());
        String query = "from User u where u.username = :username and u.password=:passowrd";
        Query queryObject = getSession().createQuery(query);
        queryObject.setParameter("username", user.getUsername());
        queryObject.setParameter("passowrd", password);
        List<User> users = queryObject.list();
        if (users != null && users.size() > 0) {
            return users.get(0);
        }
        return null;
    }

    /**
     * admin账户初始化
     */
    public void pwdInit(User user, String newPwd) {
        String query = "from User u where u.username = :username ";
        org.hibernate.query.Query queryObject = getSession().createQuery(query);
        queryObject.setParameter("username", user.getUsername());
        List<User> users = queryObject.list();
        if (null != users && users.size() > 0) {
            user = users.get(0);
            String pwd = PasswordUtils.encrypt(user.getUsername(), newPwd, PasswordUtils.getStaticSalt());
            user.setPassword(pwd);
            save(user);
        }

    }


    public String getUserRole(User user) {
        String userRole = "";
        List<RoleUser> sRoleUser = this.commonDao.findAllByProperty(RoleUser.class, "User.id", user.getId());
        for (RoleUser tsRoleUser : sRoleUser) {
            userRole += tsRoleUser.getRole().getRoleCode() + ",";
        }
        return userRole;
    }


    public int getUsersOfThisRole(String id) {
        Criteria criteria = getSession().createCriteria(RoleUser.class);
        criteria.add(Restrictions.eq("Role.id", id));
        int allCounts = ((Long) criteria.setProjection(
                Projections.rowCount()).uniqueResult()).intValue();
        return allCounts;
    }

    @Override
    public List<ExlUserBean> getExlUserList(DataGrid dataGrid, User user, CriteriaQuery cq) {
        List<User> users = this.findListByCq(cq, true);
        List<ExlUserBean> exlUserList = new ArrayList<ExlUserBean>();
        // 参数组装
        for (User model : users) {
            ExlUserBean exlUserVo = new ExlUserBean();
            StringBuffer sb = new StringBuffer();
            for (UserOrg org : model.getUserOrgList()) {
                sb.append(org.getDepart().getOrgName()).append(",");
            }
            exlUserVo.setDepartName(sb.toString());
            exlUserVo.setEmail(model.getEmail());
            exlUserVo.setMobilePhone(model.getMobilePhone());
            exlUserVo.setOfficePhone(model.getOfficePhone());
            exlUserVo.setRealName(model.getRealName());
            String roleName = "";
            for (RoleUser role : model.getRoleUserList()) {
                roleName += role.getRole().getRoleName() + ",";
            }
            exlUserVo.setRoleName(roleName);
            exlUserVo.setUsername(model.getUsername());
            exlUserList.add(exlUserVo);
        }
        return exlUserList;
    }

    @Override
    public List<Role> findRoleById(String id) {
        List<Role> roles = new ArrayList<Role>();
        if (StringUtils.isNotEmpty(id)) {
            List<RoleUser> roleUser = findAllByProperty(RoleUser.class, "User.id", id);
            if (roleUser.size() > 0) {
                for (RoleUser ru : roleUser) {
                    roles.add(ru.getRole());
                }
            }
        }
        return roles;
    }
}
