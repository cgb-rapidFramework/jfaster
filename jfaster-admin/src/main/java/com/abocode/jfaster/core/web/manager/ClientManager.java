package com.abocode.jfaster.core.web.manager;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.abocode.jfaster.core.common.util.ContextHolderUtils;
import com.abocode.jfaster.admin.system.dto.bean.ClientBean;

/**
 * 对在线用户的管理
 * @author JueYue
 * @date 2013-9-28
 * @version 1.0
 */
public class ClientManager {
	
	private static ClientManager instance = new ClientManager();
	
	private ClientManager(){
		
	}
	
	public static ClientManager getInstance(){
		return instance;
	}
	
	private Map<String,ClientBean> map = new HashMap<String, ClientBean>();
	
	/**
	 * 
	 * @param sessionId
	 * @param client
	 */
	public void addClinet(String sessionId,ClientBean client){
		map.put(sessionId, client);
	}
	/**
	 * sessionId
	 */
	public void removeClinet(String sessionId){
		map.remove(sessionId);
	}
	/**
	 * 
	 * @param sessionId
	 * @return
	 */
	public ClientBean getClient(String sessionId){
		return map.get(sessionId);
	}
	/**
	 *
	 * @return
	 */
	public ClientBean getClient(){
		return map.get(ContextHolderUtils.getSession().getId());
	}
	/**
	 * 
	 * @return
	 */
	public Collection<ClientBean> getAllClient(){
		return map.values();
	}

}
