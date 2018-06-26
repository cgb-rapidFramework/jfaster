package com.abocode.jfaster.core.platform.view.widgets.easyui;

import com.abocode.jfaster.core.common.util.JspWriterUtils;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
/**
 * 
 * 类描述：改变HTML控件颜色
 * @author:  张代浩
 * @date： 日期：2012-12-7 时间：上午10:17:45
 * @version 1.0
 */
public class ColorChangeTag extends TagSupport {
	public int doStartTag() throws JspTagException {
		return EVAL_PAGE;
	}
	public int doEndTag() throws JspTagException {
		JspWriter out = this.pageContext.getOut();
		JspWriterUtils.write(out,end());
		return EVAL_PAGE;
	}
	public String end() {
		StringBuffer sb = new StringBuffer();
		return sb.toString();
	}

}
