package com.abocode.jfaster.core.platform.view.widgets.easyui;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import lombok.Data;

/**
 * 类描述：选项卡选项标签
 * <p/>
 * 张代浩
 * @version 1.0
 * @date： 日期：2012-12-7 时间：上午10:17:45
 */
@Data
public class TabTag extends TagSupport {
    private String href;//选项卡请求地址
    private String iframe;//选项卡iframe方法请求地址
    private String id;//选项卡唯一ID
    private String title;//标题
    private String icon = "icon-default";//图标
    private String width;//宽度
    private String height;//高度
    private boolean cache;//是否打开缓冲如为TRUE则切换选项卡会再次发送请求
    private String content;
    private boolean closable = false;//是否带关闭按钮
    private String langArg;

    public int doStartTag(){
        return EVAL_PAGE;
    }

    public int doEndTag() throws JspTagException {
        Tag t = findAncestorWithClass(this, TabsTag.class);
        TabsTag parent = (TabsTag) t;
        parent.setTab(id, title, iframe, href, icon, cache, content, width, height, closable);
        return EVAL_PAGE;
    }
}
