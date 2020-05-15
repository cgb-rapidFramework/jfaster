package com.abocode.jfaster.system.entity;

import com.abocode.jfaster.core.AbstractIdEntity;
import lombok.Data;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


/**
 * TLog entity.
 *  @author  张代浩
 */
@Entity
@Table(name = "t_s_log")
@Data
public class Log extends AbstractIdEntity implements java.io.Serializable {
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userid",insertable=false,updatable=false)
	private User user;
	@Column(name = "loglevel")
	private Short loglevel;
	@Column(name = "operatetime", nullable = false, length = 35)
	private Timestamp operatetime;
	@Column(name = "operatetype")
	private Short operatetype;

	@Column(name = "logcontent", nullable = false, length = 2000)
	private String logcontent;
	//用户浏览器类型
	@Column(name = "broswer", length = 100)
	private String broswer;
	@Column(name = "note", length = 300)
	private String note;
	@Column(name = "userid", length = 300)
    private String userid;
}