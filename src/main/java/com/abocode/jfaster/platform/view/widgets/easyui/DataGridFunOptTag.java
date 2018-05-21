package com.abocode.jfaster.platform.view.widgets.easyui;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import com.abocode.jfaster.core.common.util.MutiLangUtils;

/**
 * 类描述：列表自定义函数操作项处理标签
 *
 * @author 张代浩
 * @version 1.0
 * @date： 日期：2012-12-7 时间：上午10:17:45
 */
public class DataGridFunOptTag extends TagSupport {

    protected String title;
    private String exp;//判断链接是否显示的表达式
    private String funname;//自定义函数名称
    private String operationCode;//按钮的操作Code
    private String langArg;//按钮的操作Code


    public int doStartTag() throws JspTagException {
        return EVAL_PAGE;
    }

    public int doEndTag() throws JspTagException {
        title = MutiLangUtils.doMutiLang(title, langArg);

        Tag t = findAncestorWithClass(this, DataGridTag.class);
        DataGridTag parent = (DataGridTag) t;
        parent.setFunUrl(title, exp, funname, operationCode);
        return EVAL_PAGE;
    }

    public void setFunname(String funname) {
        this.funname = funname;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setOperationCode(String operationCode) {
        this.operationCode = operationCode;
    }

    public void setLangArg(String langArg) {
        this.langArg = langArg;
    }

}
