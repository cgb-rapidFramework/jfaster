package com.abocode.jfaster.core.platform.view.widgets.easyui;

import com.abocode.jfaster.core.platform.utils.LanguageUtils;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * 类描述：列表删除操作项标签
 *
 * @author 张代浩
 * @version 1.0
 * @date： 日期：2012-12-7 时间：上午10:17:45
 */
public class DataGridDelOptTag extends TagSupport {
    protected String url;
    protected String title;
    private String message;//询问链接的提示语
    private String exp;//判断链接是否显示的表达式
    private String function;//自定义函数名称

    private String operationCode;//按钮的操作Code
    private String langArg;

    public int doStartTag() throws JspTagException {
        return EVAL_PAGE;
    }

    public int doEndTag() throws JspTagException {
        title = LanguageUtils.doLang(title, langArg);

        Tag t = findAncestorWithClass(this, DataGridTag.class);
        DataGridTag parent = (DataGridTag) t;
        parent.setDelUrl(url, title, message, exp, function, operationCode);
        return EVAL_PAGE;
    }

    public void setFunname(String function) {
        this.function = function;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setOperationCode(String operationCode) {
        this.operationCode = operationCode;
    }

    public void setLangArg(String langArg) {
        this.langArg = langArg;
    }
}
