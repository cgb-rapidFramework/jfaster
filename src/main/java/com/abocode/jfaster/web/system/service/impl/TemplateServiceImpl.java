package com.abocode.jfaster.web.system.service.impl;

import com.abocode.jfaster.web.system.constant.TemplateConstant;
import com.abocode.jfaster.web.system.entity.TemplateEntity;
import com.abocode.jfaster.web.system.service.TemplateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abocode.jfaster.core.common.service.impl.CommonServiceImpl;

@Service("templateService")
@Transactional
public class TemplateServiceImpl extends CommonServiceImpl implements TemplateService {

    @Override
    public void setDefault(String id) {
        this.clearDefault();
        TemplateEntity template = this.findEntity(TemplateEntity.class, id);
        template.setStatus(TemplateConstant.TEMPLATE_STATUS_IS_AVAILABLE);
        this.save(template);
    }

    public void clearDefault() {
        TemplateEntity templateEntity=this.findUniqueByProperty(TemplateEntity.class,"status", TemplateConstant.TEMPLATE_STATUS_IS_AVAILABLE);
        templateEntity.setStatus(TemplateConstant.TEMPLATE_STATUS_IS_UNAVAILABLE);
        this.save(templateEntity);
    }
}