package com.abocode.jfaster.core.repository.persistence.hibernate;


import com.abocode.jfaster.core.persistence.DBTable;
import com.abocode.jfaster.core.persistence.hibernate.qbc.CriteriaQuery;
import com.abocode.jfaster.core.persistence.hibernate.qbc.HqlQuery;
import com.abocode.jfaster.core.persistence.hibernate.qbc.PageHelper;
import com.abocode.jfaster.core.platform.view.interactions.easyui.Autocomplete;
import com.abocode.jfaster.core.repository.CommonRepository;
import com.abocode.jfaster.core.repository.DataGridData;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service("commonRepository")
@Transactional
public class CommonRepositoryImpl implements CommonRepository {
    @Autowired
    public CommonRepository commonRepository;

    /**
     * 获取所有数据库表
     *
     * @return
     */
    @Override
    public List<DBTable> findAllDbTableName() {
        return commonRepository.findAllDbTableName();
    }

    @Override
    public Integer findAllDbTableSize() {
        return commonRepository.findAllDbTableSize();
    }

    @Override
    public <T> Serializable save(T entity) {
        return commonRepository.save(entity);
    }


    @Override
    public <T> void saveOrUpdate(T entity) {
        commonRepository.saveOrUpdate(entity);

    }


    @Override
    public <T> void delete(T entity) {
        commonRepository.delete(entity);

    }

    /**
     * 删除实体集合
     *
     * @param <T>
     * @param entities
     */
    @Override
    public <T> void deleteEntities(Collection<T> entities) {
        commonRepository.deleteEntities(entities);
    }

    /**
     * 根据实体名获取对象
     */
    @Override
    public <T> T find(Class<T> class1, Serializable id) {
        return  commonRepository.find(class1, id);
    }


    /**
     * 根据实体名获取对象
     */
    @Override
    public <T> T findEntity(Class<T> entityName, Serializable id) {
        return  commonRepository.findEntity(entityName, id);
    }

    /**
     * 根据实体名称和字段名称和字段值获取唯一记录
     *
     * @param <T>
     * @param entityClass
     * @param propertyName
     * @param value
     * @return
     */
    @Override
    public <T> T findUniqueByProperty(Class<T> entityClass,
                                      String propertyName, Object value) {
        return commonRepository.findUniqueByProperty(entityClass, propertyName, value);
    }

    /**
     * 按属性查找对象列表.
     */
    @Override
    public <T> List<T> findAllByProperty(Class<T> entityClass,
                                         String propertyName, Object value) {
        return commonRepository.findAllByProperty(entityClass, propertyName, value);
    }

    /**
     * 加载全部实体
     *
     * @param <T>
     * @param entityClass
     * @return
     */
    @Override
    public <T> List<T> findAll(final Class<T> entityClass) {
        return commonRepository.findAll(entityClass);
    }

    @Override
    public Integer executeHql(String hql) {
        return commonRepository.executeHql(hql);
    }

    /**
     * 根据hql查询一个
     */

    @Override
    public <T> T findUniqueByHql(String hql) {
        return (T) commonRepository.findUniqueByHql(hql);
    }

    /**
     * 删除实体主键ID删除对象
     *
     * @param entities
     */
    @Override
    public  void delete(Class entities, Serializable id) {
        commonRepository.delete(entities, id);
    }

    /**
     * 更新指定的实体
     *
     * @param <T>
     * @param pojo
     */
    @Override
    public <T> void update(T pojo) {
        commonRepository.update(pojo);

    }

    /**
     * 通过hql 查询语句查找对象
     *
     * @param <T>
     * @param hql
     * @return
     */
    @Override
    public <T> List<T> findByHql(String hql) {
        return commonRepository.findByHql(hql);
    }

    /**
     * 通过属性称获取实体带排序
     *
     * @param <T>
     * @param clazz
     * @return
     */
    @Override
    public <T> List<T> findByPropertyIsOrder(Class<T> clazz,
                                             String propertyName, Object value, boolean isAsc) {
        return commonRepository.findByPropertyIsOrder(clazz, propertyName,
                value, isAsc);
    }

    /**
     * cq方式分页
     *
     * @param cq
     * @param isOffset
     * @return
     */
    @Override
    public PageHelper findPageListByCq(final CriteriaQuery cq, final boolean isOffset) {
        return commonRepository.findPageListByCq(cq, isOffset);
    }


    /**
     * hqlQuery方式分页
     *
     * @param hql
     * @param isOffset
     * @return
     */
    @Override
    public PageHelper findPageListByHql(final HqlQuery hql,
                                        final boolean isOffset) {
        return commonRepository.findPageListByHql(hql, isOffset);
    }

    /**
     * 通过cq获取全部实体
     *
     * @param <T>
     * @param cq
     * @return
     */
    @Override
    public <T> List<T> findListByCq( CriteriaQuery cq,
                                    boolean pageable) {
        return commonRepository.findListByCq(cq, pageable);
    }


    /**
     * 查询
     * 根据实体查询
     */
    @Override
    @SuppressWarnings("rawtypes")
    public List findByExample(final String entityName,
                              final Object exampleEntity) {
        return commonRepository.findByExample(entityName, exampleEntity);
    }

    /**
     * 获取自动完成列表
     *
     * @param <T>
     * @return
     */
    @Override
    public <T> List<T> findAutoList(Autocomplete autocomplete) {
        return commonRepository.findAutoList(autocomplete);
    }

    /**
     * 通过hql 查询语句查找对象
     *
     * @param <T>
     * @param hql
     * @return
     */
    @Override
    public <T> List<T> findByHql(String hql, Object[] params) {
        return this.commonRepository.findByHql(hql, params);
    }

    @Override
    public Integer executeSql(String sql, List<Object> param) {
        return commonRepository.executeSql(sql, param);
    }


    @Override
    public Integer executeSql(String sql, Object... param) {
        return commonRepository.executeSql(sql, param);
    }


    @Override
    public Integer executeSql(String sql, Map<String, Object> param) {
        return commonRepository.executeSql(sql, param);
    }

    @Override
    public Object executeSqlReturnKey(String sql, Map<String, Object> param) {
        return commonRepository.executeSqlReturnKey(sql, param);
    }

    @Override
    public List<Map<String, Object>> queryForListMap(String sql, int page, int rows) {
        return commonRepository.queryForListMap(sql, page, rows);
    }


    @Override
    public List<Map<String, Object>> queryForListMap(String sql, Object... args) {
        return commonRepository.queryForListMap(sql, args);
    }


    @Override
    public List<Map<String, Object>> queryForListMapByParameter(String dbType, String sql, int page,
                                                                int rows, Object... args) {
        return commonRepository.queryForListMapByParameter(dbType, sql, page, rows, args);
    }


    @Override
    public <T> List<T> queryForListObject(String dbType, String sql, int page, int rows,
                                          Class<T> clazz) {
        return commonRepository.queryForListObject(dbType, sql, page, rows, clazz);
    }


    @Override
    public Map<String, Object> queryForMap(String sql, Object... args) {
        return commonRepository.queryForMap(sql, args);
    }

    @Override
    public Long queryForCount(String sql, Object... args) {
        return commonRepository.queryForCount(sql, args);
    }

    @Override
    public <T> void batchSave(List<T> entities) {
        this.commonRepository.batchSave(entities);
    }


    @Override
    public <T> List<T> findByDetached(DetachedCriteria dc, int firstResult,
                                      int maxResult) {
        return this.commonRepository.findByDetached(dc, firstResult, maxResult);
    }

    @Override
    public <T> List<T> findByDetached(DetachedCriteria dc) {
        return this.commonRepository.findByDetached(dc);
    }


    /**** sql 查询 */

    /**
     * 根据sql更新
     *
     * @param sql
     * @return
     */
    @Override
    public int updateBySql(String sql) {
        return commonRepository.updateBySql(sql);
    }

    /**
     * sqlQuery方式分页
     *
     * @param hqlQuery
     * @param isOffset
     * @return
     */
    @Override
    public PageHelper findPageListBySql(final HqlQuery hqlQuery,
                                        final boolean isOffset) {
        return commonRepository.findPageListBySql(hqlQuery, isOffset);
    }

    @Override
    public Session getSession() {
        return commonRepository.getSession();
    }

    @Override
    public DataGridData findDataGridData(final CriteriaQuery cq) {
        return  commonRepository.findDataGridData(cq);
    }


    /**
     * 返回easyui datagrid模型
     *
     * @param cq
     * @param isOffset
     * @return
     */
    @Override
    public DataGridData findDataGridData(final CriteriaQuery cq,
                                         final boolean isOffset) {
        return  commonRepository.findDataGridData(cq, isOffset);
    }

    /**
     * 查询数据返回对象
     */
    @Override
    public List<String> findListBySql(String sql) {
        return commonRepository.findListBySql(sql);
    }

}
