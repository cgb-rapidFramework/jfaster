package com.abocode.jfaster.admin.system.service.impl;

import com.abocode.jfaster.admin.system.repository.SystemRepository;
import com.abocode.jfaster.admin.system.repository.TemplateRepository;
import com.abocode.jfaster.admin.system.service.TemplateService;
import com.abocode.jfaster.api.core.AvailableEnum;
import com.abocode.jfaster.core.common.constants.Globals;
import com.abocode.jfaster.core.common.util.BeanPropertyUtils;
import com.abocode.jfaster.system.entity.Template;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Slf4j
public class TemplateServiceImpl implements TemplateService {
    @Autowired
    private TemplateRepository templateRepository;
    @Autowired
    private SystemRepository systemRepository;

    @Override
    @Transactional
    public void save(Template template) {
        String message;
        if (!StringUtils.isEmpty(template.getId())) {
            message = "模版管理更新成功";
            Template t = templateRepository.find(Template.class, template.getId());

            BeanPropertyUtils.copyObjectToObject(template, t);
            if (t.getStatus() == AvailableEnum.AVAILABLE.getValue()) {
                templateRepository.setDefault(template.getId());
            }
            templateRepository.saveOrUpdate(t);
            systemRepository.addLog(message, Globals.LOG_TYPE_UPDATE, Globals.LOG_LEVEL);

        } else {
            message = "模版管理添加成功";
            template.setStatus(AvailableEnum.UNAVAILABLE.getValue());
            templateRepository.save(template);
            systemRepository.addLog(message, Globals.LOG_TYPE_INSERT, Globals.LOG_LEVEL);
        }

    }

    @Override
    public void delById(String id) {
        Object template = systemRepository.find(Template.class, id);
        String message = "模版管理删除成功";
        templateRepository.delete(template);
        systemRepository.addLog(message, Globals.LOG_TYPE_DEL, Globals.LOG_LEVEL);
    }
}
