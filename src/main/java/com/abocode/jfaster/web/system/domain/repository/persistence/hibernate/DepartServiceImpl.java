package com.abocode.jfaster.web.system.domain.repository.persistence.hibernate;

import com.abocode.jfaster.core.repository.service.impl.CommonServiceImpl;
import com.abocode.jfaster.web.system.domain.entity.Depart;
import com.abocode.jfaster.web.system.domain.repository.DepartService;
import org.springframework.stereotype.Service;

/**
 * Created by guanxf on 2016/3/20.
 */
@Service("departService")
public class DepartServiceImpl extends CommonServiceImpl implements DepartService {


    @Override
    public void deleteDepart(Depart depart) {
        String id=depart.getId();
        Long userCount = this.queryForCount("select count(1) from t_s_user_org where org_id='" + id + "'");
        if(userCount == 0) { // 组织机构下没有用户时，该组织机构才允许删除。
            this.executeSql("delete from t_s_role_org where org_id=?",id);
            this.delete(depart);
        }
    }
}
