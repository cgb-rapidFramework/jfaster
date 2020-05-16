package com.abocode.jfaster.admin.system.service;

import com.abocode.jfaster.system.entity.Role;
import com.abocode.jfaster.system.entity.User;
import org.springframework.ui.ModelMap;

import java.util.List;
import java.util.Map;

public interface UserLoginService {
    Map<String, Object> getLoginMap(User u, String id, String orgId);

    List<Role> cahModelMap(ModelMap modelMap, String id);
}
