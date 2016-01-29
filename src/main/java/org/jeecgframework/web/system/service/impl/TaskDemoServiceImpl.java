package org.jeecgframework.web.system.service.impl;

import org.jeecgframework.core.util.LogUtils;
import org.jeecgframework.web.system.service.TaskDemoServiceI;
import org.springframework.stereotype.Service;

import java.util.Date;
@Service("taskDemoService")
public class TaskDemoServiceImpl implements TaskDemoServiceI {

	
	public void work() {
		LogUtils.info(new Date().getTime());
		LogUtils.info("----------任务测试-------");
	}

}
