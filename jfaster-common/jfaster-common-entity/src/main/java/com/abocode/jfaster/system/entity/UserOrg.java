package com.abocode.jfaster.system.entity;

import com.abocode.jfaster.core.AbstractIdEntity;
import lombok.Data;

import javax.persistence.*;
@Entity
@Table(name = "t_s_user_org")
@Data
public class UserOrg extends AbstractIdEntity implements java.io.Serializable {
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "org_id")
    private Org org;
}
