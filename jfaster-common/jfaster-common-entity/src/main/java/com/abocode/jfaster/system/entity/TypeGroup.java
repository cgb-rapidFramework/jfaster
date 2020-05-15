package com.abocode.jfaster.system.entity;
import com.abocode.jfaster.core.AbstractIdEntity;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
@Entity
@Table(name = "t_s_typegroup")
@Data
public class TypeGroup extends AbstractIdEntity implements java.io.Serializable {
	@Column(name = "typegroupname", length = 50)
	private String typeGroupName;
	@Column(name = "typegroupcode", length = 50)
	private String typeGroupCode;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "typeGroup")
	private List<Type> types = new ArrayList<Type>();
}