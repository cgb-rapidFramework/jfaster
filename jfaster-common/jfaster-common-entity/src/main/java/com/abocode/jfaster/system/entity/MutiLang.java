package com.abocode.jfaster.system.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.abocode.jfaster.core.AbstractIdEntity;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

/**   
 * @Title: Entity
 * @Description: 多语言
 * @author Rocky
 * @date 2014-06-28 00:09:31
 * @version V1.0   
 *
 */
@Entity
@Table(name = "t_s_muti_lang", schema = "")
@DynamicUpdate()
@DynamicInsert()
@Data
public class MutiLang extends AbstractIdEntity implements java.io.Serializable {
	/**主键*/
	private java.lang.String id;
	/**语言主键*/
	@Column(name ="LANG_KEY",nullable=false,length=50)
	private java.lang.String langKey;
	/**内容*/
	@Column(name ="LANG_CONTEXT",nullable=false,length=500)
	private java.lang.String langContext;
	/**语言*/
	@Column(name ="LANG_CODE",nullable=false,length=50)
	private java.lang.String langCode;
	/**创建时间*/
	@Column(name ="CREATE_DATE",nullable=false)
	private java.util.Date createDate;
	/**创建人编号*/
	@Column(name ="CREATE_BY",nullable=false,length=50)
	private java.lang.String createBy;
	/**创建人姓名*/
	@Column(name ="CREATE_NAME",nullable=false,length=50)
	private java.lang.String createName;
	/**更新日期*/
	@Column(name ="UPDATE_DATE",nullable=true)
	private java.util.Date updateDate;
	/**更新人编号*/
	@Column(name ="UPDATE_BY",nullable=true,length=50)
	private java.lang.String updateBy;
	/**更新人姓名*/
	@Column(name ="UPDATE_NAME",nullable=true,length=50)
	private java.lang.String updateName;

}
