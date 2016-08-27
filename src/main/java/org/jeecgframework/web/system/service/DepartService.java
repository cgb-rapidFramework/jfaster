package org.jeecgframework.web.system.service;

import org.jeecgframework.core.common.service.CommonService;
import org.jeecgframework.web.system.entity.TSDepart;

/**
 * Created by guanxf on 2016/3/20.
 */
public interface DepartService  extends CommonService {
    void deleteDepart(TSDepart id);
}
