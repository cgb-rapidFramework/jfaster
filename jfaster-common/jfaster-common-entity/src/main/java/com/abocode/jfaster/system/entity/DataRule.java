package com.abocode.jfaster.system.entity;

import com.abocode.jfaster.core.AbstractIdEntity;
import lombok.Data;


import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 
  * @ClassName: TSDataRule 数据规则权限表
  * @author Comsys-skyCc cmzcheng@gmail.com
  * @date 2014-8-19 下午2:19:29
 */
@Entity
@Table(name = "t_s_data_rule")
@Data
public class DataRule extends AbstractIdEntity implements Serializable {
	@Column(name ="rule_name",nullable=false,length=32)
	private String ruleName;
	@Column(name ="rule_column",nullable=false,length=100)
	private String ruleColumn;
	@Column(name ="rule_conditions",nullable=false,length=100)
	private String ruleConditions;
	@Column(name ="rule_value",nullable=false,length=100)
	private String ruleValue;
	@Column(name ="create_by",nullable=false,length=32)
	private String createBy;
	@Column(name ="create_name",nullable=false,length=32)
	private String createName;
	@Column(name ="create_date",nullable=false)
	private Date createDate;
	@Column(name ="update_by",nullable=false,length=32)
	private String updateBy;
	@Column(name ="update_name",nullable=false,length=32)
	private String updateName;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "functionId")
	private Function function = new Function();
}
