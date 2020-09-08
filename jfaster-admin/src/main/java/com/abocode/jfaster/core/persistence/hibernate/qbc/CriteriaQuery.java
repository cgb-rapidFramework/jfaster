package com.abocode.jfaster.core.persistence.hibernate.qbc;

import com.abocode.jfaster.core.persistence.hibernate.hql.HqlGenerateUtil;
import com.abocode.jfaster.core.repository.DataGridParam;
import com.abocode.jfaster.core.repository.SortDirection;
import lombok.Data;
import org.hibernate.criterion.*;
import org.hibernate.type.Type;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class CriteriaQuery<T> {
    /**
     * 当前页
     */
    private int page = 1;
    /***
     * 默认一页条数
     */
    private int size = 10;
    /***
     *请求的action 地址
     */
    private String myAction;
    /***
     *  form 名字
     */
    private String myForm;
    /***
     * 自定义查询条件集合
     */
    private CriterionList criterionList = new CriterionList();
    /***
     * jquery datatable控件生成查询条件集合
     */
    private CriterionList jqueryCriterionList = new CriterionList();
    /***
     *  翻页工具条样式
     */
    private int useImage = 0;
    private DetachedCriteria detachedCriteria;
    private static Map<String, Object> map = new HashMap<>();
    /***
     * 排序字段
     */
    private Map<String, Object> orderMap = new HashMap<>();

    /***
     * 对同一字段进行第二次重命名查询时值设置FASLE不保存重命名查询条件
     */
    private boolean flag = true;
    /***
     * 查询需要显示的字段
     */
    private String field = "";
    private Class<T> entityClass;
    /***
     * 结果集
     */
    private List<T> results;
    private int total;
    private DataGridParam dataGridParam;
    /****
     * 保存创建的aliasName 防止重复创建
     */
    private List<String> alias = new ArrayList<>();

    public CriteriaQuery(Class<T> c) {
        this.detachedCriteria = DetachedCriteria.forClass(c);
    }

    public CriteriaQuery(Class<T> c, int page, String myAction, String myForm) {
        this.page = page;
        this.myAction = myAction;
        this.myForm = myForm;
        this.detachedCriteria = DetachedCriteria.forClass(c);
    }

    public CriteriaQuery(Class<T> c, int page, String myAction) {
        this.myAction = myAction;
        this.page = page;
        this.detachedCriteria = DetachedCriteria.forClass(c);
    }

    public CriteriaQuery(Class<T> entityClass, int page) {
        this.page = page;
        this.detachedCriteria = DetachedCriteria.forClass(entityClass);
    }

    public CriteriaQuery(Class<T> c, int size, int page,
                         String myAction, String myForm) {
        this.size = size;
        this.page = page;
        this.myAction = myAction;
        this.myForm = myForm;
        this.detachedCriteria = DetachedCriteria.forClass(c);
    }

    /**
     * 加载条件(条件之间有关联) hql((this_.0 like ? and this_.1 like ?) or this_.2 like ?)
     * 表示法cq.add(cq.or(cq.and(cq, 0, 1), cq, 2))----- hql2:(this_.0 like ? or
     * this_.1 like ?) 表示法:cq.add(cq.or(cq, 0, 1));
     * 例子：cq.in("TBPrjstatus.code", status);
     * cq.eq("attn", user.getUsername());
     * cq.isNull("attn");
     * cq.add(cq.and(cq.or(cq, 1, 2), cq, 0));
     */
    public void add(Criterion c) {
        detachedCriteria.add(c);
    }

    /**
     * 加载条件
     */
    public void add() {
        for (int i = 0; i < getCriterionList().size(); i++) {
            add(getCriterionList().getParas(i));
        }
        getCriterionList().removeAll(getCriterionList());
    }

    /**
     * 创建外键表关联对象
     *
     * @param name  外键表实体名
     * @param value 引用名
     */
    public void createAlias(String name, String value) {
        if (!alias.contains(name)) {
            detachedCriteria.createAlias(name, value);
            alias.add(name);
        }
    }


    public void setProjection(Property property) {
        detachedCriteria.setProjection(property);
    }

    /**
     * 设置条件之间and关系
     *
     * @param query
     * @param source
     * @param dest   hql((this_.0 like ? and this_.1 like ?) or this_.2 like ?)
     *               表示法cq.add(cq.or(cq.and(cq, 0, 1), cq, 2))
     * @return
     */
    public Criterion and(CriteriaQuery<T> query, int source, int dest) {
        return Restrictions.and(query.getCriterionList().getParas(source),
                query.getCriterionList().getParas(dest));
    }

    /**
     * 设置条件之间and关系
     *
     * @param query
     * @param c
     * @param souce hql:(this_.0 like ? or this_.1 like ?) 表示法:cq.add(cq.or(cq, 0,
     *              1));
     * @return
     */
    public Criterion and(Criterion c, CriteriaQuery<T> query, int souce) {
        return Restrictions.and(c, query.getCriterionList().getParas(souce));
    }

    /**
     * 根据CriterionList组合嵌套条件
     */
    public Criterion getOrCriterion(CriterionList list) {
        Criterion c1 = null;
        Criterion c2 = null;
        Criterion c3 = null;
        c1 = list.getParas(0);
        for (int i = 1; i < list.size(); i++) {
            c2 = list.getParas(i);
            c3 = or(c1, c2);
            c1 = c3;
        }
        return c3;
    }

    /**
     * 设置组合后的Criterion OR关系
     *
     * @param c1
     * @param c2
     * @return
     */
    public Criterion or(Criterion c1, Criterion c2) {
        return Restrictions.or(c1, c2);
    }


    /**
     * 设置条件之间and关系
     *
     * @param c1
     * @param c2
     * @return
     */
    public Criterion and(Criterion c1, Criterion c2) {
        return Restrictions.and(c1, c2);
    }

    /**
     * 设置Or查询
     *
     * @param query
     * @param source 条件1
     * @param dest   条件2
     * @return
     */
    public Criterion or(CriteriaQuery<T> query, int source, int dest) {
        return Restrictions.or(query.getCriterionList().getParas(source), query
                .getCriterionList().getParas(dest));
    }

    /**
     * 设置or(Criterion c, CriteriaQuery query, int source)（或）查询条件
     *
     * @param c
     * @param query  1
     * @param source
     */
    public Criterion or(Criterion c, CriteriaQuery<T> query, int source) {
        return Restrictions.or(c, query.getCriterionList().getParas(source));
    }

    /**
     * 设置order（排序）查询条件
     *
     * @param ordername  ：排序字段名
     * @param ordervalue ：排序字段值（"asc","desc"）
     */
    public void addOrder(String ordername, SortDirection ordervalue) {
        orderMap.put(ordername, ordervalue);

    }

    /**
     * 设置order（排序）查询条件
     *
     * @param map ：排序字段名
     *            ：排序字段值（"asc","desc"）
     */
    public void setOrder(Map<String, Object> map) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            judgecreateAlias(entry.getKey());
            if (SortDirection.ASC.equals(entry.getValue())) {
                detachedCriteria.addOrder(Order.asc(entry.getKey()));
            } else {
                detachedCriteria.addOrder(Order.desc(entry.getKey()));
            }
        }
    }

    /**
     * 创建 alias
     *
     * @param entitys 规则 entitys 为a.b.c 这种将会创建 alias a和alias  b而不会创建c
     *                因为这样更加容易传值
     */
    public void judgecreateAlias(String entitys) {
        String[] aliass = entitys.split("\\.");
        for (int i = 0; i < aliass.length - 1; i++) {
            createAlias(aliass[i], aliass[i]);
        }
    }

    /**
     * 设置eq(相等)查询条件
     *
     * @param keyName  :字段名
     * @param keyValue ：字段值
     */
    public void eq(String keyName, Object keyValue) {
        if (!StringUtils.isEmpty(keyValue)) {
            criterionList.addPara(Restrictions.eq(keyName, keyValue));
            if (flag) {
                this.put(keyName, keyValue);
            }
            flag = true;
        }
    }

    /**
     * 设置notEq(不等)查询条件
     *
     * @param keyName
     * @param keyValue
     */
    public void notEq(String keyName, Object keyValue) {
        if (!StringUtils.isEmpty(keyValue)) {
            criterionList.addPara(Restrictions.ne(keyName, keyValue));
            if (flag) {
                this.put(keyName, keyValue);
            }
            flag = true;
        }
    }

    /**
     * 设置like(模糊)查询条件
     *
     * @param keyName
     * @param keyValue
     */
    public void like(String keyName, Object keyValue) {
        if (!StringUtils.isEmpty(keyValue)) {
            Criterion criterion = Restrictions.ilike(keyName, keyValue);
            criterionList.addPara(criterion);
            if (flag) {
                this.put(keyName, keyValue);
            }
            flag = true;
        }
    }

    /**
     * 设置gt(>)查询条件
     *
     * @param keyName
     * @param keyValue
     */
    public void gt(String keyName, Object keyValue) {
        if (!StringUtils.isEmpty(keyValue)) {
            criterionList.addPara(Restrictions.gt(keyName, keyValue));
            if (flag) {
                this.put(keyName, keyValue);
            }
            flag = true;
        }
    }

    /**
     * 设置lt(<)查询条件
     *
     * @param keyName
     * @param keyValue
     */
    public void lt(String keyName, Object keyValue) {
        if (!StringUtils.isEmpty(keyValue)) {
            criterionList.addPara(Restrictions.lt(keyName, keyValue));
            if (flag) {
                this.put(keyName, keyValue);
            }
            flag = true;
        }
    }

    /**
     * 设置le(<=)查询条件
     *
     * @param keyName
     * @param keyValue
     */
    public void le(String keyName, Object keyValue) {
        if (!StringUtils.isEmpty(keyValue)) {
            criterionList.addPara(Restrictions.le(keyName, keyValue));
            if (flag) {
                this.put(keyName, keyValue);
            }
            flag = true;
        }
    }

    /**
     * 设置ge(>=)查询条件
     *
     * @param keyName
     * @param keyValue
     */
    public void ge(String keyName, Object keyValue) {
        if (!StringUtils.isEmpty(keyValue)) {
            criterionList.addPara(Restrictions.ge(keyName, keyValue));
            if (flag) {
                this.put(keyName, keyValue);
            }
            flag = true;
        }
    }

    /**
     * 设置in(包含)查询条件
     *
     * @param keyName
     * @param keyName
     * @param keyValue
     */
    public void in(String keyName, Object[] keyValue) {
        if (!StringUtils.isEmpty(keyValue)) {
            criterionList.addPara(Restrictions.in(keyName, keyValue));
        }
    }

    /**
     * 设置isNull查询条件
     *
     * @param keyName
     */
    public void isNull(String keyName) {
        criterionList.addPara(Restrictions.isNull(keyName));
    }

    /**
     * 设置isNull查询条件
     *
     * @param keyName
     */
    public void isNotNull(String keyName) {
        criterionList.addPara(Restrictions.isNotNull(keyName));
    }

    /**
     * 保存查询条件
     *
     * @param keyName
     * @param keyValue
     */
    public void put(String keyName, Object keyValue) {
        if (!StringUtils.isEmpty(keyValue)) {
            map.put(keyName, keyValue);
        }
    }

    /**
     * 设置between(之间)查询条件
     *
     * @param keyName
     * @param keyValue1
     * @param keyValue2
     */
    public void between(String keyName, Object keyValue1, Object keyValue2) {
        Criterion c = null;
        if (keyValue1 != null && keyValue2 != null) {
            c = Restrictions.between(keyName, keyValue1, keyValue2);
        } else if (keyValue1 != null) {
            c = Restrictions.ge(keyName, keyValue1);
        } else if (keyValue2 != null) {
            c = Restrictions.le(keyName, keyValue2);
        }
        criterionList.add(c);
    }

    public void sql(String sql) {
        Restrictions.sqlRestriction(sql);
    }

    public void sql(String sql, Object[] objects, Type[] type) {
        Restrictions.sqlRestriction(sql, objects, type);
    }

    public void sql(String sql, Object objects, Type type) {
        Restrictions.sqlRestriction(sql, objects, type);
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public CriteriaQuery<T> buildParameters(Object query, Map<String, String[]> parameterMap, DataGridParam dataGridParam) {
        this.page = dataGridParam.getPage();
        this.field = dataGridParam.getField();
        this.dataGridParam = dataGridParam;
        this.size = dataGridParam.getRows();
        HqlGenerateUtil.installHql(this, query, parameterMap);
        return this;
    }

    public CriteriaQuery<T> buildParameters(Object query, DataGridParam dataGridParam) {
        this.page = dataGridParam.getPage();
        this.field = dataGridParam.getField();
        this.dataGridParam = dataGridParam;
        this.size = dataGridParam.getRows();
        HqlGenerateUtil.installHql(this, query);
        return this;
    }

    public CriteriaQuery<T> buildDataGrid(DataGridParam dg) {
        this.page = dg.getPage();
        this.field = dg.getField();
        this.dataGridParam = dg;
        this.size = dg.getRows();
        return this;
    }
}
