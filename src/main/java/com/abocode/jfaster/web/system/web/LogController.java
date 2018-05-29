package com.abocode.jfaster.web.system.web;

import com.abocode.jfaster.core.common.model.json.DataGrid;
import com.abocode.jfaster.core.common.model.json.HighChart;
import com.abocode.jfaster.core.common.util.ConvertUtils;
import com.abocode.jfaster.core.common.util.MutiLangUtils;
import com.abocode.jfaster.web.system.domain.entity.Log;
import com.abocode.jfaster.web.system.domain.repository.LogService;
import com.abocode.jfaster.web.system.domain.repository.SystemService;
import com.abocode.jfaster.core.common.util.DateUtils;
import com.abocode.jfaster.core.common.util.LogUtils;
import com.abocode.jfaster.core.persistence.hibernate.qbc.CriteriaQuery;
import com.abocode.jfaster.platform.view.widgets.easyui.TagUtil;
import com.abocode.jfaster.core.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 日志处理类
 * 
 * @author 张代浩
 * 
 */
@Controller
@RequestMapping("/logController")
public class LogController extends BaseController {
    //用户浏览器统计分析的国际化KEY
    private static final String USER_BROWSER_ANALYSIS = "user.browser.analysis";
	private SystemService systemService;
	
	private LogService logService;

	@Autowired
	public void setSystemService(SystemService systemService) {
		this.systemService = systemService;
	}
	
	@Autowired
	public void setLogService(LogService logService) {
		this.logService = logService;
	}

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
		if (loglevel == null || loglevel.equals("0")) {
		} else {
			cq.eq("loglevel", ConvertUtils.getShort(loglevel));
			cq.add();
		}
        String operatetime_begin = request.getParameter("operatetime_begin");
        if(operatetime_begin != null) {
            Timestamp beginValue = null;
            try {
                beginValue = DateUtils.parseTimestamp(operatetime_begin, "yyyy-MM-dd");
            } catch (ParseException e) {
                LogUtils.error(e.getMessage());
            }
            cq.ge("operatetime", beginValue);
        }
        String operatetime_end = request.getParameter("operatetime_end");
        if(operatetime_end != null) {
            if (operatetime_end.length() == 10) {
                operatetime_end =operatetime_end + " 23:59:59";
            }
            Timestamp endValue = null;
            try {
                endValue = DateUtils.parseTimestamp(operatetime_end, "yyyy-MM-dd hh:mm:ss");
            } catch (ParseException e) {
                LogUtils.error(e.getMessage());
            }
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
		List<HighChart> list = new ArrayList<HighChart>();
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT broswer ,count(broswer) FROM Log group by broswer");
		List userBroswerList = systemService.findByHql(sb.toString());
		Long count = systemService.queryForCount("SELECT COUNT(1) FROM T_S_Log WHERE 1=1");
		List lt = new ArrayList();
		HighChart hc = new HighChart();
		hc.setName(MutiLangUtils.getLang(USER_BROWSER_ANALYSIS));
		hc.setType(reportType);
		Map<String, Object> map;
		if (userBroswerList.size() > 0) {
			for (Object object : userBroswerList) {
				map = new HashMap<String, Object>();
				Object[] obj = (Object[]) object;
				map.put("name", obj[0]);
				map.put("y", obj[1]);
				Long groupCount = (Long) obj[1];
				Double  percentage = 0.0;
				if (count != null && count.intValue() != 0) {
					percentage = new Double(groupCount)/count;
				}
				map.put("percentage", percentage*100);
				lt.add(map);
			}
		}
		hc.setData(lt);
		list.add(hc);
		return list;
	}


}
