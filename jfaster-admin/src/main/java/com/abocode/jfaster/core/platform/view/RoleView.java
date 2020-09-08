package com.abocode.jfaster.core.platform.view;

import lombok.Data;

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