package com.abocode.jfaster.system.entity;
import com.abocode.jfaster.core.AbstractIdEntity;
import lombok.Data;

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
@Data
public class Type extends AbstractIdEntity implements java.io.Serializable {
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "typegroupid")
	private TypeGroup typeGroup;//类型分组
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "typepid")
	private Type type;//父类型
	@Column(name = "typename", length = 50)
	private String typename;//类型名称
	@Column(name = "typecode", length = 50)
	private String typecode;//类型编码

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "type")
	private List<Type> types =new ArrayList();
}