package com.abocode.jfaster.system.entity;
import com.abocode.jfaster.core.AbstractIdEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * TIcon entity.
 *  @author  张代浩
 */
@Entity
@Table(name = "t_s_icon")
@Data
public class Icon extends AbstractIdEntity implements java.io.Serializable {
	@Column(name = "name", nullable = false, length = 100)
	private String iconName;
	@Column(name = "type")
	private Short iconType;
	@Column(name = "path", length = 300,precision =300)
	private String iconPath;

	@Column(name = "content",length = 1000,precision =3000)
	private byte[] iconContent;
	@Column(name = "iconclas", length = 200)
	private String iconClas;
	@Column(name = "extend")
	private String extend;
}