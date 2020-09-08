package com.abocode.jfaster.admin.system.repository;

import com.abocode.jfaster.admin.system.dto.ExlUserDto;
import com.abocode.jfaster.core.persistence.hibernate.qbc.CriteriaQuery;
import com.abocode.jfaster.core.repository.CommonRepository;
import com.abocode.jfaster.core.repository.DataGridParam;
import com.abocode.jfaster.system.entity.Role;
import com.abocode.jfaster.system.entity.User;

import java.util.List;

public interface UserRepository extends CommonRepository {

    User checkUserExits(String username,String password);

    String getUserRole(User user);

    void pwdInit(User user, String newPwd);

    int getUsersOfThisRole(String id);

    List<ExlUserDto> getExlUserList(DataGridParam dataGridParam, User user, CriteriaQuery cq);

    List<Role> findRoleById(String id);
}
