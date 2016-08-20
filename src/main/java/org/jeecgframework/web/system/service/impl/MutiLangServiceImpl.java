package org.jeecgframework.web.system.service.impl;

import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.platform.container.MutilangContainer;
import org.jeecgframework.platform.util.BrowserUtils;
import org.jeecgframework.web.system.entity.base.MutiLangEntity;
import org.jeecgframework.web.system.service.MutiLangService;
import org.jeecgframework.web.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service("mutiLangService")
@Transactional
public class MutiLangServiceImpl extends CommonServiceImpl implements MutiLangService {

    @Autowired
    private HttpServletRequest request;

    /**
     * 初始化语言信息，TOMCAT启动时直接加入到内存中
     **/
    public void initAllMutiLang() {
        List<MutiLangEntity> mutiLang = this.commonDao.findAll(MutiLangEntity.class);

        for (MutiLangEntity mutiLangEntity : mutiLang) {
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
        List<MutiLangEntity> langKeyList = this.commonDao.findAllByProperty(MutiLangEntity.class, "langKey", lang_key);
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
        List<MutiLangEntity> langContextList = this.commonDao.findAllByProperty(MutiLangEntity.class, "langContext", lang_context);
        if (!langContextList.isEmpty()) {
            return true;
        }

        return false;
    }

}