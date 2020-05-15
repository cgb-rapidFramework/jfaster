package com.abocode.jfaster.admin.system.repository;

import com.abocode.jfaster.core.repository.CommonRepository;
import com.abocode.jfaster.system.entity.Depart;

/**
 * Created by guanxf on 2016/3/20.
 */
public interface DepartRepository extends CommonRepository {
    void deleteDepart(Depart id);
}
