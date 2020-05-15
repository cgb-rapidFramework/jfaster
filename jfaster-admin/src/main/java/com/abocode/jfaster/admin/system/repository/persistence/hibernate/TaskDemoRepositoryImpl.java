package com.abocode.jfaster.admin.system.repository.persistence.hibernate;

import com.abocode.jfaster.admin.system.repository.TaskDemoRepository;
import com.abocode.jfaster.core.common.util.LogUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
@Service
public class TaskDemoRepositoryImpl implements TaskDemoRepository {

	
	public void work() {
		LogUtils.info(new Date().getTime());
		LogUtils.info("----------任务测试-------");
	}

}
