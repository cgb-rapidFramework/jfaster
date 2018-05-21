package com.abocode.jfaster.web.system.domain.entity;

import com.abocode.jfaster.core.repository.entity.IdEntity;

import javax.persistence.*;

/**
 * 用户-组织机构 实体
 * <p/>
 * <p><b>User:</b> zhanggm <a href="mailto:guomingzhang2008@gmail.com">guomingzhang2008@gmail.com</a></p>
 * <p><b>Date:</b> 2014-08-22 15:39</p>
 *
 * @author 张国明
 */
@Entity
@Table(name = "t_s_user_org")
public class UserOrg extends IdEntity implements java.io.Serializable {
    private User tsUser;
    private Depart tsDepart;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    public User getTsUser() {
        return tsUser;
    }

    public void setTsUser(User tsDepart) {
        this.tsUser = tsDepart;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "org_id")
    public Depart getTsDepart() {
        return tsDepart;
    }

    public void setTsDepart(Depart tsDepart) {
        this.tsDepart = tsDepart;
    }
}
