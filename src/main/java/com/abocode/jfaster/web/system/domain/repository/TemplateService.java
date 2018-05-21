package com.abocode.jfaster.web.system.domain.repository;

import com.abocode.jfaster.core.repository.service.CommonService;

public interface TemplateService extends CommonService{
    void setDefault(String id);
}
