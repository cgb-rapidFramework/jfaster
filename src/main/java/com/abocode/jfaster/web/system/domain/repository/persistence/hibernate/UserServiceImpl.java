package com.abocode.jfaster.web.system.domain.repository.persistence.hibernate;

import java.util.ArrayList;
import java.util.List;

import com.abocode.jfaster.core.common.model.json.DataGrid;
import com.abocode.jfaster.core.repository.service.impl.CommonServiceImpl;
import com.abocode.jfaster.web.system.domain.entity.RoleUser;
import com.abocode.jfaster.web.system.domain.entity.UserOrg;
import com.abocode.jfaster.web.system.interfaces.bean.ExlUserBean;
import com.abocode.jfaster.core.common.util.PasswordUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import com.abocode.jfaster.core.persistence.hibernate.qbc.CriteriaQuery;
import com.abocode.jfaster.web.system.domain.entity.User;
import com.abocode.jfaster.web.system.domain.repository.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author  张代浩
 *
 */
@Service("userService")
@Transactional
public class UserServiceImpl extends CommonServiceImpl implements UserService {

	/**
	 * 检查用户是否存在
	 * */
	public User checkUserExits(User user) {
		String password = PasswordUtils.encrypt(user.getUserName(), user.getPassword(), PasswordUtils.getStaticSalt());
		String query = "from User u where u.userName = :username and u.password=:passowrd";
		org.hibernate.Query queryObject =getSession().createQuery(query);
		queryObject.setParameter("username", user.getUserName());
		queryObject.setParameter("passowrd", password);
		List<User> users =queryObject.list();
		if (users != null && users.size() > 0) {
			return users.get(0);
		}
		return null;
	}
	
	/**
	 * admin账户初始化
	 */
	public void pwdInit(User user, String newPwd){
		String query ="from User u where u.userName = :username ";
		org.hibernate.query.Query queryObject = getSession().createQuery(query);
		queryObject.setParameter("username", user.getUserName());
		List<User> users =  queryObject.list();
		if(null != users && users.size() > 0){
			user = users.get(0);
			String pwd = PasswordUtils.encrypt(user.getUserName(), newPwd, PasswordUtils.getStaticSalt());
			user.setPassword(pwd);
			save(user);
		}
		
	}
	

	public String getUserRole(User user) {
		String userRole = "";
		List<RoleUser> sRoleUser =this.commonDao.findAllByProperty(RoleUser.class, "User.id", user.getId());
		for (RoleUser tsRoleUser : sRoleUser) {
			userRole += tsRoleUser.getTSRole().getRoleCode() + ",";
		}
		return userRole;
	}

	
	public int getUsersOfThisRole(String id) {
		Criteria criteria = getSession().createCriteria(RoleUser.class);
		criteria.add(Restrictions.eq("TSRole.id", id));
		int allCounts = ((Long) criteria.setProjection(
				Projections.rowCount()).uniqueResult()).intValue();
		return allCounts;
	}

	@Override
	public List<ExlUserBean> getExlUserList(DataGrid dataGrid, User user, CriteriaQuery cq) {
		List<User> users= this.findListByCq(cq, true);
		List<ExlUserBean> exlUserList=new ArrayList<ExlUserBean>();
		// 参数组装
		for (User model : users) {
			ExlUserBean exlUserVo = new ExlUserBean();
			String departName="";
			for (UserOrg org:model.getUserOrgList()){
				departName+=org.getTsDepart().getDepartname()+",";
			}
			exlUserVo.setDepartName(departName);
			exlUserVo.setEmail(model.getEmail());
			exlUserVo.setMobilePhone(model.getMobilePhone());
			exlUserVo.setOfficePhone(model.getOfficePhone());
			exlUserVo.setRealName(model.getRealName());
			String roleName = "";
			for(RoleUser role:model.getRoleUserList()){
				roleName+=role.getTSRole().getRoleName()+",";
			}
			exlUserVo.setRoleName(roleName);
			exlUserVo.setUserName(model.getUserName());
			exlUserList.add(exlUserVo);
		}
		return exlUserList;
	}




}
