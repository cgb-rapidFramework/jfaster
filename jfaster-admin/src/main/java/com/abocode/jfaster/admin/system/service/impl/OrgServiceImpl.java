package com.abocode.jfaster.admin.system.service.impl;

import com.abocode.jfaster.admin.system.repository.ResourceRepository;
import com.abocode.jfaster.admin.system.repository.SystemRepository;
import com.abocode.jfaster.admin.system.repository.UserRepository;
import com.abocode.jfaster.admin.system.service.OrgService;
import com.abocode.jfaster.api.system.OrgDto;
import com.abocode.jfaster.core.common.constants.Globals;
import com.abocode.jfaster.core.common.model.json.ComboTree;
import com.abocode.jfaster.core.common.model.json.TreeGrid;
import com.abocode.jfaster.core.common.util.IdUtils;
import com.abocode.jfaster.core.common.util.StrUtils;
import com.abocode.jfaster.core.persistence.hibernate.hql.HqlGenerateUtil;
import com.abocode.jfaster.core.persistence.hibernate.qbc.CriteriaQuery;
import com.abocode.jfaster.core.platform.utils.MutiLangUtils;
import com.abocode.jfaster.core.platform.view.interactions.easyui.ComboTreeModel;
import com.abocode.jfaster.core.platform.view.interactions.easyui.TreeGridModel;
import com.abocode.jfaster.system.entity.Org;
import com.abocode.jfaster.system.entity.User;
import com.abocode.jfaster.system.entity.UserOrg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        if (!StrUtils.isEmpty(depart.getId())) {
            message = MutiLangUtils.paramUpdSuccess("common.department");
            userRepository.saveOrUpdate(depart);
            systemRepository.addLog(message, Globals.LOG_TYPE_UPDATE, Globals.LOG_LEVEL);
        } else {
            message = MutiLangUtils.paramAddSuccess("common.department");
            userRepository.save(depart);
            systemRepository.addLog(message, Globals.LOG_TYPE_INSERT, Globals.LOG_LEVEL);
        }

    }

    @Override
    public List<Org> findAll(String selfId, String id) {
        CriteriaQuery cq = new CriteriaQuery(Org.class);
        if (null != selfId) {
            cq.notEq("id", selfId);
        }
        if (StrUtils.isEmpty(id)) {
            cq.eq("parentOrg.id", id);
        } else {
            cq.isNull("parentOrg");
        }
        cq.add();
        List<Org> departsList = systemRepository.findListByCq(cq, false);
        return departsList;
    }

    @Override
    public List<TreeGrid> findTreeGrid(String isSearch,  OrgDto orgDto, TreeGrid treegrid) {

        CriteriaQuery cq = new CriteriaQuery(Org.class);
        if ("yes".equals(isSearch)) {
            treegrid.setId(null);
            orgDto.setId(null);
        }
        if (null != orgDto.getOrgName()) {
            HqlGenerateUtil.installHql(cq, orgDto);
        }
        if (treegrid.getId() != null) {
            cq.eq("parentOrg.id", treegrid.getId());
        }
        if (treegrid.getId() == null) {
            cq.isNull("parentOrg");
        }
        cq.add();


        List<TreeGrid> departList = systemRepository.findListByCq(cq, false);
        if (departList.size() == 0 && orgDto.getOrgName() != null) {
            cq = new CriteriaQuery(Org.class);
            HqlGenerateUtil.installHql(cq, orgDto);
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

    @Override
    public List<Org> find(String orgId) {
        List<Org> departList = new ArrayList<Org>();

        if (!StrUtils.isEmpty(orgId)) {
            departList.add((userRepository.findEntity(Org.class, orgId)));
        } else {
            departList.addAll(userRepository.findAll(Org.class));
        }
        return  departList;
    }

    @Override
    public List<String> findIdByUserId(String id) {
        return    userRepository.findByHql("select d.id from Org d,UserOrg uo where d.id=uo.parentOrg.id and uo.user.id=?0", new String[]{id});
    }

    @Override
    public List<Org> findOrgByUserId(String userId) {
        List<Org> orgList = new ArrayList<Org>();
        List<Object[]> orgArrList = userRepository.findByHql("from Org d,UserOrg uo where d.id=uo.parentOrg.id and uo.user.id=?0", new String[]{userId});
        for (Object[] departs : orgArrList) {
            orgList.add((Org) departs[0]);
        }
        return  orgList;
    }
}
