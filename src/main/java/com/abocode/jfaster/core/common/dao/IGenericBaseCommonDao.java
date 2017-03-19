package com.abocode.jfaster.core.common.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.abocode.jfaster.core.tag.vo.easyui.Autocomplete;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import com.abocode.jfaster.core.common.hibernate.qbc.CriteriaQuery;
import com.abocode.jfaster.core.common.hibernate.qbc.HqlQuery;
import com.abocode.jfaster.core.common.hibernate.qbc.PageList;
import com.abocode.jfaster.core.common.model.common.DBTable;
import com.abocode.jfaster.core.common.model.json.DataGridReturn;
import com.abocode.jfaster.core.tag.vo.datatable.DataTableReturn;

/**
 * 
 * 类描述：DAO层泛型基类接口
 * 
 * 张代浩
 * @date： 日期：2012-12-8 时间：下午05:37:33
 * @version 1.0
 */
public interface IGenericBaseCommonDao {
	/**
	 * 获取所有数据库表
	 * 
	 * @return
	 */
	public List<DBTable> findAllDbTableName();

	public Integer findAllDbTableSize();

	public <T> Serializable save(T entity);

	public <T> void batchSave(List<T> entitys);

	public <T> void saveOrUpdate(T entity);

	/**
	 * 删除实体
	 * 
	 * @param <T>
	 * 
	 * @param <T>
	 * 
	 * @param <T>
	 * @param entitie
	 */
	public <T> void delete(T entitie);

	/**
	 * 根据实体名称和主键获取实体
	 * 
	 * @param <T>
	 * @param entityName
	 * @param id
	 * @return
	 */
	public <T> T find(Class<T> entityName, Serializable id);

	/**
	 * 根据实体名字获取唯一记录
	 * 
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
	 * 加载全部实体
	 * 
	 * @param <T>
	 * @param entityClass
	 * @return
	 */
	public <T> List<T> findAll(final Class<T> entityClass);

	/**
	 * 根据实体名称和主键获取实体
	 * 
	 * @param <T>
	 * 
	 * @param <T>
	 * @param entityName
	 * @param id
	 * @return
	 */
	public <T> T findEntity(Class entityName, Serializable id);

	public <T> void deleteEntityById(Class entityName, Serializable id);

	/**
	 * 删除实体集合
	 * 
	 * @param <T>
	 * @param entities
	 */
	public <T> void deleteAllEntitie(Collection<T> entities);

	/**
	 * 更新指定的实体
	 * 
	 * @param <T>
	 * @param pojo
	 */
	public <T> void update(T pojo);

	public <T> void updateEntityById(Class entityName, Serializable id);

	/**
	 * 通过hql 查询语句查找对象
	 * 
	 * @param <T>
	 * @param hql
	 * @return
	 */
	public <T> List<T> findByHql(String hql);

	/**
	 * 通过hql查询唯一对象
	 * 
	 * @param <T>
	 * @param hql
	 * @return
	 */
	public <T> T findUniqueByHql(String hql);

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

	/**
	 * 
	 * cq方式分页
	 * 
	 * @param cq
	 * @param isOffset
	 * @return
	 */
	public PageList findPageListByCq(final CriteriaQuery cq, final boolean isOffset);

	/**
	 * 通过cq获取全部实体
	 * 
	 * @param <T>
	 * @param cq
	 * @return
	 */
	public <T> List<T> findListByCq(final CriteriaQuery cq,
			Boolean ispage);

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

	public List findByExample(final String entityName,
			final Object exampleEntity);

	/**
	 * 通过hql 查询语句查找HashMap对象
	 * 
	 * @param query
	 * @return
	 */
	public Map<Object, Object> findHashMapbyHql(String query);

	/**
	 * 返回jquery datatables模型
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
	 * 执行SQL 使用:name占位符,并返回插入的主键值
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
	public <T> List<T> queryForListObject(String dbType,String sql, int page, int rows,
			Class<T> clazz);

	/**
	 * 使用指定的检索标准检索数据并分页返回数据-采用预处理方式
	 * @return
	 */
	public List<Map<String, Object>> queryForListMapByParameter(String dbType,String sql, int page,
			int rows, Object... args);

	/**
	 * 使用指定的检索标准检索数据并分页返回数据For JDBC
	 */
	public Long queryForCount(String sql, Object[] args);
	/**
	 * 通过hql 查询语句查找对象
	 * 
	 * @param <T>
	 * @param hql
	 * @return
	 */
	public <T> List<T> findByHql(String hql, Object[] param);

	/**
	 * 执行HQL语句操作更新
	 * 
	 * @param hql
	 * @return
	 */
	public Integer executeHql(String hql);

	public <T> List<T> findByDetached(DetachedCriteria dc, int firstResult,
			int maxResult);

	public <T> List<T> findByDetached(DetachedCriteria dc);
	/**
	 * 查询自动list
	 * @param autocomplete
	 * @return
	 */
	public  <T> List<T> findAutoList(Autocomplete autocomplete);
	
	/**
	 * 查询自动list
	 * @param sql
	 * @return
	 */
	public  <T> List<T> findListBySql(String sql);
	
}
