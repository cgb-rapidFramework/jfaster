package com.abocode.jfaster.admin.system.web;

import com.abocode.jfaster.admin.system.service.FunctionService;
import com.abocode.jfaster.admin.system.service.OperationService;
import com.abocode.jfaster.admin.system.service.RuleService;
import com.abocode.jfaster.core.common.model.json.*;
import com.abocode.jfaster.core.common.util.ConvertUtils;
import com.abocode.jfaster.admin.system.repository.ResourceRepository;
import com.abocode.jfaster.admin.system.repository.SystemRepository;
import com.abocode.jfaster.core.persistence.hibernate.qbc.CriteriaQuery;
import com.abocode.jfaster.core.platform.view.widgets.easyui.TagUtil;
import com.abocode.jfaster.core.common.util.StringUtils;
import com.abocode.jfaster.system.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 菜单权限处理类
 *
 * @author guanxf
 */
@Scope("prototype")
@Controller
@RequestMapping("/functionController")
public class FunctionController {
    @Autowired
    private ResourceRepository resourceService;
    @Autowired
    private SystemRepository systemService;
    @Autowired
    private FunctionService functionService;

    @Autowired
    private OperationService operationService;
    @Autowired
    private RuleService ruleService;

    /**
     * 权限列表页面跳转
     *
     * @return
     */
    @RequestMapping(params = "function")
    public ModelAndView function() {
        return new ModelAndView("system/function/functionList");
    }

    /**
     * 操作列表页面跳转
     *
     * @return
     */
    @RequestMapping(params = "operation")
    public ModelAndView operation(HttpServletRequest request, String functionId) {
        request.setAttribute("functionId", functionId);
        return new ModelAndView("system/operation/operationList");
    }

    /**
     * 数据规则列表页面跳转
     *
     * @return
     */
    @RequestMapping(params = "dataRule")
    public ModelAndView operationData(HttpServletRequest request,
                                      String functionId) {
        request.setAttribute("functionId", functionId);
        return new ModelAndView("system/dataRule/ruleDataList");
    }

    /**
     * easyuiAJAX请求数据
     * @param response
     * @param dataGrid
     */

    @RequestMapping(params = "datagrid")
    public void datagrid(HttpServletResponse response, DataGrid dataGrid) {
        CriteriaQuery cq = new CriteriaQuery(Function.class, dataGrid);
        this.systemService.findDataGridReturn(cq, true);
        TagUtil.datagrid(response, dataGrid);
    }

    /**
     * easyuiAJAX请求数据
     *
     * @param request
     * @param response
     * @param dataGrid
     */

    @RequestMapping(params = "opdategrid")
    public void opdategrid(HttpServletRequest request,
                           HttpServletResponse response, DataGrid dataGrid) {
        CriteriaQuery cq = new CriteriaQuery(Operation.class, dataGrid);
        String functionId = ConvertUtils.getString(request
                .getParameter("functionId"));
        cq.eq("parentFunction.id", functionId);
        cq.add();
        this.systemService.findDataGridReturn(cq, true);
        TagUtil.datagrid(response, dataGrid);
    }

    /**
     * 删除权限
     *
     * @param function
     * @return
     */
    @RequestMapping(params = "del")
    @ResponseBody
    public AjaxJson del(Function function) {
        // // 删除权限时先删除权限与角色之间关联表信息
        List<RoleFunction> roleFunctions = systemService.findAllByProperty(RoleFunction.class, "parentFunction.id", function.getId());
        Assert.isTrue(roleFunctions.size() > 0, "菜单已分配无法删除");
        functionService.delById(function.getId());
        return AjaxJsonBuilder.success();
    }

    /**
     * 删除操作
     *
     * @param operation
     * @return
     */
    @RequestMapping(params = "delop")
    @ResponseBody
    public AjaxJson delop(Operation operation) {
        functionService.delByLopId(operation.getId());
        return AjaxJsonBuilder.success();
    }

    /**
     * 权限录入
     *
     * @param function
     * @return
     */
    @RequestMapping(params = "saveFunction")
    @ResponseBody
    public AjaxJson saveFunction(Function function, HttpServletRequest request) {
        String functionOrder = function.getFunctionOrder();
        if (StringUtils.isEmpty(functionOrder)) {
            function.setFunctionOrder("0");
        }

        if (function.getParentFunction().getId().equals("")) {
            function.setParentFunction(null);
        } else {
            Function parent = systemService.findEntity(Function.class,
                    function.getParentFunction().getId());
            function.setFunctionLevel(Short.valueOf(parent.getFunctionLevel()
                    + 1 + ""));
        }
        functionService.saveFunction(function);
        return AjaxJsonBuilder.success();
    }

    /**
     * 操作录入
     *
     * @param operation
     * @return
     */
    @RequestMapping(params = "saveop")
    @ResponseBody
    public AjaxJson saveop(Operation operation, HttpServletRequest request) {
        String pid = request.getParameter("parentFunction.id");
        if (pid.equals("")) {
            operation.setFunction(null);
        }
        operationService.save(operation);
        return AjaxJsonBuilder.success();
    }

    /**
     * 权限列表页面跳转
     *
     * @return
     */
    @RequestMapping(params = "addorupdate")
    public ModelAndView addorupdate(Function function, HttpServletRequest req) {
        String functionId = req.getParameter("id");
        List<Function> functionList = systemService
                .getList(Function.class);
        req.setAttribute("flist", functionList);
        List<Icon> iconList = systemService
                .findByHql("from Icon where iconType != 3");
        req.setAttribute("iconlist", iconList);
        List<Icon> iconDeskList = systemService
                .findByHql("from Icon where iconType = 3");
        req.setAttribute("iconDeskList", iconDeskList);
        if (functionId != null) {
            function = systemService.findEntity(Function.class, functionId);
        }
        if (function.getParentFunction() != null
                && function.getParentFunction().getId() != null) {
            function.setFunctionLevel((short) 1);
            function.setParentFunction((Function) systemService.findEntity(
                    Function.class, function.getParentFunction().getId()));
        }
        req.setAttribute("functionView", function);
        return new ModelAndView("system/function/function");
    }

    /**
     * 操作列表页面跳转
     *
     * @return
     */
    @RequestMapping(params = "addorupdateop")
    public ModelAndView addorupdateop(Operation operation,
                                      HttpServletRequest req) {
        List<Icon> iconlist = systemService.getList(Icon.class);
        req.setAttribute("iconlist", iconlist);
        if (operation.getId() != null) {
            operation = systemService.findEntity(Operation.class, operation.getId());
            req.setAttribute("operation", operation);
        }
        String functionId = ConvertUtils.getString(req.getParameter("functionId"));
        req.setAttribute("functionId", functionId);
        return new ModelAndView("system/operation/operation");
    }

    /**
     * 权限列表
     */
    @RequestMapping(params = "functionGrid")
    @ResponseBody
    public List<TreeGrid> functionGrid(HttpServletRequest request,
                                       TreeGrid treegrid) {
        String selfId = request.getParameter("selfId");
        return functionService.findTreeGrid(selfId, treegrid.getId());
    }

    /**
     * 权限列表
     */
    @RequestMapping(params = "functionList")
    @ResponseBody
    public void functionList(HttpServletRequest request,
                             HttpServletResponse response, DataGrid dataGrid) {
        CriteriaQuery cq = new CriteriaQuery(Function.class, dataGrid);
        String id = ConvertUtils.getString(request.getParameter("id"));
        cq.isNull("function");
        if (id != null) {
            cq.eq("parentFunction.id", id);
        }
        cq.add();
        this.systemService.findDataGridReturn(cq, true);
        TagUtil.datagrid(response, dataGrid);
    }

    /**
     * 父级权限下拉菜单
     */
    @RequestMapping(params = "setPFunction")
    @ResponseBody
    public List<ComboTree> setPFunction(HttpServletRequest request,
                                        ComboTree comboTree) {
        String selfId = request.getParameter("selfId");
        List<ComboTree> comboTrees = functionService.setParentFunction(selfId, comboTree.getId());
        return comboTrees;
    }

    /**
     * 菜单模糊检索功能
     *
     * @return
     */
    @RequestMapping(params = "searchApp")
    public ModelAndView searchApp(HttpServletRequest req) {
        String name = req.getParameter("name");
        String menuListMap = functionService.search(name);
        req.setAttribute("menuListMap", menuListMap);
        return new ModelAndView("system/function/menuAppList");
    }


    /**
     * 数据规则权限的编辑和新增
     */
    @RequestMapping(params = "addorupdaterule")
    public ModelAndView addorupdaterule(DataRule operation,
                                        HttpServletRequest req) {
        List<Icon> iconlist = systemService.getList(Icon.class);
        req.setAttribute("iconlist", iconlist);
        if (operation.getId() != null) {
            operation = systemService.findEntity(DataRule.class, operation.getId());
            req.setAttribute("operationView", operation);
        }
        String functionId = ConvertUtils.getString(req.getParameter("functionId"));
        req.setAttribute("functionId", functionId);
        return new ModelAndView("system/dataRule/ruleData");
    }

    /**
     * opdategrid 数据规则的列表界面
     */
    @RequestMapping(params = "ruledategrid")
    public void ruledategrid(HttpServletRequest request,
                             HttpServletResponse response, DataGrid dataGrid) {
        CriteriaQuery cq = new CriteriaQuery(DataRule.class, dataGrid);
        String functionId = ConvertUtils.getString(request
                .getParameter("functionId"));
        cq.eq("parentFunction.id", functionId);
        cq.add();
        this.systemService.findDataGridReturn(cq, true);
        TagUtil.datagrid(response, dataGrid);
    }

    /**
     * delrule 删除数据权限规则
     */
    @RequestMapping(params = "delrule")
    @ResponseBody
    public AjaxJson delrule(DataRule operation) {
        ruleService.del(operation);
        return AjaxJsonBuilder.success();
    }

    /**
     * saverule保存规则权限值
     */
    @RequestMapping(params = "saverule")
    @ResponseBody
    public AjaxJson saverule(DataRule operation) {
        ruleService.save(operation);
        return AjaxJsonBuilder.success();
    }
}
