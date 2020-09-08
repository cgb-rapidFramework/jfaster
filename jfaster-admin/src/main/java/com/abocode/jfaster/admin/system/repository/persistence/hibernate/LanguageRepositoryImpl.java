package com.abocode.jfaster.admin.system.repository.persistence.hibernate;

import com.abocode.jfaster.admin.system.repository.LanguageRepository;
import com.abocode.jfaster.core.common.util.BrowserUtils;
import com.abocode.jfaster.core.common.util.StrUtils;
import com.abocode.jfaster.core.platform.LanguageContainer;
import com.abocode.jfaster.core.repository.persistence.hibernate.CommonRepositoryImpl;
import com.abocode.jfaster.system.entity.Language;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional
public class LanguageRepositoryImpl extends CommonRepositoryImpl implements LanguageRepository {
    /**
     * 初始化语言信息，TOMCAT启动时直接加入到内存中
     **/
    public void initLanguage() {
        List<Language> language = findAll(Language.class);
        for (Language languageEntity : language) {
            LanguageContainer.getLanguageKeyCodeMap().put(languageEntity.getLangKey(), languageEntity.getLangCode());
            LanguageContainer.getLanguageMap().put(languageEntity.getLangKey() + "_" + languageEntity.getLangCode(), languageEntity.getLangContext());
        }
    }

    /**
     * 取 o_muti_lang.lang_key 的值返回当前语言的值
     **/
    public String getLang(String langKey) {
        String language = BrowserUtils.getBrowserLanguage();
        String langContext = LanguageContainer.getLanguageMap().get(langKey + "_" + language);
        if (StrUtils.isEmpty(langContext)) {
            langContext = LanguageContainer.getLanguageMap().get("common.notfind.langkey" + "_" + language);
            if ("null".equals(langContext) ||"?".equals(langContext) || langContext == null || langKey.startsWith("?")) {
                langContext = "";
            }
            langContext = langContext + langKey;
        }
        return langContext;
    }

    public String getLang(String lanKey, String langArg) {
        String langContext;
        if (StrUtils.isEmpty(langArg)) {
            langContext = getLang(lanKey);
        } else {
            String[] argArray = langArg.split(",");
            langContext = getLang(lanKey);

            for (int i = 0; i < argArray.length; i++) {
                String langKeyArg = argArray[i].trim();
                String langKeyContext = getLang(langKeyArg);
                langContext = StrUtils.replaceAll(langContext, "{" + i + "}", langKeyContext);
            }
        }
        return langContext;
    }

    /**
     * 刷新多语言cach
     **/
    public void refreshLanguageCache() {
        LanguageContainer.getLanguageMap().clear();
        initLanguage();
    }


    /**
     * 启动执行 ---begin
     */

    /**
     * 检查国际化内容或lang_key是否已经存在
     *
     * @param lang_key
     * @return 如果存在则返回true，否则false
     */
    public boolean existLangKey(String lang_key) {
        List<Language> langKeyList = findAllByProperty(Language.class, "langKey", lang_key);
        if (!langKeyList.isEmpty()) {
            return true;
        }

        return false;
    }


    /**
     * 检查国际化内容或context是否已经存在
     *
     * @param lang_context
     * @return 如果存在则返回true，否则false
     */
    public boolean existLangContext(String lang_context) {
        List<Language> langContextList = findAllByProperty(Language.class, "langContext", lang_context);
        if (!langContextList.isEmpty()) {
            return true;
        }

        return false;
    }

}