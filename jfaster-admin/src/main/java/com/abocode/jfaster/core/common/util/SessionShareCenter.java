package com.abocode.jfaster.core.common.util;

import com.abocode.jfaster.core.web.manager.ClientManager;
import com.abocode.jfaster.admin.system.dto.bean.ClientBean;
import com.abocode.jfaster.system.entity.Role;

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
	   getSession().removeAttribute("client"+sessionId);
	   getSession().removeAttribute("roleList"+sessionId);
	   getSession().removeAttribute("user"+sessionId);
	}
	
	
	
	
}
