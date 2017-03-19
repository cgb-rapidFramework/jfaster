package com.abocode.jfaster.platform.common.tag.easyui;

import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.abocode.jfaster.platform.bean.FunctionBean;
import com.abocode.jfaster.platform.util.JspWriterUtils;
import com.abocode.jfaster.platform.util.SystemMenuUtils;


/**
 * 类描述：菜单标签
 * <p/>
 * 张代浩
 *
 * @version 1.0
 * @date： 日期：2012-12-7 时间：上午10:17:45
 */
public class MenuTag extends TagSupport {
    private static final long serialVersionUID = 1L;
    protected String style = "easyui";//菜单样式
    protected List<FunctionBean> parentFun;//一级菜单
    protected List<FunctionBean> childFun;//二级菜单
    protected Map<Integer, List<FunctionBean>> menuFun;//菜单Map


    public void setParentFun(List<FunctionBean> parentFun) {
        this.parentFun = parentFun;
    }

    public void setChildFun(List<FunctionBean> childFun) {
        this.childFun = childFun;
    }

    public int doStartTag() throws JspTagException {
        return EVAL_PAGE;
    }

    public int doEndTag() throws JspTagException {
        String menu = (String) this.pageContext.getSession().getAttribute("leftMenuCache" + style);
        if (menu == null || menu.contains("不具有任何权限")) {
            menu = end().toString();
            this.pageContext.getSession().setAttribute("leftMenuCache" + style, menu);
        }
        JspWriter out = this.pageContext.getOut();
        JspWriterUtils.write(out, menu);
        return EVAL_PAGE;
    }

    public StringBuffer end() {
        StringBuffer sb = new StringBuffer();
        if (style.equals("easyui")) {
            sb.append("<ul id=\"nav\" class=\"easyui-tree tree-lines\" fit=\"true\" border=\"false\">");
            sb.append(SystemMenuUtils.getEasyuiMultistageTree(menuFun, style));
            sb.append("</ul>");
        }
        if (style.equals("shortcut"))
//		{	sb.append("<div id=\"nav\" style=\"display:none;\" class=\"easyui-accordion\" fit=\"true\" border=\"false\">");
        {
            sb.append("<div id=\"nav\" style=\"display:block;\" class=\"easyui-accordion\" fit=\"true\" border=\"false\">");
            sb.append(SystemMenuUtils.getEasyuiMultistageTree(menuFun, style));
            sb.append("</div>");
        }
        if (style.equals("bootstrap")) {
            sb.append(SystemMenuUtils.getBootMenu(parentFun, childFun));
        }
        if (style.equals("json")) {
            sb.append("<script type=\"text/javascript\">");
            sb.append("var _menus=" + SystemMenuUtils.getMenu(parentFun, childFun));
            sb.append("</script>");
        }
        if (style.equals("june_bootstrap")) {
            sb.append(SystemMenuUtils.getBootstrapMenu(menuFun));
        }
        if (style.equals("ace")) {
            sb.append(SystemMenuUtils.getAceMultistageTree(menuFun));
        }
        if (style.equals("diy")) {
            sb.append(SystemMenuUtils.getDIYMultistageTree(menuFun));
        }
        return sb;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public void setMenuFun(Map<Integer, List<FunctionBean>> menuFun) {
        this.menuFun = menuFun;
    }


}
