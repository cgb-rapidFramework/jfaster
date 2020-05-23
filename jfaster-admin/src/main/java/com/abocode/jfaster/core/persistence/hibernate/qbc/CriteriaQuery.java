package com.abocode.jfaster.core.persistence.hibernate.qbc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.abocode.jfaster.core.common.model.json.DataGrid;
import com.abocode.jfaster.core.platform.view.interactions.datatable.DataTables;
import com.abocode.jfaster.core.platform.view.interactions.datatable.SortDirection;
import com.abocode.jfaster.core.platform.view.interactions.datatable.SortInfo;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.Type;
import org.springframework.util.StringUtils;

public class CriteriaQuery{
    /**
     * 当前页
     */
    private int curPage = 1;
    /***
     * 默认一页条数
     */
    private int pageSize = 10;
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
    private CriterionList jqcriterionList = new CriterionList();
    /***
     *  翻页工具条样式
     */
    private int isUseimage = 0;
    private DetachedCriteria detachedCriteria;
    private static Map<String, Object> map= new HashMap<String, Object>();
    /***
     * 排序字段
     */
    private  Map<String, Object> orderMap= new HashMap<String, Object>();

    /***
     * 对同一字段进行第二次重命名查询时值设置FASLE不保存重命名查询条件
     */
    private boolean flag = true;
    /***
     * 查询需要显示的字段
     */
    private String field = "";
    private Class entityClass;
    /***
     * 结果集
     */
    private List results;
    private int total;
    /****
     * 保存创建的aliasName 防止重复创建
     */
    private List<String> alias = new ArrayList<String>();

    public List getResults() {
        return results;
    }

    public void setResults(List results) {
        this.results = results;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    private DataGrid dataGrid;
    private DataTables dataTables;

    public DataTables getDataTables() {
        return dataTables;
    }

    public void setDataTables(DataTables dataTables) {
        this.dataTables = dataTables;
    }

    public DataGrid getDataGrid() {
        return dataGrid;
    }

    public void setDataGrid(DataGrid dataGrid) {
        this.dataGrid = dataGrid;
    }

    public Class getEntityClass() {
        return entityClass;
    }

    public void setEntityClass(Class entityClass) {
        this.entityClass = entityClass;
    }

    public CriterionList getJqcriterionList() {
        return jqcriterionList;
    }

    public void setJqcriterionList(CriterionList jqcriterionList) {
        this.jqcriterionList = jqcriterionList;
    }

    public CriteriaQuery(Class c) {
        this.detachedCriteria = DetachedCriteria.forClass(c);
    }

    public CriteriaQuery(Class c, int curPage, String myAction, String myForm) {
        this.curPage = curPage;
        this.myAction = myAction;
        this.myForm = myForm;
        this.detachedCriteria = DetachedCriteria.forClass(c);
    }

    public CriteriaQuery(Class c, int curPage, String myAction) {
        this.myAction = myAction;
        this.curPage = curPage;
        this.detachedCriteria = DetachedCriteria.forClass(c);
    }

    public CriteriaQuery(Class entityClass, int curPage) {
        this.curPage = curPage;
        this.detachedCriteria = DetachedCriteria.forClass(entityClass);
    }

    public CriteriaQuery(Class entityClass, DataGrid dg) {
        this.curPage = dg.getPage();
        this.detachedCriteria = DetachedCriteria.forClass(entityClass);
        this.field = dg.getField();
        this.entityClass = entityClass;
        this.dataGrid = dg;
        this.pageSize = dg.getRows();
    }

    public CriteriaQuery(Class entityClass, DataTables dataTables) {
        this.curPage = dataTables.getDisplayStart();
        String[] fieldstring = dataTables.getsColumns().split(",");
        this.detachedCriteria = DetachedCriteriaUtil
                .createDetachedCriteria(entityClass, "start", "_table", fieldstring);
        //this.detachedCriteria = DetachedCriteria.forClass(c);
        this.field = dataTables.getsColumns();
        this.entityClass = entityClass;
        this.dataTables = dataTables;
        this.pageSize = dataTables.getDisplayLength();
        addJqCriteria(dataTables);
    }

    public CriteriaQuery(Class c, int pageSize, int curPage,
                         String myAction, String myForm) {
        this.pageSize = pageSize;
        this.curPage = curPage;
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
     * 加载dataTables 默认查询条件
     *
     * @param dataTables
     */
    public void addJqCriteria(DataTables dataTables) {
        String search = dataTables.getSearch();//查询关键字
        SortInfo[] sortInfo = dataTables.getSortColumns();//排序字段
        String[] sColumns = dataTables.getsColumns().split(",");//字段
        if (!StringUtils.isEmpty(search)) {
            for (String string : sColumns) {
                if (string.indexOf("_") == -1) {
                    jqcriterionList.addPara(Restrictions.like(string, "%" + search
                            + "%"));
                }
            }
            add(getOrCriterion(jqcriterionList));

        }
        if (sortInfo.length > 0) {
            for (SortInfo sortInfo2 : sortInfo) {
                addOrder("" + sColumns[sortInfo2.getColumnId()] + "", sortInfo2.getSortOrder());
            }
        }
    }

    public void createCriteria(String name) {
        detachedCriteria.createCriteria(name);
    }

    public void createCriteria(String name, String value) {
        detachedCriteria.createCriteria(name, value);
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

    public void setResultTransformer(Class class1) {
        detachedCriteria.setResultTransformer(Transformers.aliasToBean(class1));
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
    public Criterion and(CriteriaQuery query, int source, int dest) {
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
    public Criterion and(Criterion c, CriteriaQuery query, int souce) {
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
            c3 = getor(c1, c2);
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
    public Criterion getor(Criterion c1, Criterion c2) {
        return Restrictions.or(c1, c2);
    }


    /**
     * 设置条件之间and关系
     *
     * @param c1
     * @param c2
     * @return
     */
    public Criterion and(Criterion c1, Criterion c2)

    {
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
    public Criterion or(CriteriaQuery query, int source, int dest) {
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
    public Criterion or(Criterion c, CriteriaQuery query, int source) {
        return Restrictions.or(c, query.getCriterionList().getParas(source));
    }

    /**
     * 设置or(Criterion c1, Criterion c2)（或）查询条件
     *
     * @param c1
     * @param c2 两个条件或查询： Restrictions.or(Restrictions.in("username",list1),
     *           Restrictions.idEq(1)); 三个或多个条件查询:（使用嵌套方式）
     *           criteria.add(Restrictions
     *           .or(Restrictions.in("username",list1),
     *           Restrictions.or(Restrictions.idEq(3), Restrictions.idEq(4))));
     */
    public void or(Criterion c1, Criterion c2) {
        this.detachedCriteria.add(Restrictions.or(c1, c2));
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
            if (SortDirection.asc.equals(entry.getValue())) {
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
     * @param keyname  :字段名
     * @param keyvalue ：字段值
     */
    public void eq(String keyname, Object keyvalue) {
        if (!StringUtils.isEmpty(keyvalue)) {
            criterionList.addPara(Restrictions.eq(keyname, keyvalue));
            if (flag) {
                this.put(keyname, keyvalue);
            }
            flag = true;
        }
    }

    /**
     * 设置notEq(不等)查询条件
     *
     * @param keyname
     * @param keyvalue
     */
    public void notEq(String keyname, Object keyvalue) {
        if (!StringUtils.isEmpty(keyvalue)) {
            criterionList.addPara(Restrictions.ne(keyname, keyvalue));
            if (flag) {
                this.put(keyname, keyvalue);
            }
            flag = true;
        }
    }

    /**
     * 设置like(模糊)查询条件
     *
     * @param keyname
     * @param keyvalue
     */
    public void like(String keyname, Object keyvalue) {
        if (!StringUtils.isEmpty(keyvalue)) {
//			criterionList.addPara(Restrictions.like(keyname, "%" + keyvalue+ "%"));
//			criterionList.addPara(Restrictions.like(keyname, keyvalue));
//			Criterion criterion=Restrictions.like(keyname,keyvalue+"",MatchMode.ANYWHERE);
//			Criterion criterion=Restrictions.ilike(keyname, "%" + keyvalue+ "%");
            Criterion criterion = Restrictions.ilike(keyname, keyvalue);
            criterionList.addPara(criterion);
            if (flag) {
                this.put(keyname, keyvalue);
            }
            flag = true;
        }
    }

    /**
     * 设置gt(>)查询条件
     *
     * @param keyname
     * @param keyvalue
     */
    public void gt(String keyname, Object keyvalue) {
        if (!StringUtils.isEmpty(keyvalue)) {
            criterionList.addPara(Restrictions.gt(keyname, keyvalue));
            if (flag) {
                this.put(keyname, keyvalue);
            }
            flag = true;
        }
    }

    /**
     * 设置lt(<)查询条件
     *
     * @param keyname
     * @param keyvalue
     */
    public void lt(String keyname, Object keyvalue) {
        if (!StringUtils.isEmpty(keyvalue)) {
            criterionList.addPara(Restrictions.lt(keyname, keyvalue));
            if (flag) {
                this.put(keyname, keyvalue);
            }
            flag = true;
        }
    }

    /**
     * 设置le(<=)查询条件
     *
     * @param keyname
     * @param keyvalue
     */
    public void le(String keyname, Object keyvalue) {
        if (!StringUtils.isEmpty(keyvalue)) {
            criterionList.addPara(Restrictions.le(keyname, keyvalue));
            if (flag) {
                this.put(keyname, keyvalue);
            }
            flag = true;
        }
    }

    /**
     * 设置ge(>=)查询条件
     *
     * @param keyname
     * @param keyvalue
     */
    public void ge(String keyname, Object keyvalue) {
        if (!StringUtils.isEmpty(keyvalue)) {
            criterionList.addPara(Restrictions.ge(keyname, keyvalue));
            if (flag) {
                this.put(keyname, keyvalue);
            }
            flag = true;
        }
    }

    /**
     * 设置in(包含)查询条件
     *
     * @param keyname
     * @param keyname
     * @param keyvalue
     */
    public void in(String keyname, Object[] keyvalue) {
        if (!StringUtils.isEmpty(keyvalue)) {
            criterionList.addPara(Restrictions.in(keyname, keyvalue));
        }
    }

    /**
     * 设置isNull查询条件
     *
     * @param keyname
     */
    public void isNull(String keyname) {
        criterionList.addPara(Restrictions.isNull(keyname));
    }

    /**
     * 设置isNull查询条件
     *
     * @param keyname
     */
    public void isNotNull(String keyname) {
        criterionList.addPara(Restrictions.isNotNull(keyname));
    }

    /**
     * 保存查询条件
     *
     * @param keyname
     * @param keyvalue
     */
    public void put(String keyname, Object keyvalue) {
        if (!StringUtils.isEmpty(keyvalue)) {
            map.put(keyname, keyvalue);
        }
    }

    /**
     * 设置between(之间)查询条件
     *
     * @param keyname
     * @param keyValue1
     * @param keyValue2
     */
    public void between(String keyname, Object keyValue1, Object keyValue2) {
        Criterion c = null;
        if (keyValue1 != null && keyValue2 != null) {
            c = Restrictions.between(keyname, keyValue1, keyValue2);
        } else if (keyValue1 != null) {
            c = Restrictions.ge(keyname, keyValue1);
        } else if (keyValue2 != null) {
            c = Restrictions.le(keyname, keyValue2);
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

    public Integer getCurPage() {
        return curPage;
    }

    public void setCurPage(Integer curPage) {
        this.curPage = curPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    /**
     * 设置分页显示数
     *
     * @param pageSize
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getMyAction() {
        return myAction;
    }

    public void setMyAction(String myAction) {
        this.myAction = myAction;
    }

    public String getMyForm() {
        return myForm;
    }

    public void setMyForm(String myForm) {
        this.myForm = myForm;
    }

    public CriterionList getCriterionList() {
        return criterionList;
    }

    public void setCriterionList(CriterionList criterionList) {
        this.criterionList = criterionList;
    }

    public DetachedCriteria getDetachedCriteria() {
        return detachedCriteria;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public void setDetachedCriteria(DetachedCriteria detachedCriteria) {
        this.detachedCriteria = detachedCriteria;
    }

    public int getIsUseimage() {
        return isUseimage;
    }

    /**
     * 设置工具条样式（0:不带图片：1带图片）
     *
     * @param isUseimage
     */
    public void setIsUseimage(int isUseimage) {
        this.isUseimage = isUseimage;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

    public boolean isFlag() {
        return flag;
    }

    /**
     * 对同一字段进行第二次重命名查询时值设置FASLE不保存重命名查询条件
     *
     * @param flag
     */
    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public Map<String, Object> getOrderMap() {
        return orderMap;
    }
}