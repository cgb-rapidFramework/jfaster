package com.abocode.jfaster.web.system.web;

import com.abocode.jfaster.core.common.util.LogUtils;
import com.abocode.jfaster.core.persistence.hibernate.qbc.CriteriaQuery;
import com.abocode.jfaster.core.common.model.json.AjaxJson;
import com.abocode.jfaster.core.common.model.json.DataGrid;
import com.abocode.jfaster.core.common.util.BeanPropertyUtils;
import com.abocode.jfaster.platform.view.widgets.easyui.TagUtil;
import com.abocode.jfaster.core.common.constants.Globals;
import com.abocode.jfaster.core.common.util.MutiLangUtils;
import com.abocode.jfaster.web.common.hqlsearch.HqlGenerateUtil;
import com.abocode.jfaster.web.system.domain.entity.MutiLang;
import com.abocode.jfaster.web.system.domain.repository.MutiLangService;
import com.abocode.jfaster.web.system.domain.repository.SystemService;
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
 * @author Rocky
 * @date 2014-06-28 00:09:31
 * @version V1.0
 * 
 */
@Scope("prototype")
@Controller
@RequestMapping("/mutiLangController")
public class MutiLangController extends BaseController {
	@Autowired
	private MutiLangService mutiLangService;
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
	 * 多语言列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "mutiLang")
	public ModelAndView mutiLang(HttpServletRequest request) {
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
		if(StringUtils.isNotEmpty(mutiLang.getLangKey())){
			cq.like("langKey", "%" +mutiLang.getLangKey() + "%");
			mutiLang.setLangKey("");
		}

		if(StringUtils.isNotEmpty(mutiLang.getLangContext())){
			cq.like("langContext", "%" +mutiLang.getLangContext() + "%");
			mutiLang.setLangContext("");
		}
		// 查询条件组装器
		HqlGenerateUtil.installHql(cq, mutiLang, request.getParameterMap());
		this.mutiLangService.findDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除多语言
	 * 
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(MutiLang mutiLang, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		mutiLang = systemService.findEntity(MutiLang.class, mutiLang.getId());
		message = MutiLangUtils.paramDelSuccess("common.language");
		mutiLangService.delete(mutiLang);
		mutiLangService.initAllMutiLang();
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		j.setMsg(message);
		return j;
	}

	/**
	 * 添加多语言
	 * 
	 * @param mutiLang
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(MutiLang mutiLang, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		if (StringUtils.isNotEmpty(mutiLang.getId())) {
			message = MutiLangUtils.paramUpdSuccess("common.language");
			MutiLang t = mutiLangService.find(MutiLang.class, mutiLang.getId());
			try {
				BeanPropertyUtils.copyBeanNotNull2Bean(mutiLang, t);
				mutiLangService.saveOrUpdate(t);
				mutiLangService.initAllMutiLang();
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				LogUtils.error(e.getMessage());
				message = MutiLangUtils.paramUpdFail("common.language");
			}
		} else {
			if(MutiLangUtils.existLangContext( mutiLang.getLangContext()))
			{
				message = mutiLangService.getLang("common.langcontext.exist");
			}
			if(StringUtils.isEmpty(message))
			{
				mutiLangService.save(mutiLang);
				message = MutiLangUtils.paramAddSuccess("common.language");
				systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
			}
		}
		j.setMsg(message);
		return j;
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
			mutiLang = mutiLangService.findEntity(MutiLang.class, mutiLang.getId());
			req.setAttribute("mutiLangView", mutiLang);
		}
		return new ModelAndView("system/mutilang/mutiLang");
	}
	
	
	/**
	 * 刷新前端缓存
	 * @param request
	 */
	@RequestMapping(params = "refreshCach")
	@ResponseBody
	public AjaxJson refreshCach(HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			mutiLangService.refleshMutiLangCach();
			message = mutiLangService.getLang("common.refresh.success");
		} catch (Exception e) {
			message = mutiLangService.getLang("common.refresh.fail");
		}
		j.setMsg(message);
		return j;
	}
}
