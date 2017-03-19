package com.abocode.jfaster.web.system.service.impl;

import com.abocode.jfaster.web.system.service.TaskDemoService;
import com.abocode.jfaster.web.utils.LogUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
@Service("taskDemoService")
public class TaskDemoServiceImpl implements TaskDemoService {

	
	public void work() {
		LogUtils.info(new Date().getTime());
		LogUtils.info("----------任务测试-------");
	}

}
