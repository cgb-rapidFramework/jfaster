package com.abocode.jfaster.web.system.domain.repository.persistence.hibernate;

import com.abocode.jfaster.web.system.interfaces.constant.TemplateConstant;
import com.abocode.jfaster.web.system.domain.entity.Template;
import com.abocode.jfaster.web.system.domain.repository.TemplateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abocode.jfaster.core.repository.service.impl.CommonServiceImpl;

@Service("templateService")
@Transactional
public class TemplateServiceImpl extends CommonServiceImpl implements TemplateService {

    @Override
    public void setDefault(String id) {
        this.clearDefault();
        Template template = this.findEntity(Template.class, id);
        template.setStatus(TemplateConstant.TEMPLATE_STATUS_IS_AVAILABLE);
        this.save(template);
    }

    public void clearDefault() {
        Template templateEntity=this.findUniqueByProperty(Template.class,"status", TemplateConstant.TEMPLATE_STATUS_IS_AVAILABLE);
        templateEntity.setStatus(TemplateConstant.TEMPLATE_STATUS_IS_UNAVAILABLE);
        this.save(templateEntity);
    }
}