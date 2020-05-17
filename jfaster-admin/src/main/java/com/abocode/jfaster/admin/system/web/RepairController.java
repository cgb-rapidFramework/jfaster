package com.abocode.jfaster.admin.system.web;

import com.abocode.jfaster.admin.system.repository.RepairRepository;
import com.abocode.jfaster.admin.system.repository.SystemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Description   修复数据库
 * @ClassName: RepairController
 * @author tanghan
 * @date 2013-7-19 下午01:23:08
 */
@Scope("prototype")
@Controller
@RequestMapping("/repairController")
public class RepairController{
	private SystemRepository systemService;
    
	private RepairRepository repairService;
	
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Autowired
	public void setRepairService(RepairRepository repairService) {
		this.repairService = repairService;
	}

	@Autowired
	public void setSystemService(SystemRepository systemService) {
		this.systemService = systemService;
	}

	/** 
	 * @Description repair
	 */
	@RequestMapping(params = "repair")
	public ModelAndView repair() {
		repairService.deleteAndRepair();
		systemService.initAllTypeGroups();   //初始化缓存
		return new ModelAndView("login/login");
	}
	
}
