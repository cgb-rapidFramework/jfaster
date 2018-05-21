package com.abocode.jfaster.web.system.domain.entity;

import com.abocode.jfaster.core.repository.entity.IdEntity;

import javax.persistence.*;

/**
 * 角色-组织机构 实体
 * <p/>
 * <p><b>User:</b> zhanggm <a href="mailto:guomingzhang2008@gmail.com">guomingzhang2008@gmail.com</a></p>
 * <p><b>Date:</b> 2014-08-21 13:48</p>
 *
 * @author 张国明
 */
@Entity
@Table(name = "t_s_role_org")
public class RoleOrg extends IdEntity implements java.io.Serializable {
    private Depart tsDepart;
    private Role tsRole;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "org_id")
    public Depart getTsDepart() {
        return tsDepart;
    }

    public void setTsDepart(Depart tsDepart) {
        this.tsDepart = tsDepart;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    public Role getTsRole() {
        return tsRole;
    }

    public void setTsRole(Role tsRole) {
        this.tsRole = tsRole;
    }
}
