package com.abocode.jfaster.admin.system.service.impl;

import com.abocode.jfaster.admin.system.service.BeanToTagConverter;
import com.abocode.jfaster.core.common.util.ContextHolderUtils;
import com.abocode.jfaster.core.common.util.NumberComparator;
import com.abocode.jfaster.core.web.manager.ClientManager;
import com.abocode.jfaster.system.entity.Function;
import com.abocode.jfaster.system.entity.User;
import com.abocode.jfaster.admin.system.repository.SystemRepository;
import com.abocode.jfaster.admin.system.service.FunctionService;
import com.abocode.jfaster.admin.system.dto.bean.ClientBean;
import com.abocode.jfaster.admin.system.dto.view.FunctionView;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.*;

@Service
public class FunctionApplicationService implements FunctionService {
    @Resource
    private SystemRepository systemService;
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
                    .append("where ru.role.id=rf.role.id and rf.function.id=f.id and ru.user.id=?0 ");
            StringBuilder hqlsb2=new StringBuilder("select distinct c from Function c,RoleOrg b,UserOrg a ")
                    .append("where a.org.id=b.org.id and b.role.id=c.id and a.user.id=?0");
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

                FunctionView functionBean= BeanToTagConverter.convertFunction(function);
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
