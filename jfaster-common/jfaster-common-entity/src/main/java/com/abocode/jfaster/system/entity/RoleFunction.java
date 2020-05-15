package com.abocode.jfaster.system.entity;

import com.abocode.jfaster.core.AbstractIdEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
@Entity
@Table(name = "t_s_role_function")
@Data
public class RoleFunction extends AbstractIdEntity implements java.io.Serializable {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "functionid")
    private Function function;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roleid")
    private Role role;
    @Column(name = "operation", length = 100)
    private String operation;
    @Column(name = "datarule", length = 100)
    private String dataRule;
}