package com.abocode.jfaster.core.common.util;

import com.abocode.jfaster.system.entity.Function;
import com.abocode.jfaster.system.entity.TypeGroup;
import com.abocode.jfaster.admin.system.dto.view.IconView;
import com.abocode.jfaster.admin.system.dto.view.TypeView;
import com.abocode.jfaster.admin.system.dto.view.TypeGroupView;
import com.abocode.jfaster.system.entity.Icon;
import com.abocode.jfaster.system.entity.Type;
import com.abocode.jfaster.admin.system.dto.bean.FunctionBean;
import com.abocode.jfaster.admin.system.dto.view.FunctionView;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by Franky on 2016/3/15.
 */
public class BeanToTagUtils {
	/**
	 * 转换菜单
	 * @param function
	 */
	public static FunctionView convertFunction(Function function) {
		FunctionView functionBean=null;
		if(!StringUtils.isEmpty(function)){
			functionBean=new FunctionView();
			FunctionBean functionVo=new FunctionBean();
			BeanUtils.copyProperties(function, functionVo);
			BeanUtils.copyProperties(functionVo, functionBean);

			//设置
			Function parentFunction=function.getParentFunction();
			if(parentFunction!=null){
				FunctionBean parentFunctionVo=new FunctionBean();
				FunctionView parentFunctionBean=new FunctionView();
				BeanUtils.copyProperties(parentFunction, parentFunctionVo);
				BeanUtils.copyProperties(parentFunctionVo, parentFunctionBean);
				functionBean.setFunction(parentFunctionBean);
			}

			List<Function> functionLists=function.getFunctions();
			functionBean.setFunctions(BeanToTagUtils.convertFunctions(functionLists));
			
			//functionBean.setFunction(TSFunction);
			if(!StringUtils.isEmpty(function.getIcon())){
				IconView iconBean=new IconView();
				BeanUtils.copyProperties(function.getIcon(), iconBean);
				functionBean.setIcon(iconBean);
			}

			Icon conDeskBean=function.getIconDesk();
			if(!StringUtils.isEmpty(conDeskBean)){
				IconView TSIconDeskBean=new IconView();
				BeanUtils.copyProperties(conDeskBean, TSIconDeskBean);
				functionBean.setIconDesk(TSIconDeskBean);
			}
		}
		  return functionBean;
		}
	
	/**
	 * 转换所有菜单
	 * @param functionList
	 */
	public static List<FunctionView> convertFunctions(List<Function> functionList) {
		List<FunctionView> functionBeanList=new ArrayList<FunctionView>();
		for(Function function:functionList){
			FunctionView functionBean=convertFunction(function);
			functionBeanList.add(functionBean);
		}
		return functionBeanList;
	}
   /**
    * 转换typeGroup
    * @param tsTypegroup
    * @return
    */
	public static TypeGroupView convertTypeGroup(TypeGroup tsTypegroup) {
		
		TypeGroupView typeGroupBean=null;
		if(null!=tsTypegroup){
			typeGroupBean=new TypeGroupView();
			BeanUtils.copyProperties(tsTypegroup, typeGroupBean);
		}	
		return typeGroupBean;
	}
 /**
  * 转换types
  * @param tsTypes
  * @return
  */
public static List<TypeView> convertTypes(List<Type> tsTypes) {
	List<TypeView> types=new ArrayList<TypeView>();
	for(Type tsType:tsTypes){
		TypeView typeBean=BeanToTagUtils.convertType(tsType);
		types.add(typeBean);
	}
	return types;
}
/**
 * 转换type
 * @param tsType
 * @return
 */
public static TypeView convertType(Type tsType) {
	TypeView type=null;
	if(tsType!=null){
		 type=new TypeView();
		type.setId(tsType.getId());
		type.setType(BeanToTagUtils.convertType(tsType.getType()));
		type.setTypegroup(BeanToTagUtils.convertTypeGroup(tsType.getTypeGroup()));
		type.setTypes(BeanToTagUtils.convertTypes(tsType.getTypes()));
		type.setTypecode(tsType.getTypecode());
		type.setTypename(tsType.getTypename());		
	}
	return type;
}
/***
 * 转换图标
 * @param tsIcon
 * @return
 */
public static IconView convertIcon(Icon tsIcon) {
	if(tsIcon!=null){
		IconView icon=new IconView();
		BeanUtils.copyProperties(tsIcon, icon);
		return icon;
	}
	return null;
}
}
