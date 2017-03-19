package com.abocode.jfaster.core.common.service.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.abocode.jfaster.core.common.hibernate.qbc.HqlQuery;
import com.abocode.jfaster.core.tag.vo.easyui.Autocomplete;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import com.abocode.jfaster.core.common.dao.ICommonDao;
import com.abocode.jfaster.core.common.hibernate.qbc.CriteriaQuery;
import com.abocode.jfaster.core.common.hibernate.qbc.PageList;
import com.abocode.jfaster.core.common.model.common.DBTable;
import com.abocode.jfaster.core.common.model.json.DataGridReturn;
import com.abocode.jfaster.core.common.service.CommonService;
import com.abocode.jfaster.core.tag.vo.datatable.DataTableReturn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("commonService")
@Transactional
public class CommonServiceImpl implements CommonService {
	public ICommonDao commonDao = null;
	public Session getSession()

	{
		return commonDao.getSession();
	}
	/**
	 * 获取所有数据库表
	 * 
	 * @return
	 */
	public List<DBTable> getAllDbTableName() {
		return commonDao.findAllDbTableName();
	}

	public Integer getAllDbTableSize() {
		return commonDao.findAllDbTableSize();
	}

	@Resource
	public void setCommonDao(ICommonDao commonDao) {
		this.commonDao = commonDao;
	}

	
	public <T> Serializable save(T entity) {
		return commonDao.save(entity);
	}

	
	public <T> void saveOrUpdate(T entity) {
		commonDao.saveOrUpdate(entity);

	}

	
	public <T> void delete(T entity) {
		commonDao.delete(entity);

	}

	/**
	 * 删除实体集合
	 * 
	 * @param <T>
	 * @param entities
	 */
	public <T> void deleteEntities(Collection<T> entities) {
		commonDao.deleteAllEntitie(entities);
	}

	/**
	 * 根据实体名获取对象
	 */
	public <T> T find(Class<T> class1, Serializable id) {
		return commonDao.find(class1, id);
	}

	/**
	 * 根据实体名返回全部对象
	 * 
	 * @param <T>
	 * @param clazz
	 * @return
	 */
	public <T> List<T> getList(Class clazz) {
		return commonDao.findAll(clazz);
	}

	/**
	 * 根据实体名获取对象
	 */
	@SuppressWarnings("rawtypes")
	public <T> T findEntity(Class entityName, Serializable id) {
		return commonDao.findEntity(entityName, id);
	}

	/**
	 * 根据实体名称和字段名称和字段值获取唯一记录
	 * @param <T>
	 * @param entityClass
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public <T> T findUniqueByProperty(Class<T> entityClass,
			String propertyName, Object value) {
		return commonDao.findUniqueByProperty(entityClass, propertyName, value);
	}

	/**
	 * 按属性查找对象列表.
	 * 
	 */
	public <T> List<T> findAllByProperty(Class<T> entityClass,
			String propertyName, Object value) {
		return commonDao.findAllByProperty(entityClass, propertyName, value);
	}

	/**
	 * 加载全部实体
	 * 
	 * @param <T>
	 * @param entityClass
	 * @return
	 */
	public <T> List<T> findAll(final Class<T> entityClass) {
		return commonDao.findAll(entityClass);
	}
	
   /**
    * 根据hql查询一个
    */
	
	public <T> T findUniqueByHql(String hql) {
		return commonDao.findUniqueByHql(hql);
	}

	/**
	 * 删除实体主键ID删除对象
	 * 
	 * @param <T>
	 * @param entities
	 */
	@SuppressWarnings("rawtypes")
	public <T> void delete(Class entities, Serializable id) {
		commonDao.deleteEntityById(entities, id);
	}

	/**
	 * 更新指定的实体
	 * 
	 * @param <T>
	 * @param pojo
	 */
	public <T> void update(T pojo) {
		commonDao.update(pojo);

	}

	/**
	 * 通过hql 查询语句查找对象
	 * 
	 * @param <T>
	 * @param hql
	 * @return
	 */
	public <T> List<T> findByHql(String hql) {
		return commonDao.findByHql(hql);
	}

	/**
	 * 通过属性称获取实体带排序
	 * 
	 * @param <T>
	 * @param clazz
	 * @return
	 */
	public <T> List<T> findByPropertyIsOrder(Class<T> clazz,
			String propertyName, Object value, boolean isAsc) {
		return commonDao.findByPropertyIsOrder(clazz, propertyName,
				value, isAsc);
	}

	/**
	 * 
	 * cq方式分页
	 * 
	 * @param cq
	 * @param isOffset
	 * @return
	 */
	public PageList findPageListByCq(final CriteriaQuery cq, final boolean isOffset) {
		return commonDao.findPageListByCq(cq, isOffset);
	}

	
	/**
	 * 
	 * hqlQuery方式分页
	 * 
	 * @param hql
	 * @param isOffset
	 * @return
	 */
	public PageList findPageListByHql(final HqlQuery hql,
			final boolean isOffset) {
		return commonDao.findPageListByHql(hql, isOffset);
	}

	/**
	 * 通过cq获取全部实体
	 * 
	 * @param <T>
	 * @param cq
	 * @return
	 */
	public <T> List<T> findListByCq(final CriteriaQuery cq,
			Boolean ispage) {
		return commonDao.findListByCq(cq, ispage);
	}
	

	/**
	 * 查询
	 * 根据实体查询
	 */
	@SuppressWarnings("rawtypes")
	public  List findByExample(final String entityName,
			final Object exampleEntity) {
		return commonDao.findByExample(entityName, exampleEntity);
	}
	
	/**
	 * 获取自动完成列表
	 * 
	 * @param <T>
	 * @return
	 */
	public <T> List<T> findAutoList(Autocomplete autocomplete) {
		return commonDao.findAutoList(autocomplete);
	}

	/**
	 * 通过hql 查询语句查找对象
	 *
	 * @param <T>
	 * @param hql
	 * @return
	 */
	public <T> List<T> findByHql(String hql, Object[] params) {
		return this.commonDao.findByHql(hql, params);
	}

	public Integer executeSql(String sql, List<Object> param) {
		return commonDao.executeSql(sql, param);
	}

	
	public Integer executeSql(String sql, Object... param) {
		return commonDao.executeSql(sql, param);
	}

	
	public Integer executeSql(String sql, Map<String, Object> param) {
		return commonDao.executeSql(sql, param);
	}
	
	public Object executeSqlReturnKey(String sql, Map<String, Object> param) {
		return commonDao.executeSqlReturnKey(sql, param);
	}
	
	public List<Map<String, Object>> queryForListMap(String sql, int page, int rows) {
		return commonDao.queryForListMap(sql, page, rows);
	}

	
	public List<Map<String, Object>> queryForListMap(String sql, Object... args) {
		return commonDao.queryForListMap(sql, args);
	}

	
	public List<Map<String, Object>> queryForListMapByParameter(String dbType,String sql, int page,
			int rows, Object... args) {
		return commonDao.queryForListMapByParameter(dbType,sql, page, rows, args);
	}

	
	public <T> List<T> queryForListObject(String dbType,String sql, int page, int rows,
			Class<T> clazz) {
		return commonDao.queryForListObject(dbType,sql, page, rows, clazz);
	}

	
	public Map<String, Object> queryForMap(String sql, Object... args) {
		return commonDao.queryForMap(sql, args);
	}

	public Long queryForCount(String sql, Object... args) {
		return commonDao.queryForCount(sql,args);
	}
	
	public <T> void batchSave(List<T> entities) {
		this.commonDao.batchSave(entities);
	}



	public <T> List<T> findByDetached(DetachedCriteria dc, int firstResult,
			int maxResult) {
		return this.commonDao.findByDetached(dc, firstResult, maxResult);
	}

	public <T> List<T> findByDetached(DetachedCriteria dc) {
		return this.commonDao.findByDetached(dc);
	}
	
	
	/**** sql 查询 */
	
	/**
	 * 根据sql更新
	 * 
	 * @param sql
	 * @return
	 */
	public int updateBySql(String sql) {
		return commonDao.updateBySql(sql);
	}

	/**
	 * 
	 * sqlQuery方式分页
	 * 
	 * @param hqlQuery
	 * @param isOffset
	 * @return
	 */
	public PageList findPageListBySql(final HqlQuery hqlQuery,
			final boolean isOffset) {
		return commonDao.findPageListBySql(hqlQuery, isOffset);
	}
	
	
	//***EasyUI DataGrid**//
	/**
	 * 返回DataTableReturn模型
	 * 
	 * @param cq
	 * @param isOffset
	 * @return
	 */
	public DataTableReturn findDataTableReturn(final CriteriaQuery cq,
			final boolean isOffset) {
		return commonDao.findDataTableReturn(cq, isOffset);
	}

	/**
	 * 返回easyui datagrid模型
	 * 
	 * @param cq
	 * @param isOffset
	 * @return
	 */
	public DataGridReturn findDataGridReturn(final CriteriaQuery cq,
			final boolean isOffset) {
		return commonDao.findDataGridReturn(cq, isOffset);
	}
	/**
	 * 查询数据返回对象
	 */
	   public List<String> findListBySql(String sql){
			return commonDao.findListBySql(sql);
	   }
	
}
