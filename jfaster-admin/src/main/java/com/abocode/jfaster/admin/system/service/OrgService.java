package com.abocode.jfaster.admin.system.service;

import com.abocode.jfaster.api.system.OrgDto;
import com.abocode.jfaster.core.common.model.json.ComboTree;
import com.abocode.jfaster.core.common.model.json.TreeGrid;
import com.abocode.jfaster.system.entity.Org;

import java.util.List;

public interface OrgService {
    void save(Org depart);

    List<Org> findAll(String selfId, String id);

    List<TreeGrid>  findTreeGrid(String isSearch, OrgDto orgDto, TreeGrid treegrid);

    List<ComboTree> buildComboTree();

    void saveOrgUserList(Org depart, String orgIds);

    List<Org> find(String orgId);

    List<String> findIdByUserId(String id);

    List<Org> findOrgByUserId(String userId);
}
