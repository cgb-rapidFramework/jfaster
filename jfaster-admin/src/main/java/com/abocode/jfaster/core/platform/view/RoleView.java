package com.abocode.jfaster.core.platform.view;

import com.abocode.jfaster.core.AbstractIdEntity;
import lombok.Data;

import javax.persistence.Column;

/**
 * 角色表
 *  @author  guanxf
 */
@Data
public class RoleView implements java.io.Serializable {
	private String id;
	private String roleName;//角色名称
	private String roleCode;//角色编码
}