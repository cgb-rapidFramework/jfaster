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
	@Column(name = "username",nullable = false, length = 10)
	private String username;// 用户名
	@Column(name = "real_name",length = 50)
	private String realName;// 真实姓名
	@Column(length = 100)
	private String password;//用户密码

	@Column(length = 3000)
	private String signature;// 签名
	@Column
	private Short status;// 状态1：在线,2：离线,0：禁用
	@Column(name = "signature_file",length = 100)
	private String signatureFile;// 签名文件
	@Column(name = "mobile_phone",length = 30)
	private String mobilePhone;// 手机
	@Column(name = "office_phone",length = 20)
	private String officePhone;// 办公电话
	@Column(length = 50)
	private String email;// 邮箱
	@Column(length = 20)
	private String browser;// 用户使用浏览器类型
	@Column(name = "user_key",length = 200)
	private String userKey;// 用户验证唯一标示
	@JsonIgnore
	@Transient
	private Org currentDepart = new Org();// 当前部门
	@JsonIgnore
	@OneToMany(mappedBy = "user")
	private List<UserOrg> userOrgList = new ArrayList<UserOrg>();
	@JsonIgnore
	@OneToMany(mappedBy = "user")
	private List<RoleUser> roleUserList = new ArrayList<RoleUser>();
	@JsonIgnore
	@Transient
	private List<Org> orgs = new ArrayList<Org>();
	@JsonIgnore
	@Transient
	private List<Role> roles = new ArrayList<Role>();

	@Override
	public String toString() {
		return "User{" +
				"id='" + getId() + '\'' +
				", username='" + username + '\'' +
				", realName='" + realName + '\'' +
				", password='" + password + '\'' +
				", signature='" + signature + '\'' +
				", status=" + status +
				", signatureFile='" + signatureFile + '\'' +
				", mobilePhone='" + mobilePhone + '\'' +
				", officePhone='" + officePhone + '\'' +
				", email='" + email + '\'' +
				", browser='" + browser + '\'' +
				", userKey='" + userKey + '\'' +
				'}';
	}
}