package com.abocode.jfaster.core.platform.view.widgets.easyui;

import com.abocode.jfaster.core.common.util.JspWriterUtils;
import com.abocode.jfaster.core.platform.utils.LanguageUtils;
import org.springframework.util.StringUtils;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.util.UUID;

/**
 * 
 * 类描述：选择器标签
 * 
 * @author:  张代浩
 * @date： 日期：2012-12-7 时间：上午10:17:45
 * @version 1.0
 */
public class ChooseTag extends TagSupport {
	protected String hiddenName;
	protected String textname;//显示文本框字段
	protected String icon;
	protected String title;
	protected String url;
	protected String top;
	protected String left;
	protected String width;
	protected String height;
	protected String name;
	protected String hiddenid;// 隐藏框取值ID
	protected Boolean isclear = false;
	protected String fun;//自定义函数
	protected String inputTextname;
	protected String langArg;
	protected Boolean isInit = false;//是否初始化

	public int doStartTag() throws JspTagException {
		return EVAL_PAGE;
	}


	public int doEndTag() throws JspTagException {
		JspWriter out = this.pageContext.getOut();
		JspWriterUtils.write(out,end());
		return EVAL_PAGE;
	}

	public String end() {
		title = LanguageUtils.doLang(title, langArg);
		String confirm = LanguageUtils.getLang("common.confirm");
		String cancel = LanguageUtils.getLang("common.cancel");
		String methodName =this.getMethodName();
		StringBuffer sb = new StringBuffer();
		sb.append("<a href=\"#\" class=\"easyui-linkbutton\" plain=\"true\" icon=\"" + icon + "\" onClick=\"choose_"+methodName+ StringUtils.replace("()\">{0}</a>", "{0}", LanguageUtils.getLang("common.select", langArg)));
		if (isclear&&!StringUtils.isEmpty(textname)) {
			sb.append("<a href=\"#\" class=\"easyui-linkbutton\" plain=\"true\" icon=\"icon-redo\" onClick=\"clearAll_"+methodName+ StringUtils.replace("();\">{0}</a>", "{0}", LanguageUtils.getLang("common.clear", langArg)));
		}
		sb.append("<script type=\"text/javascript\">");
		sb.append("var windowapi = frameElement.api, W = windowapi.opener;");
		sb.append("function choose_"+methodName+"(){");
		sb.append("var url = ").append("'").append(url).append("';");
		if(isInit){
			sb.append("var initValue = ").append("$(\'#" + hiddenName + "\').val();");
			sb.append("url += ").append("'&ids='+initValue;"); 
		}
		sb.append("if(typeof(windowapi) == 'undefined'){");
			sb.append("$.dialog({");
			sb.append("content: \'url:\'+url,");
			sb.append("zIndex: 2100,");
			if (title != null) {
				sb.append("title: \'" + title + "\',");
			}
			sb.append("lock : true,");
			if (width != null) {
				sb.append("width :\'" + width + "\',");
			} else {
				sb.append("width :400,");
			}
			if (height != null) {
				sb.append("height :\'" + height + "\',");
			} else {
				sb.append("height :350,");
			}
			if (left != null) {
				sb.append("left :\'" + left + "\',");
			} else {
				sb.append("left :'85%',");
			}
			if (top != null) {
				sb.append("top :\'" + top + "\',");
			} else {
				sb.append("top :'65%',");
			}
			sb.append("opacity : 0.4,");
			sb.append("button : [ {");
			sb.append(StringUtils.replace("name : \'{0}\',", "{0}", confirm));
			sb.append("callback : clickcallback_"+methodName+",");
			sb.append("focus : true");
			sb.append("}, {");
			sb.append(StringUtils.replace("name : \'{0}\',","{0}", cancel));
			sb.append("callback : function() {");
			sb.append("}");
			sb.append("} ]");
			sb.append("});");
		sb.append("}else{");
			sb.append("$.dialog({");
			sb.append("content: \'url:\'+url,");
			sb.append("zIndex: 2100,");
			if (title != null) {
				sb.append("title: \'" + title + "\',");
			}
			sb.append("lock : true,");
			sb.append("parent:windowapi,");
			if (width != null) {
				sb.append("width :\'" + width + "\',");
			} else {
				sb.append("width :400,");
			}
			if (height != null) {
				sb.append("height :\'" + height + "\',");
			} else {
				sb.append("height :350,");
			}
			if (left != null) {
				sb.append("left :\'" + left + "\',");
			} else {
				sb.append("left :'85%',");
			}
			if (top != null) {
				sb.append("top :\'" + top + "\',");
			} else {
				sb.append("top :'65%',");
			}
			sb.append("opacity : 0.4,");
			sb.append("button : [ {");
			sb.append(StringUtils.replace("name : \'{0}\',", "{0}", confirm));
			sb.append("callback : clickcallback_"+methodName+",");
			sb.append("focus : true");
			sb.append("}, {");
			sb.append(StringUtils.replace("name : \'{0}\',","{0}", cancel));
			sb.append("callback : function() {");
			sb.append("}");
			sb.append("} ]");
			sb.append("});");
			sb.append("}");
		sb.append("}");
		clearAll(sb,methodName);
		callback(sb,methodName);
		sb.append("</script>");
		return sb.toString();
	}

	private String getMethodName() {
		return UUID.randomUUID().toString().replace("-", "");
	}


	/**
	 * 清除
	 * @param sb
	 */
	private void clearAll(StringBuffer sb,String methodname) {
		String[] textnames=null;
		String[] inputTextnames=null;
		textnames = textname.split(",");
		if(!StringUtils.isEmpty(inputTextname)){
			inputTextnames = inputTextname.split(",");
		}else{
			inputTextnames = textnames;
		}
		if (isclear&&!StringUtils.isEmpty(textname)) {
			sb.append("function clearAll_"+methodname+"(){");
			for (int i = 0; i < textnames.length; i++) {
				inputTextnames[i]=inputTextnames[i].replaceAll("\\[", "\\\\\\\\[").replaceAll("\\]", "\\\\\\\\]").replaceAll("\\.", "\\\\\\\\.");
				sb.append("if($(\'#" + inputTextnames[i] + "\').length>=1){");
				sb.append("$(\'#" + inputTextnames[i] + "\').val('');");
				sb.append("$(\'#" + inputTextnames[i] + "\').blur();");
				sb.append("}");
				sb.append("if($(\"input[name='" + inputTextnames[i] + "']\").length>=1){");
				sb.append("$(\"input[name='" + inputTextnames[i] + "']\").val('');");
				sb.append("$(\"input[name='" + inputTextnames[i] + "']\").blur();");
				sb.append("}");
			}
			sb.append("$(\'#" + hiddenName + "\').val(\"\");");
			sb.append("}");
			}
	}
	/**
	 * 点击确定回填
	 * @param sb
	 */
	private void callback(StringBuffer sb,String methodname) {
		sb.append("function clickcallback_"+methodname+"(){");
		sb.append("iframe = this.iframe.contentWindow;");
		String[] textnames=null;
		String[] inputTextnames=null;
		if(!StringUtils.isEmpty(textname))
		{
		textnames = textname.split(",");
		if(!StringUtils.isEmpty(inputTextname)){
			inputTextnames = inputTextname.split(",");
		}else{
			inputTextnames = textnames;
		}
		for (int i = 0; i < textnames.length; i++) {
			inputTextnames[i]=inputTextnames[i].replaceAll("\\[", "\\\\\\\\[").replaceAll("\\]", "\\\\\\\\]").replaceAll("\\.", "\\\\\\\\.");
			sb.append("var " + textnames[i] + "=iframe.get" + name + "Selections(\'" + textnames[i] + "\');	");
			sb.append("if($(\'#" + inputTextnames[i] + "\').length>=1){");
			sb.append("$(\'#" + inputTextnames[i] + "\').val(" + textnames[i] + ");");
			sb.append("$(\'#" + inputTextnames[i] + "\').blur();");
			sb.append("}");
			sb.append("if($(\"input[name='" + inputTextnames[i] + "']\").length>=1){");
			sb.append("$(\"input[name='" + inputTextnames[i] + "']\").val(" + textnames[i] + ");");
			sb.append("$(\"input[name='" + inputTextnames[i] + "']\").blur();");
			sb.append("}");
		}
		}
		if(!StringUtils.isEmpty(hiddenName)){
			sb.append("var id =iframe.get" + name + "Selections(\'" + hiddenid + "\');");
			sb.append("if (id!== undefined &&id!=\"\"){");
			sb.append("$(\'#" + hiddenName + "\').val(id);");
			sb.append("}");
		}
		if(!StringUtils.isEmpty(fun))
		{
		sb.append(""+fun+"();");//执行自定义函数
		}
		sb.append("}");
	}



	public void setHiddenName(String hiddenName) {
		this.hiddenName = hiddenName;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public void setTextname(String textname) {
		this.textname = textname;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setTop(String top) {
		this.top = top;
	}

	public void setLeft(String left) {
		this.left = left;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public void setIsclear(Boolean isclear) {
		this.isclear = isclear;
	}

	public void setHiddenid(String hiddenid) {
		this.hiddenid = hiddenid;
	}
	public void setFun(String fun) {
		this.fun = fun;
	}

	public String getInputTextname() {
		return inputTextname;
	}

	public void setInputTextname(String inputTextname) {
		this.inputTextname = inputTextname;
	}

	public String getLangArg() {
		return langArg;
	}

	public void setLangArg(String langArg) {
		this.langArg = langArg;
	}
	
	public void setIsInit(Boolean isInit) {
		this.isInit = isInit;
	}
	
}
