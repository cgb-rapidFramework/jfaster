package com.abocode.jfaster.admin.system.service;

import com.abocode.jfaster.system.entity.User;

public interface UserService {

    String getMenus(User u);

    Object getAll();

    void restPassword(String id, String password);
}
