package com.abocode.jfaster.admin.system.service.impl;

import com.abocode.jfaster.admin.system.dto.DataRuleDto;
import com.abocode.jfaster.admin.system.repository.LanguageRepository;
import com.abocode.jfaster.admin.system.repository.ResourceRepository;
import com.abocode.jfaster.admin.system.repository.UserRepository;
import com.abocode.jfaster.admin.system.service.BeanToTagConverter;
import com.abocode.jfaster.admin.system.service.FunctionService;
import com.abocode.jfaster.admin.system.service.annotation.AutoMenu;
import com.abocode.jfaster.admin.system.service.annotation.AutoMenuOperation;
import com.abocode.jfaster.admin.system.service.annotation.MenuCodeType;
import com.abocode.jfaster.core.common.constants.Globals;
import com.abocode.jfaster.core.common.model.json.ComboTree;
import com.abocode.jfaster.core.common.model.json.TreeGrid;
import com.abocode.jfaster.core.common.util.ContextHolderUtils;
import com.abocode.jfaster.core.common.util.DataRuleUtils;
import com.abocode.jfaster.core.persistence.hibernate.hqlsearch.ObjectParseUtil;
import com.abocode.jfaster.core.platform.utils.FunctionSortUtils;
import com.abocode.jfaster.core.platform.utils.MutiLangUtils;
import com.abocode.jfaster.core.common.util.StrUtils;
import com.abocode.jfaster.core.persistence.hibernate.qbc.CriteriaQuery;
import com.abocode.jfaster.core.repository.SortDirection;
import com.abocode.jfaster.core.platform.view.interactions.easyui.ComboTreeModel;
import com.abocode.jfaster.core.platform.view.interactions.easyui.TreeGridModel;
import com.abocode.jfaster.core.web.manager.ClientManager;
import com.abocode.jfaster.system.entity.*;
import com.abocode.jfaster.admin.system.repository.SystemRepository;
import com.abocode.jfaster.core.web.manager.ClientBean;
import com.abocode.jfaster.core.platform.view.FunctionView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.*;

@Service
public class FunctionServiceImpl implements FunctionService {
    @Resource
    private SystemRepository systemService;
    @Autowired
    private UserRepository userService;
    @Autowired
    private ResourceRepository resourceService;
    @Autowired
    private LanguageRepository languageRepository;

    /**
     * 获取用户菜单列表
     *
     * @param user
     * @return
     */
    private Map<String, Function> getUserFunction(User user) {
        HttpSession session = ContextHolderUtils.getSession();
        ClientBean client = ClientManager.getInstance().getClient(session.getId());
        if (client.getFunctions() == null || client.getFunctions().size() == 0) {
            Map<String, Function> loginActionlist = new HashMap<String, Function>();
            StringBuilder hqlsb1 = new StringBuilder("select distinct f from Function f,RoleFunction rf,RoleUser ru  ")
                    .append("where ru.role.id=rf.role.id and rf.function.id=f.id and ru.user.id=?0 ");
            StringBuilder hqlsb2 = new StringBuilder("select distinct c from Function c,RoleOrg b,UserOrg a ")
                    .append("where a.org.id=b.org.id and b.role.id=c.id and a.user.id=?0");
            Object[] object = new Object[]{user.getId()};
            List<Function> list1 = systemService.findByHql(hqlsb1.toString(), object);
            List<Function> list2 = systemService.findByHql(hqlsb2.toString(), object);
            for (Function function : list1) {
                loginActionlist.put(function.getId(), function);
            }
            for (Function function : list2) {
                loginActionlist.put(function.getId(), function);
            }
            client.setFunctions(loginActionlist);
            //保存菜单到seesion中心
            session.setAttribute("functions" + session.getId(), loginActionlist);
        }
        return client.getFunctions();
    }

    /**
     * 获取权限的map
     *
     * @param user
     * @return
     */
    @Override
    public Map<Integer, List<FunctionView>> getFunctionMap(User user) {
        Map<Integer, List<FunctionView>> functionMap = new HashMap<Integer, List<FunctionView>>();
        Map<String, Function> loginActionlist = getUserFunction(user);
        if (loginActionlist.size() > 0) {
            Collection<Function> allFunctions = loginActionlist.values();
            for (Function function : allFunctions) {
                if (!functionMap.containsKey(function.getFunctionLevel() + 0)) {
                    functionMap.put(function.getFunctionLevel() + 0,
                            new ArrayList());
                }
                FunctionView functionBean = BeanToTagConverter.convertFunction(function);
                functionMap.get(function.getFunctionLevel() + 0).add(functionBean);
            }
            // 菜单栏排序
            Collection<List<FunctionView>> functionsList = functionMap.values();
            for (List<FunctionView> functionViews : functionsList) {
                FunctionSortUtils.sortView(functionViews);
            }

        }
        return functionMap;
    }

    @Override
    public void delById(String id) {
        Function function = systemService.findEntity(Function.class, id);
        String message = MutiLangUtils.paramDelSuccess("common.menu");
        systemService.updateBySql("delete from t_s_role_function where functionid='"
                + function.getId() + "'");
        systemService.delete(function);
//        userService.delete(function);
        systemService.addLog(message, Globals.LOG_TYPE_DEL,
                Globals.LOG_LEVEL);
        systemService.addLog(message, Globals.LOG_TYPE_DEL,
                Globals.LOG_LEVEL);
    }

    @Override
    public void delByLopId(String id) {
        Operation operation = systemService.findEntity(Operation.class, id);
        userService.delete(operation);
        String message = MutiLangUtils.paramDelSuccess("common.operation");
        systemService.addLog(message, Globals.LOG_TYPE_DEL,
                Globals.LOG_LEVEL);
    }

    /**
     * 递归更新子菜单的FunctionLevel
     *
     * @param subFunction
     * @param parent
     */
    private void updateSubFunction(List<Function> subFunction, Function parent) {
        if (subFunction.size() == 0) {//没有子菜单是结束
            return;
        } else {
            for (Function tsFunction : subFunction) {
                tsFunction.setFunctionLevel(Short.valueOf(parent.getFunctionLevel()
                        + 1 + ""));
                systemService.saveOrUpdate(tsFunction);
                List<Function> subFunction1 = systemService.findAllByProperty(Function.class, "parentFunction.id", tsFunction.getId());
                updateSubFunction(subFunction1, tsFunction);
            }
        }
    }

    @Override
    public void saveFunction(Function function) {
        String message;
        if (StrUtils.isNotEmpty(function.getId())) {
            message = MutiLangUtils.paramUpdSuccess("common.menu");
            userService.saveOrUpdate(function);
            systemService.addLog(message, Globals.LOG_TYPE_UPDATE,
                    Globals.LOG_LEVEL);
            List<Function> subFunction = systemService.findAllByProperty(Function.class, "parentFunction.id", function.getId());
            updateSubFunction(subFunction, function);
            systemService.flushRoleFunciton(function.getId(), function);
        } else {
            function.setFunctionOrder(function.getFunctionOrder());
            message = MutiLangUtils.paramAddSuccess("common.menu");
            systemService.save(function);
            systemService.addLog(message, Globals.LOG_TYPE_INSERT,
                    Globals.LOG_LEVEL);
        }

    }

    @Override
    public String search(String name) {
        CriteriaQuery cq = new CriteriaQuery(Function.class);
        cq.notEq("functionLevel", Short.valueOf("0"));
        if (name == null || "".equals(name)) {
            cq.isNull("parentFunction");
        } else {
            String name1 = "%" + name + "%";
            cq.like("functionName", name1);
        }
        cq.add();
        List<Function> functionList = systemService.findListByCq(
                cq, false);
        StringBuffer menuListMap = new StringBuffer();
        if (functionList != null && functionList.size() > 0) {
            for (int i = 0; i < functionList.size(); i++) {
                String icon;
                if (!StrUtils.isEmpty(functionList.get(i).getIconDesk())) {
                    icon = functionList.get(i).getIconDesk().getIconPath();
                } else {
                    icon = "plug-in/sliding/icon/default.png";
                }

                menuListMap.append(menuListMap).append("<div type='"
                        + i
                        + 1
                        + "' class='menuSearch_Info' id='"
                        + functionList.get(i).getId()
                        + "' title='"
                        + functionList.get(i).getFunctionName()
                        + "' url='"
                        + functionList.get(i).getFunctionUrl()
                        + "' icon='"
                        + icon
                        + "' style='float:left;left: 0px; top: 0px;margin-left: 30px;margin-top: 20px;'>"
                        + "<div ><img alt='"
                        + functionList.get(i).getFunctionName()
                        + "' src='"
                        + icon
                        + "'></div>"
                        + "<div class='appButton_appName_inner1' style='color:#333333;'>"
                        + functionList.get(i).getFunctionName() + "</div>"
                        + "<div class='appButton_appName_inner_right1'></div>" +
                        // "</div>" +
                        "</div>");
            }
        } else {
            menuListMap.append(menuListMap).append("很遗憾，在系统中没有检索到与“" + name + "”相关的信息！");
        }
        return menuListMap.toString();
    }

    @Override
    public List<ComboTree> setParentFunction(String selfId, String comboTreeId) {
        CriteriaQuery cq = new CriteriaQuery(Function.class);
        if (null != selfId) {
            cq.notEq("id", selfId);
        }
        if (comboTreeId != null) {
            cq.eq("parentFunction.id", comboTreeId);
        }
        if (comboTreeId == null) {
            cq.isNull("parentFunction");
        }
        cq.add();
        List<Function> functionList = systemService.findListByCq(
                cq, false);
        ComboTreeModel comboTreeModel = new ComboTreeModel("id",
                "functionName", "Functions");
        List<ComboTree> comboTrees = resourceService.ComboTree(functionList, comboTreeModel,
                null, false);
        MutiLangUtils.setMutiTree(comboTrees);
        return comboTrees;
    }

    @Override
    public List<TreeGrid> findTreeGrid(String selfId, String treeGridId) {
        CriteriaQuery cq = new CriteriaQuery(Function.class);
        if (selfId != null) {
            cq.notEq("id", selfId);
        }
        if (treeGridId != null) {
            cq.eq("parentFunction.id", treeGridId);
        }
        if (treeGridId == null) {
            cq.isNull("parentFunction");
        }
        cq.addOrder("functionOrder", SortDirection.asc);
        cq.add();
        List<Function> functionList = systemService.findListByCq(cq, false);

        List<FunctionView> functionBeanList = BeanToTagConverter.convertFunctions(functionList);
        FunctionSortUtils.sortView(functionBeanList);
        TreeGridModel treeGridModel = new TreeGridModel();
        treeGridModel.setIcon("Icon_iconPath");
        treeGridModel.setTextField("functionName");
        treeGridModel.setParentText("Function_functionName");
        treeGridModel.setParentId("Function_id");
        treeGridModel.setSrc("functionUrl");
        treeGridModel.setIdField("id");
        treeGridModel.setChildList("Functions");
        // 添加排序字段
        treeGridModel.setOrder("functionOrder");
        treeGridModel.setFunctionType("functionType");

        List<TreeGrid> treeGrids = resourceService.treegrid(functionList, treeGridModel);
        MutiLangUtils.setMutiTree(treeGrids);
        return treeGrids;
    }

    @Override
    public String getPrimaryMenu(User user) {

        List<FunctionView> primaryMenu = getFunctionMap(user).get(0);
        String floor = "";
        if (primaryMenu == null) {
            return floor;
        }
        for (FunctionView function : primaryMenu) {
            if (function.getFunctionLevel() == 0) {

                String lang_key = function.getFunctionName();
                String lang_context = languageRepository.getLang(lang_key);

                if ("Online 开发".equals(lang_context)) {

                    floor += " <li><img class='imag1' src='plug-in/login/images/online.png' /> "
                            + " <img class='imag2' src='plug-in/login/images/online_up.png' style='display: none;' />" + " </li> ";
                } else if ("统计查询".equals(lang_context)) {

                    floor += " <li><img class='imag1' src='plug-in/login/images/guanli.png' /> "
                            + " <img class='imag2' src='plug-in/login/images/guanli_up.png' style='display: none;' />" + " </li> ";
                } else if ("系统管理".equals(lang_context)) {

                    floor += " <li><img class='imag1' src='plug-in/login/images/xtgl.png' /> "
                            + " <img class='imag2' src='plug-in/login/images/xtgl_up.png' style='display: none;' />" + " </li> ";
                } else if ("常用示例".equals(lang_context)) {

                    floor += " <li><img class='imag1' src='plug-in/login/images/cysl.png' /> "
                            + " <img class='imag2' src='plug-in/login/images/cysl_up.png' style='display: none;' />" + " </li> ";
                } else if ("系统监控".equals(lang_context)) {

                    floor += " <li><img class='imag1' src='plug-in/login/images/xtjk.png' /> "
                            + " <img class='imag2' src='plug-in/login/images/xtjk_up.png' style='display: none;' />" + " </li> ";
                } else if (lang_context.contains("消息推送")) {
                    String s = "<div style='width:67px;position: absolute;top:40px;text-align:center;color:#909090;font-size:12px;'>消息推送</div>";
                    floor += " <li style='position: relative;'><img class='imag1' src='plug-in/login/images/msg.png' /> "
                            + " <img class='imag2' src='plug-in/login/images/msg_up.png' style='display: none;' />"
                            + s + "</li> ";
                } else {
                    //其他的为默认通用的图片模式
                    String s = "";
                    if (lang_context.length() >= 5 && lang_context.length() < 7) {
                        s = "<div style='width:67px;position: absolute;top:40px;text-align:center;color:#909090;font-size:12px;'><span style='letter-spacing:-1px;'>" + lang_context + "</span></div>";
                    } else if (lang_context.length() < 5) {
                        s = "<div style='width:67px;position: absolute;top:40px;text-align:center;color:#909090;font-size:12px;'>" + lang_context + "</div>";
                    } else if (lang_context.length() >= 7) {
                        s = "<div style='width:67px;position: absolute;top:40px;text-align:center;color:#909090;font-size:12px;'><span style='letter-spacing:-1px;'>" + lang_context.substring(0, 6) + "</span></div>";
                    }
                    floor += " <li style='position: relative;'><img class='imag1' src='plug-in/login/images/default.png' /> "
                            + " <img class='imag2' src='plug-in/login/images/default_up.png' style='display: none;' />"
                            + s + "</li> ";
                }
            }
        }

        return floor;
    }


    private static final String KEY_SPLIT = "-";//map key 分隔符
    private static final String MENU_TYPE_ID = "#";//采用控件ID方式的code前缀
    private static final String MENU_TYPE_CSS = ".";//采用Css样式方式的code前缀
    Map<String, FunctionView> functionMap = new HashMap<String, FunctionView>();//菜单map,key为菜单匹配规则的字符串
    Map<String, Operation> operationMap = new HashMap<String, Operation>();//菜单操作按钮map,key为菜单操作按钮匹配规则的字符串

    public void initMenu() {
        List<Function> functionList = systemService.findAll(Function.class);
        List<Operation> operationList = systemService.findAll(Operation.class);
        //设置菜单map
        for (Function function : functionList) {
            StringBuffer key = new StringBuffer();
            key.append(function.getFunctionName() == null ? "" : function.getFunctionName());
            key.append(KEY_SPLIT);
            key.append(function.getFunctionLevel() == null ? "" : function.getFunctionLevel());
            key.append(KEY_SPLIT);
            key.append(function.getFunctionUrl() == null ? "" : function.getFunctionUrl());
            FunctionView bean = new FunctionView();
            BeanUtils.copyProperties(function, bean);
            functionMap.put(key.toString(), bean);
        }
        //设置菜单操作按钮map
        for (Operation operation : operationList) {
            StringBuffer key = new StringBuffer();
            key.append(operation.getFunction() == null ? "" : operation.getFunction().getId());
            key.append(KEY_SPLIT);
            key.append(operation.getOperationCode() == null ? "" : operation.getOperationCode());
            operationMap.put(key.toString(), operation);
        }
        autoMenu();

    }

    /***
     1.扫描项目下，所有class，判断带有标签@AutoMenu
     2.循环判断@AutoMenu在系统中是否存在,如果不存在进行插入，如果存在不再插入
     比较规则[菜单名称-等级-菜单地址：全匹配]

     3.加载@AutoMenu下带有标签@AutoMenuOperation所有方法

     4.循环@AutoMenuOperation方法标签，判断该菜单下是否有该操作码配置，如果存在不插入，不存在进行插入
     比较规则[菜单ID-操作码 ：全匹配]
     */
    private void autoMenu() {
        //扫描Src目录下
//		Set<Class<?>> classSet = PackagesToScanUtil.getClasses(".*");
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        for (Class<?> clazz : classSet) {
            //判断当前类是否设置了菜单注解
            //未设置菜单注解就算在该类的方法上设置了菜单操作按钮注解也不进行菜单操作按钮的匹配
            if (clazz.isAnnotationPresent(AutoMenu.class)) {
                AutoMenu autoMenu = clazz.getAnnotation(AutoMenu.class);
                //菜单名称必须填写，否则不进行菜单和菜单操作按钮的匹配
                if (!StrUtils.isEmpty(autoMenu.name())) {
                    StringBuffer menuKey = new StringBuffer();
                    menuKey.append(autoMenu.name());
                    menuKey.append(KEY_SPLIT);
                    menuKey.append(autoMenu.level() == null ? "" : autoMenu.level());
                    menuKey.append(KEY_SPLIT);
                    menuKey.append(autoMenu.url() == null ? "" : autoMenu.url());

                    Function function = null;
                    //判断菜单map的key是否包含当前key，不包含则插入一条菜单数据
                    if (!functionMap.containsKey(menuKey.toString())) {
                        function = new Function();
                        function.setFunctionName(autoMenu.name());
                        function.setFunctionIframe(null);
                        function.setFunctionLevel(Short.valueOf(autoMenu.level()));
                        function.setFunctionOrder(Integer.toString(autoMenu.order()));
                        function.setFunctionUrl(autoMenu.url());

                        String iconId = autoMenu.icon();
                        if (!StrUtils.isEmpty(iconId)) {
                            Object obj = systemService.find(Icon.class, iconId);
                            if (obj != null) {
                                function.setIcon((Icon) obj);
                            } else {
                                function.setIcon(null);
                            }
                        } else {
                            function.setIcon(null);
                        }
                        Serializable id = systemService.save(function);
                        function.setId(id.toString());
                    } else {
                        FunctionView functionBean = functionMap.get(menuKey.toString());
                        BeanUtils.copyProperties(functionBean, function);
                    }

                    //获取该类的所有方法
                    Method[] methods = clazz.getDeclaredMethods();
                    for (Method method : methods) {
                        //判断当前方法是否设置了菜单操作按钮注解
                        if (method.isAnnotationPresent(AutoMenuOperation.class)) {
                            AutoMenuOperation autoMenuOperation = method.getAnnotation(AutoMenuOperation.class);
                            //操作码必须填写，否则不进行菜单操作按钮的匹配
                            if (!StrUtils.isEmpty(autoMenuOperation.code())) {
                                StringBuffer menuOperationKey = new StringBuffer();
                                menuOperationKey.append(function == null ? "" : function.getId());
                                menuOperationKey.append(KEY_SPLIT);

                                String code = "";
                                //设置code前缀
                                if (autoMenuOperation.codeType() == MenuCodeType.TAG) {
                                    code = autoMenuOperation.code();
                                } else if (autoMenuOperation.codeType() == MenuCodeType.ID) {
                                    code = MENU_TYPE_ID + autoMenuOperation.code();
                                } else if (autoMenuOperation.codeType() == MenuCodeType.CSS) {
                                    code = MENU_TYPE_CSS + autoMenuOperation.code();
                                }
                                menuOperationKey.append(code);

                                //判断菜单操作按钮map的key是否包含当前key，不包含则插入一条菜单操作按钮数据
                                if (!operationMap.containsKey(menuOperationKey.toString())) {
                                    Operation operation = new Operation();
                                    operation.setOperationName(autoMenuOperation.name());
                                    operation.setOperationCode(code);
                                    operation.setOperationIcon(null);
                                    operation.setStatus(Short.parseShort(Integer.toString(autoMenuOperation.status())));
                                    operation.setFunction(function);

                                    String iconId = autoMenuOperation.icon();
                                    if (!StrUtils.isEmpty(iconId)) {
                                        Icon icon = new Icon();
                                        icon.setId(iconId);
                                        operation.setIcon(icon);
                                    } else {
                                        operation.setIcon(null);
                                    }
                                    systemService.save(operation);
                                }
                            }
                        }
                    }
                }
            }

        }
    }

    @Override
    public List<Operation> findById(String functionId, String uid) {
        List<Operation> allOperation = this.systemService.findAllByProperty(Operation.class, "function.id", functionId);
        List<Operation> operations = new ArrayList<Operation>();
        if (allOperation.size() > 0) {
            for (Operation s : allOperation) {
                operations.add(s);
            }
            String hasOperSql = "SELECT operation FROM t_s_role_function fun, t_s_role_user role WHERE  " +
                    "fun.functionid='" + functionId + "' AND fun.operation!=''  AND fun.roleid=role.roleid AND role.userid='" + uid + "' ";

            List<String> hasOperList = this.systemService.findListBySql(hasOperSql);
            for (String operationIds : hasOperList) {
                for (String operationId : operationIds.split(",")) {
                    operationId = operationId.replaceAll(" ", "");
                    Operation operation = new Operation();
                    operation.setId(operationId);
                    operations.remove(operation);
                }
            }

        }
        return operations;
    }

    @Override
    public DataRuleDto installDataRule(Set<String> dataRuleCodes) {
        //Step.2  第二部分处理列表数据级权限
        //小川 -- 菜单数据规则集合(数据权限)
        List<DataRule> menuDataRules = new ArrayList<DataRule>();
        //小川 -- 菜单数据规则sql(数据权限)
        StringBuilder dataRoleSql = new StringBuilder();
        for (String dataRuleId : dataRuleCodes) {
            DataRule dataRule = systemService.findEntity(DataRule.class, dataRuleId);
            menuDataRules.add(dataRule);
            dataRoleSql.append(ObjectParseUtil.setSqlModel(dataRule));
        }
        List<DataRule> dataRules = DataRuleUtils.installDataSearchCondition(menuDataRules);//菜单数据规则集合
        String data = DataRuleUtils.installDataSearchCondition(dataRoleSql.toString());//菜单数据规则sql
        return new DataRuleDto(dataRules, data);
    }

    @Override
    public boolean hasMenuAuth(String requestPath, String clickFunctionId) {
        // 是否是功能表中管理的url
        boolean bMgrUrl = false;
        List<Function> functionList = systemService.findAll(Function.class);
        for (Function function : functionList) {
            if (function.getFunctionUrl() != null && function.getFunctionUrl().startsWith(requestPath)) {
                bMgrUrl = true;
                break;
            }
        }

        if (!bMgrUrl && (requestPath.indexOf("loginController.do") != -1 || clickFunctionId.length() == 0)) {
            return true;
        }

        if (!bMgrUrl) {
            return true;
        }

        User currLoginUser = ClientManager.getInstance().getClient(ContextHolderUtils.getSession().getId()).getUser();
        String userid = currLoginUser.getId();
        String sql = "SELECT DISTINCT f.id FROM t_s_function f,t_s_role_function  rf,t_s_role_user ru " +
                " WHERE f.id=rf.function_id AND rf.role_id=ru.role_id AND " +
                "ru.user_id='" + userid + "' AND f.function_url like '" + requestPath + "%'";
        List list = this.systemService.queryForListMap(sql);
        if (list.size() == 0) {
            String orgId = currLoginUser.getCurrentDepart().getId();
            String functionOfOrgSql = "SELECT DISTINCT f.id from t_s_function f, t_s_role_function rf, t_s_role_org ro  " +
                    "WHERE f.ID=rf.function_id AND rf.role_id=ro.role_id " +
                    "AND ro.org_id='" + orgId + "' AND f.function_url like '" + requestPath + "%'";
            List functionOfOrgList = this.systemService.queryForListMap(functionOfOrgSql);
            return functionOfOrgList.size() > 0;
        } else {
            return true;
        }
    }
}
