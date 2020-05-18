package com.abocode.jfaster.admin.system.repository;

import com.abocode.jfaster.core.repository.CommonRepository;
public interface TemplateRepository extends CommonRepository {
    void setDefault(String id);
}
