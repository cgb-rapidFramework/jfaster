package com.abocode.jfaster.core.common.container;

import com.abocode.jfaster.core.common.util.BrowserUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/***
 * 语言容器
 */
public class MutilangContainer {
    public static Map<String, String> mutiLangMap = new HashMap<String, String>();
    public static Map<String, String> KeyIsLangkeyValueIsLangcodeForLangMap = new HashMap<String, String>(); //FunctionName的keymap

    /**
     * 获取语言
     *
     * @param lanKey
     * @param langArg
     * @return
     */
    public static String getLang(String lanKey, String langArg) {
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
     * 获取语言
     *
     * @param langKey
     * @return
     */
    public static String getLang(String langKey) {
        String language = BrowserUtils.getBrowserLanguage();
        String langContext = MutilangContainer.mutiLangMap.get(langKey + "_" + language);
        if (StringUtils.isEmpty(langContext)) {
            langContext = MutilangContainer.mutiLangMap.get("common.notfind.langkey" + "_" + language);
            if ("null".equals(langContext) || langContext == null || langKey.startsWith("?")) {
                langContext = "";
            }
            langContext = langKey;
        }
        return langContext;
    }


    /***
     * 判断语言内容是否存在
     *
     * @param langContext
     * @return
     */
    public static boolean existLangContext(String langContext) {
        String map = KeyIsLangkeyValueIsLangcodeForLangMap.get(langContext);
        if (!StringUtils.isEmpty(map)) {
            return true;
        }
        return false;
    }


}
