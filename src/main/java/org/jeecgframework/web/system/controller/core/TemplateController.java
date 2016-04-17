package org.jeecgframework.web.system.controller.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jodd.util.StringUtil;
import org.apache.log4j.Logger;
import org.jeecgframework.core.util.BeanPropertyUtils;
import org.jeecgframework.web.common.hqlsearch.HqlGenerateUtil;
import org.jeecgframework.web.system.constant.core.TemplateConstant;
import org.jeecgframework.web.system.controller.BaseController;
import org.jeecgframework.web.system.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.platform.common.tag.easyui.TagUtil;
import org.jeecgframework.platform.constant.Globals;

import org.jeecgframework.web.system.service.SystemService;


import org.jeecgframework.web.system.entity.core.TemplateEntity;

import java.util.List;

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
	public void datagrid(TemplateEntity template,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(TemplateEntity.class, dataGrid);
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
	public AjaxJson del(TemplateEntity template, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		template = systemService.findEntity(TemplateEntity.class, template.getId());
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
	public AjaxJson save(TemplateEntity template, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(template.getId())) {
			message = "模版管理更新成功";
			TemplateEntity t = templateService.find(TemplateEntity.class, template.getId());
			try {
				BeanPropertyUtils.copyBeanNotNull2Bean(template, t);
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
	public ModelAndView addorupdate(TemplateEntity template, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(template.getId())) {
			template = templateService.findEntity(TemplateEntity.class, template.getId());
			req.setAttribute("templatePage", template);
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
	public AjaxJson setting(TemplateEntity template, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(template.getId())) {
			message = "模版管理更新成功";
			templateService.setDefault(template.getId());
			request.setAttribute("templatePage", template);
		}
		j.setMsg(message);
		return j;

	}
}
