package com.abocode.jfaster.web.system.manager;

import java.util.Comparator;

import com.abocode.jfaster.web.system.entity.Client;

public class ClientSort implements Comparator<Client> {

	
	public int compare(Client prev, Client now) {
		return (int) (now.getLogindatetime().getTime()-prev.getLogindatetime().getTime());
	}

}
