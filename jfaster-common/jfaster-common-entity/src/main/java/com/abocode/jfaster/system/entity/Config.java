package com.abocode.jfaster.system.entity;
import com.abocode.jfaster.core.AbstractIdEntity;
import lombok.Data;

import javax.persistence.*;

/**
 * TConfig entity.
 * 系统配置类
 * @author  张代浩
 */
@Entity
@Table(name = "t_s_config")
@Data
public class Config extends AbstractIdEntity implements java.io.Serializable {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId")
	private User user;
	@Column(name = "code", length = 100)
	private String code;
	@Column(name = "name", nullable = false, length = 100)
	private String name;
	@Column(name = "content", length = 300)
	private String contents;
	@Column(name = "note", length = 300)
	private String note;
}