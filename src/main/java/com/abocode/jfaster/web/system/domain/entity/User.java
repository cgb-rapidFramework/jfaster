package com.abocode.jfaster.web.system.domain.entity;

import com.abocode.jfaster.core.repository.entity.IdEntity;
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
public class User extends IdEntity implements java.io.Serializable {
	private String userName;// 用户名
	private String realName;// 真实姓名
	private String password;//用户密码
	private String signature;// 签名
	private Short status;// 状态1：在线,2：离线,0：禁用
	private String signatureFile;// 签名文件
	private String mobilePhone;// 手机
	private String officePhone;// 办公电话
	private String email;// 邮箱
	private String browser;// 用户使用浏览器类型
	private String userKey;// 用户验证唯一标示
	private Depart currentDepart = new Depart();// 当前部门
	private List<UserOrg> userOrgList = new ArrayList<UserOrg>();
	private List<RoleUser> roleUserList = new ArrayList<RoleUser>();
	private List<Depart> departs = new ArrayList<Depart>();
	private List<Role> roles = new ArrayList<Role>();
	@Column(name = "password", length = 100)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "username", nullable = false, length = 10)
	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name = "realname", length = 50)
	public String getRealName() {
		return this.realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	@Transient
	public Depart getCurrentDepart() {
		return currentDepart;
	}

	public void setCurrentDepart(Depart currentDepart) {
		this.currentDepart = currentDepart;
	}

	@JsonIgnore
	@OneToMany(mappedBy = "tsUser")
	public List<UserOrg> getUserOrgList() {
		return userOrgList;
	}

	public void setUserOrgList(List<UserOrg> userOrgList) {
		this.userOrgList = userOrgList;
	}

	@JsonIgnore
	@OneToMany(mappedBy = "TSUser")
	public List<RoleUser> getRoleUserList() {
		return roleUserList;
	}

	public void setRoleUserList(List<RoleUser> roleUserList) {
		this.roleUserList = roleUserList;
	}

	@JsonIgnore
	@Transient
	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	@Column(name = "signatureFile", length = 100)
	public String getSignatureFile() {
		return this.signatureFile;
	}

	public void setSignatureFile(String signatureFile) {
		this.signatureFile = signatureFile;
	}


	@JsonIgnore
	@Transient
	public List<Depart> getDeparts() {
		return departs;
	}

	public void setDeparts(List<Depart> departs) {
		this.departs = departs;
	}


	@Column(name = "mobilePhone", length = 30)
	public String getMobilePhone() {
		return this.mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	@Column(name = "officePhone", length = 20)
	public String getOfficePhone() {
		return this.officePhone;
	}

	public void setOfficePhone(String officePhone) {
		this.officePhone = officePhone;
	}

	@Column(name = "email", length = 50)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "browser", length = 20)
	public String getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}

	@Column(name = "userkey", length = 200)
	public String getUserKey() {
		return userKey;
	}

	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}
	@Column(name = "signature", length = 3000)
	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}
	@Column(name = "status")
	public Short getStatus() {
		return this.status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}
}