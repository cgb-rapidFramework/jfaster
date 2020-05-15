package com.abocode.jfaster.system.entity;

import javax.persistence.*;

import com.abocode.jfaster.core.AbstractIdEntity;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**   
 * @Title: Entity
 * @Description: job
 * @author zhangdaihao
 * @date 2016-04-29 22:58:41
 * @version V1.0   
 *
 */
@Entity
@Table(name = "t_s_job")
@DynamicUpdate(true)
@DynamicInsert(true)
@Data
public class Job extends AbstractIdEntity implements java.io.Serializable {

	/**任务名称*/
	@Column(name ="NAME",nullable=false,length=100)
	private java.lang.String name;
	/**任务分组*/
	@Column(name ="[GROUP]",nullable=false,length=100)
	private java.lang.String group;
	/**任务状态*/
	@Column(name ="STATUS",nullable=true,length=50)
	private java.lang.String status;
	/**任务运行时间表达式*/
	@Column(name ="EXPRESSION",nullable=false,length=200)
	private java.lang.String expression;
	/**任务调度业务类*/
	@Column(name ="clazz",nullable=true)
	private java.lang.String clazz;
	/**任务描述*/
	@Column(name ="DESCRIPTION",nullable=true,length=500)
	private java.lang.String description;
	/**创建时间*/
	@Column(name ="CREATE_DATE",nullable=true)
	private java.util.Date createDate;
	/**修改时间*/
	@Column(name ="UPDATE_DATE",nullable=true)
	private java.util.Date updateDate;
}
