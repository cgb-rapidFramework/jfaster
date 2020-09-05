package com.abocode.jfaster.admin.system.web;

import com.abocode.jfaster.api.system.ConfigDto;
import com.abocode.jfaster.core.repository.DataGridData;
import com.abocode.jfaster.core.repository.DataGridParam;
import com.abocode.jfaster.core.persistence.hibernate.qbc.CriteriaQuery;
import com.abocode.jfaster.core.common.model.json.AjaxJson;
import com.abocode.jfaster.core.common.constants.Globals;
import com.abocode.jfaster.system.entity.Config;
import com.abocode.jfaster.admin.system.repository.SystemRepository;
import com.abocode.jfaster.core.web.utils.SessionUtils;
import org.springframework.beans.BeanUtils;
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
 */
@Controller
@RequestMapping("/configController")
public class ConfigController{
	private static final String NAME = "配置信息: ";
	private SystemRepository systemService;
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Autowired
	public void setSystemService(SystemRepository systemService) {
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
	 * @param response
	 * @param dataGridParam
	 * @return
	 */
	@RequestMapping(params = "findDataGridData")
	@ResponseBody
	public DataGridData findDataGridData(HttpServletResponse response, DataGridParam dataGridParam) {
		CriteriaQuery cq = new CriteriaQuery(Config.class).buildDataGrid(dataGridParam);
		return this.systemService.findDataGridData(cq);
	}

	/**
	 * 删除配置信息
	 * 
	 * @param config
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(ConfigDto config) {
		AjaxJson j = new AjaxJson();
		Config entity = systemService.findEntity(Config.class, config.getId());
		message = NAME + entity.getName() + "被删除 成功";
		systemService.delete(config);
		systemService.addLog(message, Globals.Log_Type_DEL,
				Globals.Log_Leavel_INFO);
		return j;
	}

	/**
	 * 添加和更新配置信息
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(ConfigDto configDto) {
		if (StringUtils.isEmpty(configDto.getId())) {
			Config tsConfig=systemService.findUniqueByProperty(Config.class, "code", configDto.getCode());
			if(tsConfig!=null){
				message = "编码为: " + tsConfig.getCode() + "的配置信息已存在";
			}else{
				tsConfig=systemService.findEntity(Config.class,configDto.getId());
				tsConfig.setUser(SessionUtils.getCurrentUser());
				systemService.save(tsConfig);
				message = NAME + tsConfig.getName() + "被添加成功";
				systemService.addLog(message, Globals.Log_Type_INSERT,
						Globals.Log_Leavel_INFO);
			}
			
		}else{
			Config tsConfig=systemService.findEntity(Config.class,configDto.getId());
			message = NAME + tsConfig.getName() + "被修改成功";
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
	 * @param configDto
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "detail")
	public ModelAndView detail(ConfigDto configDto, HttpServletRequest request) {
		if (!StringUtils.isEmpty(configDto.getId())) {
			Config config = systemService.findEntity(Config.class,
					configDto.getId());
			BeanUtils.copyProperties(config,configDto);
			request.setAttribute("configView", configDto);
		}
		return new ModelAndView("system/config/config");
	}

}
