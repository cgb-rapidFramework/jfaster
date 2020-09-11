package com.abocode.jfaster.admin.system.service.impl;

import com.abocode.jfaster.admin.system.repository.LanguageRepository;
import com.abocode.jfaster.admin.system.repository.SystemRepository;
import com.abocode.jfaster.admin.system.service.LanguageService;
import com.abocode.jfaster.core.common.constants.Globals;
import com.abocode.jfaster.core.common.util.BeanPropertyUtils;
import com.abocode.jfaster.core.common.util.StrUtils;
import com.abocode.jfaster.core.platform.utils.LanguageUtils;
import com.abocode.jfaster.system.entity.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class LanguageServiceImpl implements LanguageService {
    @Resource
    private SystemRepository systemService;
    @Autowired
    private LanguageRepository languageRepository;
    @Override
    public void delById(String id) {
        Object language = systemService.find(Language.class, id);
        String message = LanguageUtils.paramDelSuccess("common.language");
        languageRepository.delete(language);
        languageRepository.initLanguage();
        systemService.addLog(message, Globals.LOG_TYPE_DEL, Globals.LOG_LEVEL);
    }

    @Override
    public void save(Language language) {
        String message;
        if (StrUtils.isNotEmpty(language.getId())) {
            message = LanguageUtils.paramUpdSuccess("common.language");
            Language t = languageRepository.find(Language.class, language.getId());
            BeanPropertyUtils.copyObjectToObject(language, t);
            languageRepository.saveOrUpdate(t);
            languageRepository.initLanguage();
            systemService.addLog(message, Globals.LOG_TYPE_UPDATE, Globals.LOG_LEVEL);
        } else {
            message= LanguageUtils.existLangContext(language.getLangContext())? languageRepository.getLang("common.langcontext.exist"):"";
            if(StrUtils.isEmpty(message))
            {
                languageRepository.save(language);
                message = LanguageUtils.paramAddSuccess("common.language");
                systemService.addLog(message, Globals.LOG_TYPE_INSERT, Globals.LOG_LEVEL);
            }
        }
    }
}
