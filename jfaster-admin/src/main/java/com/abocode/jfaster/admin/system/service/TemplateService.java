package com.abocode.jfaster.admin.system.service;

import com.abocode.jfaster.system.entity.Template;

public interface TemplateService{
    void save(Template template);

    void delById(String id);
}
