package com.abocode.jfaster.system.entity;

import com.abocode.jfaster.core.AbstractIdEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * 部门机构表
 * @author  张代浩
 */
@Entity
@Table(name = "t_s_depart")
@Data
public class Depart extends AbstractIdEntity implements java.io.Serializable {
	//上级部门
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parentDepartId")
	private Depart parentDepart;
	//部门名称
	@Column(name = "departname", nullable = false, length = 100)
	private String departname;
	//部门描述
	@Column(name = "description", length = 500)
	private String description;
	//机构编码
	@Column(name = "org_code", length = 64)
    private String orgCode;
	//机构编码
	@Column(name = "org_type", length = 1)
    private String orgType;
	//下属部门
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "depart")
	private List<Depart> departs;
}