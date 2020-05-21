package com.abocode.jfaster.admin.system.repository.persistence.hibernate;

import com.abocode.jfaster.admin.system.repository.MutiLangRepository;
import com.abocode.jfaster.core.repository.persistence.hibernate.CommonRepositoryImpl;
import com.abocode.jfaster.core.platform.MutilangContainer;
import com.abocode.jfaster.core.common.util.BrowserUtils;
import com.abocode.jfaster.system.entity.MutiLang;
import com.abocode.jfaster.core.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
@Service
@Transactional
public class MutiLangRepositoryImpl extends CommonRepositoryImpl implements MutiLangRepository {
    /**
     * 初始化语言信息，TOMCAT启动时直接加入到内存中
     **/
    public void initAllMutiLang() {
        List<MutiLang> mutiLang = this.commonDao.findAll(MutiLang.class);
        for (MutiLang mutiLangEntity : mutiLang) {
            MutilangContainer.KeyIsLangkeyValueIsLangcodeForLangMap.put(mutiLangEntity.getLangKey(), mutiLangEntity.getLangCode());
            MutilangContainer.mutiLangMap.put(mutiLangEntity.getLangKey() + "_" + mutiLangEntity.getLangCode(), mutiLangEntity.getLangContext());
//			MutiLangEntity.mutiLangMap.put(mutiLangEntity.getLangKey() + "_" + mutiLangEntity.getLangCode(), mutiLangEntity.getLangContext());
        }
    }

    /**
     * 取 o_muti_lang.lang_key 的值返回当前语言的值
     **/
    public String getLang(String langKey) {
        String language = BrowserUtils.getBrowserLanguage();
        String langContext = MutilangContainer.mutiLangMap.get(langKey + "_" + language);
        if (StringUtils.isEmpty(langContext)) {
            langContext = MutilangContainer.mutiLangMap.get("common.notfind.langkey" + "_" + language);
            if ("null".equals(langContext) ||"?".equals(langContext) || langContext == null || langKey.startsWith("?")) {
                langContext = "";
            }
            langContext = langContext + langKey;
        }
        return langContext;
    }

    public String getLang(String lanKey, String langArg) {
        String langContext;
        if (StringUtils.isEmpty(langArg)) {
            langContext = getLang(lanKey);
        } else {
            String[] argArray = langArg.split(",");
            langContext = getLang(lanKey);

            for (int i = 0; i < argArray.length; i++) {
                String langKeyArg = argArray[i].trim();
                String langKeyContext = getLang(langKeyArg);
                langContext = StringUtils.replace(langContext, "{" + i + "}", langKeyContext);
            }
        }
        return langContext;
    }

    /**
     * 刷新多语言cach
     **/
    public void refleshMutiLangCach() {
        MutilangContainer.mutiLangMap.clear();
        initAllMutiLang();
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
        List<MutiLang> langKeyList = this.commonDao.findAllByProperty(MutiLang.class, "langKey", lang_key);
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
        List<MutiLang> langContextList = this.commonDao.findAllByProperty(MutiLang.class, "langContext", lang_context);
        if (!langContextList.isEmpty()) {
            return true;
        }

        return false;
    }

}