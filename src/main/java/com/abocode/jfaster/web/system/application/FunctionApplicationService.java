package com.abocode.jfaster.web.system.application;

import com.abocode.jfaster.core.common.util.BeanToTagUtils;
import com.abocode.jfaster.core.common.util.ContextHolderUtils;
import com.abocode.jfaster.core.common.util.NumberComparator;
import com.abocode.jfaster.web.common.manager.ClientManager;
import com.abocode.jfaster.web.system.domain.entity.Function;
import com.abocode.jfaster.web.system.domain.entity.User;
import com.abocode.jfaster.web.system.domain.repository.SystemService;
import com.abocode.jfaster.web.system.interfaces.IFunctionService;
import com.abocode.jfaster.web.system.interfaces.bean.ClientBean;
import com.abocode.jfaster.web.system.interfaces.view.FunctionView;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.*;

@Service
public class FunctionApplicationService implements IFunctionService {
    @Resource
    private SystemService systemService;
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
            StringBuilder hqlsb1=new StringBuilder("select distinct f from Function f,RoleFunction rf,RoleUser ru  ")
                    .append("where ru.TSRole.id=rf.TSRole.id and rf.TSFunction.id=f.id and ru.TSUser.id=?0 ");
            StringBuilder hqlsb2=new StringBuilder("select distinct c from Function c,RoleOrg b,UserOrg a ")
                    .append("where a.tsDepart.id=b.tsDepart.id and b.tsRole.id=c.id and a.tsUser.id=?0");
            Object[] object=new Object[]{user.getId()};
            List<Function> list1 = systemService.findByHql(hqlsb1.toString(),object);
            List<Function> list2 = systemService.findByHql(hqlsb2.toString(),object);
            for(Function function:list1){
                loginActionlist.put(function.getId(),function);
            }
            for(Function function:list2){
                loginActionlist.put(function.getId(),function);
            }
            client.setFunctions(loginActionlist);
            //保存菜单到seesion中心
            session.setAttribute("functions"+session.getId(), loginActionlist);
        }
        return client.getFunctions();
    }

    /**
     * 获取权限的map
     *
     * @param user
     * @return
     */
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

                FunctionView functionBean= BeanToTagUtils.convertFunction(function);
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
}
