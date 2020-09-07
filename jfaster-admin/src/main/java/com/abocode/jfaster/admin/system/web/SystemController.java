package com.abocode.jfaster.admin.system.web;
import com.abocode.jfaster.core.repository.DataGridData;
import com.abocode.jfaster.core.repository.DataGridParam;
import com.abocode.jfaster.core.repository.TagUtil;
import com.abocode.jfaster.admin.system.service.manager.ClientBean;
import com.abocode.jfaster.admin.system.service.manager.ClientManager;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
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
	 * @param dataGrid
	 * @return
	 */

	@RequestMapping(params = "datagridOnline")
	@ResponseBody
	public DataGridData datagridOnline(DataGridParam dataGrid) {
		List<ClientBean> onlines = new ArrayList<ClientBean>();
		onlines.addAll(ClientManager.getInstance().getAllClient());
		dataGrid.setTotal(onlines.size());
		dataGrid.setResults(ClientManager.getClient(onlines,dataGrid));
		return TagUtil.getObject(dataGrid);
	}
}
