package com.abocode.jfaster.web.utils;

import com.abocode.jfaster.web.system.entity.Function;
import com.abocode.jfaster.web.system.entity.TypeGroup;
import com.abocode.jfaster.platform.view.IconView;
import com.abocode.jfaster.platform.view.TypeView;
import com.abocode.jfaster.platform.view.TypeGroupView;
import com.abocode.jfaster.web.system.entity.Icon;
import com.abocode.jfaster.web.system.entity.Type;
import com.abocode.jfaster.web.system.bean.FunctionBean;
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
	public static com.abocode.jfaster.platform.view.FunctionView convertFunction(Function function) {
		com.abocode.jfaster.platform.view.FunctionView functionBean=null;
		if(!StringUtils.isEmpty(function)){
			functionBean=new com.abocode.jfaster.platform.view.FunctionView();
			FunctionBean functionVo=new FunctionBean();
			BeanUtils.copyProperties(function, functionVo);
			BeanUtils.copyProperties(functionVo, functionBean);

			//设置
			Function parentFunction=function.getTSFunction();
			if(parentFunction!=null){
				FunctionBean parentFunctionVo=new FunctionBean();
				com.abocode.jfaster.platform.view.FunctionView parentFunctionBean=new com.abocode.jfaster.platform.view.FunctionView();
				BeanUtils.copyProperties(parentFunction, parentFunctionVo);
				BeanUtils.copyProperties(parentFunctionVo, parentFunctionBean);
				functionBean.setTSFunction(parentFunctionBean);
			}

			List<Function> functionLists=function.getTSFunctions();
			functionBean.setTSFunctions(BeanToTagUtils.convertFunctions(functionLists));
			
			//functionBean.setTSFunction(TSFunction);
			if(!StringUtils.isEmpty(function.getTSIcon())){
				IconView iconBean=new IconView();
				BeanUtils.copyProperties(function.getTSIcon(), iconBean);
				functionBean.setTSIcon(iconBean);
			}

			Icon conDeskBean=function.getTSIconDesk();
			if(!StringUtils.isEmpty(conDeskBean)){
				IconView TSIconDeskBean=new IconView();
				BeanUtils.copyProperties(conDeskBean, TSIconDeskBean);
				functionBean.setTSIconDesk(TSIconDeskBean);
			}
		}
		  return functionBean;
		}
	
	/**
	 * 转换所有菜单
	 * @param functionList
	 */
	public static List<com.abocode.jfaster.platform.view.FunctionView> convertFunctions(List<Function> functionList) {
		List<com.abocode.jfaster.platform.view.FunctionView> functionBeanList=new ArrayList<com.abocode.jfaster.platform.view.FunctionView>();
		for(Function function:functionList){
			com.abocode.jfaster.platform.view.FunctionView functionBean=convertFunction(function);
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
		type.setTSType(BeanToTagUtils.convertType(tsType.getTSType()));
		type.setTSTypegroup(BeanToTagUtils.convertTypeGroup(tsType.getTSTypegroup()));
		type.setTSTypes(BeanToTagUtils.convertTypes(tsType.getTSTypes()));
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
