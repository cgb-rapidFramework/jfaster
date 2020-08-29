package com.abocode.jfaster.system.entity;

import com.abocode.jfaster.core.AbstractIdEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_s_role")
@Data
public class Role extends AbstractIdEntity implements java.io.Serializable {
    //角色名称
    @Column(name = "role_name", nullable = false, length = 100)
    private String roleName;
    //角色编码
    @Column(name = "role_code", length = 10)
    private String roleCode;
}