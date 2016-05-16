package org.jeecgframework.web.system.entity.core;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.*;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

/**   
 * @Title: Entity
 * @Description: job
 * @author zhangdaihao
 * @date 2016-04-29 22:58:41
 * @version V1.0   
 *
 */
@Entity
@Table(name = "t_s_job", schema = "")
@DynamicUpdate(true)
@DynamicInsert(true)
@SuppressWarnings("serial")
public class JobEntity implements java.io.Serializable {
	/**任务id*/
	private java.lang.String id;
	/**任务名称*/
	private java.lang.String name;
	/**任务分组*/
	private java.lang.String group;
	/**任务状态*/
	private java.lang.String status;
	/**任务运行时间表达式*/
	private java.lang.String expression;
	/**任务调度业务类*/
	private java.lang.String clazz;
	/**任务描述*/
	private java.lang.String description;
	/**创建时间*/
	private java.util.Date createDate;
	/**修改时间*/
	private java.util.Date updateDate;
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  任务id
	 */
	
	@Id
	@GeneratedValue(generator = "paymentableGenerator")
	@GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
	@Column(name ="ID",nullable=false,length=32)
	public java.lang.String getId(){
		return this.id;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  任务id
	 */
	public void setId(java.lang.String id){
		this.id = id;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  任务名称
	 */
	@Column(name ="NAME",nullable=false,length=100)
	public java.lang.String getName(){
		return this.name;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  任务名称
	 */
	public void setName(java.lang.String name){
		this.name = name;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  任务分组
	 */
	//@Transient
	@Column(name ="[GROUP]",nullable=false,length=100)
	public java.lang.String getGroup(){
		return this.group;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  任务分组
	 */
	public void setGroup(java.lang.String group){
		this.group = group;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  任务状态
	 */
	@Column(name ="STATUS",nullable=true,length=50)
	public java.lang.String getStatus(){
		return this.status;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  任务状态
	 */
	public void setStatus(java.lang.String status){
		this.status = status;
	}

	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  任务运行时间表达式
	 */
	@Column(name ="EXPRESSION",nullable=false,length=200)
	public java.lang.String getExpression(){
		return this.expression;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  任务运行时间表达式
	 */
	public void setExpression(java.lang.String expression){
		this.expression = expression;
	}

	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  任务描述
	 */
	@Column(name ="DESCRIPTION",nullable=true,length=500)
	public java.lang.String getDescription(){
		return this.description;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  任务描述
	 */
	public void setDescription(java.lang.String description){
		this.description = description;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  创建时间
	 */
	@Column(name ="CREATE_DATE",nullable=true)
	public java.util.Date getCreateDate(){
		return this.createDate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  创建时间
	 */
	public void setCreateDate(java.util.Date createDate){
		this.createDate = createDate;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  修改时间
	 */
	@Column(name ="UPDATE_DATE",nullable=true)
	public java.util.Date getUpdateDate(){
		return this.updateDate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  修改时间
	 */
	public void setUpdateDate(java.util.Date updateDate){
		this.updateDate = updateDate;
	}

	@Column(name ="clazz",nullable=true)
	public String getClazz() {
		return clazz;
	}
	public void setClazz(String clazz) {
		this.clazz = clazz;
	}
}
