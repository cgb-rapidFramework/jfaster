package com.abocode.jfaster.web.system.domain.entity;
import com.abocode.jfaster.core.repository.entity.IdEntity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * 通用类型字典表
 *  @author  张代浩
 */
@Entity
@Table(name = "t_s_type")
public class Type extends IdEntity implements java.io.Serializable {

	private TypeGroup TSTypegroup;//类型分组
	private Type TSType;//父类型
	private String typename;//类型名称
	private String typecode;//类型编码
	private List<Type> TSTypes =new ArrayList();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "typegroupid")
	public TypeGroup getTSTypegroup() {
		return this.TSTypegroup;
	}

	public void setTSTypegroup(TypeGroup TSTypegroup) {
		this.TSTypegroup = TSTypegroup;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "typepid")
	public Type getTSType() {
		return this.TSType;
	}

	public void setTSType(Type TSType) {
		this.TSType = TSType;
	}

	@Column(name = "typename", length = 50)
	public String getTypename() {
		return this.typename;
	}

	public void setTypename(String typename) {
		this.typename = typename;
	}

	@Column(name = "typecode", length = 50)
	public String getTypecode() {
		return this.typecode;
	}

	public void setTypecode(String typecode) {
		this.typecode = typecode;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "TSType")
	public List<Type> getTSTypes() {
		return this.TSTypes;
	}

	public void setTSTypes(List<Type> TSTypes) {
		this.TSTypes = TSTypes;
	}

}