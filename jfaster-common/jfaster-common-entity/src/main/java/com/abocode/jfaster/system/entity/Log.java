package com.abocode.jfaster.system.entity;

import com.abocode.jfaster.core.AbstractIdEntity;
import lombok.Data;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "t_s_log")
@Data
public class Log extends AbstractIdEntity implements java.io.Serializable {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;
    @Column(name = "log_level")
    private Short loglevel;
    @Column(name = "operation_time", nullable = false, length = 35)
    private Timestamp operationTime;
    @Column(name = "operation_type")
    private Short operationType;
    @Column(name = "log_content", nullable = false, length = 2000)
    private String logContent;
    @Column(length = 100)
    private String broswer;
    @Column(length = 300)
    private String note;
    @Column(name = "user_id", length = 32)
    private String userId;
}