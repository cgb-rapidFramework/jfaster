package com.abocode.jfaster.web.system.application.dto.view;

import com.abocode.jfaster.core.domain.entity.IdEntity;

import javax.persistence.*;

/**
 * 权限操作
 *  @author  guanxf
 */
public class OperationView extends IdEntity implements java.io.Serializable {
	private String operationname;
	private String operationcode;
	private String operationicon;
	private Short status;

	
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

	@Override
    public boolean equals(Object obj) {  
        if(this == obj)  
            return false;  
        if(obj == null)  
            return false;  
        if(getClass() != obj.getClass() )  
            return false;
		OperationView other = (OperationView)obj;
        if (getId().equals(other.getId())){
        	return true; 
        }else {
        	return false;  
        }
    }  
}