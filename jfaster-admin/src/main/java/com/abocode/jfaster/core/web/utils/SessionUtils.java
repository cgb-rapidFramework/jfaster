package com.abocode.jfaster.core.web.utils;

import com.abocode.jfaster.core.web.aop.DataBaseConstant;
import com.abocode.jfaster.core.common.util.ContextHolderUtils;
import com.abocode.jfaster.core.common.util.ConvertUtils;
import com.abocode.jfaster.core.common.util.DateUtils;
import com.abocode.jfaster.core.common.util.StringUtils;
import com.abocode.jfaster.system.entity.RoleFunction;
import com.abocode.jfaster.core.web.manager.ClientBean;
import com.abocode.jfaster.system.entity.Role;
import com.abocode.jfaster.system.entity.User;
import com.abocode.jfaster.core.web.manager.ClientManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;


/**
 * 项目参数工具类
 * guanxf
 */
public class SessionUtils {

	private static final ResourceBundle bundle = java.util.ResourceBundle.getBundle("sysConfig");
	 /**
	  * 初始化session
	  */
	private static void initSession(HttpSession session) {
		ClientBean client =SessionShareCenter.getClient();
		if(!StringUtils.isEmpty(client)){
			String userId= SessionShareCenter.getUserId();
			if(!StringUtils.isEmpty(userId) &&!StringUtils.isEmpty(client.getUser())){
				client.getUser().setId(userId);
			}
			if(ClientManager.getInstance().getClient(session.getId())==null){
				  ClientManager.getInstance().addClinet(session.getId(), client);
			}
		}else{//如果sesion为空，移除单列中的session
			ClientManager.getInstance().removeClinet(session.getId());
		}		
	}
	
	
	/**
	 * 初始化session
	 */
	private static void initRoles(HttpSession session) {
		List<Role> roleList =SessionShareCenter.getRoles();
		List<Role> roles=ClientManager.getInstance().getClient().getRoles();
		if(roles==null && roleList!=null && roleList.size()>0){
			ClientManager.getInstance().getClient().setRoles(roleList);
		}
	}
	
	
	/**
	 * 获取session定义名称
	 * 
	 * @return
	 */
	public static final String getSessionattachmenttitle(String sessionName) {
		return bundle.getString(sessionName);
	}
	/**
	 * 获取用户信息
	 * @return
	 */
	public static final User getCurrentUser() {
		HttpSession session = ContextHolderUtils.getSession();
		SessionUtils.initSession(session);//必须先初始化一次Session，否则集群时候异常
		if(ClientManager.getInstance().getClient(session.getId())!=null){
			return ClientManager.getInstance().getClient(session.getId()).getUser();
		}
		return null;
	}
	/**
	 * 获取角色信息
	 * @return
	 */
	public static final List<Role>  getCurrentRole() {
		HttpSession session = ContextHolderUtils.getSession();
		SessionUtils.initRoles(session);//必须先初始化一次Session，否则集群时候异常
		if(ClientManager.getInstance().getClient(session.getId())!=null){
			return ClientManager.getInstance().getClient(session.getId()).getRoles();
		}
		return null;
	}
	
	/**
	 * 获取角色code 信息
	 * @return
	 */
	public static final String  getCurrentRoleCode() {
		String roleCode="";
		HttpSession session = ContextHolderUtils.getSession();
		if(ClientManager.getInstance().getClient(session.getId())!=null){
			List<Role> roles=getCurrentRole();
			if(null!=roles && roles.size()>0){
				for (Role role : roles) {
					roleCode += role.getRoleCode() + ",";
				}
			}
			if (roleCode.length() > 0) {
				roleCode = roleCode.substring(0, roleCode.length() - 1);
			}
			return roleCode;
		}
		return null;
	}
	
	@Deprecated
	public static final List<RoleFunction> getSessionTSRoleFunction(String roleId) {
		HttpSession session = ContextHolderUtils.getSession();
		if (session.getAttributeNames().hasMoreElements()) {
			List<RoleFunction> TSRoleFunctionList = (List<RoleFunction>)session.getAttribute(roleId);
			if (TSRoleFunctionList != null) {
				return TSRoleFunctionList;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
	
	/**
	 * 获得请求路径
	 * 
	 * @param request
	 * @return
	 */
	public static String getRequestPath(HttpServletRequest request) {
		String requestPath = request.getRequestURI() + "?" + request.getQueryString();
		if (requestPath.indexOf("&") > -1) {// 去掉其他参数
			requestPath = requestPath.substring(0, requestPath.indexOf("&"));
		}
		requestPath = requestPath.substring(request.getContextPath().length() + 1);// 去掉项目路径
		return requestPath;
	}

	/**
	 * 获取配置文件参数
	 * 
	 * @param name
	 * @return
	 */
	public static final String getConfigByName(String name) {
		return bundle.getString(name);
	}

	/**
	 * 获取配置文件参数
	 * 
	 * @param path
	 * @return
	 */
	public static final Map<Object, Object> getConfigMap(String path) {
		ResourceBundle bundle = ResourceBundle.getBundle(path);
		Set set = bundle.keySet();
		return ConvertUtils.SetToMap(set);
	}
	public static String getSystempPath() {
		return System.getProperty("java.io.tmpdir");
	}

	public static String getSeparator() {
		return System.getProperty("file.separator");
	}

	public static String getParameter(String field) {
		HttpServletRequest request = ContextHolderUtils.getRequest();
		return request.getParameter(field);
	}


    /**
     * 获取随机码的长度
     *
     * @return 随机码的长度
     */
    public static String getRandCodeLength() {
        return bundle.getString("randCodeLength");
    }

    /**
     * 获取随机码的类型
     *
     * @return 随机码的类型
     */
    public static String getRandCodeType() {
        return bundle.getString("randCodeType");
    }

    /**
     * 获取组织机构编码长度的类型
     *
     * @return 组织机构编码长度的类型
     */
    public static String getOrgCodeLengthType() {
        return bundle.getString("orgCodeLengthType");
    }
    /**
     * 获取用户系统变量
     * @param key
     * 			DataBaseConstant 中的值
     * @return
     */
	public static String getUserSystemData(String key) {
		//替换为系统的登录用户账号
		if (key.equals(DataBaseConstant.SYS_USER_CODE)
				|| key.equals(DataBaseConstant.SYS_USER_CODE_TABLE)) {
			return getCurrentUser().getUsername();
		}
		//替换为系统登录用户真实名字
		if (key.equals(DataBaseConstant.SYS_USER_NAME)
				|| key.equals(DataBaseConstant.SYS_USER_NAME_TABLE)
			) {
			return getCurrentUser().getRealName();
		}
		
		//替换为系统登录用户的公司编码
		if (key.equals(DataBaseConstant.SYS_COMPANY_CODE)|| key.equals(DataBaseConstant.SYS_COMPANY_CODE_TABLE)) {
			return getCurrentUser().getCurrentDepart().getOrgCode()
					.substring(0, Integer.valueOf(getOrgCodeLengthType()));
		}
		//替换为系统用户登录所使用的机构编码
		if (key.equals(DataBaseConstant.SYS_ORG_CODE)|| key.equals(DataBaseConstant.SYS_ORG_CODE_TABLE)) {
			return getCurrentUser().getCurrentDepart().getOrgCode();
		}
		//替换为当前系统时间(年月日)
		if (key.equals(DataBaseConstant.SYS_DATE)|| key.equals(DataBaseConstant.SYS_DATE_TABLE)) {
			return DateUtils.formatDate(DateUtils.YYYYMMDD);
		}
		//替换为当前系统时间（年月日时分秒）
		if (key.equals(DataBaseConstant.SYS_TIME)|| key.equals(DataBaseConstant.SYS_TIME_TABLE)) {
			return DateUtils.formatDate(DateUtils.YYYYMMDDHHMMSS);
		}
		return null;
	}
}
