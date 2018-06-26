package com.abocode.jfaster.web.system.domain.repository.persistence.hibernate;

import com.abocode.jfaster.core.domain.repository.persistence.hibernate.CommonRepositoryImpl;
import com.abocode.jfaster.web.system.domain.repository.LogService;
import org.springframework.stereotype.Service;

/**
 * 日志Service接口实现类
 * @author  方文荣
 *
 */
@Service("logService")
public class LogServiceImpl extends CommonRepositoryImpl implements LogService{

}
