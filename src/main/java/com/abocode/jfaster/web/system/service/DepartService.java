package com.abocode.jfaster.web.system.service;

import com.abocode.jfaster.core.common.service.CommonService;
import com.abocode.jfaster.web.system.entity.Depart;

/**
 * Created by guanxf on 2016/3/20.
 */
public interface DepartService  extends CommonService {
    void deleteDepart(Depart id);
}
