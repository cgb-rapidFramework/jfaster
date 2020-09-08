package com.abocode.jfaster.admin.system.repository.persistence.hibernate;

import com.abocode.jfaster.admin.system.repository.SystemRepository;
import com.abocode.jfaster.admin.system.service.BeanToTagConverter;
import com.abocode.jfaster.core.common.util.*;
import com.abocode.jfaster.core.persistence.hibernate.qbc.CriteriaQuery;
import com.abocode.jfaster.core.platform.SystemContainer;
import com.abocode.jfaster.core.platform.SystemContainer.IconContainer;
import com.abocode.jfaster.core.platform.SystemContainer.TypeGroupContainer;
import com.abocode.jfaster.core.platform.view.IconView;
import com.abocode.jfaster.core.platform.view.OperationView;
import com.abocode.jfaster.core.platform.view.TypeGroupView;
import com.abocode.jfaster.core.platform.view.TypeView;
import com.abocode.jfaster.core.repository.persistence.hibernate.CommonRepositoryImpl;
import com.abocode.jfaster.core.web.manager.SessionHolder;
import com.abocode.jfaster.system.entity.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Service
@Transactional
public class SystemRepositoryImpl extends CommonRepositoryImpl implements SystemRepository {

	public static final String USER_ID = "user.id";
	public static final String ROLE_ID = "role.id";
	public static final String FUNCTION_ID = "function.id";

	/**
	 * 添加日志
	 */
	public void addLog(String logcontent, Short loglevel, Short operationType) {
		HttpServletRequest request = ContextHolderUtils.getRequest();
		String broswer = BrowserUtils.checkBrowse(request);
		Log log = new Log();
		log.setLogContent(logcontent);
		log.setLoglevel(loglevel);
		log.setOperationType(operationType);
		log.setNote(ConvertUtils.getIp());
		log.setBroswer(broswer);
		log.setOperationTime(DateUtils.getTimestamp());
		log.setUserId(SessionHolder.getCurrentUser().getId());
		saveOrUpdate(log);
	}

	/**
	 * 根据类型编码和类型名称获取Type,如果为空则创建一个
	 * 
	 * @param typecode
	 * @param typename
	 * @return
	 */
	public Type getType(String typecode, String typename, TypeGroup tsTypegroup) {
		Type actType =findUniqueByProperty(Type.class, "typecode", typecode);
		if (actType == null) {
			actType = new Type();
			actType.setTypeCode(typecode);
			actType.setTypeName(typename);
			actType.setTypeGroup(tsTypegroup);
			save(actType);
		}
		return actType;

	}

	public void initAllTypeGroups() {
		List<TypeGroup> typeGroups = findAll(TypeGroup.class);
		for (TypeGroup typeGroup : typeGroups) {
			TypeGroupView typeGroupView= BeanToTagConverter.convertTypeGroup(typeGroup);
			TypeGroupContainer.getTypeGroupMap().put(typeGroup.getTypeGroupCode().toLowerCase(), typeGroupView);
			List<Type> tsTypes = findAllByProperty(Type.class, "typeGroup.id", typeGroup.getId());
			List<TypeView> types= BeanToTagConverter.convertTypes(tsTypes);
			TypeGroupContainer.getTypeMap().put(typeGroup.getTypeGroupCode().toLowerCase(), types);
		}
	}

	
	public void refreshTypesCache(Type type) {
		TypeGroup typeGroup = type.getTypeGroup();
		TypeGroup typeGroupEntity = find(TypeGroup.class, typeGroup.getId());
		List<Type> tsTypes = findAllByProperty(Type.class, "typeGroup.id", typeGroup.getId());
		List<TypeView> types= BeanToTagConverter.convertTypes(tsTypes);
		TypeGroupContainer.getTypeMap().put(typeGroupEntity.getTypeGroupCode().toLowerCase(), types);
	}

	
	public void refreshTypeGroupCache() {
		TypeGroupContainer.getTypeGroupMap().clear();
		List<TypeGroup> typeGroups = findAll(TypeGroup.class);
		for (TypeGroup tsTypegroup : typeGroups) {
			TypeGroupView typegroupBean= BeanToTagConverter.convertTypeGroup(tsTypegroup);
			TypeGroupContainer.getTypeGroupMap().put(tsTypegroup.getTypeGroupCode().toLowerCase(), typegroupBean);
		}
	}


	/**
	 * 根据角色ID 和 菜单Id 获取 具有操作权限的按钮Codes
	 * @param roleId
	 * @param functionId
	 * @return
	 */
	public Set<String> getOperationCodesByRoleIdAndFunctionId(String roleId, String functionId) {
		Set<String> operationCodes = new HashSet();
		Role role =find(Role.class, roleId);
		CriteriaQuery cq1 = new CriteriaQuery(RoleFunction.class);
		cq1.eq(ROLE_ID, role.getId());
		cq1.eq(FUNCTION_ID, functionId);
		cq1.add();
		List<RoleFunction> functions = findListByCq(cq1, false);
		if (null != functions && functions.size() > 0) {
			RoleFunction tsRoleFunction = functions.get(0);
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
		Set<String> operationCodes = new HashSet();
		List<RoleUser> rUsers = findAllByProperty(RoleUser.class, USER_ID, userId);
		for (RoleUser ru : rUsers) {
			CriteriaQuery cq1 = new CriteriaQuery(RoleFunction.class);
			cq1.eq("role.id", ru.getRole().getId());
			cq1.eq("function.id", functionId);
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
		if (functionEntity.getIcon() == null || !StrUtils.isEmpty(functionEntity.getIcon().getId())) {
			return;
		}
		Icon oldIcon = this.findEntity(Icon.class, functionEntity.getIcon().getId());
		if(StrUtils.isEmpty(oldIcon.getIconClazz())){
              return;
		}

		if (!oldIcon.getIconClazz().equals(newFunction.getIcon().getIconClazz())) {
			// 刷新缓存
			HttpSession session = ContextHolderUtils.getSession();
			User user = SessionHolder.getCurrentUser();
			List<RoleUser> rUsers = this.findAllByProperty(RoleUser.class, USER_ID, user.getId());
			for (RoleUser ru : rUsers) {
				Role role = ru.getRole();
				session.removeAttribute(role.getId());
			}
		}
	}


	public Set<String> getOperationCodesByRoleIdAndruleDataId(String roleId,
			String functionId) {
		Set<String> operationCodes = new HashSet();
		Role role =find(Role.class, roleId);
		CriteriaQuery cq1 = new CriteriaQuery(RoleFunction.class);
		cq1.eq(ROLE_ID, role.getId());
		cq1.eq(FUNCTION_ID, functionId);
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
		Set<String> dataRuleCodes = new HashSet();
		List<RoleUser> rUsers = findAllByProperty(RoleUser.class, USER_ID, userId);
		for (RoleUser ru : rUsers) {
			Role role = ru.getRole();
			CriteriaQuery cq1 = new CriteriaQuery(RoleFunction.class);
			cq1.eq(ROLE_ID, role.getId());
			cq1.eq("function.id", functionId);
			cq1.add();
			List<RoleFunction> rFunctions = findListByCq(cq1, false);
			if (null != rFunctions && rFunctions.size() > 0) {
				RoleFunction tsRoleFunction = rFunctions.get(0);
				if (null != tsRoleFunction.getDataRule()) {
					String[] operationArry = tsRoleFunction.getDataRule().split(",");
					for (int i = 0; i < operationArry.length; i++) {
						dataRuleCodes.add(operationArry[i]);
					}
				}
			}
		}
		return dataRuleCodes;
	}
	/**
	 * 加载所有图标
	 * @return
	 */
	public  void initAllTSIcons() {
		List<Icon> list = this.findAll(Icon.class);
		for (Icon tsIcon : list) {
			IconView icon= BeanToTagConverter.convertIcon(tsIcon);
			IconContainer.getIconsMap().put(tsIcon.getId(), icon);
		}
	}

	@Override
	public void initOperations() {
		List<Operation> operationList= findAll(Operation.class);
		for (Operation operation:operationList){
			OperationView operationBean=new OperationView();
			BeanUtils.copyProperties(operation,operationBean);
			SystemContainer.OperationContainer.getOperationMap().put(operation.getOperationCode(),operationBean);
		}
	}


	@Override
	public List<Function> getFucntionList(String roleId) {
		List<Function> loginActionList = new ArrayList<Function>();// 已有权限菜单
		Role role = this.find(Role.class, roleId);
		if (role != null) {
			List<RoleFunction> roleFunctionList = this.findAllByProperty(RoleFunction.class, ROLE_ID, role.getId());
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
