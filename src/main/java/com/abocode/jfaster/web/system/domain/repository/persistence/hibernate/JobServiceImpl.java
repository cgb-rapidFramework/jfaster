package com.abocode.jfaster.web.system.domain.repository.persistence.hibernate;

import com.abocode.jfaster.core.repository.service.impl.CommonServiceImpl;
import com.abocode.jfaster.web.system.domain.entity.Job;
import com.abocode.jfaster.web.system.domain.repository.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.util.List;

@Service("jobService")
@Transactional
public class JobServiceImpl extends CommonServiceImpl {


}