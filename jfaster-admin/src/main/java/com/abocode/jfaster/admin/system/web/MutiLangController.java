package com.abocode.jfaster.admin.system.web;

import com.abocode.jfaster.admin.system.service.LanguageService;
import com.abocode.jfaster.core.common.model.json.AjaxJsonBuilder;
import com.abocode.jfaster.core.persistence.hibernate.qbc.CriteriaQuery;
import com.abocode.jfaster.core.common.model.json.AjaxJson;
import com.abocode.jfaster.core.common.model.json.DataGrid;
import com.abocode.jfaster.core.platform.view.widgets.easyui.TagUtil;
import com.abocode.jfaster.core.persistence.hibernate.hqlsearch.HqlGenerateUtil;
import com.abocode.jfaster.system.entity.Language;
import com.abocode.jfaster.admin.system.repository.LanguageRepository;
import com.abocode.jfaster.admin.system.repository.SystemRepository;
import com.abocode.jfaster.core.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Title: Controller
 * @Description: 多语言
 */
@Scope("prototype")
@Controller
@RequestMapping("/mutiLangController")
public class MutiLangController {
    @Autowired
    private LanguageRepository languageRepository;
    @Autowired
    private SystemRepository systemService;
    @Autowired
    private LanguageService languageService;

    /**
     * 多语言列表 页面跳转
     *
     * @return
     */
    @RequestMapping(params = "language")
    public ModelAndView language() {
        return new ModelAndView("system/mutilang/mutiLangList");
    }

    /**
     * easyui AJAX请求数据
     *
     * @param request
     * @param response
     * @param dataGrid
     */

    @RequestMapping(params = "datagrid")
    public void datagrid(Language language, HttpServletRequest request,
                         HttpServletResponse response, DataGrid dataGrid) {
        CriteriaQuery cq = new CriteriaQuery(Language.class, dataGrid);
        if (StringUtils.isNotEmpty(language.getLangKey())) {
            cq.like("langKey", "%" + language.getLangKey() + "%");
            language.setLangKey("");
        }

        if (StringUtils.isNotEmpty(language.getLangContext())) {
            cq.like("langContext", "%" + language.getLangContext() + "%");
            language.setLangContext("");
        }
        // 查询条件组装器
        HqlGenerateUtil.installHql(cq, language, request.getParameterMap());
        this.languageRepository.findDataGridReturn(cq, true);
        TagUtil.datagrid(response, dataGrid);
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
    @RequestMapping(params = "addorupdate")
    public ModelAndView addorupdate(Language language,
                                    HttpServletRequest req) {
        if (StringUtils.isNotEmpty(language.getId())) {
            language = languageRepository.findEntity(Language.class, language.getId());
            req.setAttribute("mutiLangView", language);
        }
        return new ModelAndView("system/mutilang/language");
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
