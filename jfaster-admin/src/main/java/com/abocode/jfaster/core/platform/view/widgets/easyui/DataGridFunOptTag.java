package com.abocode.jfaster.core.platform.view.widgets.easyui;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import com.abocode.jfaster.core.platform.utils.MutiLangUtils;
import lombok.Data;

@Data
public class DataGridFunOptTag extends TagSupport {

    protected String title;
    private String exp;//判断链接是否显示的表达式
    private String function;//自定义函数名称
    private String operationCode;//按钮的操作Code
    private String langArg;//按钮的操作Code


    public int doStartTag() throws JspTagException {
        return EVAL_PAGE;
    }

    public int doEndTag() throws JspTagException {
        title = MutiLangUtils.doMutiLang(title, langArg);

        Tag t = findAncestorWithClass(this, DataGridTag.class);
        DataGridTag parent = (DataGridTag) t;
        parent.setFunUrl(title, exp, function, operationCode);
        return EVAL_PAGE;
    }

}
