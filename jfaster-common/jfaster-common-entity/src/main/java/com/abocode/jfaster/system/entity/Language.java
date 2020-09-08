package com.abocode.jfaster.system.entity;

import com.abocode.jfaster.core.AbstractIdEntity;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "t_s_language")
@DynamicUpdate()
@DynamicInsert()
@Data
public class Language extends AbstractIdEntity implements java.io.Serializable {
	/**语言主键*/
	@Column(name ="lang_key",nullable=false,length=50)
	private String langKey;
	/**内容*/
	@Column(name ="lang_context",nullable=false,length=500)
	private String langContext;
	/**语言*/
	@Column(name ="lang_code",nullable=false,length=50)
	private String langCode;
	/**创建时间*/
	@Column(name ="create_by",nullable=false,length=20)
	private String createBy;
	@Column(name ="create_by_id",nullable=false,length=32)
	private String createById;
	@Column(name = "create_date",nullable=false)
	private Date createDate;
	@Column(name = "update_by",nullable=false,length=20)
	private String updateBy;
	@Column(name = "update_by_id",nullable=false,length=32)
	private String updateById;
	@Column(name = "update_date",nullable=false,length=32)
	private Date updateDate;
}
