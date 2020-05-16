package com.abocode.jfaster.system.entity;
import com.abocode.jfaster.core.AbstractIdEntity;
import lombok.Data;

import javax.persistence.*;
@Entity
@Table(name = "t_s_config")
@Data
public class Config extends AbstractIdEntity implements java.io.Serializable {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
	@Column(length = 100)
	private String code;
	@Column( nullable = false, length = 100)
	private String name;
	@Column(length = 300)
	private String content;
	@Column(length = 300)
	private String note;
}