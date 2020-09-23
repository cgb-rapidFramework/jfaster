package com.abocode.jfaster.admin.system.repository;

import com.abocode.jfaster.core.repository.CommonRepository;
import com.abocode.jfaster.system.entity.Function;
import com.abocode.jfaster.system.entity.Type;
import com.abocode.jfaster.system.entity.TypeGroup;

import java.util.List;

public  interface SystemRepository extends CommonRepository {

	/**
	 * 日志添加
	 * @param LogContent 内容
	 * @param loglevel 级别
	 * @param operationType 类型
	 */
	 void addLog(String LogContent, Short loglevel,Short operationType);
	/**
	 * 根据类型编码和类型名称获取Type,如果为空则创建一个
	 * @param typecode
	 * @param typename
	 * @return
	 */
	 Type getType(String typecode, String typename, TypeGroup tsTypegroup);
	/**
	 * 根据用户ID 和 菜单Id 获取 具有操作权限的按钮Codes
	 * @param userId
	 * @param functionId
	 * @return
	 */
	  String[] getOperationCodesByUserIdAndFunctionId(String userId, String functionId);
	/**
	 * 根据角色ID 和 菜单Id 获取 具有操作权限的按钮Codes
	 * @param roleId
	 * @param functionId
	 * @return
	 */
	  String[] getOperationCodesByRoleIdAndFunctionId(String roleId, String functionId);

	/**
	 * 对数据字典进行缓存
	 */
	 void initAllTypeGroups();
	
	/**
	 * 刷新字典缓存
	 * @param type
	 */
	 void refreshTypesCache(Type type);
	/**
	 * 刷新字典分组缓存
	 */
	 void refreshTypeGroupCache();
	/**
	 * 刷新菜单
	 * 
	 * @param id
	 */
	 void flushRoleFunciton(String id, Function newFunciton);

	/**
	 * 
	  * getOperationCodesByRoleIdAndruleDataId
	  * 根据角色id 和 菜单Id 获取 具有操作权限的数据规则
	  *
	  * @Title: getOperationCodesByRoleIdAndruleDataId
	  * @Description: TODO
	  * @param @param roleId
	  * @param @param functionId
	  * @param @return    设定文件
	  * @return Set<String>    返回类型
	  * @throws
	 */
	
	  Object getOperationCodesByRoleIdAndruleDataId(String roleId, String functionId);
	
	  String[] getOperationCodesByUserIdAndDataId(String userId, String functionId);
	
	/**
	 * 加载所有图标
	 * @return
	 */
	  void initAllTSIcons();


	 void initOperations();

	/***
	 * 获取角色菜单
	 * @param roleId
	 * @return
     */
	List<Function> getFucntionList(String roleId);
}
