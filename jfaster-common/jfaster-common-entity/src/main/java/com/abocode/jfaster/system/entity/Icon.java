package com.abocode.jfaster.system.entity;

import com.abocode.jfaster.core.AbstractIdEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_s_icon")
@Data
public class Icon extends AbstractIdEntity implements java.io.Serializable {
    @Column(name = "icon_name", nullable = false, length = 100)
    private String iconName;
    @Column(name = "icon_type")
    private Short iconType;
    @Column(name = "icon_path", length = 300, precision = 300)
    private String iconPath;
    @Column(name = "icon_content", length = 1000, precision = 3000)
    private byte[] iconContent;
    @Column(name = "icon_clazz", length = 200)
    private String iconClazz;
    @Column(name = "icon_extend")
    private String iconExtend;
}