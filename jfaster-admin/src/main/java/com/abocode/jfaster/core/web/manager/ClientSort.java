package com.abocode.jfaster.core.web.manager;

import java.util.Comparator;

public class ClientSort implements Comparator<ClientBean> {

	
	public int compare(ClientBean prev, ClientBean now) {
		return (int) (now.getLoginTime().getTime()-prev.getLoginTime().getTime());
	}

}
