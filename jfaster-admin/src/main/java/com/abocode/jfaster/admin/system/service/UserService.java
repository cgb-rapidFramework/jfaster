package com.abocode.jfaster.admin.system.service;

import com.abocode.jfaster.admin.system.dto.ExlUserDto;
import com.abocode.jfaster.core.common.model.json.ComboBox;
import com.abocode.jfaster.core.repository.DataGridParam;
import com.abocode.jfaster.core.persistence.hibernate.qbc.CriteriaQuery;
import com.abocode.jfaster.system.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface UserService {

    String getMenus(User u);

    Object getAll();

    void restPassword(String id, String password);

    void updatePwd(User user, String password, String newPassword);

    void lockById(String id);

    List<ComboBox> findComboBox(String id, String[] fields);

    CriteriaQuery buildCq(User user, DataGridParam dataGridParam, String orgIds);

    void del(String id);

    void saveUser(User user, String roleId, String password, String orgIds);

    void importFile(Map<String, MultipartFile> fileMap);

    List<ExlUserDto> findExportUserList(User user, String orgIds, DataGridParam dataGridParam);
}
