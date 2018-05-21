package com.abocode.jfaster.web.system.domain.repository.persistence.hibernate;

import com.abocode.jfaster.web.system.domain.repository.TaskDemoService;
import com.abocode.jfaster.core.common.util.LogUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
@Service("taskDemoService")
public class TaskDemoServiceImpl implements TaskDemoService {

	
	public void work() {
		LogUtils.info(new Date().getTime());
		LogUtils.info("----------任务测试-------");
	}

}
