package com.abocode.jfaster.web.system.domain.entity;

import com.abocode.jfaster.core.repository.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * TRoleFunction entity. 
 *  @author  张代浩
 */
@Entity
@Table(name = "t_s_role_function")
public class RoleFunction extends IdEntity implements java.io.Serializable {
	private Function TSFunction;
	private Role TSRole;
	private String operation;
	private String dataRule;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "functionid")
	public Function getTSFunction() {
		return this.TSFunction;
	}

	public void setTSFunction(Function TSFunction) {
		this.TSFunction = TSFunction;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "roleid")
	public Role getTSRole() {
		return this.TSRole;
	}

	public void setTSRole(Role TSRole) {
		this.TSRole = TSRole;
	}

	@Column(name = "operation", length = 100)
	public String getOperation() {
		return this.operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}
	@Column(name = "datarule", length = 100)
	public String getDataRule() {
		return dataRule;
	}

	public void setDataRule(String dataRule) {
		this.dataRule = dataRule;
	}

}