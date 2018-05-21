package com.abocode.jfaster.web.system.domain.repository;

import com.abocode.jfaster.core.repository.service.CommonService;
import com.abocode.jfaster.web.system.domain.entity.Depart;

/**
 * Created by guanxf on 2016/3/20.
 */
public interface DepartService  extends CommonService {
    void deleteDepart(Depart id);
}
