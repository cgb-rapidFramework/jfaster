package com.abocode.jfaster.system.entity;

import com.abocode.jfaster.core.AbstractIdEntity;
import lombok.Data;

import javax.persistence.*;

/**
 * 权限操作表
 */
@Entity
@Table(name = "t_s_operation")
@Data
public class Operation extends AbstractIdEntity implements java.io.Serializable {
    @Column(name = "operation_name", length = 20)
    private String operationName;
    @Column(name = "operation_code", length = 50)
    private String operationCode;
    @Column(name = "operation_icon", length = 100)
    private String operationIcon;
    @Column
    private Short status;
    @Column(name = "operation_type")
    private Short operationType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "icon_id")
    private Icon icon = new Icon();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "function_id")
    private Function function = new Function();

}