package com.abocode.jfaster.core.persistence.hibernate;

import com.abocode.jfaster.core.repository.DataGridParam;
import com.abocode.jfaster.core.persistence.jdbc.JdbcDao;
import com.abocode.jfaster.core.common.exception.BusinessException;
import com.abocode.jfaster.core.persistence.hibernate.qbc.CriteriaQuery;
import com.abocode.jfaster.core.platform.view.interactions.easyui.Autocomplete;
import com.abocode.jfaster.core.common.util.BeanPropertyUtils;
import com.abocode.jfaster.core.common.util.ConvertUtils;
import com.abocode.jfaster.core.persistence.hibernate.qbc.HqlQuery;
import com.abocode.jfaster.core.persistence.hibernate.qbc.PageHelper;
import com.abocode.jfaster.core.persistence.hibernate.qbc.PagerUtil;
import com.abocode.jfaster.core.repository.DataGridData;
import com.abocode.jfaster.core.repository.TagUtil;
import com.abocode.jfaster.core.repository.CommonRepository;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Criteria;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.*;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.hibernate.type.Type;
import com.abocode.jfaster.core.persistence.DBTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.*;

/**
 * 
 * 类描述： DAO层泛型基类
 * @param <T>
 * @version 1.0
 */
@Slf4j
@Repository
public    class HibernateCommonRepository<T extends Serializable>
		implements CommonRepository {
	/**
	 * 注入一个sessionFactory属性,并注入到父类(HibernateDaoSupport)
	 * **/
	@Autowired
	@Qualifier("sessionFactory")
	private SessionFactory sessionFactory;

	@Override
	public Session getSession() {
		// 事务必须是开启的(Required)，否则获取不到
		return sessionFactory.getCurrentSession();
	}

	@Override
	public Integer executeHql(String hql) {
		Query q = getSession().createQuery(hql);
		return q.executeUpdate();
	}

	/**
	 * 获取所有数据表
	 * 
	 * @return
	 */
	public List<DBTable> findAllDbTableName() {
		List<DBTable> resultList = new ArrayList<DBTable>();
		SessionFactory factory = getSession().getSessionFactory();
		Map<String, ClassMetadata> metaMap = factory.getAllClassMetadata();
		for (String key : metaMap.keySet()) {
			DBTable dbTable = new DBTable();
			AbstractEntityPersister classMetadata = (AbstractEntityPersister) metaMap
					.get(key);
			dbTable.setTableName(classMetadata.getTableName());
			dbTable.setEntityName(classMetadata.getEntityName());
			Class<?> c;
			try {
				c = Class.forName(key);
				EntityTitle t = c.getAnnotation(EntityTitle.class);
				dbTable.setTableTitle(t != null ? t.name() : "");
			} catch (ClassNotFoundException e) {
				log.error(e.getMessage());
			}
			resultList.add(dbTable);
		}
		return resultList;
	}

	/**
	 * 获取所有数据表
	 * 
	 * @return
	 */
	public Integer findAllDbTableSize() {
		SessionFactory factory = getSession().getSessionFactory();
		Map<String, ClassMetadata> metaMap = factory.getAllClassMetadata();
		return metaMap.size();
	}

	/**
	 * 按属性查找对象列表.
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> findAllByProperty(Class<T> entityClass,
			String propertyName, Object value) {
		Assert.hasText(propertyName,"属性不能为空");
		return (List<T>) createCriteria(entityClass,
				Restrictions.eq(propertyName, value)).list();
	}

	/**
	 * 根据传入的实体持久化对象
	 */
	public <T> Serializable save(T entity) {
		try {
			Serializable id = getSession().save(entity);
			getSession().flush();
			return id;
		} catch (RuntimeException e) {
			throw e;
		}

	}

	/**
	 * 批量保存数据
	 * 
	 * @param <T>
	 * @param entitys
	 *            要持久化的临时实体对象集合
	 */
	public <T> void batchSave(List<T> entitys) {
		for (int i = 0; i < entitys.size(); i++) {
			getSession().save(entitys.get(i));
			if (i % 20 == 0) {
				// 20个对象后才清理缓存，写入数据库
				getSession().flush();
				getSession().clear();
			}
		}
		// 最后清理一下----防止大于20小于40的不保存
		getSession().flush();
		getSession().clear();
	}

	/**
	 * 根据传入的实体添加或更新对象
	 * 
	 * @param <T>
	 * 
	 * @param entity
	 */

	public <T> void saveOrUpdate(T entity) {
		try {
			getSession().saveOrUpdate(entity);
			getSession().flush();
		} catch (RuntimeException e) {
			throw e;
		}
	}

	/**
	 * 根据传入的实体删除对象
	 */
	public <T> void delete(T entity) {
		try {
			getSession().delete(entity);
			getSession().flush();
		} catch (RuntimeException e) {
			throw e;
		}
	}

	/**
	 * 根据主键删除指定的实体
	 * 
	 * @param <T>
	 * @param entityName
	 */
	@Override
	public <T> void delete(Class entityName, Serializable id) {
		delete(find(entityName, id));
		getSession().flush();
	}

	/**
	 * 删除全部的实体
	 * 
	 * @param <T>
	 * 
	 * @param entitys
	 */
	@Override
	public <T> void deleteEntities(Collection<T> entitys) {
		for (Object entity : entitys) {
			getSession().delete(entity);
			getSession().flush();
		}
	}

	/**
	 * 根据Id获取对象。
	 */
	@Override
	public <T> T find(Class<T> entityClass, final Serializable id) {

		return (T) getSession().get(entityClass, id);

	}

	/**
	 * 根据主键获取实体并加锁。
	 * 
	 * @param <T>
	 * @param entityName
	 * @param id
	 * @return
	 */
	public <T> T findEntity(Class entityName, Serializable id) {

		T t = (T) getSession().get(entityName, id);
		if (t != null) {
			getSession().flush();
		}
		return t;
	}

	/**
	 * 更新指定的实体
	 * 
	 * @param <T>
	 * @param pojo
	 */
	public <T> void update(T pojo) {
		getSession().update(pojo);
		getSession().flush();
	}

	/**
	 * 更新指定的实体
	 * 
	 * @param <T>
	 * @param className
	 */
	public <T> void updateEntitie(String className, Object id) {
		getSession().update(className, id);
		getSession().flush();
	}

	/**
	 * 根据主键更新实体
	 */
	public <T> void updateEntityById(Class entityName, Serializable id) {
		update(find(entityName, id));
	}

	/**
	 * 通过hql 查询语句查找对象
	 * 
	 * @param query
	 * @return
	 */
	public List<T> findByHql(final String query) {

		Query queryObject = getSession().createQuery(query);
		List<T> list = queryObject.list();
		if (list.size() > 0) {
			getSession().flush();
		}
		return list;

	}

	/**
	 * 通过hql查询唯一对象
	 * 
	 * @param <T>
	 * @param hql
	 * @return
	 */
	public <T> T findUniqueByHql(String hql) {
		T t = null;
		Query queryObject = getSession().createQuery(hql);
		List<T> list = queryObject.list();
		if (list.size() == 1) {
			getSession().flush();
			t = list.get(0);
		} else if (list.size() > 0) {
			throw new BusinessException("查询结果数:" + list.size() + "大于1");
		}
		return t;
	}

	/**
	 * 通过hql 查询语句查找HashMap对象
	 * 
	 * @param hql
	 * @return
	 */
	public Map<Object, Object> findHashMapbyHql(String hql) {

		Query query = getSession().createQuery(hql);
		List list = query.list();
		Map<Object, Object> map = new HashMap<Object, Object>();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Object[] tm = (Object[]) iterator.next();
			map.put(tm[0].toString(), tm[1].toString());
		}
		return map;

	}

	/**
	 * 通过sql更新记录
	 * 
	 * @param query
	 * @return
	 */
	public int updateBySql(final String query) {

		Query querys = getSession().createSQLQuery(query);
		return querys.executeUpdate();
	}

	/**
	 * 通过sql查询语句查找对象
	 * 
	 * @param sql
	 * @return
	 */
	public List<String> findListBySql(final String sql) {
		Query querys = getSession().createSQLQuery(sql);
		return querys.list();
	}

	/**
	 * 创建Criteria对象，有排序功能。
	 * 
	 * @param <T>
	 * @param entityClass
	 * @param isAsc
	 * @param criterions
	 * @return
	 */
	private <T> Criteria createCriteria(Class<T> entityClass, boolean isAsc,
			Criterion... criterions) {
		Criteria criteria = createCriteria(entityClass, criterions);
		if (isAsc) {
			criteria.addOrder(Order.asc("asc"));
		} else {
			criteria.addOrder(Order.desc("desc"));
		}
		return criteria;
	}

	/**
	 * 创建Criteria对象带属性比较
	 * 
	 * @param <T>
	 * @param entityClass
	 * @param criterions
	 * @return
	 */
	private <T> Criteria createCriteria(Class<T> entityClass,
			Criterion... criterions) {
		Criteria criteria = getSession().createCriteria(entityClass);
		for (Criterion c : criterions) {
			criteria.add(c);
		}
		return criteria;
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> findAll(final Class<T> entityClass) {
		Criteria criteria = createCriteria(entityClass);
		return criteria.list();
	}

	/**
	 * 创建单一Criteria对象
	 * 
	 * @param <T>
	 * @param entityClass
	 * @return
	 */
	private <T> Criteria createCriteria(Class<T> entityClass) {
		Criteria criteria = getSession().createCriteria(entityClass);
		return criteria;
	}

	/**
	 * 根据属性名和属性值查询. 有排序
	 * 
	 * @param <T>
	 * @param entityClass
	 * @param propertyName
	 * @param value
	 * @param isAsc
	 * @return
	 */
	public <T> List<T> findByPropertyIsOrder(Class<T> entityClass,
			String propertyName, Object value, boolean isAsc) {
		Assert.hasText(propertyName);
		return createCriteria(entityClass, isAsc,
				Restrictions.eq(propertyName, value)).list();
	}

	/**
	 * 根据属性名和属性值 查询 且要求对象唯一.
	 * 
	 * @return 符合条件的唯一对象.
	 */
	public <T> T findUniqueByProperty(Class<T> entityClass, String propertyName,
			Object value) {
		Assert.hasText(propertyName,"属性不能为空");
		Object res = createCriteria(entityClass,
				Restrictions.eq(propertyName, value)).uniqueResult();
		return (T) res;
	}


	/**
	 * 根据查询条件与参数列表创建Query对象
	 * 
	 * @param session
	 *            Hibernate会话
	 * @param hql
	 *            HQL语句
	 * @param objects
	 *            参数列表
	 * @return Query对象
	 */
	public Query createQuery(Session session, String hql, Object... objects) {
		Query query = session.createQuery(hql);
		if (objects != null) {
			for (int i = 0; i < objects.length; i++) {
				query.setParameter(i, objects[i]);
			}
		}
		return query;
	}

	/**
	 * 批量插入实体
	 * 
	 * @param entityList
	 * @return
	 */
	public <T> int batchInsertsEntitie(List<T> entityList) {
		int num = 0;
		for (int i = 0; i < entityList.size(); i++) {
			save(entityList.get(i));
			num++;
		}
		return num;
	}

	/**
	 * 根据实体名返回全部对象
	 * 
	 * @param <T>
	 * @param hql
	 * @param size
	 * @return
	 */
	/**
	 * 使用占位符的方式填充值 请注意：like对应的值格式："%"+username+"%" Hibernate Query
	 * 
	 * @param hql
	 * @param values
	 *            占位符号?对应的值，顺序必须一一对应 可以为空对象数组，但是不能为null
	 * @return 2008-07-19 add by liuyang
	 */
	public List<T> executeQuery(final String hql, final Object[] values) {
		Query query = getSession().createQuery(hql);
		// query.setCacheable(true);
		for (int i = 0; values != null && i < values.length; i++) {
			query.setParameter(i, values[i]);
		}

		return query.list();

	}

	/**
	 * 根据实体模版查找
	 * 
	 * @param entityName
	 * @param exampleEntity
	 * @return
	 */

	public List findByExample(final String entityName,
			final Object exampleEntity) {
		Assert.notNull(exampleEntity, "Example entity must not be null");
		Criteria executableCriteria = (entityName != null ? getSession()
				.createCriteria(entityName) : getSession().createCriteria(
				exampleEntity.getClass()));
		executableCriteria.add(Example.create(exampleEntity));
		return executableCriteria.list();
	}

	// 使用指定的检索标准获取满足标准的记录数
	public Integer getRowCount(DetachedCriteria criteria) {
		return ConvertUtils.getInt(((Criteria) criteria
				.setProjection(Projections.rowCount())).uniqueResult(), 0);
	}

	/**
	 * 查询指定实体的总记录数
	 * 
	 * @param clazz
	 * @return
	 */
	public int getCount(Class<T> clazz) {

		int count = DataAccessUtils.intResult(getSession().createQuery(
				"select count(*) from " + clazz.getName()).list());
		return count;
	}

	/**
	 * 获取分页记录CriteriaQuery 老方法final int allCounts =
	 * oConvertUtils.getInt(criteria
	 * .setProjection(Projections.rowCount()).uniqueResult(), 0);
	 * 
	 * @param cq
	 * @param isOffset
	 * @return
	 */
	public PageHelper findPageListByCq(final CriteriaQuery cq, final boolean isOffset) {

		Criteria criteria = cq.getDetachedCriteria().getExecutableCriteria(
				getSession());
		CriteriaImpl impl = (CriteriaImpl) criteria;
		// 先把Projection和OrderBy条件取出来,清空两者来执行Count操作
		Projection projection = impl.getProjection();
		final int allCounts = ((Long) criteria.setProjection(
				Projections.rowCount()).uniqueResult()).intValue();
		criteria.setProjection(projection);
		if (projection == null) {
			criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		}

		// 判断是否有排序字段
		if (!cq.getOrderMap().isEmpty()) {
			cq.setOrder(cq.getOrderMap());
		}
		int pageSize = cq.getSize();// 每页显示数
		int page = PagerUtil.getcurPageNo(allCounts, cq.getPage(),
				pageSize);// 当前页
		int offset = PagerUtil.getOffset(allCounts, page, pageSize);
		String toolBar = "";
		if (isOffset) {// 是否分页
			criteria.setFirstResult(offset);
			criteria.setMaxResults(cq.getSize());
			if (cq.getUseImage() == 1) {
				toolBar = PagerUtil.getBar(cq.getMyAction(), cq.getMyForm(),
						allCounts, page, pageSize, cq.getMap());
			} else {
				toolBar = PagerUtil.getBar(cq.getMyAction(), allCounts,
						page, pageSize, cq.getMap());
			}
		}
		return new PageHelper(criteria.list(), toolBar, offset, page,
				allCounts);
	}

	public DataGridData findDataGridData(final CriteriaQuery cq) {
		return  findDataGridData(cq,true);
	}

	/**
	 * 返回easyui datagrid DataGridReturn模型对象
	 * @return
	 */
	public DataGridData findDataGridData(final CriteriaQuery cq,
										 final boolean isOffset) {
		CriteriaImpl criteria = (CriteriaImpl) cq.getDetachedCriteria().getExecutableCriteria(
				getSession());
		// 先把Projection和OrderBy条件取出来,清空两者来执行Count操作
		Projection projection = criteria.getProjection();
		final int allCounts = ((Long) criteria.setProjection(
				Projections.rowCount()).uniqueResult()).intValue();
		criteria.setProjection(projection);
		if (projection == null) {
			criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		}
		if (!StringUtils.isEmpty(cq.getDataGridParam().getSort())) {
			cq.addOrder(cq.getDataGridParam().getSort(), cq.getDataGridParam().getOrder());
		}

		// 判断是否有排序字段
		if (!cq.getOrderMap().isEmpty()) {
			cq.setOrder(cq.getOrderMap());
		}
		int pageSize = cq.getSize();// 每页显示数
		int curPageNO = PagerUtil.getcurPageNo(allCounts, cq.getPage(),
				pageSize);// 当前页
		int offset = PagerUtil.getOffset(allCounts, curPageNO, pageSize);
		if (isOffset) {// 是否分页
			criteria.setFirstResult(offset);
			criteria.setMaxResults(cq.getSize());
		}
		List list = criteria.list();
		cq.getDataGridParam().setResults(list);
		cq.getDataGridParam().setTotal(allCounts);
		DataGridParam dataGridParam = cq.getDataGridParam();
		return TagUtil.getObject(dataGridParam);
	}

	/**
	 * 获取分页记录SqlQuery
	 * 
	 * @param hqlQuery
	 * @param isOffset
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageHelper findPageListBySql(final HqlQuery hqlQuery,
                                        final boolean isOffset) {

		Query query = getSession().createSQLQuery(hqlQuery.getQueryString());
		int allCounts = query.list().size();
		int curPageNO = hqlQuery.getCurPage();
		int offset = PagerUtil.getOffset(allCounts, curPageNO,
				hqlQuery.getPageSize());
		query.setFirstResult(offset);
		query.setMaxResults(hqlQuery.getPageSize());
		List list;
		if (isOffset) {
			list = BeanPropertyUtils.toEntityList(query.list(),
					hqlQuery.getClass1(), hqlQuery.getDataGridParam().getField()
							.split(","));
		} else {
			list = query.list();
		}
		return new PageHelper(list, offset, curPageNO, allCounts);
	}

	/**
	 * 获取分页记录HqlQuery
	 * 
	 * @param hqlQuery
	 * @param isOffset
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageHelper findPageListByHql(final HqlQuery hqlQuery,
                                        final boolean isOffset) {

		Query query = getSession().createQuery(hqlQuery.getQueryString());
		if (isOffset) {
			query.setParameters(hqlQuery.getParam(),
					(Type[]) hqlQuery.getTypes());
		}
		int allCounts = query.list().size();
		int curPageNO = hqlQuery.getCurPage();
		int offset = PagerUtil.getOffset(allCounts, curPageNO,
				hqlQuery.getPageSize());
		String toolBar = PagerUtil.getBar(hqlQuery.getAction(), allCounts,
				curPageNO, hqlQuery.getPageSize(), hqlQuery.getMap());
		query.setFirstResult(offset);
		query.setMaxResults(hqlQuery.getPageSize());
		return new PageHelper(query.list(), toolBar, offset, curPageNO, allCounts);
	}

	/**
	 * 根据CriteriaQuery获取List
	 * 
	 * @param cq
	 * @param isOffset
	 * @return
	 */
	public List<T> findListByCq(final CriteriaQuery cq, Boolean isOffset) {
		Criteria criteria = cq.getDetachedCriteria().getExecutableCriteria(
				getSession());
		// 判断是否有排序字段
		if (!cq.getOrderMap().isEmpty()) {
			cq.setOrder(cq.getOrderMap());
		}
		if (isOffset){
			criteria.setFirstResult((cq.getPage()-1)*cq.getSize());
			criteria.setMaxResults(cq.getSize());
		}
		return criteria.list();

	}

	@Autowired
	@Qualifier("jdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	@Autowired
	@Qualifier("namedParameterJdbcTemplate")
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	/**
	 * 使用指定的检索标准检索数据并分页返回数据
	 */
	public List<Map<String, Object>> findForJdbc(String dbType, String sql, int page, int rows) {
		// 封装分页SQL
		sql = JdbcDao.jeecgCreatePageSql(dbType,sql, page, rows);
		return this.jdbcTemplate.queryForList(sql);
	}

	/**
	 * 使用指定的检索标准检索数据并分页返回数据
	 * 
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public <T> List<T> queryForListObject(String dbType,String sql, int page, int rows,
			Class<T> clazz) {
		List<T> rsList = new ArrayList<T>();
		// 封装分页SQL
		sql = JdbcDao.jeecgCreatePageSql(dbType,sql, page, rows);
		List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sql);

		for (Map<String, Object> m : mapList) {
			try {
				T po = clazz.newInstance();
				BeanPropertyUtils.copyObjectToMap(po, m);
				rsList.add(po);
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}
		return rsList;
	}

	/**
	 * 使用指定的检索标准检索数据并分页返回数据-采用预处理方式
	 * 
	 * @param dbType
	 * @param sql
	 * @param page
	 * @return
	 * @throws DataAccessException
	 */
	public List<Map<String, Object>> queryForListMapByParameter(String dbType,String sql, int page,
			int rows, Object... args) {
		// 封装分页SQL
		sql = JdbcDao.jeecgCreatePageSql(dbType,sql, page, rows);
		return this.jdbcTemplate.queryForList(sql, args);
	}

	/**
	 * 使用指定的检索标准检索数据并分页返回数据For JDBC-采用预处理方式
	 *
	 */
	public Long queryForCount(String sql, Object... args) {
		return  this.jdbcTemplate.queryForObject(sql,args,Long.class);
	}

	public List<Map<String, Object>> queryForListMap(String sql, Object... args) {
		return this.jdbcTemplate.queryForList(sql, args);
	}

	public Integer executeSql(String sql, List<Object> param) {
		return this.jdbcTemplate.update(sql, param);
	}

	public Integer executeSql(String sql, Object... param) {
		return this.jdbcTemplate.update(sql, param);
	}

	public Integer executeSql(String sql, Map<String, Object> param) {
		return this.namedParameterJdbcTemplate.update(sql, param);
	}

	public Object executeSqlReturnKey(final String sql, Map<String, Object> param) {
		Object keyValue = null;
		KeyHolder keyHolder = new GeneratedKeyHolder(); 
		SqlParameterSource sqlp  = new MapSqlParameterSource(param);
		this.namedParameterJdbcTemplate.update(sql,sqlp, keyHolder);
		if(ConvertUtils.isNotEmpty(keyHolder.getKey())){
			keyValue = keyHolder.getKey().longValue();
		}
		return keyValue;
	}
	public Map<String, Object> queryForMap(String sql, Object... args) {
		try {
			return this.jdbcTemplate.queryForMap(sql, args);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Override
	public List<Map<String, Object>> queryForListMap(String sql, int page, int rows) {
		return null;
	}

	/**
	 * 通过hql 查询语句查找对象
	 * 
	 * @param <T>
	 * @param hql
	 * @return
	 */
	public <T> List<T> findByHql(String hql, Object[] params) {
		Query q = getSession().createQuery(hql);
		if (params != null && params.length > 0) {
			for (int i = 0; i < params.length; i++) {
				q.setParameter(i, params[i]);
			}
		}
		return q.list();
	}

	public <T> List<T> findByDetached(DetachedCriteria dc, int firstResult,
			int maxResult) {
		Criteria criteria = dc.getExecutableCriteria(getSession());
		criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		criteria.setFirstResult(firstResult);
		criteria.setMaxResults(maxResult);
		return criteria.list();
	}

	/**
	 * 离线查询
	 */
	public <T> List<T> findByDetached(DetachedCriteria dc) {
		return dc.getExecutableCriteria(getSession()).list();
	}
	
	/**
	 * 查询自动列表
	 * @param autocomplete
	 * @return
	 */
	public <T> List<T> findAutoList(Autocomplete autocomplete) {
		StringBuffer sb = new StringBuffer("");
		for (String searchField : autocomplete.getSearchField().split(",")) {
			sb.append("  or " + searchField + " like '%"
					+ autocomplete.getTrem() + "%' ");
		}
		String hql = "from " + autocomplete.getEntityName() + " where 1!=1 "
				+ sb.toString();
		return this.getSession().createQuery(hql)
				.setFirstResult(autocomplete.getCurPage() - 1)
				.setMaxResults(autocomplete.getMaxRows()).list();
	}

	
	
}
