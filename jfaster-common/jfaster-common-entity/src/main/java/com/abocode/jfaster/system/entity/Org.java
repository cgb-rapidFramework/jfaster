package com.abocode.jfaster.system.entity;

import com.abocode.jfaster.core.AbstractIdEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "t_s_org")
@Data
public class Org extends AbstractIdEntity implements java.io.Serializable {
	//上级部门
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id")
	private Org parentOrg;
	//部门名称
	@Column(name = "org_name",nullable = false, length = 100)
	private String orgName;
	//部门描述
	@Column(length = 500)
	private String description;
	//机构编码
	@Column(name = "org_code",length = 64)
    private String orgCode;
	//机构编码
	@Column(name = "org_type",length = 1)
    private String orgType;
	//下属部门
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "parentOrg")
	private List<Org> orgs;
}