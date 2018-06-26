package com.abocode.jfaster.web.system.domain.entity;
// default package

import com.abocode.jfaster.core.domain.entity.IdEntity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * TTypegroup entity.
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "t_s_typegroup")
public class TypeGroup extends IdEntity implements java.io.Serializable {	private String typegroupname;
	private String typegroupcode;
	private List<Type> TSTypes = new ArrayList<Type>();
	@Column(name = "typegroupname", length = 50)
	public String getTypegroupname() {
		return this.typegroupname;
	}

	public void setTypegroupname(String typegroupname) {
		this.typegroupname = typegroupname;
	}

	@Column(name = "typegroupcode", length = 50)
	public String getTypegroupcode() {
		return this.typegroupcode;
	}

	public void setTypegroupcode(String typegroupcode) {
		this.typegroupcode = typegroupcode;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "TSTypegroup")
	public List<Type> getTSTypes() {
		return this.TSTypes;
	}

	public void setTSTypes(List<Type> TSTypes) {
		this.TSTypes = TSTypes;
	}

}