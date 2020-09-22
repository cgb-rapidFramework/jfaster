package com.abocode.jfaster.admin.system.service.impl;

import com.abocode.jfaster.admin.system.repository.LanguageRepository;
import com.abocode.jfaster.admin.system.repository.SystemRepository;
import com.abocode.jfaster.admin.system.service.UserLoginService;
import com.abocode.jfaster.core.common.constants.Globals;
import com.abocode.jfaster.core.common.util.ContextHolderUtils;
import com.abocode.jfaster.core.common.util.ConvertUtils;
import com.abocode.jfaster.core.web.manager.ClientBean;
import com.abocode.jfaster.core.web.manager.ClientManager;
import com.abocode.jfaster.core.web.manager.SessionShareCenter;
import com.abocode.jfaster.system.entity.Org;
import com.abocode.jfaster.system.entity.Role;
import com.abocode.jfaster.system.entity.RoleUser;
import com.abocode.jfaster.system.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import javax.servlet.http.HttpSession;
import java.util.*;

@Service
public class UserLoginServiceImpl implements UserLoginService {
    @Autowired
    private SystemRepository systemService;
    @Autowired
    private LanguageRepository mutiLangService;
    @Override
    public Map<String, Object> getLoginMap(User u, String orgId, String ip) {
        Map<String, Object> attrMap = new HashMap<>();
        if (ConvertUtils.isEmpty(orgId)) { // 没有传组织机构参数，则获取当前用户的组织机构
            //获取默认部门
            Long orgNum = systemService.queryForCount("select count(1) from t_s_user_org where user_id =?", u.getId());
            if (orgNum > 1) {
                User res = new User();
                res.setId(u.getId());
                //暂时未处理多部门
                attrMap.put("orgNum", orgNum);
                attrMap.put("user", res);
            } else {
                Map<String, Object> userOrgMap = systemService.queryForMap("select org_id as orgId from t_s_user_org where user_id=?", u.getId());
                saveLoginSuccessInfo(u, (String) userOrgMap.get("orgId"),ip);
            }
        } else {
            attrMap.put("orgNum", 1);
           saveLoginSuccessInfo(u, orgId,ip);
        }
        return attrMap;
    }


    @Override
    public List<Role> cahModelMap(ModelMap modelMap, String id) {
        List<Role> roleList = new ArrayList<>();
        List<RoleUser> rUsers = systemService.findAllByProperty(RoleUser.class, "user.id",id);
        StringBuilder roleBuilder=new StringBuilder();
        for (RoleUser ru : rUsers) {
            Role role = ru.getRole();
            roleBuilder.append(role.getRoleName()  ).append(",");
            roleList.add(role);
        }

        String roles = roleBuilder.toString();
        modelMap.put("roleName", roles.length() > 0? roles.substring(0, roles.length() - 1):roles);
        return roleList;
    }


    /**
     * 保存用户登录的信息，并将当前登录用户的组织机构赋值到用户实体中；
     * @param user  当前登录用户
     * @param orgId 组织主键
     */
    private void saveLoginSuccessInfo(User user, String orgId, String ip) {
        Org currentDepart = systemService.find(Org.class, orgId);
        user.setCurrentDepart(currentDepart);
        HttpSession session = ContextHolderUtils.getSession();
        String message = mutiLangService.getLang("common.user") + ": " + user.getUsername() + "["
                + currentDepart.getOrgName() + "]" + mutiLangService.getLang("common.login.success");
        ClientBean client = new ClientBean();
        client.setIp(ip);
        client.setLoginTime(new Date());
        client.setUser(user);
        SessionShareCenter.putUserId(client.getUser().getId());
        SessionShareCenter.putClient(client);
        ClientManager.getInstance().addClinet(session.getId(), client);
        // 添加登陆日志
        systemService.addLog(message, Globals.LOG_TYPE_LOGIN, Globals.LOG_LEVEL);
    }

}
