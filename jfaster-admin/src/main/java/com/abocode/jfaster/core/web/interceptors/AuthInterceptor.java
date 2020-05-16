package com.abocode.jfaster.core.web.interceptors;

import com.abocode.jfaster.core.common.util.ContextHolderUtils;
import com.abocode.jfaster.core.common.util.ConvertUtils;
import com.abocode.jfaster.core.common.constants.Globals;
import com.abocode.jfaster.admin.system.dto.bean.ClientBean;
import com.abocode.jfaster.core.common.util.DataRuleUtils;
import com.abocode.jfaster.core.web.hqlsearch.SysContextSqlConvert;
import com.abocode.jfaster.core.web.manager.ClientManager;
import com.abocode.jfaster.admin.system.repository.SystemRepository;
import com.abocode.jfaster.core.common.util.ConfigUtils;
import com.abocode.jfaster.system.entity.DataRule;
import com.abocode.jfaster.system.entity.Function;
import com.abocode.jfaster.system.entity.Operation;
import com.abocode.jfaster.system.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * 权限拦截器
 * 
 * @author  张代浩
 * 
 */
public class AuthInterceptor implements HandlerInterceptor {
	 
	private SystemRepository systemService;
	private List<String> excludeUrls;
	private static List<Function> functionList;


	public List<String> getExcludeUrls() {
		return excludeUrls;
	}

	public void setExcludeUrls(List<String> excludeUrls) {
		this.excludeUrls = excludeUrls;
	}

	public SystemRepository getSystemService() {
		return systemService;
	}

	@Autowired
	public void setSystemService(SystemRepository systemService) {
		this.systemService = systemService;
	}

	/**
	 * 在controller后拦截
	 */
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object object, Exception exception) throws Exception {
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object object, ModelAndView modelAndView) throws Exception {

	}

	/**
	 * 在controller前拦截
	 */
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
		String requestPath = ConfigUtils.getRequestPath(request);// 用户访问的资源地址
		HttpSession session = ContextHolderUtils.getSession();
		ClientBean client = ClientManager.getInstance().getClient(session.getId());
		if(client == null){ 
			client = ClientManager.getInstance().getClient(
					request.getParameter("sessionId"));
		}
		if (excludeUrls.contains(requestPath)) {
			//如果该请求不在拦截范围内，直接返回true
			return true;
		} else {
			if (client != null && client.getUser()!=null ) {
				if((!hasMenuAuth(request)) && !client.getUser().getUsername().equals("admin")){
					 response.sendRedirect("loginController.do?noAuth");
					//request.getRequestDispatcher("webpage/common/noAuth.jsp").forward(request, response);
					return false;
				} 
				//String functionId=ConvertUtils.getString(request.getParameter("clickFunctionId"));
				String functionId="";
				//onlinecoding的访问地址有规律可循，数据权限链接篡改
				if(requestPath.equals("cgAutoListController.do?datagrid")) {
					requestPath += "&configId=" +  request.getParameter("configId");
				}
				if(requestPath.equals("cgAutoListController.do?list")) {
					requestPath += "&id=" +  request.getParameter("id");
				}
				if(requestPath.equals("cgFormBuildController.do?ftlForm")) {
					requestPath += "&tableName=" +  request.getParameter("tableName");
				}
				//这个地方用全匹配？应该是模糊查询吧
				//TODO
				List<Function> functions = systemService.findAllByProperty(Function.class, "functionUrl", requestPath);
				
				if (functions.size()>0){
					functionId = functions.get(0).getId();
				}
				
				//Step.1 第一部分处理页面表单和列表的页面控件权限（页面表单字段+页面按钮等控件）
				if(!ConvertUtils.isEmpty(functionId)){
					//获取菜单对应的页面控制权限（包括表单字段和操作按钮）
					Set<String> operationCodes = systemService.getOperationCodesByUserIdAndFunctionId(client.getUser().getId(), functionId);
					request.setAttribute(Globals.OPERATIONCODES, operationCodes);
				}
				if(!ConvertUtils.isEmpty(functionId)){
					//List<String> allOperation=this.systemService.findListbySql("SELECT operationcode FROM t_s_operation  WHERE functionid='"+functionId+"'");
					List<Operation> allOperation=this.systemService.findAllByProperty(Operation.class, "Function.id", functionId);
					
					List<Operation> newall = new ArrayList<Operation>();
					if(allOperation.size()>0){
						for(Operation s:allOperation){
						    //s=s.replaceAll(" ", "");
							newall.add(s); 
						}						 
						String hasOperSql="SELECT operation FROM t_s_role_function fun, t_s_role_user role WHERE  " +
							"fun.functionid='"+functionId+"' AND fun.operation!=''  AND fun.roleid=role.roleid AND role.userid='"+client.getUser().getId()+"' ";
						
						List<String> hasOperList = this.systemService.findListBySql(hasOperSql);
					    for(String operationIds:hasOperList){
							    for(String operationId:operationIds.split(",")){
							    	operationId=operationId.replaceAll(" ", "");
							        Operation operation =  new Operation();
							        operation.setId(operationId);
							    	newall.remove(operation);
							    } 
						} 
					}
					request.setAttribute(Globals.NOAUTO_OPERATIONCODES, newall);
					
					 //Step.2  第二部分处理列表数据级权限
					 //小川 -- 菜单数据规则集合(数据权限)
					 List<DataRule> MENU_DATA_AUTHOR_RULES = new ArrayList<DataRule>();
					 //小川 -- 菜单数据规则sql(数据权限)
					 String MENU_DATA_AUTHOR_RULE_SQL="";

					
				 	//数据权限规则的查询
				 	//查询所有的当前这个用户所对应的角色和菜单的datarule的数据规则id
					Set<String> dataruleCodes = systemService.getOperationCodesByUserIdAndDataId(client.getUser().getId(), functionId);
					request.setAttribute("dataRulecodes", dataruleCodes);
					for (String dataRuleId : dataruleCodes) {
						DataRule dataRule = systemService.findEntity(DataRule.class, dataRuleId);
						    MENU_DATA_AUTHOR_RULES.add(dataRule);
							MENU_DATA_AUTHOR_RULE_SQL += SysContextSqlConvert.setSqlModel(dataRule);
					
					}
					 DataRuleUtils.installDataSearchConditon(request, MENU_DATA_AUTHOR_RULES);//菜单数据规则集合
					 DataRuleUtils.installDataSearchConditon(request, MENU_DATA_AUTHOR_RULE_SQL);//菜单数据规则sql

				}
				return true;
			} else {
				//forword(request);
				forward(request, response);
				return false;
			}

		}
	}
	
	/**
	 * 判断用户是否有菜单访问权限
	 * @param request
	 * @return
	 */
	private boolean hasMenuAuth(HttpServletRequest request){
		String requestPath = ConfigUtils.getRequestPath(request);// 用户访问的资源地址
		// 是否是功能表中管理的url
		boolean bMgrUrl = false;
		if (functionList == null) {
			functionList = systemService.findAll(Function.class);
		}
		for (Function function : functionList) {
			if (function.getFunctionUrl() != null && function.getFunctionUrl().startsWith(requestPath)) {
				bMgrUrl = true;
				break;
			}
		}
		String funcid= ConvertUtils.getString(request.getParameter("clickFunctionId"));
		if(!bMgrUrl && (requestPath.indexOf("loginController.do")!=-1||funcid.length()==0)){
			return true;
		}

		if (!bMgrUrl) {
			return true;
		}

		User currLoginUser = ClientManager.getInstance().getClient(ContextHolderUtils.getSession().getId()).getUser();
        String userid = currLoginUser.getId();
		String sql = "SELECT DISTINCT f.id FROM t_s_function f,t_s_role_function  rf,t_s_role_user ru " +
					" WHERE f.id=rf.functionid AND rf.roleid=ru.roleid AND " +
					"ru.userid='"+userid+"' AND f.functionurl like '"+requestPath+"%'";
		List list = this.systemService.queryForListMap(sql);
		if(list.size()==0){
            String orgId = currLoginUser.getCurrentDepart().getId();
            String functionOfOrgSql = "SELECT DISTINCT f.id from t_s_function f, t_s_role_function rf, t_s_role_org ro  " +
                    "WHERE f.ID=rf.functionid AND rf.roleid=ro.role_id " +
                    "AND ro.org_id='" +orgId+ "' AND f.functionurl like '"+requestPath+"%'";
            List functionOfOrgList = this.systemService.queryForListMap(functionOfOrgSql);
			return functionOfOrgList.size() > 0;
        }else{
			return true;
		}
	}
	/**
	 * 转发
	 * 
	 * @return
	 */
	@RequestMapping(params = "forword")
	public ModelAndView forword(HttpServletRequest request) {
		return new ModelAndView(new RedirectView("loginController.do?login"));
	}

	private void forward(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("webpage/login/timeout.jsp").forward(request, response);
	}

}
