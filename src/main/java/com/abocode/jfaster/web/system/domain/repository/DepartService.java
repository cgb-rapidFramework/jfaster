package com.abocode.jfaster.web.system.domain.repository;

import com.abocode.jfaster.core.domain.repository.CommonRepository;
import com.abocode.jfaster.web.system.domain.entity.Depart;

/**
 * Created by guanxf on 2016/3/20.
 */
public interface DepartService  extends CommonRepository {
    void deleteDepart(Depart id);
}
