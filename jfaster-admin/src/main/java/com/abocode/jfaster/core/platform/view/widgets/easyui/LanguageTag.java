
package com.abocode.jfaster.core.platform.view.widgets.easyui;

import com.abocode.jfaster.core.common.util.JspWriterUtils;
import com.abocode.jfaster.core.platform.utils.MutiLangUtils;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
public class LanguageTag extends TagSupport {
    protected String langKey;
    protected String langArg = "zh-cn";

    public int doStartTag() throws JspTagException {
        return EVAL_PAGE;
    }

    public int doEndTag() throws JspTagException {
        JspWriter out = this.pageContext.getOut();
        JspWriterUtils.write(out, end());
        return EVAL_PAGE;
    }


    public String end() {
        String lang_context = MutiLangUtils.getLang(langKey, langArg);
        return new StringBuffer(lang_context).toString();
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

    public void setLangArg(String langArg) {
        this.langArg = langArg;
    }
}
