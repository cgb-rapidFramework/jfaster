package com.abocode.jfaster.admin.system.repository.persistence.hibernate;

import com.abocode.jfaster.admin.system.dto.view.IconView;
import com.abocode.jfaster.core.common.util.*;
import com.abocode.jfaster.core.persistence.hibernate.qbc.CriteriaQuery;
import com.abocode.jfaster.core.repository.persistence.hibernate.CommonRepositoryImpl;
import com.abocode.jfaster.admin.system.dto.view.OperationView;
import com.abocode.jfaster.admin.system.dto.view.TypeView;
import com.abocode.jfaster.admin.system.dto.view.TypeGroupView;
import com.abocode.jfaster.core.common.container.SystemContainer;
import com.abocode.jfaster.core.common.container.SystemContainer.IconContainer;
import com.abocode.jfaster.core.common.container.SystemContainer.TypeGroupContainer;
import com.abocode.jfaster.admin.system.dto.bean.DictBean;
import com.abocode.jfaster.admin.system.repository.SystemRepository;
import com.abocode.jfaster.admin.system.repository.UserRepository;
import com.abocode.jfaster.system.entity.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Service
@Transactional
public class SystemRepositoryImpl extends CommonRepositoryImpl implements SystemRepository {

	
	@Autowired
	private UserRepository userService;
	
	public User checkUserExits(User user) throws Exception {
		return this.userService.checkUserExits(user);
	}
	
	public List<DictBean> queryDict(String dicTable, String dicCode, String dicText){
		 throw new RuntimeException("this's function not implements");

		//step.1 如果没有字典表则使用系统字典表/*
		/*
		List<DictBean> dictList = null;
		if(StringUtils.isEmpty(dicTable)){
			dictList = jeecgDictDao.querySystemDict(dicCode);
		}else {
			dicText = StringUtils.isEmpty(dicText, dicCode);
			dictList = jeecgDictDao.queryCustomDict(dicTable, dicCode, dicText);
		}
		return dictList;
		*/

	}

	/**
	 * 添加日志
	 */
	public void addLog(String logcontent, Short loglevel, Short operatetype) {
		HttpServletRequest request = ContextHolderUtils.getRequest();
		String broswer = BrowserUtils.checkBrowse(request);
		Log log = new Log();
		log.setLogcontent(logcontent);
		log.setLoglevel(loglevel);
		log.setOperatetype(operatetype);
		log.setNote(ConvertUtils.getIp());
		log.setBroswer(broswer);
		log.setOperatetime(DateUtils.getTimestamp());
		log.setUserid(SessionUtils.getCurrentUser().getId());
		commonDao.saveOrUpdate(log);
	}

	/**
	 * 根据类型编码和类型名称获取Type,如果为空则创建一个
	 * 
	 * @param typecode
	 * @param typename
	 * @return
	 */
	public Type getType(String typecode, String typename, TypeGroup tsTypegroup) {
		Type actType = commonDao.findUniqueByProperty(Type.class, "typecode", typecode);
		if (actType == null) {
			actType = new Type();
			actType.setTypecode(typecode);
			actType.setTypename(typename);
			actType.setTypeGroup(tsTypegroup);
			commonDao.save(actType);
		}
		return actType;

	}

	/**
	 * 根据类型分组编码和名称获取TypeGroup,如果为空则创建一个
	 * 
	 * @param typegroupcode
	 * @param typgroupename
	 * @return
	 */
	public TypeGroup getTypeGroup(String typegroupcode, String typgroupename) {
		TypeGroup tsTypegroup = commonDao.findUniqueByProperty(TypeGroup.class, "typegroupcode", typegroupcode);
		if (tsTypegroup == null) {
			tsTypegroup = new TypeGroup();
			tsTypegroup.setTypeGroupCode(typegroupcode);
			tsTypegroup.setTypeGroupName(typgroupename);
			commonDao.save(tsTypegroup);
		}
		return tsTypegroup;
	}

	
	public TypeGroup getTypeGroupByCode(String typegroupCode) {
		TypeGroup typeGroup = commonDao.findUniqueByProperty(TypeGroup.class, "typegroupcode", typegroupCode);
		return typeGroup;
	}

	
	public void initAllTypeGroups() {
		List<TypeGroup> typeGroups = this.commonDao.findAll(TypeGroup.class);
		for (TypeGroup typeGroup : typeGroups) {
			TypeGroupView typegroupBean= BeanToTagUtils.convertTypeGroup(typeGroup);
			TypeGroupContainer.allTypeGroups.put(typeGroup.getTypeGroupCode().toLowerCase(), typegroupBean);
			List<Type> tsTypes = this.commonDao.findAllByProperty(Type.class, "TSTypegroup.id", typeGroup.getId());
			List<TypeView> types=BeanToTagUtils.convertTypes(tsTypes);
			TypeGroupContainer.allTypes.put(typeGroup.getTypeGroupCode().toLowerCase(), types);
		}
	}

	
	public void refleshTypesCach(Type type) {
		TypeGroup tsTypegroup = type.getTypeGroup();
		TypeGroup typeGroupEntity = this.commonDao.find(TypeGroup.class, tsTypegroup.getId());
		List<Type> tsTypes = this.commonDao.findAllByProperty(Type.class, "TSTypegroup.id", tsTypegroup.getId());
		List<TypeView> types=BeanToTagUtils.convertTypes(tsTypes);
		TypeGroupContainer.allTypes.put(typeGroupEntity.getTypeGroupCode().toLowerCase(), types);
	}

	
	public void refleshTypeGroupCach() {
		TypeGroupContainer.allTypeGroups.clear();
		List<TypeGroup> typeGroups = this.commonDao.findAll(TypeGroup.class);
		for (TypeGroup tsTypegroup : typeGroups) {
			TypeGroupView typegroupBean=BeanToTagUtils.convertTypeGroup(tsTypegroup);
			TypeGroupContainer.allTypeGroups.put(tsTypegroup.getTypeGroupCode().toLowerCase(), typegroupBean);
		}
	}


	/**
	 * 根据角色ID 和 菜单Id 获取 具有操作权限的按钮Codes
	 * @param roleId
	 * @param functionId
	 * @return
	 */
	public Set<String> getOperationCodesByRoleIdAndFunctionId(String roleId, String functionId) {
		Set<String> operationCodes = new HashSet<String>();
		Role role = commonDao.find(Role.class, roleId);
		CriteriaQuery cq1 = new CriteriaQuery(RoleFunction.class);
		cq1.eq("TSRole.id", role.getId());
		cq1.eq("TSFunction.id", functionId);
		cq1.add();
		List<RoleFunction> rFunctions = findListByCq(cq1, false);
		if (null != rFunctions && rFunctions.size() > 0) {
			RoleFunction tsRoleFunction = rFunctions.get(0);
			if (null != tsRoleFunction.getOperation()) {
				String[] operationArry = tsRoleFunction.getOperation().split(",");
				for (int i = 0; i < operationArry.length; i++) {
					operationCodes.add(operationArry[i]);
				}
			}
		}
		return operationCodes;
	}

	/**
	 * 根据用户ID 和 菜单Id 获取 具有操作权限的按钮Codes
	 * @param userId
	 * @param functionId
	 * @return
	 */
	public Set<String> getOperationCodesByUserIdAndFunctionId(String userId, String functionId) {
		Set<String> operationCodes = new HashSet<String>();
		List<RoleUser> rUsers = findAllByProperty(RoleUser.class, "TSUser.id", userId);
		for (RoleUser ru : rUsers) {
			Role role = ru.getRole();
			CriteriaQuery cq1 = new CriteriaQuery(RoleFunction.class);
			cq1.eq("TSRole.id", role.getId());
			cq1.eq("TSFunction.id", functionId);
			cq1.add();
			List<RoleFunction> rFunctions = findListByCq(cq1, false);
			if (null != rFunctions && rFunctions.size() > 0) {
				RoleFunction tsRoleFunction = rFunctions.get(0);
				if (null != tsRoleFunction.getOperation()) {
					String[] operationArry = tsRoleFunction.getOperation().split(",");
					for (int i = 0; i < operationArry.length; i++) {
						operationCodes.add(operationArry[i]);
					}
				}
			}
		}
		return operationCodes;
	}

	
	public void flushRoleFunciton(String id, Function newFunction) {
		Function functionEntity = this.findEntity(Function.class, id);
		if (functionEntity.getIcon() == null || !StringUtils.isEmpty(functionEntity.getIcon().getId())) {
			return;
		}
		Icon oldIcon = this.findEntity(Icon.class, functionEntity.getIcon().getId());
		if(StringUtils.isEmpty(oldIcon.getIconClas())){
              return;
		}

		if (!oldIcon.getIconClas().equals(newFunction.getIcon().getIconClas())) {
			// 刷新缓存
			HttpSession session = ContextHolderUtils.getSession();
			User user = SessionUtils.getCurrentUser();
			List<RoleUser> rUsers = this.findAllByProperty(RoleUser.class, "TSUser.id", user.getId());
			for (RoleUser ru : rUsers) {
				Role role = ru.getRole();
				session.removeAttribute(role.getId());
			}
		}
	}

    public String generateOrgCode(String id, String pid) {
        int orgCodeLength = 2; // 默认编码长度
        if ("3".equals(ConfigUtils.getOrgCodeLengthType())) { // 类型2-编码长度为3，如001
            orgCodeLength = 3;
        }
        String  newOrgCode = "";
        if(!org.springframework.util.StringUtils.hasText(pid)) { // 第一级编码
            String sql = "select max(t.org_code) orgCode from t_s_depart t where t.parentdepartid is null";
            Map<String, Object> pOrgCodeMap = commonDao.queryForMap(sql);
            if(pOrgCodeMap.get("orgCode") != null) {
                String curOrgCode = pOrgCodeMap.get("orgCode").toString();
                newOrgCode = String.format("%0" + orgCodeLength + "d", Integer.valueOf(curOrgCode) + 1);
            } else {
                newOrgCode = String.format("%0" + orgCodeLength + "d", 1);
            }
        } else { // 下级编码
            String sql = "select max(t.org_code) orgCode from t_s_depart t where t.parentdepartid = ?";
            Map<String, Object> orgCodeMap = commonDao.queryForMap(sql, pid);
            if(orgCodeMap.get("orgCode") != null) { // 当前基本有编码时
                String curOrgCode = orgCodeMap.get("orgCode").toString();
                String pOrgCode = curOrgCode.substring(0, curOrgCode.length() - orgCodeLength);
                String subOrgCode = curOrgCode.substring(curOrgCode.length() - orgCodeLength, curOrgCode.length());
                newOrgCode = pOrgCode + String.format("%0" + orgCodeLength + "d", Integer.valueOf(subOrgCode) + 1);
            } else { // 当前级别没有编码时
                String pOrgCodeSql = "select max(t.org_code) orgCode from t_s_depart t where t.id = ?";
                Map<String, Object> pOrgCodeMap = commonDao.queryForMap(pOrgCodeSql, pid);
            	String curOrgCode= pOrgCodeMap.get("orgCode")+"";
                if(curOrgCode.equals("null")){
                	curOrgCode="";
                }
                newOrgCode = curOrgCode + String.format("%0" + orgCodeLength + "d", 1);
              
            }
        }

        return newOrgCode;
    }

	public Set<String> getOperationCodesByRoleIdAndruleDataId(String roleId,
			String functionId) {
		Set<String> operationCodes = new HashSet<String>();
		Role role = commonDao.find(Role.class, roleId);
		CriteriaQuery cq1 = new CriteriaQuery(RoleFunction.class);
		cq1.eq("TSRole.id", role.getId());
		cq1.eq("TSFunction.id", functionId);
		cq1.add();
		List<RoleFunction> rFunctions = findListByCq(cq1, false);
		if (null != rFunctions && rFunctions.size() > 0) {
			RoleFunction tsRoleFunction = rFunctions.get(0);
			if (null != tsRoleFunction.getDataRule()) {
				String[] operationArry = tsRoleFunction.getDataRule().split(",");
				for (int i = 0; i < operationArry.length; i++) {
					operationCodes.add(operationArry[i]);
				}
			}
		}
		return operationCodes;
	}

	public Set<String> getOperationCodesByUserIdAndDataId(String userId,
			String functionId) {
		// TODO Auto-generated method stub
		Set<String> dataRulecodes = new HashSet<String>();
		List<RoleUser> rUsers = findAllByProperty(RoleUser.class, "TSUser.id", userId);
		for (RoleUser ru : rUsers) {
			Role role = ru.getRole();
			CriteriaQuery cq1 = new CriteriaQuery(RoleFunction.class);
			cq1.eq("TSRole.id", role.getId());
			cq1.eq("TSFunction.id", functionId);
			cq1.add();
			List<RoleFunction> rFunctions = findListByCq(cq1, false);
			if (null != rFunctions && rFunctions.size() > 0) {
				RoleFunction tsRoleFunction = rFunctions.get(0);
				if (null != tsRoleFunction.getDataRule()) {
					String[] operationArry = tsRoleFunction.getDataRule().split(",");
					for (int i = 0; i < operationArry.length; i++) {
						dataRulecodes.add(operationArry[i]);
					}
				}
			}
		}
		return dataRulecodes;
	}
	/**
	 * 加载所有图标
	 * @return
	 */
	public  void initAllTSIcons() {
		List<Icon> list = this.findAll(Icon.class);
		for (Icon tsIcon : list) {
			IconView icon=BeanToTagUtils.convertIcon(tsIcon);
			IconContainer.allTSIcons.put(tsIcon.getId(), icon);
		}
	}
	/**
	 * 更新图标
	 * @param tsIcon
	 */
	public  void updateTSIcons(Icon tsIcon) {
		IconView icon=BeanToTagUtils.convertIcon(tsIcon);
		IconContainer.allTSIcons.put(tsIcon.getId(), icon);
	}
	/**
	 * 更新图标
	 * @param icon
	 */
	public  void delTSIcons(Icon icon) {
		IconContainer.allTSIcons.remove(icon.getId());
	}

	@Override
	public void initOperations() {
		List<Operation> operationList= this.commonDao.findAll(Operation.class);
		for (Operation operation:operationList){
			OperationView operationBean=new OperationView();
			BeanUtils.copyProperties(operation,operationBean);
			SystemContainer.OperationContainer.operations.put(operation.getOperationcode(),operationBean);
		}
	}


	@Override
	public List<Function> getFucntionList(String roleId) {
		List<Function> loginActionList = new ArrayList<Function>();// 已有权限菜单
		Role role = this.find(Role.class, roleId);
		if (role != null) {
			List<RoleFunction> roleFunctionList = this.findAllByProperty(RoleFunction.class, "TSRole.id", role.getId());
			if (roleFunctionList.size() > 0) {
				for (RoleFunction roleFunction : roleFunctionList) {
					Function function = roleFunction.getFunction();
					loginActionList.add(function);
				}
			}
		}
		return loginActionList;
	}
}
