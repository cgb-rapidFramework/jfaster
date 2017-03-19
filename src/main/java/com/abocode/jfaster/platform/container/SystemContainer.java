package com.abocode.jfaster.platform.container;

import com.abocode.jfaster.platform.bean.IconBean;
import com.abocode.jfaster.platform.bean.OperationBean;
import com.abocode.jfaster.platform.bean.TypeGroupBean;
import com.abocode.jfaster.platform.bean.TypeBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SystemContainer {
	/**
	 * 图标
	 * @author guanxf
	 *
	 */
	public  final static class IconContainer{
		public static Map<String, IconBean> allTSIcons= new HashMap<String,IconBean>();
	}
	
	
   /***
    * 字典
    */
	public  final static class TypeGroupContainer{
		public static Map<String, TypeGroupBean> allTypeGroups = new HashMap<String,TypeGroupBean>();
		public static Map<String, List<TypeBean>> allTypes = new HashMap<String,List<TypeBean>>();
		
	}

	/**
	 * 操作
	 * @author guanxf
	 *
	 */
	public  final static class OperationContainer{
		public static Map<String, OperationBean> operations= new HashMap<String,OperationBean>();
	}

	/**
	 *模版
	 * @author guanxf
	 *
	 */
	public  final static class TemplateContainer{
		public static Map<String, String> template= new HashMap<String,String>();
	}

}
