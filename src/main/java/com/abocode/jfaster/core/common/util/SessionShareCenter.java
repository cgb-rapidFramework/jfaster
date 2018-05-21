package com.abocode.jfaster.core.common.util;

import com.abocode.jfaster.core.common.util.ContextHolderUtils;
import com.abocode.jfaster.web.common.manager.ClientManager;
import com.abocode.jfaster.web.system.interfaces.bean.ClientBean;
import com.abocode.jfaster.web.system.domain.entity.Role;

import javax.servlet.http.HttpSession;
import java.util.List;
/**
 * 通过session共享信息
 * @author guanxf
 *
 */
public class SessionShareCenter extends ContextHolderUtils {
	 /**
     * 共享用户信息
     * @param client
     */
	public static void putClient(ClientBean client) {
		HttpSession session= getSession();
		session.setAttribute("client"+getSession().getId(), client);
	      
	}
	public static void putUserId(String  userId) {
		HttpSession session= getSession();
		session.setAttribute("userId"+session.getId(),userId);
	}
	
	
	public static void putRoles(List<Role> roleList) {
		HttpSession session= getSession();
		session.setAttribute("roleList"+session.getId(), roleList);
	}
	

	/**
	 * 获取用户信息
	 */
	public static String getUserId() {
		HttpSession session= getSession();
		return  (String) session.getAttribute("userId"+session.getId());
	}
	public static ClientBean getClient() {
		HttpSession session= getSession();
		return  (ClientBean)session.getAttribute("client"+getSession().getId());
	}
	
	@SuppressWarnings("unchecked")
	public static List<Role>  getRoles() {
		HttpSession session= getSession();
		return (List<Role>) session.getAttribute("roleList"+session.getId());
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
