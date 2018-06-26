package com.abocode.jfaster.web.system.domain.entity;

import com.abocode.jfaster.core.domain.entity.IdEntity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * TSRoleUser entity.
 * @author  张代浩
 */
@Entity
@Table(name = "t_s_role_user")
public class RoleUser extends IdEntity implements java.io.Serializable {
	private User TSUser;
	private Role TSRole;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userid")
	public User getTSUser() {
		return this.TSUser;
	}

	public void setTSUser(User TSUser) {
		this.TSUser = TSUser;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "roleid")
	public Role getTSRole() {
		return this.TSRole;
	}

	public void setTSRole(Role TSRole) {
		this.TSRole = TSRole;
	}

}