package com.abocode.jfaster.core.platform.view.widgets.easyui;

import com.abocode.jfaster.core.platform.utils.MutiLangUtils;
import lombok.Data;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * 类描述：列表字段处理项目
 * <p/>
 * 张代浩
 *
 * @version 1.0
 * @date： 日期：2012-12-7 时间：上午10:17:45
 */
@Data
public class DataGridColumnTag extends TagSupport {
    private static final long serialVersionUID = 1L;
    protected String title;
    protected String field;
    protected Integer width;
    protected String rowspan;
    protected String colspan;
    protected String align;
    protected boolean sortable = true;
    protected boolean checkbox;
    protected String formatter;
    protected boolean hidden = false;
    protected String replace;
    protected String treeField;
    protected boolean image;
    protected boolean query = false;
    private String queryMode = "single";//字段查询模式：single单字段查询；scope范围查询
    private boolean frozenColumn = false; // 是否是冰冻列    默认不是
    protected boolean bSearchable = true;
    protected String url;//自定义链接
    protected String function = "openwindow";//自定义函数名称
    protected String arg;//自定义链接传入参数字段
    protected String dictionary;    //数据字典组编码
    protected boolean popup = false;    //是否启用popup模式选择 默认不启用
    protected String extend; //扩展属性
    protected String style; //Td的CSS
    protected String imageSize;//自定义图片显示大小
    protected String downloadName;//附件下载
    private boolean autocomplete = false;//自动完成
    private String extendParams;//扩展参数
    private String langArg;

    public int doEndTag() throws JspTagException {
        title = MutiLangUtils.doMutiLang(title, langArg);
        Tag t = findAncestorWithClass(this, DataGridTag.class);
        DataGridTag parent = (DataGridTag) t;
        parent.setColumn(title, field, width, rowspan, colspan, align, sortable, checkbox, formatter, hidden, replace, treeField, image, imageSize, query, url, function, arg, queryMode, dictionary, popup, frozenColumn, extend, style, downloadName, autocomplete, extendParams);
        return EVAL_PAGE;
    }

}
