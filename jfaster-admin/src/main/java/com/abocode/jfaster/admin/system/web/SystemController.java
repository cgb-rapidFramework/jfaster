package com.abocode.jfaster.admin.system.web;
import com.abocode.jfaster.admin.system.repository.SystemRepository;
import com.abocode.jfaster.core.common.util.StrUtils;
import com.abocode.jfaster.core.common.util.SystemJsonUtils;
import com.abocode.jfaster.core.platform.view.interactions.easyui.Autocomplete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 系统相关
 */
@Scope("prototype")
@Controller
@RequestMapping("/systemController")
public class SystemController{
	@Autowired
	private SystemRepository systemService;
	/**
	 * 自动完成请求返回数据
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "getAutoList")
	public void getAutoList(HttpServletRequest request, HttpServletResponse response, Autocomplete autocomplete) {
		String trem = StrUtils.getEncodePra(request.getParameter("trem"));// 重新解析参数
		autocomplete.setTrem(trem);
		List autoList = systemService.findAutoList(autocomplete);
		String labelFields = autocomplete.getLabelField();
		String[] fieldArr = labelFields.split(",");
		String valueField = autocomplete.getValueField();
		if (!StrUtils.isEmpty(valueField)) {
			String[] allFieldArr = new String[fieldArr.length + 1];
			for (int i = 0; i < fieldArr.length; i++) {
				allFieldArr[i] = fieldArr[i];
			}
			allFieldArr[fieldArr.length] = valueField;
			try {
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
	}
}
