package com.abocode.jfaster.platform.view.widgets.easyui;

import com.abocode.jfaster.core.common.util.ConvertUtils;
import com.abocode.jfaster.web.system.interfaces.view.OperationView;
import com.abocode.jfaster.core.common.constants.Globals;
import com.abocode.jfaster.core.common.container.SystemContainer;
import com.abocode.jfaster.core.common.util.JspWriterUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.util.Set;
/**
 * 
 * @Title:AuthFilterTag
 * @description:列表按钮权限过滤
 * @date Aug 24, 2013 7:46:57 PM
 * @version V1.0
 */
public class AuthFilterTag extends TagSupport{
	/**列表容器的ID*/
	protected String name;
	protected Boolean filter=false;

	public int doStartTag() throws JspException {
		return super.doStartTag();
	}
	
	public int doEndTag() throws JspException {
		JspWriter out = this.pageContext.getOut();
		JspWriterUtils.write(out,end().toString());
		return EVAL_PAGE;
	}

	protected String end() {
		StringBuilder out = new StringBuilder();
		getAuthFilter(out);
		return out.toString();
	}
	/**
	 * 获取隐藏按钮的JS代码
	 * @param out
	 */
	protected void getAuthFilter(StringBuilder out) {
		out.append("<script type=\"text/javascript\">");
		out.append("$(document).ready(function(){");
		if(filter){
			Set<String> operationCodes = (Set<String>) super.pageContext.getRequest().getAttribute(Globals.OPERATIONCODES);
			if (null!=operationCodes) {
				for (String MyoperationCode : operationCodes) {
					if (ConvertUtils.isEmpty(MyoperationCode))
						break;
					OperationView operation = SystemContainer.OperationContainer.operations.get(MyoperationCode);
					if (operation.getOperationcode().startsWith(".") || operation.getOperationcode().startsWith("#")){
						if (operation.getOperationType().intValue()==Globals.OPERATION_TYPE_HIDE){
							//out.append("$(\""+name+"\").find(\"#"+operation.getOperationcode().replaceAll(" ", "")+"\").hide();");
							out.append("$(\""+operation.getOperationcode().replaceAll(" ", "")+"\").hide();");
						}else {
							//out.append("$(\""+name+"\").find(\"#"+operation.getOperationcode().replaceAll(" ", "")+"\").find(\":input\").attr(\"disabled\",\"disabled\");");
							out.append("$(\""+operation.getOperationcode().replaceAll(" ", "")+"\").attr(\"disabled\",\"disabled\");");
							out.append("$(\""+operation.getOperationcode().replaceAll(" ", "")+"\").find(\":input\").attr(\"disabled\",\"disabled\");");
						}
					}
				}
			}
			
		}
		out.append("});");
		out.append("</script>");
	}
	
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
