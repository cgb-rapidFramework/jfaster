package com.abocode.jfaster.web.system.domain.entity;

import com.abocode.jfaster.core.repository.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 权限操作表
 *  @author  张代浩
 */
@Entity
@Table(name = "t_s_operation")
public class Operation extends IdEntity implements java.io.Serializable {
	private String operationname;
	private String operationcode;
	private String operationicon;
	private Short status;
	private Icon TSIcon = new Icon();
	private Function TSFunction = new Function();
	
	private Short operationType;
	
	@Column(name = "operationtype")
	public Short getOperationType() {
		return operationType;
	}

	public void setOperationType(Short operationType) {
		this.operationType = operationType;
	}

	@Column(name = "operationname", length = 50)
	public String getOperationname() {
		return this.operationname;
	}

	public void setOperationname(String operationname) {
		this.operationname = operationname;
	}

	@Column(name = "operationcode", length = 50)
	public String getOperationcode() {
		return this.operationcode;
	}

	public void setOperationcode(String operationcode) {
		this.operationcode = operationcode;
	}

	@Column(name = "operationicon", length = 100)
	public String getOperationicon() {
		return this.operationicon;
	}

	public void setOperationicon(String operationicon) {
		this.operationicon = operationicon;
	}

	@Column(name = "status")
	public Short getStatus() {
		return this.status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "iconid")
	public Icon getTSIcon() {
		return TSIcon;
	}

	public void setTSIcon(Icon tSIcon) {
		TSIcon = tSIcon;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "functionid")
	public Function getTSFunction() {
		return TSFunction;
	}

	public void setTSFunction(Function tSFunction) {
		TSFunction = tSFunction;
	}
	
	@Override
    public boolean equals(Object obj) {  
        if(this == obj)  
            return false;  
        if(obj == null)  
            return false;  
        if(getClass() != obj.getClass() )  
            return false;  
        Operation other = (Operation)obj;
        if (getId().equals(other.getId())){
        	return true; 
        }else {
        	return false;  
        }
    }  
}