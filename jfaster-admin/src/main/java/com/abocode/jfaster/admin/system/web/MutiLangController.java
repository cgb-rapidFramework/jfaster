package com.abocode.jfaster.admin.system.web;

import com.abocode.jfaster.admin.system.service.MutiLangService;
import com.abocode.jfaster.core.common.model.json.AjaxJsonBuilder;
import com.abocode.jfaster.core.common.util.LogUtils;
import com.abocode.jfaster.core.persistence.hibernate.qbc.CriteriaQuery;
import com.abocode.jfaster.core.common.model.json.AjaxJson;
import com.abocode.jfaster.core.common.model.json.DataGrid;
import com.abocode.jfaster.core.common.util.BeanPropertyUtils;
import com.abocode.jfaster.core.platform.view.widgets.easyui.TagUtil;
import com.abocode.jfaster.core.common.constants.Globals;
import com.abocode.jfaster.core.common.util.MutiLangUtils;
import com.abocode.jfaster.core.web.hqlsearch.HqlGenerateUtil;
import com.abocode.jfaster.system.entity.MutiLang;
import com.abocode.jfaster.admin.system.repository.MutiLangRepository;
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
    private MutiLangRepository mutiLangRepository;
    @Autowired
    private SystemRepository systemService;
    @Autowired
    private MutiLangService mutiLangService;

    /**
     * 多语言列表 页面跳转
     *
     * @return
     */
    @RequestMapping(params = "mutiLang")
    public ModelAndView mutiLang() {
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
    public void datagrid(MutiLang mutiLang, HttpServletRequest request,
                         HttpServletResponse response, DataGrid dataGrid) {
        CriteriaQuery cq = new CriteriaQuery(MutiLang.class, dataGrid);
        if (StringUtils.isNotEmpty(mutiLang.getLangKey())) {
            cq.like("langKey", "%" + mutiLang.getLangKey() + "%");
            mutiLang.setLangKey("");
        }

        if (StringUtils.isNotEmpty(mutiLang.getLangContext())) {
            cq.like("langContext", "%" + mutiLang.getLangContext() + "%");
            mutiLang.setLangContext("");
        }
        // 查询条件组装器
        HqlGenerateUtil.installHql(cq, mutiLang, request.getParameterMap());
        this.mutiLangRepository.findDataGridReturn(cq, true);
        TagUtil.datagrid(response, dataGrid);
    }

    /**
     * 删除多语言
     *
     * @return
     */
    @RequestMapping(params = "del")
    @ResponseBody
    public AjaxJson del(MutiLang mutiLang) {
        mutiLangService.delById(mutiLang.getId());
        return AjaxJsonBuilder.success();
    }

    /**
     * 添加多语言
     *
     * @param mutiLang
     * @return
     */
    @RequestMapping(params = "save")
    @ResponseBody
    public AjaxJson save(MutiLang mutiLang) {
        mutiLangService.save(mutiLang);
        return AjaxJsonBuilder.success();
    }

    /**
     * 多语言列表页面跳转
     *
     * @return
     */
    @RequestMapping(params = "addorupdate")
    public ModelAndView addorupdate(MutiLang mutiLang,
                                    HttpServletRequest req) {
        if (StringUtils.isNotEmpty(mutiLang.getId())) {
            mutiLang = mutiLangRepository.findEntity(MutiLang.class, mutiLang.getId());
            req.setAttribute("mutiLangView", mutiLang);
        }
        return new ModelAndView("system/mutilang/mutiLang");
    }


    /**
     * 刷新前端缓存
     */
    @RequestMapping(params = "refreshCach")
    @ResponseBody
    public AjaxJson refreshCach() {
        mutiLangRepository.refleshMutiLangCach();
        String message = mutiLangRepository.getLang("common.refresh.success");
        return AjaxJsonBuilder.success(message);
    }
}
