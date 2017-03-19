package com.abocode.jfaster.web.system.service;

import com.abocode.jfaster.core.common.model.json.DataGrid;
import com.abocode.jfaster.web.system.vo.ExlUserVo;
import com.abocode.jfaster.core.common.hibernate.qbc.CriteriaQuery;
import com.abocode.jfaster.core.common.service.CommonService;
import com.abocode.jfaster.web.system.entity.TSUser;

import java.util.List;

/**
 * 
 * @author  张代浩
 *
 */
public interface UserService extends CommonService{
	
	public TSUser checkUserExits(TSUser user);
	public String getUserRole(TSUser user);
	public void pwdInit(TSUser user, String newPwd);
	/**
	 * 判断这个角色是不是还有用户使用
	 *@Author JueYue
	 *@date   2013-11-12
	 *@param id
	 *@return
	 */
	public int getUsersOfThisRole(String id);

	List<ExlUserVo> getExlUserList(DataGrid dataGrid, TSUser user, CriteriaQuery cq);
}
