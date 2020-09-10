package com.abocode.jfaster.admin.system.web;

import com.abocode.jfaster.admin.system.repository.SystemRepository;
import com.abocode.jfaster.admin.system.service.FunctionService;
import com.abocode.jfaster.admin.system.service.OperationService;
import com.abocode.jfaster.admin.system.service.RuleService;
import com.abocode.jfaster.api.system.DataRuleDTO;
import com.abocode.jfaster.api.system.FunctionDTO;
import com.abocode.jfaster.api.system.OperationDTO;
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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public static final String FUNCTION_ID = "functionId";
    public static final String PARENT_FUNCTION_ID = "parentFunction.id";
    public static final String ICON_LIST = "iconlist";
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
        request.setAttribute(FUNCTION_ID, functionId);
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
        request.setAttribute(FUNCTION_ID, functionId);
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
                .getParameter(FUNCTION_ID));
        cq.eq(PARENT_FUNCTION_ID, functionId);
        cq.add();
        return this.systemService.findDataGridData(cq, true);
    }

    /**
     * 删除权限
     *
     * @param id
     * @return
     */
    @RequestMapping(params = "del")
    @ResponseBody
    public AjaxJson del(@RequestParam String id) {
        // // 删除权限时先删除权限与角色之间关联表信息
        List<RoleFunction> roleFunctions = systemService.findAllByProperty(RoleFunction.class, PARENT_FUNCTION_ID, id);
        Assert.isTrue(!CollectionUtils.isEmpty(roleFunctions), "菜单已分配无法删除");
        functionService.delById(id);
        return AjaxJsonBuilder.success();
    }

    /**
     * 删除操作
     *
     * @param id
     * @return
     */
    @RequestMapping(params = "delop")
    @ResponseBody
    public AjaxJson delop(String id) {
        functionService.delByLopId(id);
        return AjaxJsonBuilder.success();
    }

    /**
     * 权限录入
     * @param functionDto
     * @return
     */
    @RequestMapping(params = "saveFunction")
    @ResponseBody
    public AjaxJson saveFunction(FunctionDTO functionDto) {
        Function function=new Function();
        BeanUtils.copyProperties(functionDto,function);
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
     * @param operationDTO
     * @return
     */
    @RequestMapping(params = "saveop")
    @ResponseBody
    public AjaxJson saveop(OperationDTO operationDTO, HttpServletRequest request) {
        String pid = request.getParameter(PARENT_FUNCTION_ID);
        Operation operation=new Operation();
        BeanUtils.copyProperties(operationDTO,operation);
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
    public ModelAndView detail(FunctionDTO functionDTO, HttpServletRequest request) {
        List<Function> functionList = systemService
                .findAll(Function.class);
        request.setAttribute("flist", functionList);
        List<Icon> iconList = systemService
                .findByHql("from Icon where iconType != 3");
        request.setAttribute(ICON_LIST, iconList);
        List<Icon> iconDeskList = systemService
                .findByHql("from Icon where iconType = 3");
        request.setAttribute("iconDeskList", iconDeskList);
        Function function=new Function();
        BeanUtils.copyProperties(functionDTO,function);
        if ( functionDTO.getId() != null) {
            function = systemService.findEntity(Function.class, functionDTO.getId());
        }
        if (function.getParentFunction() != null
                && function.getParentFunction().getId() != null) {
            function.setFunctionLevel((short) 1);
            function.setParentFunction(systemService.findEntity(
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
    public ModelAndView detailop(OperationDTO operationDTO,
                                 HttpServletRequest request) {
        List<Icon> iconlist = systemService.findAll(Icon.class);
        request.setAttribute(ICON_LIST, iconlist);
        if (operationDTO.getId() != null) {
            Operation operation = systemService.findEntity(Operation.class, operationDTO.getId());
            request.setAttribute("operation", operation);
        }
        String functionId = ConvertUtils.getString(request.getParameter(FUNCTION_ID));
        request.setAttribute(FUNCTION_ID, functionId);
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
            cq.eq(PARENT_FUNCTION_ID, id);
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
        return functionService.setParentFunction(selfId, comboTree.getId());
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
    public ModelAndView detailrule(DataRuleDTO operation,
                                   HttpServletRequest request) {
        List<Icon> iconlist = systemService.findAll(Icon.class);
        request.setAttribute(ICON_LIST, iconlist);
        if (operation.getId() != null) {
            operation = systemService.findEntity(DataRule.class, operation.getId());
            request.setAttribute("operationView", operation);
        }
        String functionId = ConvertUtils.getString(request.getParameter(FUNCTION_ID));
        request.setAttribute(FUNCTION_ID, functionId);
        return new ModelAndView("system/dataRule/ruleData");
    }

    /**
     * opdategrid 数据规则的列表界面
     *
     * @return
     */
    @RequestMapping(params = "ruledategrid")
    @ResponseBody
    public DataGridData ruledategrid(String functionId, DataGridParam dataGridParam) {
        CriteriaQuery cq = new CriteriaQuery(DataRule.class).buildDataGrid(dataGridParam);
        cq.eq("function.id", functionId);
        cq.add();
        return this.systemService.findDataGridData(cq, true);
    }

    /**
     * delrule 删除数据权限规则
     */
    @RequestMapping(params = "delrule")
    @ResponseBody
    public AjaxJson delrule(DataRuleDTO dataRuleDTO) {
        DataRule operation=new DataRule();
        BeanUtils.copyProperties(dataRuleDTO,operation);
        ruleService.del(operation);
        return AjaxJsonBuilder.success();
    }

    /**
     * saverule保存规则权限值
     */
    @RequestMapping(params = "saverule")
    @ResponseBody
    public AjaxJson saverule(DataRuleDTO dataRuleDTO) {
        DataRule operation=new DataRule();
        BeanUtils.copyProperties(dataRuleDTO,operation);
        ruleService.save(operation);
        return AjaxJsonBuilder.success();
    }
}
