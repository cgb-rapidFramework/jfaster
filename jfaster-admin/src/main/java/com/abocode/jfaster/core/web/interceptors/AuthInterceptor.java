package com.abocode.jfaster.core.web.interceptors;

import com.abocode.jfaster.admin.system.dto.DataRuleDto;
import com.abocode.jfaster.admin.system.repository.SystemRepository;
import com.abocode.jfaster.admin.system.service.FunctionService;
import com.abocode.jfaster.core.common.constants.Globals;
import com.abocode.jfaster.core.common.util.ConfigUtils;
import com.abocode.jfaster.core.common.util.ContextHolderUtils;
import com.abocode.jfaster.core.common.util.ConvertUtils;
import com.abocode.jfaster.core.web.manager.ClientBean;
import com.abocode.jfaster.core.web.manager.ClientManager;
import com.abocode.jfaster.system.entity.Function;
import com.abocode.jfaster.system.entity.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Set;


/**
 * 权限拦截器
 */
public class AuthInterceptor implements HandlerInterceptor {
    @Autowired
    private SystemRepository systemService;
    @Autowired
    private FunctionService functionService;
    private List<String> excludeUrls;

    public List<String> getExcludeUrls() {
        return excludeUrls;
    }

    public void setExcludeUrls(List<String> excludeUrls) {
        this.excludeUrls = excludeUrls;
    }


    /**
     * 在controller前拦截
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
        String requestPath = ConfigUtils.getRequestPath(request);// 用户访问的资源地址
        HttpSession session = ContextHolderUtils.getSession();
        ClientBean client = ClientManager.getInstance().getClient(session.getId());
        if (client == null) {
            client = ClientManager.getInstance().getClient(
                    request.getParameter("sessionId"));
        }
        if (excludeUrls.contains(requestPath)) {
            //如果该请求不在拦截范围内，直接返回true
            return true;
        }
        String clickFunctionId = ConvertUtils.getString(request.getParameter("clickFunctionId"));
        if (client != null && client.getUser() != null) {
            if ((!functionService.hasMenuAuth(requestPath,clickFunctionId)) && !client.getUser().getUsername().equals("admin")) {
                response.sendRedirect("loginController.do?noAuth");
                return false;
            }
            String functionId = "";
            //onlinecoding的访问地址有规律可循，数据权限链接篡改
            if (requestPath.equals("cgAutoListController.do?findDataGridData")) {
                requestPath += "&configId=" + request.getParameter("configId");
            }
            if (requestPath.equals("cgAutoListController.do?list")) {
                requestPath += "&id=" + request.getParameter("id");
            }
            if (requestPath.equals("cgFormBuildController.do?ftlForm")) {
                requestPath += "&tableName=" + request.getParameter("tableName");
            }
            //这个地方用全匹配？应该是模糊查询吧
            //TODO
            List<Function> functions = systemService.findAllByProperty(Function.class, "functionUrl", requestPath);
            if (functions.size() > 0) {
                functionId = functions.get(0).getId();
            }

            //Step.1 第一部分处理页面表单和列表的页面控件权限（页面表单字段+页面按钮等控件）
            if (!ConvertUtils.isEmpty(functionId)) {
                //获取菜单对应的页面控制权限（包括表单字段和操作按钮）
                Set<String> operationCodes = systemService.getOperationCodesByUserIdAndFunctionId(client.getUser().getId(), functionId);
                request.setAttribute(Globals.OPERATION_CODES, operationCodes);
            }
            List<Operation> operations = functionService.findById(functionId, client.getUser().getId());
            request.setAttribute(Globals.NO_AUTO_OPERATION_CODES, operations);

            //数据权限规则的查询
            //查询所有的当前这个用户所对应的角色和菜单的datarule的数据规则id
            Set<String> dataRuleCodes = systemService.getOperationCodesByUserIdAndDataId(client.getUser().getId(), functionId);
            request.setAttribute("dataRulecodes", dataRuleCodes);
            DataRuleDto dataRuleDto = functionService.installDataRule(dataRuleCodes);
            request.setAttribute(Globals.MENU_DATA_AUTHOR_RULES, dataRuleDto.getHqlDataRules()); // 3.往list里面增量存指
            request.setAttribute(Globals.MENU_DATA_AUTHOR_RULE_SQL, dataRuleDto.getRuleSql());// 3.往sql串里面增量拼新的条件
            return true;
        } else {
            forward(request, response);
            return false;
        }
    }

    private void forward(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("webpage/login/timeout.jsp").forward(request, response);
    }

}
