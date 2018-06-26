package com.abocode.jfaster.web.system.application;

import com.abocode.jfaster.web.system.domain.entity.User;

public interface IUserService {

    String getMenus(User u);

    Object getAll();

    void restPassword(String id, String password);
}
