package com.abocode.jfaster.platform.view.widgets.easyui;

import com.abocode.jfaster.core.common.util.PropertiesUtils;
import com.abocode.jfaster.core.common.util.JspWriterUtils;
import org.springframework.util.StringUtils;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
/**
 * 
 * @author  张代浩
 *
 */
public class CkeditorTag extends TagSupport {

	private static final long serialVersionUID = 1L;
	protected String name;// 属性名称
	protected String value;// 默认值
	protected boolean isfinder;// 是否加载ckfinder(默认true)
	protected String type;// 其它属性(用法:height:400,uiColor:'#9AB8F3' 用,分割)

	public boolean isIsfinder() {
		return isfinder;
	}

	public void setIsfinder(boolean isfinder) {
		this.isfinder = isfinder;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

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

		sb.append("<textarea id=\"" + name + "_text\" name=\"" + name + "\">"
				+ value + "</textarea>");
		sb.append("<script type=\"text/javascript\">var ckeditor_" + name
				+ "=CKEDITOR.replace(\"" + name + "_text\",{");
		if (isfinder) {
			PropertiesUtils util = new PropertiesUtils("sysConfig.properties");
			sb.append("filebrowserBrowseUrl:"
					+ util.readProperty("filebrowserBrowseUrl") + ",");
			sb.append("filebrowserImageBrowseUrl:"
					+ util.readProperty("filebrowserImageBrowseUrl") + ",");
			sb.append("filebrowserFlashBrowseUrl:"
					+ util.readProperty("filebrowserFlashBrowseUrl") + ",");
			sb.append("filebrowserUploadUrl:"
					+ util.readProperty("filebrowserUploadUrl") + ",");
			sb.append("filebrowserImageUploadUrl:"
					+ util.readProperty("filebrowserImageUploadUrl") + ",");
			sb.append("filebrowserFlashUploadUrl:"
					+ util.readProperty("filebrowserFlashUploadUrl") + "");
		}
		if (isfinder && !StringUtils.isEmpty(type))
			sb.append(",");
		if (!StringUtils.isEmpty(type))
			sb.append(type);
		sb.append("});");
		if (isfinder) {
			sb.append("CKFinder.SetupCKEditor(ckeditor_" + name + ");");
		}
		sb.append("</script>");
		return sb.toString();
	}
}