package com.abocode.jfaster.core.web.manager;

import java.util.Comparator;

import com.abocode.jfaster.admin.system.dto.bean.ClientBean;

public class ClientSort implements Comparator<ClientBean> {

	
	public int compare(ClientBean prev, ClientBean now) {
		return (int) (now.getLogindatetime().getTime()-prev.getLogindatetime().getTime());
	}

}
