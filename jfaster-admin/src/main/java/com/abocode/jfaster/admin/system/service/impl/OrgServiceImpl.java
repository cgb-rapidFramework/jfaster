package com.abocode.jfaster.admin.system.service.impl;

import com.abocode.jfaster.admin.system.repository.ResourceRepository;
import com.abocode.jfaster.admin.system.repository.SystemRepository;
import com.abocode.jfaster.admin.system.repository.UserRepository;
import com.abocode.jfaster.admin.system.service.OrgService;
import com.abocode.jfaster.core.common.constants.Globals;
import com.abocode.jfaster.core.common.model.json.ComboTree;
import com.abocode.jfaster.core.common.model.json.TreeGrid;
import com.abocode.jfaster.core.common.util.ConvertUtils;
import com.abocode.jfaster.core.common.util.IdUtils;
import com.abocode.jfaster.core.common.util.MutiLangUtils;
import com.abocode.jfaster.core.common.util.StringUtils;
import com.abocode.jfaster.core.persistence.hibernate.qbc.CriteriaQuery;
import com.abocode.jfaster.core.platform.view.interactions.easyui.ComboTreeModel;
import com.abocode.jfaster.core.platform.view.interactions.easyui.TreeGridModel;
import com.abocode.jfaster.core.web.hqlsearch.HqlGenerateUtil;
import com.abocode.jfaster.system.entity.Org;
import com.abocode.jfaster.system.entity.User;
import com.abocode.jfaster.system.entity.UserOrg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrgServiceImpl implements OrgService {
    @Autowired
    private ResourceRepository resourceService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SystemRepository systemRepository;

    @Override
    public void save(Org depart) {
        String message;
        if (!StringUtils.isEmpty(depart.getId())) {
            message = MutiLangUtils.paramUpdSuccess("common.department");
            userRepository.saveOrUpdate(depart);
            systemRepository.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
        } else {
            message = MutiLangUtils.paramAddSuccess("common.department");
            userRepository.save(depart);
            systemRepository.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
        }

    }

    @Override
    public List<Org> findAll(String selfId, String id) {
        CriteriaQuery cq = new CriteriaQuery(Org.class);
        if (null != selfId) {
            cq.notEq("id", selfId);
        }
        if (StringUtils.isEmpty(id)) {
            cq.eq("parentOrg.id", id);
        } else {
            cq.isNull("parentOrg");
        }
        cq.add();
        List<Org> departsList = systemRepository.findListByCq(cq, false);
        return departsList;
    }

    @Override
    public List<TreeGrid> findTreeGrid(String isSearch, Org tSDepart, TreeGrid treegrid) {
        CriteriaQuery cq = new CriteriaQuery(Org.class);
        if ("yes".equals(isSearch)) {
            treegrid.setId(null);
            tSDepart.setId(null);
        }
        if (null != tSDepart.getOrgName()) {
            HqlGenerateUtil.installHql(cq, tSDepart);
        }
        if (treegrid.getId() != null) {
            cq.eq("parentOrg.id", treegrid.getId());
        }
        if (treegrid.getId() == null) {
            cq.isNull("parentOrg");
        }
        cq.add();


        List<TreeGrid> departList = systemRepository.findListByCq(cq, false);
        if (departList.size() == 0 && tSDepart.getOrgName() != null) {
            cq = new CriteriaQuery(Org.class);
            Org parDepart = new Org();
            tSDepart.setParentOrg(parDepart);
            HqlGenerateUtil.installHql(cq, tSDepart);
            departList = systemRepository.findListByCq(cq, false);
        }

        TreeGridModel treeGridModel = new TreeGridModel();
        treeGridModel.setTextField("departname");
        treeGridModel.setParentText("PDepart_departname");
        treeGridModel.setParentId("PDepart_id");
        treeGridModel.setSrc("description");
        treeGridModel.setIdField("id");
        treeGridModel.setChildList("Departs");
        Map<String, Object> fieldMap = new HashMap<String, Object>();
        fieldMap.put("orgCode", "orgCode");
        fieldMap.put("orgType", "orgType");
        treeGridModel.setFieldMap(fieldMap);
        List<TreeGrid> treeGrids = resourceService.treegrid(departList, treeGridModel);
        return treeGrids;
    }

    @Override
    public List<ComboTree> buildComboTree() {
        List<Org> departsList = systemRepository.findByHql("from Org where parentOrg.id is null");
        ComboTreeModel comboTreeModel = new ComboTreeModel("id", "departname", "orgs");
        return resourceService.ComboTree(departsList, comboTreeModel, null, true);
    }
    /**
     * 保存 组织机构-用户 关系信息
     * @param   depart
     * @param   orgIds
     */

    @Override
    public void saveOrgUserList(Org depart, String orgIds) {
        List<UserOrg> userOrgList = new ArrayList<UserOrg>();
        List<String> userIdList = IdUtils.extractIdListByComma(orgIds);
        for (String userId : userIdList) {
            User user = new User();
            user.setId(userId);
            UserOrg userOrg = new UserOrg();
            userOrg.setUser(user);
            userOrg.setOrg(depart);
            userOrgList.add(userOrg);
        }
        if (!userOrgList.isEmpty()) {
            systemRepository.batchSave(userOrgList);
        }
    }
}
