package com.abocode.jfaster.admin.system.service;

import com.abocode.jfaster.core.common.model.json.ComboTree;
import com.abocode.jfaster.core.common.model.json.TreeGrid;
import com.abocode.jfaster.system.entity.Territory;

import java.util.List;

public interface TerritoryService {
    List<TreeGrid> findById(String id);

    List<ComboTree> findComboTree(String id);

    void save(Territory territory);

    void del(String id);
}
