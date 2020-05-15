package com.abocode.jfaster.system.entity;

import com.abocode.jfaster.core.AbstractIdEntity;
import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 系统用户表
 *  @author  Franky
 */
@Entity
@Table(name = "t_s_user")
@PrimaryKeyJoinColumn(name = "id")
@Data
public class User extends AbstractIdEntity implements java.io.Serializable {
	@Column(name = "username", nullable = false, length = 10)
	private String userName;// 用户名
	@Column(name = "realname", length = 50)
	private String realName;// 真实姓名
	@Column(name = "password", length = 100)
	private String password;//用户密码

	@Column(name = "signature", length = 3000)
	private String signature;// 签名
	@Column(name = "status")
	private Short status;// 状态1：在线,2：离线,0：禁用
	@Column(name = "signatureFile", length = 100)
	private String signatureFile;// 签名文件
	@Column(name = "mobilePhone", length = 30)
	private String mobilePhone;// 手机
	@Column(name = "officePhone", length = 20)
	private String officePhone;// 办公电话
	@Column(name = "email", length = 50)
	private String email;// 邮箱
	@Column(name = "browser", length = 20)
	private String browser;// 用户使用浏览器类型
	@Column(name = "userkey", length = 200)
	private String userKey;// 用户验证唯一标示
	@JsonIgnore
	@Transient
	private Depart currentDepart = new Depart();// 当前部门
	@JsonIgnore
	@OneToMany(mappedBy = "user")
	private List<UserOrg> userOrgList = new ArrayList<UserOrg>();
	@JsonIgnore
	@OneToMany(mappedBy = "user")
	private List<RoleUser> roleUserList = new ArrayList<RoleUser>();
	private List<Depart> departs = new ArrayList<Depart>();
	@JsonIgnore
	@Transient
	private List<Role> roles = new ArrayList<Role>();
}