package com.abocode.jfaster.admin.system.service.impl;

import com.abocode.jfaster.admin.system.repository.MutiLangRepository;
import com.abocode.jfaster.admin.system.repository.SystemRepository;
import com.abocode.jfaster.admin.system.service.MutiLangService;
import com.abocode.jfaster.core.common.constants.Globals;
import com.abocode.jfaster.core.common.util.BeanPropertyUtils;
import com.abocode.jfaster.core.common.util.LogUtils;
import com.abocode.jfaster.core.common.util.MutiLangUtils;
import com.abocode.jfaster.core.common.util.StringUtils;
import com.abocode.jfaster.system.entity.MutiLang;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class MutiLangServiceImpl  implements MutiLangService {
    @Resource
    private SystemRepository systemService;
    @Autowired
    private MutiLangRepository mutiLangRepository;
    @Override
    public void delById(String id) {
        Object mutiLang = systemService.findEntity(MutiLang.class, id);
        String message = MutiLangUtils.paramDelSuccess("common.language");
        mutiLangRepository.delete(mutiLang);
        mutiLangRepository.initAllMutiLang();
        systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
    }

    @Override
    public void save(MutiLang mutiLang) {
        String message;
        if (StringUtils.isNotEmpty(mutiLang.getId())) {
            message = MutiLangUtils.paramUpdSuccess("common.language");
            MutiLang t = mutiLangRepository.find(MutiLang.class, mutiLang.getId());
            BeanPropertyUtils.copyBeanNotNull2Bean(mutiLang, t);
            mutiLangRepository.saveOrUpdate(t);
            mutiLangRepository.initAllMutiLang();
            systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
        } else {
            message=MutiLangUtils.existLangContext(mutiLang.getLangContext())?mutiLangRepository.getLang("common.langcontext.exist"):"";
            if(StringUtils.isEmpty(message))
            {
                mutiLangRepository.save(mutiLang);
                message = MutiLangUtils.paramAddSuccess("common.language");
                systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
            }
        }
    }
}
