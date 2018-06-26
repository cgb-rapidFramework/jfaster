package com.abocode.jfaster.web.common.manager;

import java.util.Comparator;

import com.abocode.jfaster.web.system.application.dto.bean.ClientBean;

public class ClientSort implements Comparator<ClientBean> {

	
	public int compare(ClientBean prev, ClientBean now) {
		return (int) (now.getLogindatetime().getTime()-prev.getLogindatetime().getTime());
	}

}
