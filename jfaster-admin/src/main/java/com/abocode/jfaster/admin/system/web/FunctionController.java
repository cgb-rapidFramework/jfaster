package com.abocode.jfaster.admin.system.web;

import com.abocode.jfaster.admin.system.repository.SystemRepository;
import com.abocode.jfaster.admin.system.service.FunctionService;
import com.abocode.jfaster.admin.system.service.OperationService;
import com.abocode.jfaster.admin.system.service.RuleService;
import com.abocode.jfaster.core.common.model.json.AjaxJson;
import com.abocode.jfaster.core.common.model.json.AjaxJsonBuilder;
import com.abocode.jfaster.core.common.model.json.ComboTree;
import com.abocode.jfaster.core.common.model.json.TreeGrid;
import com.abocode.jfaster.core.common.util.ConvertUtils;
import com.abocode.jfaster.core.common.util.StrUtils;
import com.abocode.jfaster.core.persistence.hibernate.qbc.CriteriaQuery;
import com.abocode.jfaster.core.repository.DataGridData;
import com.abocode.jfaster.core.repository.DataGridParam;
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
     *
     * @param dataGridParam
     * @return
     */

    @RequestMapping(params = "findDataGridData")
    @ResponseBody
    public DataGridData findDataGridData(DataGridParam dataGridParam) {
        CriteriaQuery cq = new CriteriaQuery(Function.class).buildDataGrid(dataGridParam);
        return this.systemService.findDataGridData(cq, true);
    }

    /**
     * easyuiAJAX请求数据
     *
     * @param request
     * @param response
     * @param dataGridParam
     * @return
     */

    @RequestMapping(params = "opdategrid")
    @ResponseBody
    public DataGridData opdategrid(HttpServletRequest request,
                                   HttpServletResponse response, DataGridParam dataGridParam) {
        CriteriaQuery cq = new CriteriaQuery(Operation.class).buildDataGrid(dataGridParam);
        String functionId = ConvertUtils.getString(request
                .getParameter("functionId"));
        cq.eq("parentFunction.id", functionId);
        cq.add();
        return this.systemService.findDataGridData(cq, true);
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
        if (StrUtils.isEmpty(functionOrder)) {
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
    @RequestMapping(params = "detail")
    public ModelAndView detail(Function function, HttpServletRequest request) {
        String functionId = request.getParameter("id");
        List<Function> functionList = systemService
                .findAll(Function.class);
        request.setAttribute("flist", functionList);
        List<Icon> iconList = systemService
                .findByHql("from Icon where iconType != 3");
        request.setAttribute("iconlist", iconList);
        List<Icon> iconDeskList = systemService
                .findByHql("from Icon where iconType = 3");
        request.setAttribute("iconDeskList", iconDeskList);
        if (functionId != null) {
            function = systemService.findEntity(Function.class, functionId);
        }
        if (function.getParentFunction() != null
                && function.getParentFunction().getId() != null) {
            function.setFunctionLevel((short) 1);
            function.setParentFunction((Function) systemService.findEntity(
                    Function.class, function.getParentFunction().getId()));
        }
        request.setAttribute("functionView", function);
        return new ModelAndView("system/function/function");
    }

    /**
     * 操作列表页面跳转
     *
     * @return
     */
    @RequestMapping(params = "detailop")
    public ModelAndView detailop(Operation operation,
                                 HttpServletRequest request) {
        List<Icon> iconlist = systemService.findAll(Icon.class);
        request.setAttribute("iconlist", iconlist);
        if (operation.getId() != null) {
            operation = systemService.findEntity(Operation.class, operation.getId());
            request.setAttribute("operation", operation);
        }
        String functionId = ConvertUtils.getString(request.getParameter("functionId"));
        request.setAttribute("functionId", functionId);
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
     *
     * @return
     */
    @RequestMapping(params = "functionList")
    @ResponseBody
    public DataGridData functionList(HttpServletRequest request,
                                     HttpServletResponse response, DataGridParam dataGridParam) {
        CriteriaQuery cq = new CriteriaQuery(Function.class).buildDataGrid(dataGridParam);
        String id = ConvertUtils.getString(request.getParameter("id"));
        cq.isNull("function");
        if (id != null) {
            cq.eq("parentFunction.id", id);
        }
        cq.add();
        return this.systemService.findDataGridData(cq, true);
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
    public ModelAndView searchApp(HttpServletRequest request) {
        String name = request.getParameter("name");
        String menuListMap = functionService.search(name);
        request.setAttribute("menuListMap", menuListMap);
        return new ModelAndView("system/function/menuAppList");
    }


    /**
     * 数据规则权限的编辑和新增
     */
    @RequestMapping(params = "detailrule")
    public ModelAndView detailrule(DataRule operation,
                                   HttpServletRequest request) {
        List<Icon> iconlist = systemService.findAll(Icon.class);
        request.setAttribute("iconlist", iconlist);
        if (operation.getId() != null) {
            operation = systemService.findEntity(DataRule.class, operation.getId());
            request.setAttribute("operationView", operation);
        }
        String functionId = ConvertUtils.getString(request.getParameter("functionId"));
        request.setAttribute("functionId", functionId);
        return new ModelAndView("system/dataRule/ruleData");
    }

    /**
     * opdategrid 数据规则的列表界面
     *
     * @return
     */
    @RequestMapping(params = "ruledategrid")
    @ResponseBody
    public DataGridData ruledategrid(HttpServletRequest request,
                                     HttpServletResponse response, DataGridParam dataGridParam) {
        CriteriaQuery cq = new CriteriaQuery(DataRule.class).buildDataGrid(dataGridParam);
        String functionId = ConvertUtils.getString(request
                .getParameter("functionId"));
        cq.eq("function.id", functionId);
        cq.add();
        return this.systemService.findDataGridData(cq, true);
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
