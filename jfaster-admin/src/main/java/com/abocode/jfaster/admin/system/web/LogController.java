package com.abocode.jfaster.admin.system.web;

import com.abocode.jfaster.admin.system.service.ChartService;
import com.abocode.jfaster.core.common.model.json.DataGrid;
import com.abocode.jfaster.core.common.model.json.HighChart;
import com.abocode.jfaster.core.common.util.ConvertUtils;
import com.abocode.jfaster.system.entity.Log;
import com.abocode.jfaster.admin.system.repository.LogRepository;
import com.abocode.jfaster.admin.system.repository.SystemRepository;
import com.abocode.jfaster.core.common.util.DateUtils;
import com.abocode.jfaster.core.persistence.hibernate.qbc.CriteriaQuery;
import com.abocode.jfaster.core.platform.view.widgets.easyui.TagUtil;
import com.abocode.jfaster.core.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.List;


/**
 * 日志处理类
 * 
 * @author 张代浩
 * 
 */
@Controller
@RequestMapping("/logController")
public class LogController{
	@Autowired
	private SystemRepository systemService;
	@Autowired
	private LogRepository logService;
	@Autowired
	private ChartService chartService;
	/**
	 * 日志列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "log")
	public ModelAndView log() {
		return new ModelAndView("system/log/logList");
	}

	/**
	 * easyuiAJAX请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "datagrid")
	public void datagrid(HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(Log.class, dataGrid);
		String loglevel = request.getParameter("loglevel");
		if (loglevel != null &&!loglevel.equals("0")) {
			cq.eq("loglevel", ConvertUtils.getShort(loglevel));
			cq.add();
		}
        String operatetime_begin = request.getParameter("operatetime_begin");
        if(operatetime_begin != null) {
            Timestamp beginValue =  DateUtils.parseTimestamp(operatetime_begin, "yyyy-MM-dd");;
            cq.ge("operatetime", beginValue);
        }
        String operatetime_end = request.getParameter("operatetime_end");
        if(operatetime_end != null) {
            if (operatetime_end.length() == 10) {
                operatetime_end =operatetime_end + " 23:59:59";
            }
            Timestamp endValue = DateUtils.parseTimestamp(operatetime_end, "yyyy-MM-dd hh:mm:ss");
            cq.le("operatetime", endValue);
        }
        cq.add();
        this.systemService.findDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}
	
	/**
	 * 获取日志详情
	 * @param tsLog
	 * @param request
	 * @return
	 * @Author fangwenrong
	 * @Date 2015-05-10
	 */
	@RequestMapping(params = "logDetail")
	public ModelAndView logDetail(Log tsLog, HttpServletRequest request){
		if (StringUtils.isNotEmpty(tsLog.getId())) {
			tsLog = logService.findEntity(Log.class, tsLog.getId());
			request.setAttribute("logView", tsLog);
		}
		return new ModelAndView("system/log/logDetail");
		
	}

	/**
	 * 统计集合页面
	 * 
	 * @return
	 */
	@RequestMapping(params = "statisticTabs")
	public ModelAndView statisticTabs(HttpServletRequest request) {
		return new ModelAndView("system/log/statisticTabs");
	}
	/**
	 * 用户浏览器使用统计图
	 * 
	 * @return
	 */
	@RequestMapping(params = "userBroswer")
	public ModelAndView userBroswer(String reportType, HttpServletRequest request) {
		request.setAttribute("reportType", reportType);
		if("pie".equals(reportType)){
			return new ModelAndView("system/log/userBroswerPie");
		}else if("line".equals(reportType)) {
			return new ModelAndView("system/log/userBroswerLine");
		}
		return new ModelAndView("system/log/userBroswer");
	}

	/**
	 * 报表数据生成
	 * 
	 * @return
	 */
	@RequestMapping(params = "getBroswerBar")
	@ResponseBody
	public List<HighChart> getBroswerBar(HttpServletRequest request, String reportType, HttpServletResponse response) {
		return  chartService.buildChart(reportType);
	}
}