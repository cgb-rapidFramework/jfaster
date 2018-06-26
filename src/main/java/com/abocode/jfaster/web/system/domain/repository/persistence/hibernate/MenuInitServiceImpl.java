package com.abocode.jfaster.web.system.domain.repository.persistence.hibernate;

import com.abocode.jfaster.web.system.domain.entity.Function;
import com.abocode.jfaster.web.system.domain.entity.Icon;
import com.abocode.jfaster.web.system.domain.repository.MenuInitService;
import com.abocode.jfaster.core.common.annotation.config.AutoMenu;
import com.abocode.jfaster.core.common.annotation.config.AutoMenuOperation;
import com.abocode.jfaster.core.common.annotation.config.MenuCodeType;
import com.abocode.jfaster.core.domain.repository.persistence.hibernate.CommonRepositoryImpl;
import com.abocode.jfaster.web.system.application.dto.view.FunctionView;
import com.abocode.jfaster.web.system.domain.entity.Operation;
import com.abocode.jfaster.core.common.util.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service("menuInitService")
@Transactional
public class MenuInitServiceImpl extends CommonRepositoryImpl implements
        MenuInitService {

	private static final String KEY_SPLIT = "-";//map key 分隔符
	private static final String MENU_TYPE_ID = "#";//采用控件ID方式的code前缀
	private static final String MENU_TYPE_CSS = ".";//采用Css样式方式的code前缀
	
	
	public void initMenu() {
		// 1.扫描项目下，所有class，判断带有标签@AutoMenu
		// TODO
		// 2.循环判断@AutoMenu在系统中是否存在,如果不存在进行插入，如果存在不再插入
		// 比较规则[菜单名称-等级-菜单地址：全匹配]

		// 3.加载@AutoMenu下带有标签@AutoMenuOperation所有方法

		// 4.循环@AutoMenuOperation方法标签，判断该菜单下是否有该操作码配置，如果存在不插入，不存在进行插入
		// 比较规则[菜单ID-操作码 ：全匹配]

		List<Function> functionList = this.findAll(Function.class);
		List<Operation> operationList = this.findAll(Operation.class);
		
		Map<String, FunctionView> functionMap = new HashMap<String, FunctionView>();//菜单map,key为菜单匹配规则的字符串
		Map<String, Operation> operationMap = new HashMap<String, Operation>();//菜单操作按钮map,key为菜单操作按钮匹配规则的字符串
		
		//设置菜单map
		for (Function function : functionList) {
			StringBuffer key = new StringBuffer();
			key.append(function.getFunctionName() == null ? "" : function.getFunctionName());
			key.append(KEY_SPLIT);
			key.append(function.getFunctionLevel() == null ? "" : function.getFunctionLevel());
			key.append(KEY_SPLIT);
			key.append(function.getFunctionUrl() == null ? "" : function.getFunctionUrl());
			FunctionView bean=new FunctionView();
			BeanUtils.copyProperties(function, bean);
			functionMap.put(key.toString(), bean);
		}
		//设置菜单操作按钮map
		for (Operation operation : operationList) {
			StringBuffer key = new StringBuffer();
			key.append(operation.getTSFunction() == null ? "" : operation.getTSFunction().getId());
			key.append(KEY_SPLIT);
			key.append(operation.getOperationcode() == null ? "" : operation.getOperationcode());
			operationMap.put(key.toString(), operation);
		}
		
		//扫描Src目录下
//		Set<Class<?>> classSet = PackagesToScanUtil.getClasses(".*");
		Set<Class<?>> classSet =null;
		for (Class<?> clazz : classSet) {
			//判断当前类是否设置了菜单注解
			//未设置菜单注解就算在该类的方法上设置了菜单操作按钮注解也不进行菜单操作按钮的匹配
			if (clazz.isAnnotationPresent(AutoMenu.class)) {
				AutoMenu autoMenu = clazz.getAnnotation(AutoMenu.class);
				//菜单名称必须填写，否则不进行菜单和菜单操作按钮的匹配
				if (!StringUtils.isEmpty(autoMenu.name())) {
					StringBuffer menuKey = new StringBuffer();
					menuKey.append(autoMenu.name());
					menuKey.append(KEY_SPLIT);
					menuKey.append(autoMenu.level() == null ? "" : autoMenu.level());
					menuKey.append(KEY_SPLIT);
					menuKey.append(autoMenu.url() == null ? "" : autoMenu.url());
					
					Function function = null;
					//判断菜单map的key是否包含当前key，不包含则插入一条菜单数据
					if (!functionMap.containsKey(menuKey.toString())) {
						function = new Function();
						function.setFunctionName(autoMenu.name());
						function.setFunctionIframe(null);
						function.setFunctionLevel(Short.valueOf(autoMenu.level()));
						function.setFunctionOrder(Integer.toString(autoMenu.order()));
						function.setFunctionUrl(autoMenu.url());
						function.setTSFunction(null);
						
						String iconId = autoMenu.icon();
						if (!StringUtils.isEmpty(iconId)) {
							Object obj = this.find(Icon.class, iconId);
							if(obj!=null){
								function.setTSIcon((Icon)obj);
							}else{
								function.setTSIcon(null);
							}
						} else {
							function.setTSIcon(null);
						}
						Serializable id = this.save(function);
						function.setId(id.toString());
					} else {
						FunctionView functionBean=functionMap.get(menuKey.toString());
						BeanUtils.copyProperties(functionBean, function);
					}
						
					//获取该类的所有方法
					Method[] methods = clazz.getDeclaredMethods();
					for(Method method : methods){
						//判断当前方法是否设置了菜单操作按钮注解
						if (method.isAnnotationPresent(AutoMenuOperation.class)) {
							AutoMenuOperation autoMenuOperation = method.getAnnotation(AutoMenuOperation.class);
							//操作码必须填写，否则不进行菜单操作按钮的匹配
							if (!StringUtils.isEmpty(autoMenuOperation.code())) {
								StringBuffer menuOperationKey = new StringBuffer();
								menuOperationKey.append(function == null ? "" : function.getId());
								menuOperationKey.append(KEY_SPLIT);
								
								String code = "";
								//设置code前缀
								if (autoMenuOperation.codeType() == MenuCodeType.TAG) {
									code = autoMenuOperation.code();
								} else if (autoMenuOperation.codeType() == MenuCodeType.ID) {
									code = MENU_TYPE_ID + autoMenuOperation.code();
								} else if (autoMenuOperation.codeType() == MenuCodeType.CSS) {
									code = MENU_TYPE_CSS + autoMenuOperation.code();
								}
								menuOperationKey.append(code);
								
								//判断菜单操作按钮map的key是否包含当前key，不包含则插入一条菜单操作按钮数据
								if (!operationMap.containsKey(menuOperationKey.toString())) {
									Operation operation = new Operation();
									operation.setOperationname(autoMenuOperation.name());
									operation.setOperationcode(code);
									operation.setOperationicon(null);
									operation.setStatus(Short.parseShort(Integer.toString(autoMenuOperation.status())));
									operation.setTSFunction(function);
									
									String iconId = autoMenuOperation.icon();
									if (!StringUtils.isEmpty(iconId)) {
										Icon icon = new Icon();
										icon.setId(iconId);
										operation.setTSIcon(icon);
									} else {
										operation.setTSIcon(null);
									}
									this.save(operation);
								}
							}
						}
					}
				}
			}
		}
	}
}
