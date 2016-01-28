package org.jeecgframework.web.system.entity.base;

import org.jeecgframework.core.common.entity.IdEntity;

import javax.persistence.*;
import java.sql.Timestamp;


/**
 * @author  张代浩
 * 项目附件父表(其他附件表需继承该表)
 */
@Entity
@Table(name = "t_s_UploadFile")
@Inheritance(strategy = InheritanceType.JOINED)
public  class TSUploadFile extends IdEntity implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private String sessionKey;// 业务类主键
	private String name;// 附件名称
	private byte[] content;// 附件内容
	private String path;// 附件物理路径
	private String extend;// 扩展名
	private Timestamp createdate;

	
	@Column(name = "extend", length = 32)
	public String getExtend() {
		return extend;
	}

	public void setExtend(String extend) {
		this.extend = extend;
	}

	@Column(name = "session_key", length = 32)
	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

   @Column(name = "name", length = 100)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "content",length=3000)
	@Lob
	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}
	 @Column(name = "path", length = 100)
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	@Column(name = "createdate", length = 35)
	public Timestamp getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Timestamp createdate) {
		this.createdate = createdate;
	}
}