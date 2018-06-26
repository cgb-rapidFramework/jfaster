package com.abocode.jfaster.web.system.interfaces.web;

import com.abocode.jfaster.core.common.model.json.DataGrid;
import com.abocode.jfaster.core.persistence.hibernate.qbc.CriteriaQuery;
import com.abocode.jfaster.core.common.model.json.AjaxJson;
import com.abocode.jfaster.core.platform.view.widgets.easyui.TagUtil;
import com.abocode.jfaster.core.common.constants.Globals;
import com.abocode.jfaster.web.system.domain.entity.Config;
import com.abocode.jfaster.web.system.domain.repository.SystemService;
import com.abocode.jfaster.core.common.util.SessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 配置信息处理类 *
 * @author franky
 * 
 */
@Deprecated
@Controller
@RequestMapping("/configController")
public class ConfigController extends BaseController {
	private SystemService systemService;
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Autowired
	public void setSystemService(SystemService systemService) {
		this.systemService = systemService;
	}

	/**
	 * 配置列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "config")
	public ModelAndView config() {
		return new ModelAndView("system/config/configList");
	}

	/**
	 * easyuiAjax表单请求
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "datagrid")
	public void datagrid(HttpServletRequest request,
			HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(Config.class, dataGrid);
		this.systemService.findDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除配置信息
	 * 
	 * @param config
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(Config config, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		config = systemService.findEntity(Config.class, config.getId());
		message = "配置信息: " + config.getName() + "被删除 成功";
		systemService.delete(config);
		systemService.addLog(message, Globals.Log_Type_DEL,
				Globals.Log_Leavel_INFO);
		
		return j;
	}

	/**
	 * 添加和更新配置信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(Config tsConfig, HttpServletRequest request) {
		if (StringUtils.isEmpty(tsConfig.getId())) {
			Config tsConfig2=systemService.findUniqueByProperty(Config.class, "code", tsConfig.getCode());
			if(tsConfig2!=null){
				message = "编码为: " + tsConfig.getCode() + "的配置信息已存在";
			}else{
				tsConfig.setTSUser(SessionUtils.getCurrentUser());
				systemService.save(tsConfig);
				message = "配置信息: " + tsConfig.getName() + "被添加成功";
				systemService.addLog(message, Globals.Log_Type_INSERT,
						Globals.Log_Leavel_INFO);
			}
			
		}else{
			message = "配置信息: " + tsConfig.getName() + "被修改成功";
			systemService.update(tsConfig);
			systemService.addLog(message, Globals.Log_Type_INSERT,
					Globals.Log_Leavel_INFO);
		}
		AjaxJson j = new AjaxJson();
		j.setMsg(message);
		
		return j;
	}

	/**
	 * 添加和更新配置信息页面
	 * 
	 * @param config
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(Config config, HttpServletRequest req) {
		if (!StringUtils.isEmpty(config.getId())) {
			config = systemService.findEntity(Config.class,
					config.getId());
			req.setAttribute("configView", config);
		}
		return new ModelAndView("system/config/config");
	}

}
