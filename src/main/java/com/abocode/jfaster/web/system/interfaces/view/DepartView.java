package com.abocode.jfaster.web.system.interfaces.view;

//import com.abocode.jfaster.core.repository.entity.IdEntity;

import com.abocode.jfaster.core.repository.entity.IdEntity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 * 部门机构表
 * @author  guanxf
 */
public class DepartView extends IdEntity implements java.io.Serializable {
	private DepartView TSPDepart;//上级部门
	private String departname;//部门名称
	private String description;//部门描述
    private String orgCode;//机构编码
    private String orgType;//机构编码
	private List<DepartView> TSDeparts = new ArrayList<DepartView>();//下属部门

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parentdepartid")
	public DepartView getTSPDepart() {
		return this.TSPDepart;
	}

	public void setTSPDepart(DepartView TSPDepart) {
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
	public List<DepartView> getTSDeparts() {
		return TSDeparts;
	}

	public void setTSDeparts(List<DepartView> tSDeparts) {
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