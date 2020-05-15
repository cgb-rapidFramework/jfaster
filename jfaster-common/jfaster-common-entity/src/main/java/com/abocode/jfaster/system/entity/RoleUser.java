package com.abocode.jfaster.system.entity;

import com.abocode.jfaster.core.AbstractIdEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "t_s_role_user")
@Data
public class RoleUser extends AbstractIdEntity implements java.io.Serializable {
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userid")
	private User user;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "roleid")
	private Role role;
}