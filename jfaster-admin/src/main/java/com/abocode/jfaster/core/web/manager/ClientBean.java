package com.abocode.jfaster.core.web.manager;

import com.abocode.jfaster.system.entity.Function;
import com.abocode.jfaster.system.entity.Role;
import com.abocode.jfaster.system.entity.User;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Created by Franky on 2016/3/15.
 */
@Data
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
	private java.util.Date loginTime;

}
