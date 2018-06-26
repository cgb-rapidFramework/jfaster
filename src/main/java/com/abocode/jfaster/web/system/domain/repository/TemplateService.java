package com.abocode.jfaster.web.system.domain.repository;

import com.abocode.jfaster.core.domain.repository.CommonRepository;

public interface TemplateService extends CommonRepository {
    void setDefault(String id);
}
