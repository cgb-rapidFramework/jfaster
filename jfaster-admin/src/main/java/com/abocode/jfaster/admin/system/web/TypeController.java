package com.abocode.jfaster.admin.system.web;

import com.abocode.jfaster.admin.system.repository.LanguageRepository;
import com.abocode.jfaster.admin.system.repository.SystemRepository;
import com.abocode.jfaster.admin.system.repository.UserRepository;
import com.abocode.jfaster.core.common.constants.Globals;
import com.abocode.jfaster.core.common.model.json.AjaxJson;
import com.abocode.jfaster.core.repository.DataGridData;
import com.abocode.jfaster.core.repository.DataGridParam;
import com.abocode.jfaster.core.common.model.json.TreeGrid;
import com.abocode.jfaster.core.common.model.json.ValidForm;
import com.abocode.jfaster.core.common.util.ConvertUtils;
import com.abocode.jfaster.core.platform.utils.MutiLangUtils;
import com.abocode.jfaster.core.common.util.StrUtils;
import com.abocode.jfaster.core.persistence.hibernate.hqlsearch.ObjectParseUtil;
import com.abocode.jfaster.core.persistence.hibernate.hqlsearch.PageValueConvertRuleEnum;
import com.abocode.jfaster.core.persistence.hibernate.hqlsearch.vo.HqlRuleEnum;
import com.abocode.jfaster.core.persistence.hibernate.qbc.CriteriaQuery;
import com.abocode.jfaster.system.entity.Type;
import com.abocode.jfaster.system.entity.TypeGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Scope("prototype")
@Controller
@RequestMapping("/typeController")
public class TypeController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SystemRepository systemRepository;
    @Autowired
    private LanguageRepository languageRepository;

    /**
     * 类型字典列表页面跳转
     *
     * @return
     */
    @RequestMapping(params = "typeGroupTabs")
    public ModelAndView typeGroupTabs(HttpServletRequest request) {
        List<TypeGroup> typeGroupList = systemRepository.findAll(TypeGroup.class);
        request.setAttribute("typegroupList", typeGroupList);
        return new ModelAndView("system/type/typeGroupTabs");
    }

    /**
     * 类型分组列表页面跳转
     *
     * @return
     */
    @RequestMapping(params = "typeGroupList")
    public ModelAndView typeGroupList() {
        return new ModelAndView("system/type/typeGroupList");
    }

    /**
     * 类型列表页面跳转
     *
     * @return
     */
    @RequestMapping(params = "typeList")
    public ModelAndView typeList(HttpServletRequest request) {
        String typeGroupId = request.getParameter("typegroupid");
        TypeGroup typegroup = systemRepository.findEntity(TypeGroup.class, typeGroupId);
        request.setAttribute("typegroup", typegroup);
        return new ModelAndView("system/type/typeList");
    }

    /**
     * easyuiAJAX请求数据
     *
     * @return
     */
    @RequestMapping(params = "typeGroupGrid")
    @ResponseBody
    public DataGridData typeGroupGrid(HttpServletRequest request, DataGridParam dataGridParam, TypeGroup typegroup) {
        CriteriaQuery cq = new CriteriaQuery(TypeGroup.class).buildDataGrid(dataGridParam);
        String typeGroupName = request.getParameter("typegroupname");
        if (typeGroupName != null && typeGroupName.trim().length() > 0) {
            typeGroupName = typeGroupName.trim();
            List<String> typegroupnameKeyList = systemRepository.findByHql("select typeGroupName from TypeGroup");
            MutiLangUtils.assembleCondition(typegroupnameKeyList, cq, "typeGroupName", typeGroupName);
        }
//        MutiLangUtils.setMutiLangValueForList(dataGridParam.getResults(), "typeGroupName");
        return this.systemRepository.findDataGridData(cq, true);
    }


    /**
     * easyuiAJAX请求数据
     *
     * @param request
     * @param dataGridParam
     * @return
     */

    @RequestMapping(params = "typeGrid")
    @ResponseBody
    public DataGridData typeGrid(HttpServletRequest request, DataGridParam dataGridParam) {
        String typegroupid = request.getParameter("typegroupid");
        String typename = request.getParameter("typename");
        CriteriaQuery cq = new CriteriaQuery(Type.class).buildDataGrid(dataGridParam);
        cq.eq("typeGroup.id", typegroupid);
        cq.like("typeName", typename);
        cq.add();
//        MutiLangUtils.setMutiLangValueForList(dataGridParam.getResults(), "typeName");
        return this.systemRepository.findDataGridData(cq, true);
    }

    /**
     * 跳转到类型页面
     *
     * @param request request
     * @return
     */
    @RequestMapping(params = "goTypeGrid")
    public ModelAndView goTypeGrid(HttpServletRequest request) {
        String typegroupid = request.getParameter("typegroupid");
        request.setAttribute("typegroupid", typegroupid);
        return new ModelAndView("system/type/typeListForTypegroup");
    }

    @RequestMapping(params = "typeGridTree")
    @ResponseBody
    @Deprecated
    public List<TreeGrid> typeGridTree(HttpServletRequest request, TreeGrid treegrid) {
        CriteriaQuery cq;
        List<TreeGrid> treeGrids = new ArrayList<TreeGrid>();
        if (treegrid.getId() != null) {
            cq = new CriteriaQuery(Type.class);
            cq.eq("typeGroup.id", treegrid.getId().substring(1));
            cq.add();
            List<Type> typeList = systemRepository.findListByCq(cq, false);
            for (Type obj : typeList) {
                TreeGrid treeNode = new TreeGrid();
                treeNode.setId("T" + obj.getId());
                treeNode.setText(obj.getTypeName());
                treeNode.setCode(obj.getTypeCode());
                treeGrids.add(treeNode);
            }
        } else {
            cq = new CriteriaQuery(TypeGroup.class);
            String typegroupcode = request.getParameter("typegroupcode");
            if (typegroupcode != null) {
                HqlRuleEnum rule = PageValueConvertRuleEnum
                        .convert(typegroupcode);
                Object value = PageValueConvertRuleEnum.replaceValue(rule,
                        typegroupcode);
                ObjectParseUtil.addCriteria(cq, "typegroupcode", rule, value);
                cq.add();
            }
            String typegroupname = request.getParameter("typegroupname");
            if (typegroupname != null && typegroupname.trim().length() > 0) {
                typegroupname = typegroupname.trim();
                List<String> typegroupnameKeyList = systemRepository.findByHql("select typegroupname from TypeGroup");
                MutiLangUtils.assembleCondition(typegroupnameKeyList, cq, "typegroupname", typegroupname);
            }
            List<TypeGroup> typeGroupList = systemRepository.findListByCq(cq, false);
            for (TypeGroup obj : typeGroupList) {
                TreeGrid treeNode = new TreeGrid();
                treeNode.setId("G" + obj.getId());
                treeNode.setText(obj.getTypeGroupName());
                treeNode.setCode(obj.getTypeGroupCode());
                treeNode.setState("closed");
                treeGrids.add(treeNode);
            }
        }
        MutiLangUtils.setMutiTree(treeGrids);
        return treeGrids;
    }

    /**
     * 删除类型分组或者类型（ID以G开头的是分组）
     *
     * @return
     */
    @RequestMapping(params = "delTypeGridTree")
    @ResponseBody
    public AjaxJson delTypeGridTree(String id, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        String message;
        if (id.startsWith("G")) {//分组
            TypeGroup typegroup = systemRepository.findEntity(TypeGroup.class, id.substring(1));
            message = "数据字典分组: " + languageRepository.getLang(typegroup.getTypeGroupName()) + "被删除 成功";
            systemRepository.delete(typegroup);
        } else {
            Type type = systemRepository.findEntity(Type.class, id.substring(1));
            message = "数据字典类型: " + languageRepository.getLang(type.getTypeName()) + "被删除 成功";
            systemRepository.delete(type);
        }
        systemRepository.addLog(message, Globals.LOG_TYPE_DEL, Globals.LOG_LEVEL);
        //刷新缓存
        systemRepository.refleshTypeGroupCach();
        j.setMsg(message);
        return j;
    }

    /**
     * 删除类型分组
     *
     * @return
     */
    @RequestMapping(params = "delTypeGroup")
    @ResponseBody
    public AjaxJson delTypeGroup(TypeGroup typegroup, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        typegroup = systemRepository.findEntity(TypeGroup.class, typegroup.getId());
        String message = "类型分组: " + languageRepository.getLang(typegroup.getTypeGroupName()) + " 被删除 成功";
        if (StrUtils.isEmpty(typegroup.getTypes())) {
            systemRepository.delete(typegroup);
            systemRepository.addLog(message, Globals.LOG_TYPE_DEL, Globals.LOG_LEVEL);
            //刷新缓存
            systemRepository.refleshTypeGroupCach();
        } else {
            message = "类型分组: " + languageRepository.getLang(typegroup.getTypeGroupName()) + " 下有类型信息，不能删除！";
        }
        j.setMsg(message);
        return j;
    }

    /**
     * 删除类型
     *
     * @return
     */
    @RequestMapping(params = "delType")
    @ResponseBody
    public AjaxJson delType(Type type, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        type = systemRepository.findEntity(Type.class, type.getId());
        String message;
        if (!StrUtils.isNotEmpty(type)) {
            message = "已经被删除了";
            j.setMsg(message);
            j.setSuccess(false);
            return j;
        }
        message = "类型: " + languageRepository.getLang(type.getTypeName()) + "被删除 成功";
        systemRepository.delete(type);
        //刷新缓存
        systemRepository.refleshTypesCach(type);
        systemRepository.addLog(message, Globals.LOG_TYPE_DEL, Globals.LOG_LEVEL);
        j.setMsg(message);
        return j;
    }

    /**
     * 检查分组代码
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "checkTypeGroup")
    @ResponseBody
    public ValidForm checkTypeGroup(HttpServletRequest request) {
        ValidForm v = new ValidForm();
        String typegroupcode = ConvertUtils.getString(request.getParameter("param"));
        String code = ConvertUtils.getString(request.getParameter("code"));
        List<TypeGroup> typegroups = systemRepository.findAllByProperty(TypeGroup.class, "typegroupcode", typegroupcode);
        if (typegroups.size() > 0 && !code.equals(typegroupcode)) {
            v.setInfo("分组已存在");
            v.setStatus("n");
        }
        return v;
    }

    /**
     * 添加类型分组
     *
     * @param typegroup
     * @return
     */
    @RequestMapping(params = "saveTypeGroup")
    @ResponseBody
    public AjaxJson saveTypeGroup(TypeGroup typegroup, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        String message;
        if (StrUtils.isNotEmpty(typegroup.getId())) {
            message = "类型分组: " + languageRepository.getLang(typegroup.getTypeGroupName()) + "被更新成功";
            userRepository.saveOrUpdate(typegroup);
            systemRepository.addLog(message, Globals.LOG_TYPE_UPDATE, Globals.LOG_LEVEL);
        } else {
            message = "类型分组: " + languageRepository.getLang(typegroup.getTypeGroupName()) + "被添加成功";
            userRepository.save(typegroup);
            systemRepository.addLog(message, Globals.LOG_TYPE_INSERT, Globals.LOG_LEVEL);
        }
        //刷新缓存
        systemRepository.refleshTypeGroupCach();
        j.setMsg(message);
        return j;
    }

    /**
     * 检查类型代码
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "checkType")
    @ResponseBody
    public ValidForm checkType(HttpServletRequest request) {
        ValidForm v = new ValidForm();
        String typecode = ConvertUtils.getString(request.getParameter("param"));
        String code = ConvertUtils.getString(request.getParameter("code"));
        String typeGroupCode = ConvertUtils.getString(request.getParameter("typeGroupCode"));
        StringBuilder hql = new StringBuilder("FROM ").append(Type.class.getName()).append(" AS entity WHERE 1=1 ");
        hql.append(" AND entity.TSTypegroup.typegroupcode =  '").append(typeGroupCode).append("'");
        hql.append(" AND entity.typecode =  '").append(typecode).append("'");
        List<Object> types = this.systemRepository.findByHql(hql.toString());
        if (types.size() > 0 && !code.equals(typecode)) {
            v.setInfo("类型已存在");
            v.setStatus("n");
        }
        return v;
    }

    /**
     * 添加类型
     *
     * @param type
     * @return
     */
    @RequestMapping(params = "saveType")
    @ResponseBody
    public AjaxJson saveType(Type type, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        String message;
        if (StrUtils.isNotEmpty(type.getId())) {
            message = "类型: " + languageRepository.getLang(type.getTypeName()) + "被更新成功";
            userRepository.saveOrUpdate(type);
            systemRepository.addLog(message, Globals.LOG_TYPE_UPDATE, Globals.LOG_LEVEL);
        } else {
            message = "类型: " + languageRepository.getLang(type.getTypeName()) + "被添加成功";
            userRepository.save(type);
            systemRepository.addLog(message, Globals.LOG_TYPE_INSERT, Globals.LOG_LEVEL);
        }
        //刷新缓存
        systemRepository.refleshTypesCach(type);
        j.setMsg(message);
        return j;
    }


    /**
     * 类型分组列表页面跳转
     *
     * @return
     */
    @RequestMapping(params = "aouTypeGroup")
    public ModelAndView aouTypeGroup(TypeGroup typegroup, HttpServletRequest request) {
        if (typegroup.getId() != null) {
            typegroup = systemRepository.findEntity(TypeGroup.class, typegroup.getId());
            request.setAttribute("typeGroupView", typegroup);
        }
        return new ModelAndView("system/type/typegroup");
    }

    /**
     * 类型列表页面跳转
     *
     * @return
     */
    @RequestMapping(params = "detailType")
    public ModelAndView detailType(Type type, HttpServletRequest request) {
        String typegroupid = request.getParameter("typegroupid");
        request.setAttribute("typegroupid", typegroupid);
        TypeGroup typegroup = systemRepository.findUniqueByProperty(TypeGroup.class, "id", typegroupid);
        String typegroupname = typegroup.getTypeGroupName();
        request.setAttribute("typegroupname", languageRepository.getLang(typegroupname));
        if (StrUtils.isNotEmpty(type.getId())) {
            type = systemRepository.findEntity(Type.class, type.getId());
            request.setAttribute("typeView", type);
        }
        return new ModelAndView("system/type/type");
    }

}
