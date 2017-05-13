package com.abocode.jfaster.web.system.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.abocode.jfaster.core.common.model.json.AjaxJson;
import com.abocode.jfaster.core.common.model.json.DataGrid;
import com.abocode.jfaster.core.util.BeanPropertyUtils;
import com.abocode.jfaster.platform.constant.Globals;
import com.abocode.jfaster.web.common.hqlsearch.HqlGenerateUtil;
import com.abocode.jfaster.web.system.constant.TemplateConstant;
import com.abocode.jfaster.web.system.entity.Template;
import com.abocode.jfaster.web.system.service.SystemService;
import com.abocode.jfaster.web.system.service.TemplateService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


import com.abocode.jfaster.core.common.hibernate.qbc.CriteriaQuery;
import com.abocode.jfaster.platform.common.tag.easyui.TagUtil;

/**
 * @Title: Controller
 * @Description: 模版管理
 * @author zhangdaihao
 * @date 2016-04-16 22:19:56
 * @version V1.0   
 *
 */
@Scope("prototype")
@Controller
@RequestMapping("/templateController")
public class TemplateController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(TemplateController.class);

	@Autowired
	private TemplateService templateService;
	@Autowired
	private SystemService systemService;
	private String message;
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}


	/**
	 * 模版管理列表 页面跳转
	 * 
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
		this.templateService.findDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除模版管理
	 * 
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(Template template, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		template = systemService.findEntity(Template.class, template.getId());
		message = "模版管理删除成功";
		templateService.delete(template);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加模版管理
	 * 
	 * @param template
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(Template template, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		if (!StringUtils.isEmpty(template.getId())) {
			message = "模版管理更新成功";
			Template t = templateService.find(Template.class, template.getId());
			try {
				BeanPropertyUtils.copyBeanNotNull2Bean(template, t);
				if(t.getStatus()==TemplateConstant.TEMPLATE_STATUS_IS_AVAILABLE){
					templateService.setDefault(template.getId());
				}
				templateService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
				message = "模版管理更新失败";
			}
		} else {
			message = "模版管理添加成功";
			template.setStatus(TemplateConstant.TEMPLATE_STATUS_IS_UNAVAILABLE);
			templateService.save(template);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}


		j.setMsg(message);
		return j;
	}

	/**
	 * 模版管理列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(Template template, HttpServletRequest req) {
		if (!StringUtils.isEmpty(template.getId())) {
			template = templateService.findEntity(Template.class, template.getId());
			req.setAttribute("templateView", template);
		}
		return new ModelAndView("system/template/template");
	}


	/**
	 * 模版管理列表页面跳转
	 *
	 * @return
	 */
	@RequestMapping(params = "setting")
	@ResponseBody
	public AjaxJson setting(Template template, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		if (!StringUtils.isEmpty(template.getId())) {
			message = "模版管理更新成功";
			templateService.setDefault(template.getId());
			request.setAttribute("templatePage", template);
		}
		j.setMsg(message);
		return j;

	}
}
