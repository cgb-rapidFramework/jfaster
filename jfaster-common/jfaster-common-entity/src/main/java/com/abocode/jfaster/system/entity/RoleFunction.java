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
    @JoinColumn(name = "function_id")
    private Function function;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;
    @Column(length = 100)
    private String operation;
    @Column(name = "data_rule", length = 100)
    private String dataRule;

    @Override
    public String toString() {
        return "RoleFunction{" +
                "id='" + getId() + '\'' +
                ",operation='" + operation + '\'' +
                ", dataRule='" + dataRule + '\'' +
                '}';
    }
}