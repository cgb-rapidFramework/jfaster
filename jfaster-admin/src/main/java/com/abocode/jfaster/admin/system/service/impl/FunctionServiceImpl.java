package com.abocode.jfaster.admin.system.service.impl;

import com.abocode.jfaster.admin.system.repository.MutiLangRepository;
import com.abocode.jfaster.admin.system.repository.ResourceRepository;
import com.abocode.jfaster.admin.system.repository.UserRepository;
import com.abocode.jfaster.admin.system.service.BeanToTagConverter;
import com.abocode.jfaster.admin.system.service.FunctionService;
import com.abocode.jfaster.core.common.constants.Globals;
import com.abocode.jfaster.core.common.model.json.ComboTree;
import com.abocode.jfaster.core.common.model.json.TreeGrid;
import com.abocode.jfaster.core.common.util.ContextHolderUtils;
import com.abocode.jfaster.core.platform.utils.MutiLangUtils;
import com.abocode.jfaster.core.platform.utils.NumberComparator;
import com.abocode.jfaster.core.common.util.StringUtils;
import com.abocode.jfaster.core.persistence.hibernate.qbc.CriteriaQuery;
import com.abocode.jfaster.core.platform.view.interactions.datatable.SortDirection;
import com.abocode.jfaster.core.platform.view.interactions.easyui.ComboTreeModel;
import com.abocode.jfaster.core.platform.view.interactions.easyui.TreeGridModel;
import com.abocode.jfaster.core.web.manager.ClientManager;
import com.abocode.jfaster.system.entity.Function;
import com.abocode.jfaster.system.entity.Operation;
import com.abocode.jfaster.system.entity.User;
import com.abocode.jfaster.admin.system.repository.SystemRepository;
import com.abocode.jfaster.core.web.manager.ClientBean;
import com.abocode.jfaster.core.platform.view.FunctionView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
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
    private MutiLangRepository mutiLangRepository;
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
	            /*if(function.getFunctionType().intValue()==Globals.Function_TYPE_FROM.intValue()){
					//如果为表单或者弹出 不显示在系统菜单里面
					continue;
				}*/
                if (!functionMap.containsKey(function.getFunctionLevel() + 0)) {
                    functionMap.put(function.getFunctionLevel() + 0,
                            new ArrayList<FunctionView>());
                }

                FunctionView functionBean = BeanToTagConverter.convertFunction(function);
                functionMap.get(function.getFunctionLevel() + 0).add(functionBean);
            }
            // 菜单栏排序
            Collection<List<FunctionView>> c = functionMap.values();
            for (List<FunctionView> list : c) {
                Collections.sort(list, new NumberComparator());
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
        systemService.addLog(message, Globals.Log_Type_DEL,
                Globals.Log_Leavel_INFO);
        systemService.addLog(message, Globals.Log_Type_DEL,
                Globals.Log_Leavel_INFO);
    }

    @Override
    public void delByLopId(String id) {
        Operation operation = systemService.findEntity(Operation.class, id);
        userService.delete(operation);
        String message = MutiLangUtils.paramDelSuccess("common.operation");
        systemService.addLog(message, Globals.Log_Type_DEL,
                Globals.Log_Leavel_INFO);
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
                List<Function> subFunction1 = systemService.findAllByProperty(Function.class, "Function.id", tsFunction.getId());
                updateSubFunction(subFunction1, tsFunction);
            }
        }
    }

    @Override
    public void saveFunction(Function function) {
        String message;
        if (StringUtils.isNotEmpty(function.getId())) {
            message = MutiLangUtils.paramUpdSuccess("common.menu");
            userService.saveOrUpdate(function);
            systemService.addLog(message, Globals.Log_Type_UPDATE,
                    Globals.Log_Leavel_INFO);
            List<Function> subFunction = systemService.findAllByProperty(Function.class, "Function.id", function.getId());
            updateSubFunction(subFunction, function);
            systemService.flushRoleFunciton(function.getId(), function);
        } else {
            if (function.getFunctionLevel().equals(Globals.Function_Leave_ONE)) {
			/*	List<TSFunction> functionList = systemService.findAllByProperty(
						TSFunction.class, "functionLevel",
						Globals.Function_Leave_ONE);
				 int ordre=functionList.size()+1;
				 function.setFunctionOrder(Globals.Function_Order_ONE+ordre);*/
                function.setFunctionOrder(function.getFunctionOrder());
            } else {
			/*	List<TSFunction> functionList = systemService.findAllByProperty(
						TSFunction.class, "functionLevel",
						Globals.Function_Leave_TWO);
				 int ordre=functionList.size()+1;
				 function.setFunctionOrder(Globals.Function_Order_TWO+ordre);*/
                function.setFunctionOrder(function.getFunctionOrder());
            }
            message = MutiLangUtils.paramAddSuccess("common.menu");
            systemService.save(function);
            systemService.addLog(message, Globals.Log_Type_INSERT,
                    Globals.Log_Leavel_INFO);
        }

    }

    @Override
    public String search(String name) {
        String menuListMap = "";
        CriteriaQuery cq = new CriteriaQuery(Function.class);

        cq.notEq("functionLevel", Short.valueOf("0"));
        if (name == null || "".equals(name)) {
            cq.isNull("Function");
        } else {
            String name1 = "%" + name + "%";
            cq.like("functionName", name1);
        }
        cq.add();
        List<Function> functionList = systemService.findListByCq(
                cq, false);
        if (functionList != null && functionList.size() > 0) {
            for (int i = 0; i < functionList.size(); i++) {
                String icon;
                if (!StringUtils.isEmpty(functionList.get(i).getIconDesk())) {
                    icon = functionList.get(i).getIconDesk().getIconPath();
                } else {
                    icon = "plug-in/sliding/icon/default.png";
                }
                menuListMap = menuListMap
                        + "<div type='"
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
                        "</div>";
            }
        } else {
            menuListMap = menuListMap + "很遗憾，在系统中没有检索到与“" + name + "”相关的信息！";
        }
        return menuListMap;
    }

    @Override
    public List<ComboTree> setParentFunction(String selfId, String comboTreeId) {
        CriteriaQuery cq = new CriteriaQuery(Function.class);
        if (null != selfId) {
            cq.notEq("id", selfId);
        }
        if (comboTreeId != null) {
            cq.eq("Function.id", comboTreeId);
        }
        if (comboTreeId == null) {
            cq.isNull("Function");
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
            cq.eq("Function.id", treeGridId);
        }
        if (treeGridId == null) {
            cq.isNull("Function");
        }
        cq.addOrder("functionOrder", SortDirection.asc);
        cq.add();
        List<Function> functionList = systemService.findListByCq(cq, false);

        List<FunctionView> functionBeanList = BeanToTagConverter.convertFunctions(functionList);
        Collections.sort(functionBeanList, new NumberComparator());
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

        List<FunctionView> primaryMenu =getFunctionMap(user).get(0);
        String floor = "";
        if (primaryMenu == null) {
            return floor;
        }
        for (FunctionView function : primaryMenu) {
            if (function.getFunctionLevel() == 0) {

                String lang_key = function.getFunctionName();
                String lang_context = mutiLangRepository.getLang(lang_key);

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
}
