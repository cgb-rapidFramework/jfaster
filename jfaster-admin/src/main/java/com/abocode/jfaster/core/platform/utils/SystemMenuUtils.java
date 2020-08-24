package com.abocode.jfaster.core.platform.utils;

import com.abocode.jfaster.core.platform.view.IconView;
import com.abocode.jfaster.core.platform.SystemContainer;
import org.apache.commons.lang.StringUtils;
import com.abocode.jfaster.core.platform.view.FunctionView;

import java.util.List;
import java.util.Map;

/**
 * 动态菜单栏生成
 * 
 * @author 张代浩
 * 
 */
public class SystemMenuUtils {
	/**
	 * 拼装easyui菜单JSON方式
	 * 
	 * @param set
	 * @param set1
	 * @return
	 */
	public static String getMenu(List<FunctionView> set, List<FunctionView> set1) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("{\"menus\":[");
		for (FunctionView node : set) {
			String iconClas = "default";// 权限图标样式
			if (node.getIcon() != null) {
				iconClas = node.getIcon().getIconClazz();
			}
			buffer.append("{\"menuid\":\"" + node.getId() + "\",\"icon\":\""
					+ iconClas + "\"," + "\"menuname\":\""
			+ MutiLangUtils.getLang(node.getFunctionName()) + "\",\"menus\":[");
			iterGet(set1, node.getId(), buffer);
			buffer.append("]},");
		}
		buffer.append("]}");

		// 将,\n]替换成\n]

		String tmp = buffer.toString();

		tmp = tmp.replaceAll(",\n]", "\n]");
		tmp = tmp.replaceAll(",]}", "]}");
		return tmp;

	}

	static int count = 0;

	/**
	 * @param pid
	 */

	static void iterGet(List<FunctionView> set1, String pid, StringBuffer buffer) {

		for (FunctionView node : set1) {

			// 查找所有父节点为pid的所有对象，然后拼接为json格式的数据
			count++;
			if (node.getParentFunction().getId().equals(pid))

			{
				buffer.append("{\"menuid\":\"" + node.getId()
						+ " \",\"icon\":\"" + node.getIcon().getIconClazz()
						+ "\"," + "\"menuname\":\"" + MutiLangUtils.getLang(node.getFunctionName())
						+ "\",\"url\":\"" + node.getFunctionUrl() + "\"");
				if (count == set1.size()) {
					buffer.append("}\n");
				}
				buffer.append("},\n");

			}
		}

	}

	/**
	 * 拼装Bootstarp菜单
	 * 
	 * @param pFunctions
	 * @param functions
	 * @return
	 */
	public static String getBootMenu(List<FunctionView> pFunctions,
			List<FunctionView> functions) {
		StringBuffer menuString = new StringBuffer();
		menuString.append("<ul>");
		for (FunctionView pFunction : pFunctions) {
			menuString
					.append("<li><a href=\"#\"><span class=\"icon16 icomoon-icon-stats-up\"></span><b>"
			+ MutiLangUtils.getLang(pFunction.getFunctionName()) + "</b></a>");
			int submenusize = pFunction.getFunctions().size();
			if (submenusize == 0) {
				menuString.append("</li>");
			}
			if (submenusize > 0) {
				menuString.append("<ul class=\"sub\">");
			}
			for (FunctionView function : functions) {

				if (function.getParentFunction().getId().equals(pFunction.getId())) {
					menuString
							.append("<li><a href=\""
									+ function.getFunctionUrl()
									+ "\" target=\"contentiframe\"><span class=\"icon16 icomoon-icon-file\"></span>"
									+ MutiLangUtils.getLang(function.getFunctionName()) + "</a></li>");
				}
			}
			if (submenusize > 0) {
				menuString.append("</ul></li>");
			}
		}
		menuString.append("</ul>");
		return menuString.toString();

	}

	/**
	 * 拼装EASYUI菜单
	 * 
	 * @param pFunctions
	 * @param functions
	 * @return
	 */
	public static String getEasyuiMenu(List<FunctionView> pFunctions,
			List<FunctionView> functions) {
		StringBuffer menuString = new StringBuffer();
		for (FunctionView pFunction : pFunctions) {
			menuString.append("<div  title=\"" + MutiLangUtils.getLang(pFunction.getFunctionName())
					+ "\" iconCls=\"" + pFunction.getIcon().getIconClazz()
					+ "\">");
			int submenusize = pFunction.getFunctions().size();
			if (submenusize == 0) {
				menuString.append("</div>");
			}
			if (submenusize > 0) {
				menuString.append("<ul>");
			}
			for (FunctionView function : functions) {

				if (function.getParentFunction().getId().equals(pFunction.getId())) {
					String icon = "folder";
					if (function.getIcon() != null) {
						icon = function.getIcon().getIconClazz();
					}
					// menuString.append("<li><div> <a class=\""+function.getFunctionName()+"\" iconCls=\""+icon+"\" target=\"tabiframe\"  href=\""+function.getFunctionUrl()+"\"> <span class=\"icon "+icon+"\" >&nbsp;</span> <span class=\"nav\">"+function.getFunctionName()+"</span></a></div></li>");
					menuString.append("<li><div onclick=\"addTab(\'"
							+ MutiLangUtils.getLang(function.getFunctionName()) + "\',\'"
							+ function.getFunctionUrl() + "&clickFunctionId="
							+ function.getId() + "\',\'" + icon
							+ "\')\"  title=\"" + MutiLangUtils.getLang(function.getFunctionName())
							+ "\" url=\"" + function.getFunctionUrl()
							+ "\" iconCls=\"" + icon + "\"> <a class=\""
							+ MutiLangUtils.getLang(function.getFunctionName())
							+ "\" href=\"#\" > <span class=\"icon " + icon
							+ "\" >&nbsp;</span> <span class=\"nav\" >"
							+ MutiLangUtils.getLang(function.getFunctionName())
							+ "</span></a></div></li>");
				}
			}
			if (submenusize > 0) {
				menuString.append("</ul></div>");
			}
		}
		return menuString.toString();

	}

	/**
	 * 拼装EASYUI 多级 菜单
	 * 
	 * @param map
	 * @return
	 */
	public static String getEasyuiMultistageMenu(
			Map<Integer, List<FunctionView>> map) {
		StringBuffer menuString = new StringBuffer();
		List<FunctionView> list = map.get(0);
		for (FunctionView function : list) {
			menuString.append("<div   title=\"" + MutiLangUtils.getLang(function.getFunctionName())
					+ "\" iconCls=\"" + function.getIcon().getIconClazz()
					+ "\">");
			int submenusize = function.getFunctions().size();
			if (submenusize == 0) {
				menuString.append("</div>");
			}
			if (submenusize > 0) {
				menuString.append("<ul>");
			}
			menuString.append(getChild(function,1,map));
			if (submenusize > 0) {
				menuString.append("</ul></div>");
			}
		}
		return menuString.toString();
	}

    /**
     * 拼装EASYUI 多级 菜单  下级菜单为树形
     * @param map  the map of Map<Integer, List<TSFunction>>
     * @param style 样式：easyui-经典风格、shortcut-shortcut风格
     * @return
     */
	public static String getEasyuiMultistageTree(Map<Integer, List<FunctionView>> map, String style) {
		if(map==null||map.size()==0||!map.containsKey(0)){return "不具有任何权限,\n请找管理员分配权限";}
		StringBuffer menuString = new StringBuffer();
		List<FunctionView> list = map.get(0);
        int curIndex = 0;
        if ("easyui".equals(style)) {
            for (FunctionView function : list) {
                if(curIndex == 0) { // 第一个菜单，默认展开
                    menuString.append("<li>");
                } else {
                    menuString.append("<li state='closed'>");
                }
                menuString.append("<span>").append(MutiLangUtils.getLang(function.getFunctionName())).append("</span>");
                int submenusize = function.getFunctions().size();
                if (submenusize == 0) {
                    menuString.append("</li>");
                }
                if (submenusize > 0) {
                    menuString.append("<ul>");
                }
                menuString.append(getChildOfTree(function,1,map));
                if (submenusize > 0) {
                    menuString.append("</ul></li>");
                }
                curIndex++;
            }
        } else if("shortcut".equals(style)) {
            for (FunctionView function : list) {
                menuString.append("<div   title=\"" + MutiLangUtils.getLang(function.getFunctionName())
                        + "\" iconCls=\"" + function.getIcon().getIconClazz()
                        + "\">");
                int submenusize = function.getFunctions().size();
                if (submenusize == 0) {
                    menuString.append("</div>");
                }
                if (submenusize > 0) {
                    menuString.append("<ul class=\"easyui-tree tree-lines\"  fit=\"false\" border=\"false\">");
                }
                menuString.append(getChildOfTree(function,1,map));
                if (submenusize > 0) {
                    menuString.append("</ul></div>");
                }
            }
        }

		return menuString.toString();
	}

	/**
	 * 获取顶级菜单的下级菜单-----面板式菜单
	 * @param parent
	 * @param level
	 * @param map
	 * @return
	 */
	private static String getChild(FunctionView parent, int level, Map<Integer, List<FunctionView>> map){
		StringBuffer menuString = new StringBuffer();
		List<FunctionView> list = map.get(level);
		for (FunctionView function : list) {
			if (function.getParentFunction().getId().equals(parent.getId())){
				if(function.getFunctions().size()==0||!map.containsKey(level+1)){
					menuString.append(getLeaf(function));
				}else if(map.containsKey(level+1)){
					menuString.append("<div  class=\"easyui-accordion\"  fit=\"false\" border=\"false\">");
					menuString.append("<div></div>");//easy ui 默认展开第一级,所以这里设置一个控制,就不展开了
					menuString.append("<div title=\"" + MutiLangUtils.getLang(function.getFunctionName())
							+ "\" iconCls=\"" + function.getIcon().getIconClazz()
							+ "\"><ul>");
					menuString.append(getChild(function,level+1,map));
					menuString.append("</ul></div>");
					menuString.append("</div>");
				}
			}
		}
		return menuString.toString();
	}
	/**
	 * 获取树形菜单
	 * @param parent
	 * @param level
	 * @param map
	 * @return
	 */
	private static String getChildOfTree(FunctionView parent, int level, Map<Integer, List<FunctionView>> map){
		StringBuffer menuString = new StringBuffer();
		List<FunctionView> list = map.get(level);
		for (FunctionView function : list) {
			if (function.getParentFunction().getId().equals(parent.getId())){
				if(function.getFunctions().size()==0||!map.containsKey(level+1)){
					menuString.append(getLeafOfTree(function));
				}else if(map.containsKey(level+1)){
					menuString.append("<li state=\"closed\" iconCls=\"" + function.getIcon().getIconClazz()+"\" ><span>"+ MutiLangUtils.getLang(function.getFunctionName()) +"</span>");
					menuString.append("<ul >");
					menuString.append(getChildOfTree(function,level+1,map));
					menuString.append("</ul></li>");
				}
			}
		}
		return menuString.toString();
	}
	/**
	 * 获取叶子节点
	 * @param function
	 * @return
	 */
	private static String getLeaf(FunctionView function){
		StringBuffer menuString = new StringBuffer();
		String icon = "folder";
		if (function.getIcon() != null) {
			icon = function.getIcon().getIconClazz();
		}
		menuString.append("<li><div onclick=\"addTab(\'");
		menuString.append(MutiLangUtils.getLang(function.getFunctionName()));
		menuString.append("\',\'");
		menuString.append(function.getFunctionUrl());
		menuString.append("&clickFunctionId=");
		menuString.append(function.getId());
		menuString.append("\',\'");
		menuString.append(icon);
		menuString.append("\')\"  title=\"");
		menuString.append(MutiLangUtils.getLang(function.getFunctionName()));
		menuString.append("\" url=\"");
		menuString.append(function.getFunctionUrl());
		menuString.append("\" iconCls=\"");
		menuString.append(icon);
		menuString.append("\"> <a class=\"");
		menuString.append(MutiLangUtils.getLang(function.getFunctionName()));
		menuString.append("\" href=\"#\" > <span class=\"icon ");
		menuString.append(icon);
		menuString.append("\" >&nbsp;</span> <span class=\"nav\" >");
		menuString.append(MutiLangUtils.getLang(function.getFunctionName()));
		menuString.append("</span></a></div></li>");
		return menuString.toString();
	}
	/**
	 * 获取叶子节点  ------树形菜单的叶子节点
	 * @param function
	 * @return
	 */
	private static String getLeafOfTree(FunctionView function){
		StringBuffer menuString = new StringBuffer();
		String icon = "folder";
		if (function.getIcon() != null) {
			icon = function.getIcon().getIconClazz();
		}
		menuString.append("<li iconCls=\"");
		menuString.append(icon);
		menuString.append("\"> <a onclick=\"addTab(\'");
		menuString.append(MutiLangUtils.getLang(function.getFunctionName()));
		menuString.append("\',\'");
		menuString.append(function.getFunctionUrl());
		menuString.append("&clickFunctionId=");
		menuString.append(function.getId());
		menuString.append("\',\'");
		menuString.append(icon);
		menuString.append("\')\"  title=\"");
		menuString.append(MutiLangUtils.getLang(function.getFunctionName()));
		menuString.append("\" url=\"");
		menuString.append(function.getFunctionUrl());
		menuString.append("\" href=\"#\" ><span class=\"nav\" >");
		menuString.append(MutiLangUtils.getLang(function.getFunctionName()));
		menuString.append("</span></a></li>");
		return menuString.toString();
	}
	/**
	 * 拼装bootstrap头部菜单
	 * @param map
	 * @return
	 */
	public static String getBootstrapMenu(Map<Integer, List<FunctionView>> map) {
		StringBuffer menuString = new StringBuffer();
		menuString.append("<ul class=\"nav\">");
		List<FunctionView> pFunctions = (List<FunctionView>) map.get(0);
		if(pFunctions==null || pFunctions.size()==0){
			return "";
		}
		for (FunctionView pFunction : pFunctions) {
			//是否有子菜单
			boolean hasSub = pFunction.getFunctions().size()==0?false:true;
			
			//绘制一级菜单
			menuString.append("	<li class=\"dropdown\"> ");
			menuString.append("		<a href=\"javascript:;\" class=\"dropdown-toggle\" data-toggle=\"dropdown\"> ");
			menuString.append("			<span class=\"bootstrap-icon\" style=\"background-image: url('"+pFunction.getIcon().getIconPath()+"')\"></span> "+pFunction.getFunctionName()+" ");
			if(hasSub){
				menuString.append("			<b class=\"caret\"></b> ");
			}
			menuString.append("		</a> ");
			//绘制二级菜单
			if(hasSub){
				menuString.append(getBootStrapChild(pFunction, 1, map));
			}
			menuString.append("	</li> ");
		}
		menuString.append("</ul>");
		return menuString.toString();
	}
	/**
	* @Title: getBootStrapChild
	* @Description: 递归获取bootstrap的子菜单
	*  @param parent
	*  @param level
	*  @param map
	* @return String    
	* @throws
	 */
	private static String getBootStrapChild(FunctionView parent, int level, Map<Integer, List<FunctionView>> map){
		StringBuffer menuString = new StringBuffer();
		List<FunctionView> list = map.get(level);
		if(list==null || list.size()==0){
			return "";
		}
		menuString.append("		<ul class=\"dropdown-menu\"> ");
		for (FunctionView function : list) {
			if (function.getParentFunction().getId().equals(parent.getId())){
				boolean hasSub = function.getFunctions().size()!=0 && map.containsKey(level+1);
				String menu_url = function.getFunctionUrl();
				if(StringUtils.isNotEmpty(menu_url)){
					menu_url += "&clickFunctionId="+function.getId();
				}
				menuString.append("		<li onclick=\"showContent(\'"+ MutiLangUtils.getLang(function.getFunctionName()) +"\',\'"+menu_url+"\')\"  title=\""+ MutiLangUtils.getLang(function.getFunctionName()) +"\" url=\""+function.getFunctionUrl()+"\" ");
				if(hasSub){
					menuString.append(" class=\"dropdown-submenu\"");
				}
				menuString.append(" > ");
				menuString.append("			<a href=\"javascript:;\"> ");
				menuString.append("				<span class=\"bootstrap-icon\" style=\"background-image: url('"+function.getIcon().getIconPath()+"')\"></span>		 ");
				menuString.append(MutiLangUtils.getLang(function.getFunctionName()));
				menuString.append("			</a> ");
				if(hasSub){
					menuString.append(getBootStrapChild(function,level+1,map));
				}
				menuString.append("		</li> ");
			}
		}
		menuString.append("		</ul> ");
		return menuString.toString();
	}
	
	
	//update-start--Author:gaofeng  Date:2014-02-14：新增webos头部菜单导航,多级菜单
	/**
	 * 拼装webos头部菜单
	 * @param map
	 * @return
	 */
	public static String getWebosMenu(Map<Integer, List<FunctionView>> map) {
		StringBuffer menuString = new StringBuffer();
		StringBuffer DeskpanelString = new StringBuffer();
		StringBuffer dataString = new StringBuffer();
		String menu = "";
		String desk = "";
		String data = "";
		
		//menu的全部json，这里包括对菜单的展示及每个二级菜单的点击出详情
//		menuString.append("[");
		menuString.append("{");
		//绘制data.js数组，用于替换data.js中的app:{//桌面1 'dtbd':{ appid:'2534',,······
		dataString.append("{app:{");
		//绘制Deskpanel数组，用于替换webos-core.js中的Icon1:['dtbd','sosomap','jinshan'],······
		DeskpanelString.append("{");
		
		List<FunctionView> pFunctions = (List<FunctionView>) map.get(0);
		if(pFunctions==null || pFunctions.size()==0){
			return "";
		}
		int n = 1;
		for (FunctionView pFunction : pFunctions) {
			//是否有子菜单
			boolean hasSub = pFunction.getFunctions().size()==0?false:true;
			//绘制一级菜单
//			menuString.append("{ ");
			menuString.append("\""+ pFunction.getId() + "\":");
			menuString.append("{\"id\":\""+pFunction.getId()+"\",\"name\":\""+pFunction.getFunctionName()+"\",\"path\":\""+pFunction.getIcon().getIconPath()+"\",\"level\":\""+pFunction.getFunctionLevel()+"\",");
			menuString.append("\"child\":{");

			//绘制Deskpanel数组
			DeskpanelString.append("Icon"+n+":[");
			
			//绘制二级菜单
			if(hasSub){
//				menuString.append(getWebosChild(pFunction, 1, map));
				DeskpanelString.append(getWebosDeskpanelChild(pFunction, 1, map));
				dataString.append(getWebosDataChild(pFunction, 1, map));
			}
			DeskpanelString.append("],");
			menuString.append("}},");
			n++;
		}

		menu = menuString.substring(0, menuString.toString().length()-1);
//		menu += "]";
		menu += "}";
		
		data = dataString.substring(0, dataString.toString().length()-1);
		data += "}}";
		
		desk = DeskpanelString.substring(0, DeskpanelString.toString().length()-1);
		desk += "}";
		
		//初始化为1，需减少一个个数。
		n = n-1;
		
//		System.out.println("-------------------");
//		System.out.println(menu+"$$"+desk+"$$"+data+"$$"+"{\"total\":"+n+"}");
		return menu+"$$"+desk+"$$"+data+"$$"+n;
	}
	/**
	 * @Title: getWebosChild
	 * @Description: 递归获取Webos的子菜单
	 *  @param parent
	 *  @param level
	 *  @param map
	 * @return String    
	 * @throws
	 */
	private static String getWebosChild(FunctionView parent, int level, Map<Integer, List<FunctionView>> map){
		StringBuffer menuString = new StringBuffer();
		String menu = "";
		List<FunctionView> list = map.get(level);
		if(list==null || list.size()==0){
			return "";
		}
		for (FunctionView function : list) {
			if (function.getParentFunction().getId().equals(parent.getId())){
				boolean hasSub = function.getFunctions().size()!=0 && map.containsKey(level+1);
//				String menu_url = function.getFunctionUrl();
//				if(StringUtils.isNotEmpty(menu_url)){
//					menu_url += "&clickFunctionId="+function.getId();
//				}
				menuString.append("\""+ function.getId() + "\":");
				menuString.append("{\"id\":\""+function.getId()+"\",\"name\":\""+ MutiLangUtils.getLang(function.getFunctionName()) +"\",\"path\":\""+function.getIcon().getIconPath()+"\",\"url\":\""+function.getFunctionUrl()+"\",\"level\":\""+function.getFunctionLevel()+"\"}");
				
				if(hasSub){
					menuString.append("\"child\":{");
					menuString.append(getWebosChild(function,level+1,map));
					menuString.append("	} ");
				}
				menuString.append(",");
			}
		}
		menu = menuString.substring(0, menuString.toString().length()-1);
		return menu;
	}
	private static String getWebosDeskpanelChild(FunctionView parent, int level, Map<Integer, List<FunctionView>> map){
		StringBuffer DeskpanelString = new StringBuffer();
		String desk = "";
		List<FunctionView> list = map.get(level);
		if(list==null || list.size()==0){
			return "";
		}
		for (FunctionView function : list) {
			if (function.getParentFunction().getId().equals(parent.getId())){
				DeskpanelString.append("'"+function.getId()+"',");
			}
		}
		desk = DeskpanelString.substring(0, DeskpanelString.toString().length()-1);
		return desk;
	}
	private static String getWebosDataChild(FunctionView parent, int level, Map<Integer, List<FunctionView>> map){
		StringBuffer dataString = new StringBuffer();
		String data = "";
		List<FunctionView> list = map.get(level);
		if(list==null || list.size()==0){
			return "";
		}
		for (FunctionView function : list) {
			if (function.getParentFunction().getId().equals(parent.getId())){
				dataString.append("'"+function.getId()+"':{ ");
				dataString.append("appid:'"+function.getId()+"',");
				dataString.append("url:'"+function.getFunctionUrl()+"',");
//				dataString.append(getIconandName(function.getFunctionName()));
				dataString.append(getIconAndNameForDesk(function));
				dataString.append("asc :"+function.getFunctionOrder());
				dataString.append(" },");
			}
		}
//		data = dataString.substring(0, dataString.toString().length()-1);
		data = dataString.toString();
		return data;
	}

    private static String getIconAndNameForDesk(FunctionView function) {
        StringBuffer dataString = new StringBuffer();

        String colName = function.getIconDesk() == null ? null : function.getIconDesk().getIconPath();
        colName = (colName == null || colName.equals("")) ? "plug-in/sliding/icon/default.png" : colName;
        String functionName = MutiLangUtils.getLang(function.getFunctionName());

        dataString.append("icon:'" + colName + "',");
        dataString.append("name:'"+functionName+"',");

        return dataString.toString();
    }
    @Deprecated
	private static String getIconandName(String functionName) {
		StringBuffer dataString = new StringBuffer();
		
		if("online开发".equals(functionName)){
			dataString.append("icon:'Customize.png',");
		}else if("表单配置".equals(functionName)){
			dataString.append("icon:'Applications Folder.png',");
		}else if("动态表单配置".equals(functionName)){
			dataString.append("icon:'Documents Folder.png',");
		}else if("用户分析".equals(functionName)){
			dataString.append("icon:'User.png',");
		}else if("报表分析".equals(functionName)){
			dataString.append("icon:'Burn.png',");
		}else if("用户管理".equals(functionName)){
			dataString.append("icon:'Finder.png',");
		}else if("数据字典".equals(functionName)){
			dataString.append("icon:'friendnear.png',");
		}else if("角色管理".equals(functionName)){
			dataString.append("icon:'friendgroup.png',");
		}else if("菜单管理".equals(functionName)){
			dataString.append("icon:'kaikai.png',");
		}else if("图标管理".equals(functionName)){
			dataString.append("icon:'kxjy.png',");
		}else if("表单验证".equals(functionName)){
			dataString.append("icon:'qidianzhongwen.png',");
		}else if("一对多模型".equals(functionName)){
			dataString.append("icon:'qqread.png',");
		}else if("特殊布局".equals(functionName)){
			dataString.append("icon:'xiami.png',");
		}else if("在线word".equals(functionName)){
			dataString.append("icon:'musicbox.png',");
		}else if("多附件管理".equals(functionName)){
			dataString.append("icon:'vadio.png',");
		}else if("数据监控".equals(functionName)){
			dataString.append("icon:'Super Disk.png',");
		}else if("定时任务".equals(functionName)){
			dataString.append("icon:'Utilities.png',");
		}else if("系统日志".equals(functionName)){
			dataString.append("icon:'fastsearch.png',");
		}else if("在线维护".equals(functionName)){
			dataString.append("icon:'Utilities Folder.png',");
		}else{
			dataString.append("icon:'folder_o.png',");
		}
		dataString.append("name:'"+functionName+"',");
		
		return dataString.toString();
	}
    /***
     * Ace 风格
     * @param map
     * @return
     */
	public static String getAceMultistageTree(Map<Integer, List<FunctionView>> map) {
		if(map==null||map.size()==0||!map.containsKey(0)){return "不具有任何权限,\n请找管理员分配权限";}
		StringBuffer menuString = new StringBuffer();
		List<FunctionView> list = map.get(0);
        int curIndex = 0;
            for (FunctionView function : list) {
                menuString.append("<li>");
                menuString.append("<a href=\"#\" class=\"dropdown-toggle\" ><i class=\"menu-icon fa fa-desktop\"></i>")
                .append(MutiLangUtils.getLang(function.getFunctionName()));
                if(!function.hasSubFunction(map)){
                	menuString.append("</a></li>");
                	menuString.append(getSubMenu(function,1,map));
                }else{
                	menuString.append("<b class=\"arrow\"></b><ul  class=\"submenu\" >");
                	menuString.append(getSubMenu(function,1,map));
                	menuString.append("</ul></li>");
                }
                curIndex++;
            }

		return menuString.toString();
	}
	
	private static String getSubMenu(FunctionView parent, int level, Map<Integer, List<FunctionView>> map){
		StringBuffer menuString = new StringBuffer();
		List<FunctionView> list = map.get(level);
		for (FunctionView function : list) {
			if (function.getParentFunction().getId().equals(parent.getId())){
				if(!function.hasSubFunction(map)){
					menuString.append(getLeafOfACETree(function));
				}else if(map.containsKey(level+1)){
					String icon = "folder";
					icon=getDefaultIcon(icon, function.getIcon());
					menuString.append("<li><a href=\"#\" ><i class=\"menu-icon fa fa-eye pink\" iconCls=\"" 
							+ icon+"\" ></i>"+ MutiLangUtils.getLang(function.getFunctionName()) +"<b class=\"arrow\"></b>");
					menuString.append("<ul class=\"submenu\">");
					menuString.append(getChildOfTree(function,level+1,map));
					menuString.append("</ul></li>");
				}
			}
		}
		return menuString.toString();
	}
	private static String getLeafOfACETree(FunctionView function){
		StringBuffer menuString = new StringBuffer();
		String icon = "folder";
		icon=getDefaultIcon(icon, function.getIcon());
		String name =  MutiLangUtils.getLang(function.getFunctionName()) ;
		menuString.append("<li iconCls=\"");
		menuString.append(icon);
		menuString.append("\"> <a href=\"javascript:loadModule(\'");
		menuString.append(name);
		menuString.append("\',\'");
		menuString.append(function.getFunctionUrl());
		menuString.append("&clickFunctionId=");
		menuString.append(function.getId());
		menuString.append("\',\'");
		menuString.append(icon);
		menuString.append("\')\"  title=\"");
		menuString.append(name);
		menuString.append("\" url=\"");
		menuString.append(function.getFunctionUrl());
		menuString.append("\"  >");
		menuString.append(name);
		menuString.append("</a></li>");
		return menuString.toString();
	}

	private static String getDefaultIcon(String icon, IconView tsIcon) {
		try{
			if (tsIcon != null) {
				icon = SystemContainer.IconContainer.allTSIcons.get(tsIcon.getId()).getIconClazz();
			}
		}catch (Exception e){
//			log.error(e.getMessage());
		}
		return icon;
	}


	public static String getDIYMultistageTree(Map<Integer, List<FunctionView>> map) {
		if(map==null||map.size()==0||!map.containsKey(0)){return "不具有任何权限,\n请找管理员分配权限";}
		StringBuffer menuString = new StringBuffer();
		List<FunctionView> list = map.get(0);
		int curIndex = 0;
		for (FunctionView function : list) {
			menuString.append("<li>");
			menuString.append("<a href=\"#\" class=\"dropdown-toggle\" ><i class=\"menu-icon fa fa-desktop\"></i>")
					.append(MutiLangUtils.getLang(function.getFunctionName()));
			if(!function.hasSubFunction(map)){
				menuString.append("</a></li>");
				menuString.append(getDIYSubMenu(function,1,map));
			}else{
				menuString.append("<b class=\"arrow\"></b><ul  class=\"submenu\" >");
				menuString.append(getDIYSubMenu(function,1,map));
				menuString.append("</ul></li>");
			}
			curIndex++;
		}

		return menuString.toString();
	}

	private static String getDIYSubMenu(FunctionView parent, int level, Map<Integer, List<FunctionView>> map){
		StringBuffer menuString = new StringBuffer();
		List<FunctionView> list = map.get(level);
		for (FunctionView function : list) {
			if (function.getParentFunction().getId().equals(parent.getId())){
				if(!function.hasSubFunction(map)){
					menuString.append(getLeafOfDIYTree(function));
				}else if(map.containsKey(level+1)){
					String icon = "folder";
					try{
						if (function.getIcon() != null) {
							icon = SystemContainer.IconContainer.allTSIcons.get(function.getIcon().getId()).getIconClazz();
						}
					}catch(Exception e){
						//TODO handle icon load exception
					}
					menuString.append("<li><a href=\"#\" ><i class=\"menu-icon fa fa-eye pink\" iconCls=\""
							+ icon+"\" ></i>"+MutiLangUtils.getLang(function.getFunctionName()) +"<b class=\"arrow\"></b>");
					menuString.append("<ul class=\"submenu\">");
					menuString.append(getChildOfTree(function,level+1,map));
					menuString.append("</ul></li>");
				}
			}
		}
		return menuString.toString();
	}


	private static String getLeafOfDIYTree(FunctionView function){
		StringBuffer menuString = new StringBuffer();
		String icon = "folder";
		if (function.getIcon() != null) {
			icon = SystemContainer.IconContainer.allTSIcons.get(function.getIcon().getId()).getIconClazz();
		}
		String name =MutiLangUtils.getLang(function.getFunctionName()) ;
		menuString.append("<li iconCls=\"");
		menuString.append(icon);
		menuString.append("\"> <a href=\"javascript:loadModule(\'");
		menuString.append(name);
		menuString.append("\',\'");
		menuString.append(function.getFunctionUrl());
		menuString.append("&clickFunctionId=");
		menuString.append(function.getId());
		menuString.append("\',\'");
		menuString.append(icon);
		menuString.append("\')\"  title=\"");
		menuString.append(name);
		menuString.append("\" url=\"");
		menuString.append(function.getFunctionUrl());
		menuString.append("\"  >");
		menuString.append(name);
		menuString.append("</a></li>");
		return menuString.toString();
	}
}