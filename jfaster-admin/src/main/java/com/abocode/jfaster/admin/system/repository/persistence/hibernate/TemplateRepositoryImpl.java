package com.abocode.jfaster.admin.system.repository.persistence.hibernate;

import com.abocode.jfaster.admin.system.repository.TemplateRepository;
import com.abocode.jfaster.api.core.AvailableEnum;
import com.abocode.jfaster.system.entity.Template;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abocode.jfaster.core.repository.persistence.hibernate.CommonRepositoryImpl;
@Service
@Transactional
public class TemplateRepositoryImpl extends CommonRepositoryImpl implements TemplateRepository {

    @Override
    public void setDefault(String id) {
        this.clearDefault();
        Template template = this.findEntity(Template.class, id);
        template.setStatus(AvailableEnum.AVAILABLE.getValue());
        this.save(template);
    }

    public void clearDefault() {
        Template templateEntity=this.findUniqueByProperty(Template.class,"status", AvailableEnum.AVAILABLE.getValue());
        templateEntity.setStatus(AvailableEnum.UNAVAILABLE.getValue());
        this.save(templateEntity);
    }
}