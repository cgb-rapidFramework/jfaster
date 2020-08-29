package com.abocode.jfaster.admin.system.service;

import com.abocode.jfaster.system.entity.Language;

public interface LanguageService {
    void delById(String id);

    void save(Language language);
}
