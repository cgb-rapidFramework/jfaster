package com.abocode.jfaster.core.common.container;

import com.abocode.jfaster.web.system.interfaces.view.IconView;
import com.abocode.jfaster.web.system.interfaces.view.OperationView;
import com.abocode.jfaster.web.system.interfaces.view.TypeGroupView;
import com.abocode.jfaster.web.system.interfaces.view.TypeView;

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
		public static Map<String, IconView> allTSIcons= new HashMap<String,IconView>();
	}
	
	
   /***
    * 字典
    */
	public  final static class TypeGroupContainer{
		public static Map<String, TypeGroupView> allTypeGroups = new HashMap<String,TypeGroupView>();
		public static Map<String, List<TypeView>> allTypes = new HashMap<String,List<TypeView>>();
		
	}

	/**
	 * 操作
	 * @author guanxf
	 *
	 */
	public  final static class OperationContainer{
		public static Map<String, OperationView> operations= new HashMap<String,OperationView>();
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
