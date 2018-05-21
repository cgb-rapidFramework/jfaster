package com.abocode.jfaster.web.system.domain.entity;

import com.abocode.jfaster.core.repository.entity.IdEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 部门机构表
 * @author  张代浩
 */
@Entity
@Table(name = "t_s_depart")
public class Depart extends IdEntity implements java.io.Serializable {
	private Depart TSPDepart;//上级部门
	private String departname;//部门名称
	private String description;//部门描述
    private String orgCode;//机构编码
    private String orgType;//机构编码
	private List<Depart> TSDeparts = new ArrayList<Depart>();//下属部门
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parentdepartid")
	public Depart getTSPDepart() {
		return this.TSPDepart;
	}

	public void setTSPDepart(Depart TSPDepart) {
		this.TSPDepart = TSPDepart;
	}

	@Column(name = "departname", nullable = false, length = 100)
	public String getDepartname() {
		return this.departname;
	}

	public void setDepartname(String departname) {
		this.departname = departname;
	}

	@Column(name = "description", length = 500)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "TSPDepart")
	public List<Depart> getTSDeparts() {
		return TSDeparts;
	}

	public void setTSDeparts(List<Depart> tSDeparts) {
		TSDeparts = tSDeparts;
	}

    @Column(name = "org_code", length = 64)
    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    @Column(name = "org_type", length = 1)
    public String getOrgType() {
        return orgType;
    }

    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }
}