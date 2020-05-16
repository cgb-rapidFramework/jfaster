package com.abocode.jfaster.admin.system.repository.persistence.hibernate;

import com.abocode.jfaster.core.repository.persistence.hibernate.CommonRepositoryImpl;
import com.abocode.jfaster.system.entity.Org;
import com.abocode.jfaster.admin.system.repository.DepartRepository;
import org.springframework.stereotype.Service;

/**
 * Created by guanxf on 2016/3/20.
 */
@Service
public class DepartRepositoryImpl extends CommonRepositoryImpl implements DepartRepository {
    @Override
    public void deleteDepart(Org depart) {
        String id=depart.getId();
        Long userCount = this.queryForCount("select count(1) from t_s_user_org where org_id='" + id + "'");
        // 组织机构下没有用户时，该组织机构才允许删除。
        if(userCount == 0) {
            this.executeSql("delete from t_s_role_org where org_id=?",id);
            this.delete(depart);
        }
    }
}
