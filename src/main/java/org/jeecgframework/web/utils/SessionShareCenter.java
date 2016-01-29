package org.jeecgframework.web.utils;

import org.jeecgframework.core.util.ContextHolderUtils;
import org.jeecgframework.web.system.entity.base.Client;
import org.jeecgframework.web.system.entity.base.TSRole;
import org.jeecgframework.web.system.manager.ClientManager;

import javax.servlet.http.HttpSession;
import java.util.List;
/**
 * 通过session共享信息
 * @author guanxf
 *
 */
public class SessionShareCenter extends ContextHolderUtils{
	 /**
     * 共享用户信息
     * @param client
     */
	public static void putClient(Client client) {
		HttpSession session= getSession();
		session.setAttribute("client"+getSession().getId(), client);
	      
	}
	public static void putUserId(String  userId) {
		HttpSession session= getSession();
		session.setAttribute("userId"+session.getId(),userId);
	}
	
	
	public static void putRoles(List<TSRole> roleList) {
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
	public static Client getClient() {
		HttpSession session= getSession();
		return  (Client)session.getAttribute("client"+getSession().getId());
	}
	
	@SuppressWarnings("unchecked")
	public static List<TSRole>  getRoles() {
		HttpSession session= getSession();
		return (List<TSRole>) session.getAttribute("roleList"+session.getId());
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
