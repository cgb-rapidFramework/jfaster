package com.abocode.jfaster.core.common.model.json;

import com.google.gson.Gson;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class TreeGrid implements java.io.Serializable {
	private String id;
	private String text;
 	private String parentId;
 	private String parentText;
 	private String code;
 	private String src;
 	private String note;
	private Map<String,String> attributes;// 其他参数
 	private String  operations;// 其他参数
 	private String state = "open";// 是否展开(open,closed)
 	private String order;//排序
    private Map<String, Object> fieldMap; // 存储实体字段信息容器： key-字段名称，value-字段值
    private String  functionType;// 其他参数
    public String toJson() {
        return "{" +
                "'id':'" + id + '\'' +
                ", 'text':'" + text + '\'' +
                ", 'parentId':'" + parentId + '\'' +
                ", 'parentText':'" + parentText + '\'' +
                ", 'code':'" + code + '\'' +
                ", 'src':'" + src + '\'' +
                ", 'note':'" + note + '\'' +
                ", 'attributes':" + attributes +
                ", 'operations':'" + operations + '\'' +
                ", 'state':'" + state + '\'' +
                ", 'order':'" + order + '\'' +
                assembleFieldsJson() +
                '}';
    }

    private String assembleFieldsJson() {
        String fieldsJson = ", 'fieldMap':" + fieldMap;
        if (fieldMap != null && fieldMap.size() > 0) {
            Map<String, Object> resultMap = new HashMap<String, Object>();
            for (Map.Entry<String, Object> entry : fieldMap.entrySet()) {
                resultMap.put("fieldMap." + entry.getKey(), entry.getValue());
            }
            fieldsJson = ", " + new Gson().toJson(resultMap).toString().replace("{", "").replace("}", "");
        }
        return fieldsJson;
    }
 
}
