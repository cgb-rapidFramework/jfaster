package org.jeecgframework.web.system.controller;

import org.apache.log4j.Logger;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.ComboTree;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.common.model.json.TreeGrid;
import org.jeecgframework.core.tag.vo.easyui.ComboTreeModel;
import org.jeecgframework.core.tag.vo.easyui.TreeGridModel;
import org.jeecgframework.core.util.BeanPropertyUtils;
import org.jeecgframework.platform.constant.Globals;
import org.jeecgframework.platform.util.MutiLangUtils;
import org.jeecgframework.web.common.hqlsearch.HqlGenerateUtil;
import org.jeecgframework.web.system.entity.TSCategoryEntity;
import org.jeecgframework.web.system.entity.TSIcon;
import org.jeecgframework.web.system.service.CategoryService;
import org.jeecgframework.web.system.service.ResourceService;
import org.jeecgframework.web.system.service.SystemService;
import org.jeecgframework.web.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author JueYue
 * @version V1.0
 * @Title: Controller
 * @Description: 分类管理
 * @date 2014-09-16 21:50:55
 */
@Controller
@RequestMapping("/categoryController")
public class CategoryController extends BaseController {
    private static final Logger logger = Logger
            .getLogger(CategoryController.class);

    private static final String CATEGORY_LIST = "system/category/categoryList";
    private static final String CATEGORY_ADD_OR_UPDATE = "system/category/category";

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private SystemService systemService;

    /**
     * 分类管理列表 页面跳转
     *
     * @return
     */
    @RequestMapping(params = "category")
    public String category(HttpServletRequest request) {
        return CATEGORY_LIST;
    }

    /**
     * easyui AJAX请求数据
     *
     * @param request
     * @param dataGrid
     */

    @SuppressWarnings("unchecked")
    @RequestMapping(params = "datagrid")
    @ResponseBody
    public List<TreeGrid> datagrid(TSCategoryEntity category,
                                   HttpServletRequest request,
                                   DataGrid dataGrid) {
        CriteriaQuery cq = new CriteriaQuery(TSCategoryEntity.class, dataGrid);
        if (category.getId() == null || StringUtils.isEmpty(category.getId())) {
            cq.isNull("parent");
        } else {
            cq.eq("parent.code", category.getId());
            category.setId(null);
        }
        // 查询条件组装器
        HqlGenerateUtil.installHql(cq,
                category, request.getParameterMap());
        List<TSCategoryEntity> list = this.categoryService
                .findListByCq(cq, false);
        TreeGridModel treeGridModel = new TreeGridModel();
        treeGridModel.setIdField("code");
        treeGridModel.setSrc("id");
        treeGridModel.setTextField("name");
        treeGridModel.setIcon("icon_iconPath");
        treeGridModel.setParentText("parent_name");
        treeGridModel.setParentId("parent_code");
        treeGridModel.setChildList("list");
        List<TreeGrid> treeGrids = resourceService.treegrid(list, treeGridModel);
        return treeGrids;
    }

    /**
     * 删除分类管理
     *
     * @return
     */
    @RequestMapping(params = "del")
    @ResponseBody
    public AjaxJson del(TSCategoryEntity tSCategory, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        tSCategory = systemService.findEntity(TSCategoryEntity.class,
                tSCategory.getId());
        j.setMsg("分类管理删除成功");
        categoryService.delete(tSCategory);
        systemService.addLog(j.getMsg(), Globals.Log_Type_DEL,
                Globals.Log_Leavel_INFO);
        return j;
    }

    /**
     * 添加分类管理
     *
     * @return
     */
    @RequestMapping(params = "save")
    @ResponseBody
    public AjaxJson save(TSCategoryEntity category, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        if (StringUtils.isNotEmpty(category.getId())) {
            j.setMsg("分类管理更新成功");
            try {
                TSCategoryEntity t = categoryService.find(TSCategoryEntity.class,
                        category.getId());
                BeanPropertyUtils.copyBeanNotNull2Bean(category, t);
                categoryService.saveOrUpdate(t);
                systemService.addLog(j.getMsg(), Globals.Log_Type_UPDATE,
                        Globals.Log_Leavel_INFO);
            } catch (Exception e) {
                logger.error(e.getMessage(), e.fillInStackTrace());
                j.setMsg("分类管理更新失败");
            }
        } else {
            j.setMsg("分类管理添加成功");
            categoryService.saveCategory(category);
            systemService.addLog(j.getMsg(), Globals.Log_Type_INSERT,
                    Globals.Log_Leavel_INFO);
        }
        return j;
    }

    /**
     * 分类管理列表页面跳转
     *
     * @return
     */
    @RequestMapping(params = "addorupdate")
    public String addorupdate(ModelMap map, TSCategoryEntity category) {
        if (StringUtils.isNotEmpty(category.getId())) {
            category = categoryService.findUniqueByProperty(TSCategoryEntity.class,
                    "code", category.getId());
            map.put("categoryPage", category);
        }
        map.put("iconlist", systemService.findAllByProperty(TSIcon.class,
                "iconType", (short) 1));
        if (category.getParent() != null
                && StringUtils.isNotEmpty(category.getParent().getId())) {
            TSCategoryEntity parent = categoryService.findEntity(
                    TSCategoryEntity.class, category.getParent().getId());
            category.setParent(parent);
            map.put("categoryPage", category);
        }
        return CATEGORY_ADD_OR_UPDATE;
    }

    @RequestMapping(params = "combotree")
    @ResponseBody
    public List<ComboTree> combotree(String selfCode, ComboTree comboTree) {
        CriteriaQuery cq = new CriteriaQuery(TSCategoryEntity.class);
        this.getCriteriaQuery(cq, selfCode, comboTree);
        List<TSCategoryEntity> categoryList = systemService
                .findListByCq(cq, false);
        ComboTreeModel comboTreeModel = new ComboTreeModel("code", "name", "list");
        List<ComboTree> comboTrees = resourceService.ComboTree(categoryList, comboTreeModel,
                null, false);
        MutiLangUtils.setMutiTree(comboTrees);
        return comboTrees;
    }

    /**
     * 鉴于树的问题,这里自己加载全部数据来做同步树
     *
     * @param comboTree
     * @return
     */
    @RequestMapping(params = "tree")
    @ResponseBody
    public List<ComboTree> tree(String selfCode, ComboTree comboTree, boolean isNew) {
        CriteriaQuery cq = new CriteriaQuery(TSCategoryEntity.class);
        this.getCriteriaQuery(cq, selfCode, comboTree);
        List<TSCategoryEntity> categoryList = systemService
                .findListByCq(cq, false);
        List<ComboTree> comboTrees = new ArrayList<ComboTree>();
        for (int i = 0; i < categoryList.size(); i++) {
            comboTrees.add(categoryConvertToTree(categoryList.get(i)));
        }
        return comboTrees;
    }

    /***
     * categoryConvertToTree
     *
     * @param entity
     * @return
     */
    private ComboTree categoryConvertToTree(TSCategoryEntity entity) {
        ComboTree tree = new ComboTree();
        tree.setId(entity.getCode());
        tree.setText(entity.getName());
        tree.setIconCls(entity.getIcon().getIconClas());
        if (entity.getList() != null && entity.getList().size() > 0) {
            List<ComboTree> comboTrees = new ArrayList<ComboTree>();
            for (int i = 0; i < entity.getList().size(); i++) {
                comboTrees.add(categoryConvertToTree(entity.getList().get(
                        i)));
            }
            tree.setChildren(comboTrees);
        }
        return tree;
    }

    /**
     * 获取查询cq
     *
     * @param cq
     * @param selfCode
     * @param comboTree
     */
    private void getCriteriaQuery(CriteriaQuery cq, String selfCode, ComboTree comboTree) {
        if (StringUtils.isNotEmpty(comboTree.getId())) {
            cq.createAlias("parent", "parent");
            cq.eq("parent.code", comboTree.getId());
        } else if (StringUtils.isNotEmpty(selfCode)) {
            cq.eq("code", selfCode);
        } else {
            cq.isNull("parent");
        }
        cq.add();
    }
}
