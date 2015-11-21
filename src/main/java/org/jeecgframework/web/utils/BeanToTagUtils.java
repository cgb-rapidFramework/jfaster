package org.jeecgframework.web.utils;

import java.util.ArrayList;
import java.util.List;

import jodd.bean.BeanUtil;

import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.platform.bean.FunctionBean;
import org.jeecgframework.platform.bean.IconBean;
import org.jeecgframework.platform.bean.TypeBean;
import org.jeecgframework.platform.bean.TypeGroupBean;
import org.jeecgframework.web.system.entity.base.TSFunction;
import org.jeecgframework.web.system.entity.base.TSIcon;
import org.jeecgframework.web.system.entity.base.TSType;
import org.jeecgframework.web.system.entity.base.TSTypegroup;
import org.jeecgframework.web.system.vo.platform.FunctionVo;
import org.springframework.beans.BeanUtils;

import com.sun.star.uno.Type;

public class BeanToTagUtils {
	/**
	 * 转换菜单
	 * @param function
	 */
	public static FunctionBean  convertFunction(TSFunction function) {
		FunctionBean functionBean=null;
		if(StringUtil.isNotEmpty(function)){
			functionBean=new FunctionBean();
			FunctionVo functionVo=new FunctionVo();
			BeanUtils.copyProperties(function, functionVo);
			BeanUtils.copyProperties(functionVo, functionBean);

			//设置
			TSFunction parentFunction=function.getTSFunction();
			if(parentFunction!=null){
				FunctionVo parentFunctionVo=new FunctionVo();
				FunctionBean parentFunctionBean=new FunctionBean();
				BeanUtils.copyProperties(parentFunction, parentFunctionVo);
				BeanUtils.copyProperties(parentFunctionVo, parentFunctionBean);
				functionBean.setTSFunction(parentFunctionBean);
			}

			List<TSFunction> functionLists=function.getTSFunctions();
			functionBean.setTSFunctions(BeanToTagUtils.convertFunctions(functionLists));
			
			//functionBean.setTSFunction(TSFunction);
			if(StringUtil.isNotEmpty(function.getTSIcon())){
				IconBean iconBean=new IconBean();
				BeanUtils.copyProperties(function.getTSIcon(), iconBean);
				functionBean.setTSIcon(iconBean);
			}

			TSIcon conDeskBean=function.getTSIconDesk();
			if(StringUtil.isNotEmpty(conDeskBean)){
				IconBean TSIconDeskBean=new IconBean();
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
	public static List<FunctionBean> convertFunctions(List<TSFunction> functionList) {
		List<FunctionBean> functionBeanList=new ArrayList<FunctionBean>();
		for(TSFunction function:functionList){
			FunctionBean functionBean=convertFunction(function);
			functionBeanList.add(functionBean);
		}
		return functionBeanList;
	}
   /**
    * 转换typeGroup
    * @param tsTypegroup
    * @return
    */
	public static TypeGroupBean convertTypeGroup(TSTypegroup tsTypegroup) {
		
		TypeGroupBean typeGroupBean=null;
		if(null!=tsTypegroup){
			typeGroupBean=new TypeGroupBean();
			BeanUtils.copyProperties(tsTypegroup, typeGroupBean);
		}	
		return typeGroupBean;
	}
 /**
  * 转换types
  * @param tsTypes
  * @return
  */
public static List<TypeBean> convertTypes(List<TSType> tsTypes) {
	List<TypeBean> types=new ArrayList<TypeBean>();
	for(TSType tsType:tsTypes){
		TypeBean typeBean=BeanToTagUtils.convertType(tsType);
		types.add(typeBean);
	}
	return types;
}
/**
 * 转换type
 * @param tsType
 * @return
 */
public static TypeBean convertType(TSType tsType) {
	TypeBean type=null;
	if(tsType!=null){
		 type=new TypeBean();
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
public static IconBean convertIcon(TSIcon tsIcon) {
	if(tsIcon!=null){
		IconBean icon=new IconBean();
		BeanUtils.copyProperties(tsIcon, icon);
		return icon;
	}
	return null;
}
}
