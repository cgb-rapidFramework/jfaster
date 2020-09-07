package com.abocode.jfaster.admin.system.service.manager;

import java.io.Serializable;
import java.util.*;

import com.abocode.jfaster.core.common.util.ContextHolderUtils;
import com.abocode.jfaster.core.repository.DataGridParam;

public class ClientManager {
	
	private static ClientManager instance = new ClientManager();
	
	private ClientManager(){
		
	}
	
	public static ClientManager getInstance(){
		return instance;
	}
	
	private Map<String,ClientBean> map = new HashMap<>();
	
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

	public static List<ClientBean>  getClient(List<ClientBean> clients, DataGridParam dataGrid) {
		Collections.sort(clients, new ClientSort());
		List<ClientBean> result = new ArrayList<>();
		for(int i = (dataGrid.getPage()-1)*dataGrid.getRows();
			i<clients.size()&&i<dataGrid.getPage()*dataGrid.getRows();i++){
			result.add(clients.get(i));
		}
		return result;
	}

}

class ClientSort implements Comparator<ClientBean>, Serializable {

	public int compare(ClientBean prev, ClientBean now) {
		return (int) (now.getLoginTime().getTime()-prev.getLoginTime().getTime());
	}

}

