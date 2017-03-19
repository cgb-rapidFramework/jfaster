package com.abocode.jfaster.web.system.entity;
// default package

import com.abocode.jfaster.core.common.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * TType entity.
 *  @author  张代浩
 */
@Entity
@Table(name = "t_s_opintemplate")
public class TSOpinTemplate extends IdEntity implements java.io.Serializable {
	private String descript;
	@Column(name = "descript", length = 100)
	public String getDescript() {
		return descript;
	}

	public void setDescript(String descript) {
		this.descript = descript;
	}
	
}