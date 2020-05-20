package com.abocode.jfaster.admin.system.web;

import com.abocode.jfaster.core.common.model.json.*;
import com.abocode.jfaster.admin.system.dto.bean.ClientBean;
import com.abocode.jfaster.core.platform.view.widgets.easyui.TagUtil;
import com.abocode.jfaster.core.web.manager.ClientManager;
import com.abocode.jfaster.core.web.manager.ClientSort;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 系统相关
 */
@Scope("prototype")
@Controller
@RequestMapping("/systemController")
public class SystemController{
	@RequestMapping(params = "druid")
	public ModelAndView druid() {
		return new ModelAndView(new RedirectView("druid/index.html"));
	}
	/**
	 * 在线用户列表
	 * @param request
	 * @param response
	 * @param dataGrid
	 */

	@RequestMapping(params = "datagridOnline")
	public void datagridOnline(ClientBean tSOnline, HttpServletRequest request,
							   HttpServletResponse response, DataGrid dataGrid) {
		List<ClientBean> onlines = new ArrayList<ClientBean>();
		onlines.addAll(ClientManager.getInstance().getAllClient());
		dataGrid.setTotal(onlines.size());
		dataGrid.setResults(getClinetList(onlines,dataGrid));
		TagUtil.datagrid(response, dataGrid);
	}
	/**
	 * 获取当前页面的用户列表
	 * @param onlines
	 * @param dataGrid
	 * @return
	 */
	private List<ClientBean> getClinetList(List<ClientBean> onlines, DataGrid dataGrid) {
		Collections.sort(onlines, new ClientSort());
		List<ClientBean> result = new ArrayList<ClientBean>();
		for(int i = (dataGrid.getPage()-1)*dataGrid.getRows();
				i<onlines.size()&&i<dataGrid.getPage()*dataGrid.getRows();i++){
			result.add(onlines.get(i));
		}
		return result;
	}


}
