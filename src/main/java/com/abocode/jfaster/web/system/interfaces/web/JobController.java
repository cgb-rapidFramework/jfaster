/*
package com.abocode.jfaster.web.system.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.abocode.jfaster.core.common.model.json.AjaxJson;
import com.abocode.jfaster.core.common.model.json.DataGrid;
import com.abocode.jfaster.core.common.util.BeanPropertyUtils;
import com.abocode.jfaster.core.common.constants.Globals;
import com.abocode.jfaster.web.common.hqlsearch.HqlGenerateUtil;
import com.abocode.jfaster.web.system.domain.repository.JobService;
import com.abocode.jfaster.web.system.domain.repository.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.abocode.jfaster.core.persistence.hibernate.qbc.CriteriaQuery;
import com.abocode.jfaster.core.common.util.StringUtils;
import com.abocode.jfaster.platform.view.widgets.easyui.TagUtil;


import com.abocode.jfaster.web.system.domain.entity.Job;

*/
/**
 * @Title: Controller
 * @Description: job
 * @author zhangdaihao
 * @date 2016-04-29 22:58:41
 * @version V1.0   
 *
 *//*

@Scope("prototype")
@Controller
@RequestMapping("/jobController")
public class JobController extends BaseController {
	@Autowired
	private SystemService systemService;
	private String message;
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	*/
/**
	 * job列表 页面跳转
	 * 
	 * @return
	 *//*

	@RequestMapping(params = "job")
	public ModelAndView job(HttpServletRequest request) {
		return new ModelAndView("system/job/jobList");
	}

	*/
/**
	 * easyui AJAX请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param job
	 *//*


	@RequestMapping(params = "datagrid")
	public void datagrid(Job job, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(Job.class, dataGrid);
		//查询条件组装器
		HqlGenerateUtil.installHql(cq, job, request.getParameterMap());
		this.jobService.findDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	*/
/**
	 * 删除job
	 * 
	 * @return
	 *//*

	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(Job job, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			job = systemService.findEntity(Job.class, job.getId());
			message = "job废止成功";
			jobService.deleteJob(job.getId());
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			j.setMsg(message);
		} catch (Exception e) {
			j.setMsg("job废止失败");
		}
		return j;
	}

	*/
/**
	 * 暂停job
	 *
	 * @return
	 *//*

	@RequestMapping(params = "pause")
	@ResponseBody
	public AjaxJson pause(Job job, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			job = systemService.findEntity(Job.class, job.getId());
			message = "job暂停成功";
			jobService.pauseJob(job.getId());
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);

			j.setMsg(message);
		} catch (Exception e) {
			j.setMsg("job暂停失败");
		}
		return j;
	}

	*/
/**
	 * 恢复job
	 *
	 * @return
	 *//*

	@RequestMapping(params = "resume")
	@ResponseBody
	public AjaxJson resume(Job job, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			job = systemService.findEntity(Job.class, job.getId());
			message = "job恢复成功";
			jobService.resumeJob(job.getId());
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);

			j.setMsg(message);
		} catch (Exception e) {
			j.setMsg("job恢复失败");
		}
		return j;
	}

	*/
/**
	 * 立即运行一次job
	 *
	 * @return
	 *//*

	@RequestMapping(params = "runOnce")
	@ResponseBody
	public AjaxJson runOnce(Job job, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			job = systemService.findEntity(Job.class, job.getId());
			message = "job立即运行一次成功";
			jobService.runOnceJob(job.getId());
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);

			j.setMsg(message);
		} catch (Exception e) {
			j.setMsg("job立即运行一次失败");
		}
		return j;
	}

	*/
/**
	 * 添加job
	 * 
	 * @param job
	 * @return
	 *//*

	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(Job job, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			if (StringUtils.isNotEmpty(job.getId())) {
				message = "job更新成功";
				Job t = jobService.find(Job.class, job.getId());
				try {
					BeanPropertyUtils.copyBeanNotNull2Bean(job, t);
					jobService.updateJob(t);
					systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
				} catch (Exception e) {
					message = "job更新失败";
				}
			} else {
				message = "job添加成功";
				jobService.addJob(job);
				systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
			}
			j.setMsg(message);
		} catch (Exception e) {
			j.setMsg("job添加失败");
		}
		return j;
	}

	*/
/**
	 * job列表页面跳转
	 * 
	 * @return
	 *//*

	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(Job job, HttpServletRequest req) {
		if (StringUtils.isNotEmpty(job.getId())) {
			job = jobService.findEntity(Job.class, job.getId());
			req.setAttribute("jobView", job);
		}
		return new ModelAndView("system/job/job");
	}
}
*/
