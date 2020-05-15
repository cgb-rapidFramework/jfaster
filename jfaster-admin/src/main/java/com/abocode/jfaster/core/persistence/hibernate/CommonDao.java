package com.abocode.jfaster.core.persistence.hibernate;


import com.abocode.jfaster.core.persistence.ICommonDao;
import com.abocode.jfaster.core.persistence.IGenericBaseCommonDao;
import org.springframework.stereotype.Repository;

/**
 * 公共扩展方法
 * @author  张代浩
 *
 */
@Repository
public  class CommonDao extends GenericBaseCommonDao implements ICommonDao, IGenericBaseCommonDao {

}
