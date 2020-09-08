package com.abocode.jfaster.admin.system.web;

import com.abocode.jfaster.admin.system.repository.LanguageRepository;
import com.abocode.jfaster.admin.system.service.LanguageService;
import com.abocode.jfaster.core.common.model.json.AjaxJson;
import com.abocode.jfaster.core.common.model.json.AjaxJsonBuilder;
import com.abocode.jfaster.core.common.util.StrUtils;
import com.abocode.jfaster.core.persistence.hibernate.qbc.CriteriaQuery;
import com.abocode.jfaster.core.repository.DataGridData;
import com.abocode.jfaster.core.repository.DataGridParam;
import com.abocode.jfaster.system.entity.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @Title: Controller
 * @Description: 多语言
 */
@Scope("prototype")
@Controller
@RequestMapping("/languageController")
public class LanguageController {
    @Autowired
    private LanguageRepository languageRepository;
    @Autowired
    private LanguageService languageService;

    /**
     * 多语言列表 页面跳转
     *
     * @return
     */
    @RequestMapping(params = "language")
    public ModelAndView language() {
        return new ModelAndView("system/language/languageList");
    }

    /**
     * easyui AJAX请求数据
     *  @param request
     * @param dataGridParam
     * @return
     */

    @RequestMapping(params = "findDataGridData")
    @ResponseBody
    public DataGridData findDataGridData(Language language, HttpServletRequest request, DataGridParam dataGridParam) {
        CriteriaQuery cq = new CriteriaQuery(Language.class);
        if (StrUtils.isNotEmpty(language.getLangKey())) {
            cq.like("langKey", "%" + language.getLangKey() + "%");
            language.setLangKey("");
        }
        if (StrUtils.isNotEmpty(language.getLangContext())) {
            cq.like("langContext", "%" + language.getLangContext() + "%");
            language.setLangContext("");
        }
        // 查询条件组装器
        cq.buildParameters(language, request.getParameterMap(),dataGridParam);
       return  this.languageRepository.findDataGridData(cq);
    }

    /**
     * 删除多语言
     *
     * @return
     */
    @RequestMapping(params = "del")
    @ResponseBody
    public AjaxJson del(Language language) {
        languageService.delById(language.getId());
        return AjaxJsonBuilder.success();
    }

    /**
     * 添加多语言
     *
     * @param language
     * @return
     */
    @RequestMapping(params = "save")
    @ResponseBody
    public AjaxJson save(Language language) {
        languageService.save(language);
        return AjaxJsonBuilder.success();
    }

    /**
     * 多语言列表页面跳转
     *
     * @return
     */
    @RequestMapping(params = "detail")
    public ModelAndView detail(Language language,
                                    HttpServletRequest request) {
        if (StrUtils.isNotEmpty(language.getId())) {
            language = languageRepository.findEntity(Language.class, language.getId());
            request.setAttribute("mutiLangView", language);
        }
        return new ModelAndView("system/language/language");
    }


    /**
     * 刷新前端缓存
     */
    @RequestMapping(params = "refreshCach")
    @ResponseBody
    public AjaxJson refreshCach() {
        languageRepository.refreshLanguageCache();
        String message = languageRepository.getLang("common.refresh.success");
        return AjaxJsonBuilder.success(message);
    }
}
