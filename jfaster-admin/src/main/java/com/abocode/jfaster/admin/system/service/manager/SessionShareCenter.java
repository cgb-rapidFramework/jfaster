package com.abocode.jfaster.admin.system.service.manager;

import com.abocode.jfaster.core.common.util.ContextHolderUtils;
import com.abocode.jfaster.admin.system.service.manager.ClientManager;
import com.abocode.jfaster.admin.system.service.manager.ClientBean;
import com.abocode.jfaster.system.entity.Role;

import javax.servlet.http.HttpSession;
import java.util.List;
/**
 * 通过session共享信息
 * @author guanxf
 *
 */
public class SessionShareCenter   {
	 /**
     * 共享用户信息
     * @param client
     */
	public static void putClient(ClientBean client) {
		HttpSession session= ContextHolderUtils.getSession();
		session.setAttribute("client"+ContextHolderUtils.getSession().getId(), client);
	      
	}
	public static void putUserId(String  userId) {
		HttpSession session= ContextHolderUtils.getSession();
		session.setAttribute("userId"+session.getId(),userId);
	}
	
	
	public static void putRoles(List<Role> roleList) {
		HttpSession session= ContextHolderUtils.getSession();
		session.setAttribute("roleList"+session.getId(), roleList);
	}
	

	/**
	 * 获取用户信息
	 */
	public static String getUserId() {
		HttpSession session= ContextHolderUtils.getSession();
		return  (String) session.getAttribute("userId"+session.getId());
	}
	public static ClientBean getClient() {
		HttpSession session= ContextHolderUtils.getSession();
		return  (ClientBean)session.getAttribute("client"+ContextHolderUtils.getSession().getId());
	}
	

	 /***
     * 删除session
     * @param sessionId
     */
	public static void removeSession(String sessionId) {
	   ClientManager.getInstance().removeClinet(sessionId);
	    ContextHolderUtils.getSession().removeAttribute("client"+sessionId);
	   ContextHolderUtils.getSession().removeAttribute("roleList"+sessionId);
	   ContextHolderUtils.getSession().removeAttribute("user"+sessionId);
	}
	
	
	
	
}
