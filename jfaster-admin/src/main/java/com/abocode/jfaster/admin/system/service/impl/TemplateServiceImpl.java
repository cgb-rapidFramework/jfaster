package com.abocode.jfaster.admin.system.service.impl;

import com.abocode.jfaster.admin.system.repository.SystemRepository;
import com.abocode.jfaster.admin.system.repository.TemplateRepository;
import com.abocode.jfaster.admin.system.service.TemplateService;
import com.abocode.jfaster.api.core.AvailableEnum;
import com.abocode.jfaster.core.common.constants.Globals;
import com.abocode.jfaster.core.common.util.BeanPropertyUtils;
import com.abocode.jfaster.core.common.util.LogUtils;
import com.abocode.jfaster.system.entity.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class TemplateServiceImpl implements TemplateService {
    @Autowired
    private TemplateRepository templateRepository;
    @Autowired
    private SystemRepository systemRepository;
    @Override
    public void save(Template template) {
        String message;
        if (!StringUtils.isEmpty(template.getId())) {
            message = "模版管理更新成功";
            Template t = templateRepository.find(Template.class, template.getId());
            try {
                BeanPropertyUtils.copyBeanNotNull2Bean(template, t);
                if(t.getStatus()== AvailableEnum.AVAILABLE.getValue()){
                    templateRepository.setDefault(template.getId());
                }
                templateRepository.saveOrUpdate(t);
                systemRepository.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
            } catch (Exception e) {
                LogUtils.error(e.getMessage());
                message = "模版管理更新失败";
            }
        } else {
            message = "模版管理添加成功";
            template.setStatus(AvailableEnum.UNAVAILABLE.getValue());
            templateRepository.save(template);
            systemRepository.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
        }

    }

    @Override
    public void delById(String id) {
        Object template = systemRepository.findEntity(Template.class, id);
        String message = "模版管理删除成功";
        templateRepository.delete(template);
        systemRepository.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
    }
}
