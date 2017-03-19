package com.abocode.jfaster.core.common.service;

import com.abocode.jfaster.core.common.hibernate.qbc.HqlQuery;
import com.abocode.jfaster.core.tag.vo.datatable.DataTableReturn;
import com.abocode.jfaster.core.tag.vo.easyui.Autocomplete;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import com.abocode.jfaster.core.common.hibernate.qbc.CriteriaQuery;
import com.abocode.jfaster.core.common.hibernate.qbc.PageList;
import com.abocode.jfaster.core.common.model.common.DBTable;
import com.abocode.jfaster.core.common.model.json.DataGridReturn;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface CommonService {
	/**
	 * 获取所有数据库表
	 * 
	 * @return
	 */
	public List<DBTable> getAllDbTableName();

	public Integer getAllDbTableSize();

	public <T> Serializable save(T entity);

	public <T> void saveOrUpdate(T entity);

	public <T> void delete(T entity);

	public <T> void batchSave(List<T> entitys);
	public <T> List<T> findAll(Class<T> entity);
    
	/**
	 * 根据实体名称和主键获取实体
	 * 
	 * @param <T>
	 * @param clazz
	 * @param id
	 * @return
	 */
	public <T> T find(Class<T> clazz, Serializable id);

	/**
	 * 根据实体名称和主键获取实体
	 * 
	 * @param <T>
	 * @param entityName
	 * @param id
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public <T> T findEntity(Class entityName, Serializable id);

	/**
	 * 根据实体名称和字段名称和字段值获取唯一记录
	 * 
	 * @param <T>
	 * @param entityClass
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public <T> T findUniqueByProperty(Class<T> entityClass,
			String propertyName, Object value);

	/**
	 * 按属性查找对象列表.
	 */
	public <T> List<T> findAllByProperty(Class<T> entityClass,
			String propertyName, Object value);

	/**
	 * 删除实体主键删除
	 * 
	 * @param <T>
	 * @param entities
	 */
	@SuppressWarnings("rawtypes")
	public <T> void delete(Class entities, Serializable id);

	/**
	 * 删除实体集合
	 * 
	 * @param <T>
	 * @param entities
	 */
	public <T> void deleteEntities(Collection<T> entities);

	/**
	 * 更新指定的实体
	 * 
	 * @param <T>
	 * @param pojo
	 */
	public <T> void update(T pojo);

	/**
	 * 通过hql 查询语句查找对象
	 * 
	 * @param <T>
	 * @param hql
	 * @return
	 */
	public <T> List<T> findByHql(String hql);

	/**
	 * 根据sql更新
	 * 
	 * @param sql
	 * @return
	 */
	public int updateBySql(String sql);
	

	/**
	 * 通过属性称获取实体带排序
	 * 
	 * @param <T>
	 * @param entityClass
	 * @return
	 */
	public <T> List<T> findByPropertyIsOrder(Class<T> entityClass,
			String propertyName, Object value, boolean isAsc);

	/***
	 * 查询所有
	 * 
	 * @param clas
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public <T> List<T> getList(Class clas);

	/**
	 * 查询实体
	 * 
	 * @param hql
	 * @return
	 */
	public <T> T findUniqueByHql(String hql);

	/**
	 * cq方式分页
	 * 
	 * @param cq
	 * @param isOffset
	 * @return
	 */
	public PageList findPageListByCq(final CriteriaQuery cq,
			final boolean isOffset);

	/**
	 * 
	 * hqlQuery方式分页
	 * 
	 * @param hqlQuery
	 * @param isOffset
	 * @return
	 */
	public PageList findPageListByHql(final HqlQuery hqlQuery,
			final boolean isOffset);

	/**
	 * 
	 * sqlQuery方式分页
	 * 
	 * @param hqlQuery
	 * @param isOffset
	 * @return
	 */
	public PageList findPageListBySql(final HqlQuery hqlQuery,
			final boolean isOffset);

	public Session getSession();

	/**
	 * 根据实体查询列表
	 * 
	 * @param entityName
	 * @param exampleEntity
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List findByExample(final String entityName,
			final Object exampleEntity);

	/**
	 * 通过cq获取全部实体
	 * 
	 * @param <T>
	 * @param cq
	 * @return
	 */
	public <T> List<T> findListByCq(final CriteriaQuery cq, Boolean ispage);
	
	/**
	 * 获取自动完成列表
	 * 
	 * @param <T>
	 * @return
	 */
	public <T> List<T> findAutoList(Autocomplete autocomplete);
	
	/**
	 * 通过hql 查询语句查找对象
	 * 
	 * @param <T>
	 * @param hql
	 * @return
	 */
	public <T> List<T> findByHql(String hql, Object[] param);
	/**
	 * 
	 * @param dc
	 * @param firstResult
	 * @param maxResult
	 * @return
	 */

	public <T> List<T> findByDetached(DetachedCriteria dc, int firstResult,
			int maxResult);
	/**
	 * @param dc
	 * @return
	 */
	public <T> List<T> findByDetached(DetachedCriteria dc);
	
	/**
	 * 返回DataTableReturn模型
	 * 
	 * @param cq
	 * @param isOffset
	 * @return
	 */
	public DataTableReturn findDataTableReturn(final CriteriaQuery cq,
                                               final boolean isOffset);

	/**
	 * 返回easyui datagrid模型
	 * 
	 * @param cq
	 * @param isOffset
	 * @return
	 */
	public DataGridReturn findDataGridReturn(final CriteriaQuery cq,
			final boolean isOffset);
	
	
	/**
	 * 查询问题  
	 * @param sql
	 * @return
	 */
    public List<String> findListBySql(String sql);
	/**
	 * 执行SQL
	 */
	public Integer executeSql(String sql, List<Object> param);

	/**
	 * 执行SQL
	 */
	public Integer executeSql(String sql, Object... param);

	/**
	 * 执行SQL 使用:name占位符
	 */
	public Integer executeSql(String sql, Map<String, Object> param);

	/**
	 * 执行SQL 使用:name占位符,并返回执行后的主键值
	 */
	public Object executeSqlReturnKey(String sql, Map<String, Object> param);

	/**
	 * 通过JDBC查找对象集合 使用指定的检索标准检索数据返回数据
	 */
	public List<Map<String, Object>> queryForListMap(String sql, Object... args);

	/**
	 * 通过JDBC查找对象集合 使用指定的检索标准检索数据返回数据
	 */
	public Map<String, Object> queryForMap(String sql, Object... args);

	/**
	 * 通过JDBC查找对象集合,带分页 使用指定的检索标准检索数据并分页返回数据
	 */
	public List<Map<String, Object>> queryForListMap(String sql, int page, int rows);

	/**
	 * 通过JDBC查找对象集合,带分页 使用指定的检索标准检索数据并分页返回数据
	 */
	public <T> List<T> queryForListObject(String dbType, String sql, int page, int rows,
			Class<T> clazz);

	/**
	 * 使用指定的检索标准检索数据并分页返回数据-采用预处理方式
	 * 
	 * @param dbType
	 * @param sql
	 * @param page
	 * @return
	 */
	public List<Map<String, Object>> queryForListMapByParameter(String dbType,String sql, int page,
			int rows, Object... args);


	/**
	 * 使用指定的检索标准检索数据并分页返回数据For JDBC-采用预处理方式
	 * 
	 */
	public Long queryForCount(String sql,Object... args);



	
}
