package org.jeecgframework.web.system.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.platform.constant.Globals;
import org.jeecgframework.web.common.hqlsearch.HqlGenerateUtil;
import org.jeecgframework.web.system.entity.base.TSRole;
import org.jeecgframework.web.system.entity.base.TSRoleUser;
import org.jeecgframework.web.system.entity.base.TSUser;
import org.jeecgframework.web.system.entity.base.TSUserOrg;
import org.jeecgframework.web.system.service.UserService;
import org.jeecgframework.web.system.vo.base.ExlUserVo;
import org.jeecgframework.web.utils.PasswordUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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
	public TSUser checkUserExits(TSUser user) {
		String password = PasswordUtils.encrypt(user.getUserName(), user.getPassword(), PasswordUtils.getStaticSalt());
		String query = "from TSUser u where u.userName = :username and u.password=:passowrd";
		org.hibernate.Query queryObject =getSession().createQuery(query);
		queryObject.setParameter("username", user.getUserName());
		queryObject.setParameter("passowrd", password);
		List<TSUser> users =queryObject.list();
		if (users != null && users.size() > 0) {
			return users.get(0);
		}
		return null;
	}
	
	/**
	 * admin账户初始化
	 */
	public void pwdInit(TSUser user,String newPwd){
		String query ="from TSUser u where u.userName = :username ";
		Query queryObject = getSession().createQuery(query);
		queryObject.setParameter("username", user.getUserName());
		List<TSUser> users =  ((Criteria) queryObject).list();
		if(null != users && users.size() > 0){
			user = users.get(0);
			String pwd = PasswordUtils.encrypt(user.getUserName(), newPwd, PasswordUtils.getStaticSalt());
			user.setPassword(pwd);
			save(user);
		}
		
	}
	

	public String getUserRole(TSUser user) {
		String userRole = "";
		List<TSRoleUser> sRoleUser =this.commonDao.findAllByProperty(TSRoleUser.class, "TSUser.id", user.getId());
		for (TSRoleUser tsRoleUser : sRoleUser) {
			userRole += tsRoleUser.getTSRole().getRoleCode() + ",";
		}
		return userRole;
	}

	
	public int getUsersOfThisRole(String id) {
		Criteria criteria = getSession().createCriteria(TSRoleUser.class);
		criteria.add(Restrictions.eq("TSRole.id", id));
		int allCounts = ((Long) criteria.setProjection(
				Projections.rowCount()).uniqueResult()).intValue();
		return allCounts;
	}

	@Override
	public List<ExlUserVo> getExlUserList(DataGrid dataGrid, TSUser user, CriteriaQuery cq) {
		List<TSUser> users= this.findListByCq(cq, true);
		List<ExlUserVo> exlUserList=new ArrayList<ExlUserVo>();
		// 参数组装
		for (TSUser model : users) {
			ExlUserVo exlUserVo = new ExlUserVo();
			String departName="";
			for (TSUserOrg org:model.getUserOrgList()){
				departName+=org.getTsDepart().getDepartname()+",";
			}
			exlUserVo.setDepartName(departName);
			exlUserVo.setEmail(model.getEmail());
			exlUserVo.setMobilePhone(model.getMobilePhone());
			exlUserVo.setOfficePhone(model.getOfficePhone());
			exlUserVo.setRealName(model.getRealName());
			String roleName = "";
			for(TSRoleUser role:model.getRoleUserList()){
				roleName+=role.getTSRole().getRoleName()+",";
			}
			exlUserVo.setRoleName(roleName);
			exlUserVo.setUserName(model.getUserName());
			exlUserList.add(exlUserVo);
		}
		return exlUserList;
	}




}
