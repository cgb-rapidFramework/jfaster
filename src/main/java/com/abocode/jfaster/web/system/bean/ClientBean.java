package com.abocode.jfaster.web.system.bean;

import com.abocode.jfaster.web.system.entity.Function;
import com.abocode.jfaster.web.system.entity.Role;
import com.abocode.jfaster.web.system.entity.User;

import java.util.List;
import java.util.Map;

/**
 * Created by Franky on 2016/3/15.
 */
@SuppressWarnings("serial")
public class ClientBean implements java.io.Serializable{
	private User user;
	
	private List<Role> roles;

	private Map<String, Function> functions;
	/**
	 * 用户IP
	 */
	private java.lang.String ip;
	/**
	 *登录时间
	 */
	private java.util.Date logindatetime;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}


	public Map<String, Function> getFunctions() {
		return functions;
	}

	public void setFunctions(Map<String, Function> functions) {
		this.functions = functions;
	}

	public java.lang.String getIp() {
		return ip;
	}

	public void setIp(java.lang.String ip) {
		this.ip = ip;
	}

	public java.util.Date getLogindatetime() {
		return logindatetime;
	}

	public void setLogindatetime(java.util.Date logindatetime) {
		this.logindatetime = logindatetime;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}


}
