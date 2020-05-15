package com.abocode.jfaster.system.entity;

import com.abocode.jfaster.core.AbstractIdEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 权限操作表
 *
 * @author 张代浩
 */
@Entity
@Table(name = "t_s_operation")
@Data
public class Operation extends AbstractIdEntity implements java.io.Serializable {
    @Column(name = "operationtype")
    private String operationname;
    @Column(name = "operationname", length = 50)
    private String operationcode;
    @Column(name = "operationcode", length = 50)
    private String operationicon;
    @Column(name = "operationicon", length = 100)
    private Short status;
    @Column(name = "status")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "iconid")
    private Icon icon = new Icon();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "functionid")
    private Function function = new Function();
    private Short operationType;
}