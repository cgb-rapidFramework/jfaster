package org.jeecgframework.web.system.service.impl;

import org.jeecgframework.web.system.service.TaskDemoService;
import org.jeecgframework.web.utils.LogUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
@Service("taskDemoService")
public class TaskDemoServiceImpl implements TaskDemoService {

	
	public void work() {
		LogUtils.info(new Date().getTime());
		LogUtils.info("----------任务测试-------");
	}

}
