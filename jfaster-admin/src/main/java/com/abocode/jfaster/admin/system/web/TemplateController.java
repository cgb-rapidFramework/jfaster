package com.abocode.jfaster.admin.system.web;

import javax.servlet.http.HttpServletRequest;

import com.abocode.jfaster.admin.system.service.TemplateService;
import com.abocode.jfaster.api.system.TemplateDto;
import com.abocode.jfaster.api.system.TemplateQuery;
import com.abocode.jfaster.core.common.model.json.AjaxJson;
import com.abocode.jfaster.core.common.model.json.AjaxJsonBuilder;
import com.abocode.jfaster.core.repository.DataGridParam;
import com.abocode.jfaster.core.repository.DataGridData;
import com.abocode.jfaster.system.entity.Template;
import com.abocode.jfaster.admin.system.repository.TemplateRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


import com.abocode.jfaster.core.persistence.hibernate.qbc.CriteriaQuery;

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
    public ModelAndView template() {
        return new ModelAndView("system/template/templateList");
    }

    /**
     * easyui AJAX请求数据
     *
     * @param request
     * @param dataGridParam
     * @param templateQuery
     */

    @RequestMapping(params = "findDataGridData")
    @ResponseBody
    public DataGridData findDataGridData(TemplateQuery templateQuery, HttpServletRequest request, DataGridParam dataGridParam) {
        CriteriaQuery cq = new CriteriaQuery(Template.class).buildParameters(templateQuery, request.getParameterMap(), dataGridParam);
        return  this.templateRepository.findDataGridData(cq);
    }


    /**
     * 添加模版管理
     * @param templateDto
     * @return
     */
    @RequestMapping(params = "save")
    @ResponseBody
    public AjaxJson save(TemplateDto templateDto) {
        Template template=new Template();
        BeanUtils.copyProperties(templateDto,template);
        templateService.save(template);
        return AjaxJsonBuilder.success();
    }

    /**
     * 模版管理列表页面跳转
     *
     * @return
     */
    @RequestMapping(params = "detail")
    public ModelAndView detail(@RequestParam String id,HttpServletRequest request) {
        if (!StringUtils.isEmpty(id)) {
            Template template = templateRepository.findEntity(Template.class, id);
            request.setAttribute("templateView", template);
        }
        return new ModelAndView("system/template/template");
    }


    /**
     * 模版管理列表页面跳转
     * @return
     */
    @RequestMapping(params = "setting")
    @ResponseBody
    public AjaxJson setting(@RequestParam String id) {
        templateRepository.setDefault(id);
        return AjaxJsonBuilder.success();
    }
}
