package com.abocode.jfaster.web.system.web;

import org.apache.log4j.Logger;
import com.abocode.jfaster.platform.view.interactions.easyui.Autocomplete;
import com.abocode.jfaster.core.common.util.StringUtils;
import com.abocode.jfaster.web.system.domain.repository.SystemService;
import com.abocode.jfaster.core.common.util.SystemJsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * 通用业务处理
 * 
 * @author 张代浩
 * 
 */
@Scope("prototype")
@Controller
@RequestMapping("/commonController")
public class CommonController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(CommonController.class);
	private SystemService systemService;
	@Autowired
	public void setSystemService(SystemService systemService) {
		this.systemService = systemService;
	}

	/**
	 * 自动完成请求返回数据
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "getAutoList")
	public void getAutoList(HttpServletRequest request, HttpServletResponse response, Autocomplete autocomplete) {
		String trem = StringUtils.getEncodePra(request.getParameter("trem"));// 重新解析参数
		autocomplete.setTrem(trem);
		List autoList = systemService.findAutoList(autocomplete);
		String labelFields = autocomplete.getLabelField();
		String[] fieldArr = labelFields.split(",");
		String valueField = autocomplete.getValueField();
		String[] allFieldArr = null;
		if (!StringUtils.isEmpty(valueField)) {
			allFieldArr = new String[fieldArr.length+1];
			for (int i=0; i<fieldArr.length; i++) {
				allFieldArr[i] = fieldArr[i];
			}
			allFieldArr[fieldArr.length] = valueField;
		}

		try {
			/*String str = TagUtil.getAutoList(autocomplete, autoList);
			str = "(" + str + ")";*/
			response.setContentType("application/json;charset=UTF-8");
			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);
			response.getWriter().write(SystemJsonUtils.listToJson(allFieldArr, allFieldArr.length, autoList));
			response.getWriter().flush();
			response.getWriter().close();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * 通用列表页面跳转
	 */
	@Deprecated
	@RequestMapping(params = "listTurn")
	public ModelAndView listTurn(HttpServletRequest request) {
		String turn = request.getParameter("turn");// 跳转的目标页面
		return new ModelAndView(turn);
	}

	/***
	 * 导入文件
	 * @return
	 */
	@Deprecated
	@RequestMapping(params = "importdata")
	public ModelAndView importdata() {
		return new ModelAndView("system/upload");
	}
}
