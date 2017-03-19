package com.abocode.jfaster.web.system.controller;

import com.abocode.jfaster.core.common.model.json.AjaxJson;
import com.abocode.jfaster.core.common.model.json.DataGrid;
import com.abocode.jfaster.core.util.BeanPropertyUtils;
import com.abocode.jfaster.platform.constant.Globals;
import com.abocode.jfaster.platform.util.MutiLangUtils;
import com.abocode.jfaster.web.common.hqlsearch.HqlGenerateUtil;
import com.abocode.jfaster.web.system.service.DynamicDataSourceService;
import com.abocode.jfaster.web.system.service.SystemService;
import org.apache.log4j.Logger;
import com.abocode.jfaster.core.common.hibernate.qbc.CriteriaQuery;
import com.abocode.jfaster.platform.common.tag.easyui.TagUtil;
import com.abocode.jfaster.web.utils.StringUtils;
import com.abocode.jfaster.web.system.entity.DynamicDataSourceEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**   
 * @Title: Controller
 * @Description: 数据源配置
 * @author zhangdaihao
 * @date 2014-09-05 13:22:10
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/dynamicDataSourceController")
public class DynamicDataSourceController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(DynamicDataSourceController.class);

	@Autowired
	private DynamicDataSourceService dynamicDataSourceService;
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
	 * 数据源配置列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "dbSource")
	public ModelAndView dbSource(HttpServletRequest request) {
		return new ModelAndView("system/dbsource/dbSourceList");
	}

	/**
	 * easyui AJAX请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 */

	@RequestMapping(params = "datagrid")
	public void datagrid(DynamicDataSourceEntity dbSource,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(DynamicDataSourceEntity.class, dataGrid);
		//查询条件组装器
		HqlGenerateUtil.installHql(cq, dbSource, request.getParameterMap());
		this.dynamicDataSourceService.findDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
		
	}

	/**
	 * 删除数据源配置
	 * 
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(DynamicDataSourceEntity dbSource, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		dbSource = systemService.findEntity(DynamicDataSourceEntity.class, dbSource.getId());
		
		message = MutiLangUtils.paramDelSuccess("common.datasource.manage");
		
		dynamicDataSourceService.delete(dbSource);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加数据源配置
	 * 
	 * @param dbSource
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(DynamicDataSourceEntity dbSource, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		if (!StringUtils.isEmpty(dbSource.getId())) {
			message = MutiLangUtils.paramUpdSuccess("common.datasource.manage");
			DynamicDataSourceEntity t = dynamicDataSourceService.find(DynamicDataSourceEntity.class, dbSource.getId());
			try {
				BeanPropertyUtils.copyBeanNotNull2Bean(dbSource, t);
				dynamicDataSourceService.saveOrUpdate(t);
				dynamicDataSourceService.refleshCache();
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
				message = MutiLangUtils.paramUpdFail("common.datasource.manage");
			}
		} else {
			message = MutiLangUtils.paramAddSuccess("common.datasource.manage");
			
			dynamicDataSourceService.save(dbSource);
			dynamicDataSourceService.refleshCache();
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 数据源配置列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(DynamicDataSourceEntity dbSource, HttpServletRequest req) {
		if (!StringUtils.isEmpty(dbSource.getId())) {
			dbSource = dynamicDataSourceService.findEntity(DynamicDataSourceEntity.class, dbSource.getId());
			req.setAttribute("dbSourcePage", dbSource);
		}
		return new ModelAndView("system/dbsource/dbSource");
	}
}
