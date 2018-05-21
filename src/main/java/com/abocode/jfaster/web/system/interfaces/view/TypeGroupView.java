package com.abocode.jfaster.web.system.interfaces.view;
import com.abocode.jfaster.core.repository.entity.IdEntity;

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
public class TypeGroupView extends IdEntity implements java.io.Serializable {
	public static Map<String, TypeGroupView> allTypeGroups = new HashMap<String,TypeGroupView>();
	public static Map<String, List<TypeView>> allTypes = new HashMap<String,List<TypeView>>();
	private String typegroupname;
	private String typegroupcode;
	private List<TypeView> TSTypes = new ArrayList<TypeView>();
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

	public List<TypeView> getTSTypes() {
		return this.TSTypes;
	}

	public void setTSTypes(List<TypeView> TSTypes) {
		this.TSTypes = TSTypes;
	}

}