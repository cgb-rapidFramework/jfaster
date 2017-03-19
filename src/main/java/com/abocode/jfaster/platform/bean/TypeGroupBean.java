package com.abocode.jfaster.platform.bean;
import com.abocode.jfaster.core.common.entity.IdEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Column;

/**
 * TTypegroup entity.
 *
 */
@SuppressWarnings("serial")
public class TypeGroupBean extends IdEntity implements java.io.Serializable {
	public static Map<String, TypeGroupBean> allTypeGroups = new HashMap<String,TypeGroupBean>();
	public static Map<String, List<TypeBean>> allTypes = new HashMap<String,List<TypeBean>>();
	private String typegroupname;
	private String typegroupcode;
	private List<TypeBean> TSTypes = new ArrayList<TypeBean>();
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

	public List<TypeBean> getTSTypes() {
		return this.TSTypes;
	}

	public void setTSTypes(List<TypeBean> TSTypes) {
		this.TSTypes = TSTypes;
	}

}