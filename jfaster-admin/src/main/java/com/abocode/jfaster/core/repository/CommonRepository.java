package com.abocode.jfaster.core.repository;

import com.abocode.jfaster.core.persistence.hibernate.qbc.CriteriaQuery;
import com.abocode.jfaster.core.persistence.hibernate.qbc.HqlQuery;
import com.abocode.jfaster.core.persistence.hibernate.qbc.PageHelper;
import com.abocode.jfaster.core.platform.view.interactions.easyui.Autocomplete;
import org.apache.poi.ss.formula.functions.T;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface CommonRepository {
	/**
	 * 获取所有数据库表
	 * 
	 * @return
	 */
	 List<T> findAllDbTableName();

	 Integer findAllDbTableSize();

	 <T> Serializable save(T entity);

	 <T> void saveOrUpdate(T entity);

	 <T> void delete(T entity);

	 <T> void batchSave(List<T> entitys);
	 <T> List<T> findAll(Class<T> entity);
	Integer executeHql(String hql);

    
	/**
	 * 根据实体名称和主键获取实体
	 * 
	 * @param <T>
	 * @param clazz
	 * @param id
	 * @return
	 */
	 <T> T find(Class<T> clazz, Serializable id);

	/**
	 * 根据实体名称和字段名称和字段值获取唯一记录
	 * 
	 * @param <T>
	 * @param entityClass
	 * @param propertyName
	 * @param value
	 * @return
	 */
	 <T> T findUniqueByProperty(Class<T> entityClass,
			String propertyName, Object value);

	/**
	 * 按属性查找对象列表.
	 */
	 <T> List<T> findAllByProperty(Class<T> entityClass,
			String propertyName, Object value);

	/**
	 * 删除实体主键删除
	 * 
	 * @param entities
	 */
	 void delete(Class<T> entities, Serializable id);

	/**
	 * 删除实体集合
	 * 
	 * @param <T>
	 * @param entities
	 */
	 <T> void deleteEntities(Collection<T> entities);

	/**
	 * 更新指定的实体
	 * 
	 * @param <T>
	 * @param pojo
	 */
	 <T> void update(T pojo);

	/**
	 * 通过hql 查询语句查找对象
	 * 
	 * @param <T>
	 * @param hql
	 * @return
	 */
	 <T> List<T> findByHql(String hql);

	/**
	 * 根据sql更新
	 * 
	 * @param sql
	 * @return
	 */
	 int updateBySql(String sql);
	

	/**
	 * 通过属性称获取实体带排序
	 * 
	 * @param <T>
	 * @param entityClass
	 * @return
	 */
	 <T> List<T> findByPropertyIsOrder(Class<T> entityClass,
			String propertyName, Object value, boolean isAsc);


	/**
	 * 查询实体
	 * 
	 * @param hql
	 * @return
	 */
	 <T> T findUniqueByHql(String hql);

	/**
	 * cq方式分页
	 * 
	 * @param cq
	 * @param isOffset
	 * @return
	 */
	 PageHelper findPageListByCq(final CriteriaQuery cq,
                                 final boolean isOffset);

	/**
	 * 
	 * hqlQuery方式分页
	 * 
	 * @param hqlQuery
	 * @param isOffset
	 * @return
	 */
	 PageHelper findPageListByHql(final HqlQuery hqlQuery,
                                  final boolean isOffset);

	/**
	 * 
	 * sqlQuery方式分页
	 * 
	 * @param hqlQuery
	 * @param isOffset
	 * @return
	 */
	 PageHelper findPageListBySql(final HqlQuery hqlQuery,
                                  final boolean isOffset);

	 Session getSession();

	/**
	 * 根据实体查询列表
	 * 
	 * @param entityName
	 * @param exampleEntity
	 * @return
	 */
	 List<T> findByExample(String entityName,
			final Object exampleEntity);

	/**
	 * 通过cq获取全部实体
	 * 
	 * @param <T>
	 * @param cq
	 * @return
	 */
	 <T> List<T> findListByCq(final CriteriaQuery cq, boolean ispage);
	
	/**
	 * 获取自动完成列表
	 * 
	 * @param <T>
	 * @return
	 */
	 <T> List<T> findAutoList(Autocomplete autocomplete);
	
	/**
	 * 通过hql 查询语句查找对象
	 * 
	 * @param <T>
	 * @param hql
	 * @return
	 */
	 <T> List<T> findByHql(String hql, Object[] param);
	/**
	 * 
	 * @param dc
	 * @param firstResult
	 * @param maxResult
	 * @return
	 */

	 <T> List<T> findByDetached(DetachedCriteria dc, int firstResult,
			int maxResult);
	/**
	 * @param dc
	 * @return
	 */
	 <T> List<T> findByDetached(DetachedCriteria dc);
	

	 DataGridData findDataGridData(final CriteriaQuery cq);
	/**
	 * 返回easyui datagrid模型
	 * @param cq
	 * @param isOffset
	 * @return
	 */
	DataGridData findDataGridData(final CriteriaQuery cq,
								  final boolean isOffset);
	
	
	/**
	 * 查询问题  
	 * @param sql
	 * @return
	 */
     List<String> findListBySql(String sql);
	/**
	 * 执行SQL
	 */
	 Integer executeSql(String sql, List<Object> param);

	/**
	 * 执行SQL
	 */
	 Integer executeSql(String sql, Object... param);

	/**
	 * 执行SQL 使用:name占位符
	 */
	 Integer executeSql(String sql, Map<String, Object> param);

	/**
	 * 执行SQL 使用:name占位符,并返回执行后的主键值
	 */
	 Object executeSqlReturnKey(String sql, Map<String, Object> param);

	/**
	 * 通过JDBC查找对象集合 使用指定的检索标准检索数据返回数据
	 */
	 List<Map<String, Object>> queryForListMap(String sql, Object... args);

	/**
	 * 通过JDBC查找对象集合 使用指定的检索标准检索数据返回数据
	 */
	 Map<String, Object> queryForMap(String sql, Object... args);

	/**
	 * 通过JDBC查找对象集合,带分页 使用指定的检索标准检索数据并分页返回数据
	 */
	 List<Map<String, Object>> queryForListMap(String sql, int page, int rows);

	/**
	 * 通过JDBC查找对象集合,带分页 使用指定的检索标准检索数据并分页返回数据
	 */
	 <T> List<T> queryForListObject(String dbType, String sql, int page, int rows,
			Class<T> clazz);

	/**
	 * 使用指定的检索标准检索数据并分页返回数据-采用预处理方式
	 * 
	 * @param dbType
	 * @param sql
	 * @param page
	 * @return
	 */
	 List<Map<String, Object>> queryForListMapByParameter(String dbType,String sql, int page,
			int rows, Object... args);


	/**
	 * 使用指定的检索标准检索数据并分页返回数据For JDBC-采用预处理方式
	 * 
	 */
	 Long queryForCount(String sql,Object... args);



	
}
