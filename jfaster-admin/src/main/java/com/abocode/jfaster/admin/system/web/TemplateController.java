package com.abocode.jfaster.admin.system.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.abocode.jfaster.admin.system.service.TemplateService;
import com.abocode.jfaster.api.core.AvailableEnum;
import com.abocode.jfaster.core.common.model.json.AjaxJson;
import com.abocode.jfaster.core.common.model.json.AjaxJsonBuilder;
import com.abocode.jfaster.core.common.model.json.DataGrid;
import com.abocode.jfaster.core.common.util.BeanPropertyUtils;
import com.abocode.jfaster.core.common.constants.Globals;
import com.abocode.jfaster.core.web.hqlsearch.HqlGenerateUtil;
import com.abocode.jfaster.system.entity.Template;
import com.abocode.jfaster.admin.system.repository.SystemRepository;
import com.abocode.jfaster.admin.system.repository.TemplateRepository;
import com.abocode.jfaster.core.common.util.LogUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


import com.abocode.jfaster.core.persistence.hibernate.qbc.CriteriaQuery;
import com.abocode.jfaster.core.platform.view.widgets.easyui.TagUtil;

/**
 * @Title: Controller
 * @Description: 模版管理
 */
@Scope("prototype")
@Controller
@RequestMapping("/templateController")
public class TemplateController {
    @Autowired
    private TemplateRepository templateRepository;
    @Autowired
    private TemplateService templateService;

    /**
     * 模版管理列表 页面跳转
     * @return
     */
    @RequestMapping(params = "template")
    public ModelAndView template(HttpServletRequest request) {
        return new ModelAndView("system/template/templateList");
    }

    /**
     * easyui AJAX请求数据
     *
     * @param request
     * @param response
     * @param dataGrid
     * @param template
     */

    @RequestMapping(params = "datagrid")
    public void datagrid(Template template, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
        CriteriaQuery cq = new CriteriaQuery(Template.class, dataGrid);
        //查询条件组装器
        HqlGenerateUtil.installHql(cq, template, request.getParameterMap());
        this.templateRepository.findDataGridReturn(cq, true);
        TagUtil.datagrid(response, dataGrid);
    }

    /**
     * 删除模版管理
     *
     * @return
     */
    @RequestMapping(params = "del")
    @ResponseBody
    public AjaxJson del(Template template) {
        templateService.delById(template.getId());
        return AjaxJsonBuilder.success();
    }


    /**
     * 添加模版管理
     *
     * @param template
     * @return
     */
    @RequestMapping(params = "save")
    @ResponseBody
    public AjaxJson save(Template template) {
        templateService.save(template);
        return AjaxJsonBuilder.success();
    }

    /**
     * 模版管理列表页面跳转
     *
     * @return
     */
    @RequestMapping(params = "addorupdate")
    public ModelAndView addorupdate(Template template, HttpServletRequest req) {
        if (!StringUtils.isEmpty(template.getId())) {
            template = templateRepository.findEntity(Template.class, template.getId());
            req.setAttribute("templateView", template);
        }
        return new ModelAndView("system/template/template");
    }


    /**
     * 模版管理列表页面跳转
     * @return
     */
    @RequestMapping(params = "setting")
    @ResponseBody
    public AjaxJson setting(Template template, HttpServletRequest request) {
        Assert.isTrue(!StringUtils.isEmpty(template.getId()), "未找到该模版");
        templateRepository.setDefault(template.getId());
        request.setAttribute("templatePage", template);
        return AjaxJsonBuilder.success();
    }
}
