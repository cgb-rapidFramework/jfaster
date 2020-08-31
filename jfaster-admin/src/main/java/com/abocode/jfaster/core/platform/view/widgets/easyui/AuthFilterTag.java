package com.abocode.jfaster.core.platform.view.widgets.easyui;

import com.abocode.jfaster.core.common.util.ConvertUtils;
import com.abocode.jfaster.core.common.util.StringUtils;
import com.abocode.jfaster.core.platform.view.OperationView;
import com.abocode.jfaster.core.common.constants.Globals;
import com.abocode.jfaster.core.platform.SystemContainer;
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
		JspWriterUtils.write(out,end());
		return EVAL_PAGE;
	}

	protected String end() {
		StringBuilder out = new StringBuilder();
		out.append("<script type=\"text/javascript\">");
		out.append("$(document).ready(function(){");
		if (StringUtils.isNotEmpty(getJsContent())){
			out.append(getJsContent());
		}
		out.append("});");
		out.append("</script>");
		return out.toString();
	}

	private String  getJsContent() {
		StringBuilder out = new StringBuilder();
		if(filter){
			Set<String> operationCodes = (Set<String>) super.pageContext.getRequest().getAttribute(Globals.OPERATIONCODES);
			if (null!=operationCodes) {
				for (String ops : operationCodes) {
					if (ConvertUtils.isEmpty(ops))
						break;
					OperationView operation = SystemContainer.OperationContainer.getOperationMap().get(ops);
					if (operation.getOperationCode().startsWith(".") || operation.getOperationCode().startsWith("#")){
						if (operation.getOperationType().intValue()==Globals.OPERATION_TYPE_HIDE){
							//out.append("$(\""+name+"\").find(\"#"+operation.getOperationCode().replaceAll(" ", "")+"\").hide();");
							out.append("$(\""+operation.getOperationCode().replaceAll(" ", "")+"\").hide();");
						}else {
							//out.append("$(\""+name+"\").find(\"#"+operation.getOperationCode().replaceAll(" ", "")+"\").find(\":input\").attr(\"disabled\",\"disabled\");");
							out.append("$(\""+operation.getOperationCode().replaceAll(" ", "")+"\").attr(\"disabled\",\"disabled\");");
							out.append("$(\""+operation.getOperationCode().replaceAll(" ", "")+"\").find(\":input\").attr(\"disabled\",\"disabled\");");
						}
					}
				}
			}
		}
		return  out.toString();
	}


	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
