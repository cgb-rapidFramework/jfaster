package com.abocode.jfaster.core.platform.view.widgets.easyui;

import com.abocode.jfaster.core.common.util.JspWriterUtils;
import com.abocode.jfaster.core.platform.SystemContainer;
import com.abocode.jfaster.core.platform.utils.LanguageUtils;
import com.abocode.jfaster.core.platform.view.TypeGroupView;
import com.abocode.jfaster.core.platform.view.TypeView;
import com.google.gson.Gson;
import org.springframework.util.StringUtils;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.util.List;
import java.util.Map;

/**
 * 选择下拉框
 *
 * @version 1.0
 * @author: lianglaiyang
 * @date： 日期：2013-04-18
 */
public class DictSelectTag extends TagSupport {

    private static final long serialVersionUID = 1;
    private String typeGroupCode; // 数据字典类型
    private String field; // 选择表单的Name EAMPLE:<select name="selectName" id = ""
    // />
    private String id; // 选择表单ID EAMPLE:<select name="selectName" id = "" />
    private String defaultVal; // 默认值
    private String divClass; // DIV样式
    private String labelClass; // Label样式
    private String title; // label显示值
    private boolean hasLabel = true; // 是否显示label
    private String type;// 控件类型select|radio|checkbox
    private String dictTable;// 自定义字典表
    private String dictField;// 自定义字典表的匹配字段-字典的编码值
    private String dictText;// 自定义字典表的显示文本-字典的显示值
    private String extendJson;//扩展参数
    private String dictCondition;

    public String getDictCondition() {
        return dictCondition;
    }

    public void setDictCondition(String dicCondition) {
        this.dictCondition = dicCondition;
    }

    public int doStartTag() throws JspTagException {
        return EVAL_PAGE;
    }

    public int doEndTag() throws JspTagException {
        JspWriter out = this.pageContext.getOut();
        JspWriterUtils.write(out, end());
        return EVAL_PAGE;
    }

    public String end() {
        StringBuffer sb = new StringBuffer();
        if (StringUtils.isEmpty(divClass)) {
            divClass = "form"; // 默认form样式
        }
        if (StringUtils.isEmpty(labelClass)) {
            labelClass = "Validform_label"; // 默认label样式
        }
        if (dictTable != null) {
            List<Map<String, Object>> list = queryDic();
            if ("radio".equals(type)) {
                for (Map<String, Object> map : list) {
                    radio(map.get("text").toString(), map.get("field")
                            .toString(), sb);
                }
            } else if ("checkbox".equals(type)) {
                for (Map<String, Object> map : list) {
                    checkbox(map.get("text").toString(), map.get("field")
                            .toString(), sb);
                }
            } else if ("text".equals(type)) {
                for (Map<String, Object> map : list) {
                    text(map.get("text").toString(), map.get("field")
                            .toString(), sb);
                }
            } else {
                sb.append("<select name=\"" + field + "\"");
                //增加扩展属性
                if (!StringUtils.isEmpty(this.extendJson)) {
                    Gson gson = new Gson();
                    Map<String, String> mp = gson.fromJson(extendJson, Map.class);
                    for (Map.Entry<String, String> entry : mp.entrySet()) {
                        sb.append(entry.getKey() + "=\"" + entry.getValue() + "\"");
                    }
                }
                if (!StringUtils.isEmpty(this.id)) {
                    sb.append(" id=\"" + id + "\"");
                }
                sb.append(">");
                select("common.please.select", "", sb);
                for (Map<String, Object> map : list) {
                    select(map.get("text").toString(), map.get("field").toString(), sb);
                }
                sb.append("</select>");
            }
        } else {
            TypeGroupView typeGroup = SystemContainer.TypeGroupContainer.getTypeGroupMap().get(this.typeGroupCode.toLowerCase());
            List<TypeView> types = SystemContainer.TypeGroupContainer.getTypeMap().get(this.typeGroupCode.toLowerCase());
            if (hasLabel) {
                sb.append("<div class=\"" + divClass + "\">");
                sb.append("<label class=\"" + labelClass + "\" >");
            }
            if (typeGroup != null) {
                if (hasLabel) {
                    if (StringUtils.isEmpty(this.title)) {
                        this.title = LanguageUtils.getLang(typeGroup.getTypeGroupName());
                    }
                    sb.append(this.title + ":");
                    sb.append("</label>");
                }
                if ("radio".equals(type)) {
                    for (TypeView type : types) {
                        radio(type.getTypeName(), type.getTypeCode(), sb);
                    }
                } else if ("checkbox".equals(type)) {
                    for (TypeView type : types) {
                        checkbox(type.getTypeName(), type.getTypeCode(), sb);
                    }
                } else if ("text".equals(type)) {
                    for (TypeView type : types) {
                        text(type.getTypeName(), type.getTypeCode(), sb);
                    }
                } else {
                    sb.append("<select name=\"" + field + "\"");
                    //增加扩展属性
                    if (!StringUtils.isEmpty(this.extendJson)) {
                        Gson gson = new Gson();
                        Map<String, String> mp = gson.fromJson(extendJson, Map.class);
                        for (Map.Entry<String, String> entry : mp.entrySet()) {
                            sb.append(" " + entry.getKey() + "=\"" + entry.getValue() + "\"");
                        }
                    }
                    if (!StringUtils.isEmpty(this.id)) {
                        sb.append(" id=\"" + id + "\"");
                    }
                    sb.append(">");
                    select("common.please.select", "", sb);
                    for (TypeView type : types) {
                        select(type.getTypeName(), type.getTypeCode(), sb);
                    }
                    sb.append("</select>");
                }
                if (hasLabel) {
                    sb.append("</div>");
                }
            }
        }

        return sb.toString();
    }

    /**
     * 文本框方法
     *
     * @param name
     * @param code
     * @param sb
     */
    private void text(String name, String code, StringBuffer sb) {
        if (code.equals(this.defaultVal)) {
            sb.append("<input name='" + field + "'" + " id='" + id + "' value='" + LanguageUtils.getLang(name) + "' readOnly = 'readOnly' />");
        } else {
        }
    }


    /**
     * 单选框方法
     *
     * @param name
     * @param code
     * @param sb
     * @作者：Alexander
     */
    private void radio(String name, String code, StringBuffer sb) {
        if (code.equals(this.defaultVal)) {
            sb.append("<input type=\"radio\" name=\"" + field
                    + "\" checked=\"checked\" value=\"" + code + "\"");
            if (!StringUtils.isEmpty(this.id)) {
                sb.append(" id=\"" + id + "\"");
            }
            sb.append(" />");
        } else {
            sb.append("<input type=\"radio\" name=\"" + field + "\" value=\""
                    + code + "\"");
            if (!StringUtils.isEmpty(this.id)) {
                sb.append(" id=\"" + id + "\"");
            }
            sb.append(" />");
        }
        sb.append(LanguageUtils.getLang(name));
    }

    /**
     * 复选框方法
     *
     * @param name
     * @param code
     * @param sb
     * @作者：Alexander
     */
    private void checkbox(String name, String code, StringBuffer sb) {
        String[] values = this.defaultVal.split(",");
        Boolean checked = false;
        for (int i = 0; i < values.length; i++) {
            String value = values[i];
            if (code.equals(value)) {
                checked = true;
                break;
            }
            checked = false;
        }
        if (checked) {
            sb.append("<input type=\"checkbox\" name=\"" + field
                    + "\" checked=\"checked\" value=\"" + code + "\"");
            if (!StringUtils.isEmpty(this.id)) {
                sb.append(" id=\"" + id + "\"");
            }
            sb.append(" />");
        } else {
            sb.append("<input type=\"checkbox\" name=\"" + field
                    + "\" value=\"" + code + "\"");
            if (!StringUtils.isEmpty(this.id)) {
                sb.append(" id=\"" + id + "\"");
            }
            sb.append(" />");
        }
        sb.append(LanguageUtils.getLang(name));
    }

    /**
     * 选择框方法
     *
     * @param name
     * @param code
     * @param sb
     * @作者：Alexander
     */
    private void select(String name, String code, StringBuffer sb) {
        if (code.equals(this.defaultVal)) {
            sb.append(" <option value=\"" + code + "\" selected=\"selected\">");
        } else {
            sb.append(" <option value=\"" + code + "\">");
        }
        sb.append(LanguageUtils.getLang(name));
        sb.append(" </option>");
    }

    /**
     * 查询自定义数据字典
     *
     * @作者：Alexander
     */
    private List<Map<String, Object>> queryDic() {
        return null;/*
        String sql = "select " + dictField + " as field," + dictText
				+ " as text from " + dictTable;
		systemService = ApplicationContextUtil.getContext().getBean(
				SystemService.class);
		List<Map<String, Object>> list = systemService.queryForListMap(sql);
		return list;*/
    }

    public String getTypeGroupCode() {
        return typeGroupCode;
    }

    public void setTypeGroupCode(String typeGroupCode) {
        this.typeGroupCode = typeGroupCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDefaultVal() {
        return defaultVal;
    }

    public void setDefaultVal(String defaultVal) {
        this.defaultVal = defaultVal;
    }

    public String getDivClass() {
        return divClass;
    }

    public void setDivClass(String divClass) {
        this.divClass = divClass;
    }

    public String getLabelClass() {
        return labelClass;
    }

    public void setLabelClass(String labelClass) {
        this.labelClass = labelClass;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isHasLabel() {
        return hasLabel;
    }

    public void setHasLabel(boolean hasLabel) {
        this.hasLabel = hasLabel;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDictTable() {
        return dictTable;
    }

    public void setDictTable(String dictTable) {
        this.dictTable = dictTable;
    }

    public String getDictField() {
        return dictField;
    }

    public void setDictField(String dictField) {
        this.dictField = dictField;
    }

    public String getDictText() {
        return dictText;
    }

    public void setDictText(String dictText) {
        this.dictText = dictText;
    }

    public String getExtendJson() {
        return extendJson;
    }

    public void setIconExtendJson(String extendJson) {
        this.extendJson = extendJson;
    }
}
