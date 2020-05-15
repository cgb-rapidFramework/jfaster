package com.abocode.jfaster.admin.system.repository;

import com.abocode.jfaster.core.common.model.json.DataGrid;
import com.abocode.jfaster.admin.system.dto.bean.ExlUserBean;
import com.abocode.jfaster.core.persistence.hibernate.qbc.CriteriaQuery;
import com.abocode.jfaster.core.repository.CommonRepository;
import com.abocode.jfaster.system.entity.User;

import java.util.List;

/**
 * 
 * @author  张代浩
 *
 */
public interface UserRepository extends CommonRepository {
	
	public User checkUserExits(User user);
	public String getUserRole(User user);
	public void pwdInit(User user, String newPwd);
	/**
	 * 判断这个角色是不是还有用户使用
	 *@Author JueYue
	 *@date   2013-11-12
	 *@param id
	 *@return
	 */
	public int getUsersOfThisRole(String id);

	List<ExlUserBean> getExlUserList(DataGrid dataGrid, User user, CriteriaQuery cq);
}
