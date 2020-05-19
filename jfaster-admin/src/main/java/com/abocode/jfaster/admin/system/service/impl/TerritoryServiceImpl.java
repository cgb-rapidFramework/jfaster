package com.abocode.jfaster.admin.system.service.impl;

import com.abocode.jfaster.admin.system.repository.ResourceRepository;
import com.abocode.jfaster.admin.system.repository.SystemRepository;
import com.abocode.jfaster.admin.system.service.TerritoryService;
import com.abocode.jfaster.core.common.constants.Globals;
import com.abocode.jfaster.core.common.model.json.ComboTree;
import com.abocode.jfaster.core.common.model.json.TreeGrid;
import com.abocode.jfaster.core.common.util.MutiLangUtils;
import com.abocode.jfaster.core.persistence.hibernate.qbc.CriteriaQuery;
import com.abocode.jfaster.core.platform.view.interactions.datatable.SortDirection;
import com.abocode.jfaster.core.platform.view.interactions.easyui.ComboTreeModel;
import com.abocode.jfaster.core.platform.view.interactions.easyui.TreeGridModel;
import com.abocode.jfaster.system.entity.Territory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class TerritoryServiceImpl implements TerritoryService {
    @Autowired
    private ResourceRepository resourceRepository;
    @Autowired
    private SystemRepository systemRepository;
    @Override
    public List<TreeGrid> findById(String id) {
        CriteriaQuery cq = new CriteriaQuery(Territory.class);
        if (id != null) {
            cq.eq("parentTerritory.id", id);
        }
        if (id == null) {
            cq.eq("parentTerritory.id", "0");//这个是全国最高级
        }

        cq.addOrder("territorySort", SortDirection.asc);
        cq.add();
        List<Territory> territoryList = systemRepository.findListByCq(cq, false);
        TreeGridModel treeGridModel = new TreeGridModel();
        treeGridModel.setIcon("");
        treeGridModel.setTextField("territoryName");
        treeGridModel.setParentText("Territory_territoryName");
        treeGridModel.setParentId("Territory_id");
        treeGridModel.setSrc("territoryCode");
        treeGridModel.setIdField("id");
        treeGridModel.setChildList("Territorys");
        treeGridModel.setOrder("territorySort");
        List<TreeGrid> treeGrids = resourceRepository.treegrid(territoryList, treeGridModel);
        return  treeGrids;
    }

    @Override
    public List<ComboTree> findComboTree(String id) {
        CriteriaQuery cq = new CriteriaQuery(Territory.class);
        if (id != null) {
            cq.eq("parentTerritory.id", id);
        }
        if (id == null) {
            cq.isNull("parentTerritory");
        }
        cq.add();
        List<Territory> territoryList = systemRepository.findListByCq(cq, false);
        ComboTreeModel comboTreeModel = new ComboTreeModel("id", "territoryName", "territories");
        List<ComboTree> comboTrees = resourceRepository.ComboTree(territoryList, comboTreeModel, null, false);
        return  comboTrees;
    }

    @Override
    public void save(Territory territory) {
        String functionOrder = territory.getTerritorySort();
        if (StringUtils.isEmpty(functionOrder)) {
            territory.setTerritorySort("0");
        }
        if (territory.getParentTerritory().getId().equals("")) {
            territory.setParentTerritory(null);
        } else {
            Territory parent = systemRepository.findEntity(Territory.class, territory.getParentTerritory().getId());
            territory.setTerritoryLevel(Short.valueOf(parent.getTerritoryLevel() + 1 + ""));
        }
        String message;
        if (!StringUtils.isEmpty(territory.getId())) {
            message = "地域: " + territory.getTerritoryName() + "被更新成功";
            systemRepository.saveOrUpdate(territory);
            systemRepository.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
        } else {
            territory.setTerritorySort(territory.getTerritorySort());
            message = "地域: " + territory.getTerritoryName() + "被添加成功";
            systemRepository.save(territory);
            systemRepository.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
        }
    }

    @Override
    public void del(String id) {
        Territory territory = systemRepository.findEntity(Territory.class, id);
        String message = "地域: " + territory.getTerritoryName() + "被删除成功";
        systemRepository.delete(territory);
        systemRepository.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
    }
}
