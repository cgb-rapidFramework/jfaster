package com.abocode.jfaster.core.repository;

import com.abocode.jfaster.core.common.model.json.ComboBox;
import com.abocode.jfaster.core.platform.view.ReflectHelper;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Element;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class TagUtil {
    private TagUtil() {
    }

    /**
     * 封装成项目
     *
     * @param fields
     * @param total
     * @param list
     * @param footers
     * @return
     * @throws Exception
     */
    private static DataGridData getDataObject(String[] fields, int total, List<?> list, String[] footers) {
        DataGridData dataGridData = new DataGridData();
        dataGridData.setTotal(total);
        //设置行数
        List<Map<String, Object>> rows = new ArrayList<>();
        Object[] values = new Object[fields.length];
        String fieldName;
        for (int j = 0; j < list.size(); ++j) {
            Map<String, Object> row = new HashMap<>();
            row.put("state", "closed");
            for (int i = 0; i < fields.length; ++i) {
                fieldName = fields[i];
                if (list.get(j) instanceof Map)
                    values[i] = ((Map<?, ?>) list.get(j)).get(fieldName);
                else {
                    Object value = fieldNameToValues(fieldName, list.get(j));
                    if (!StringUtils.isEmpty(value)) {
                        String strValue = value.toString();
                        if (strValue.endsWith(",")) {
                            value = strValue.substring(0, strValue.length() - 1);
                        }
                    }
                    values[i] = value;
                }
                row.put(fieldName, values[i]);
            }
            rows.add(row);
        }
        dataGridData.setRows(rows);

        if (footers != null) {
            Map<String, Object> footer = new HashMap<>();
            footer.put("name", "合计");
            for (String f : footers) {
                String footerFiled = f.split(":")[0];
                Object value = null;
                if (f.split(":").length == 2)
                    value = f.split(":")[1];
                else {
                    value = getTotalValue(footerFiled, list);
                }
                footer.put(footerFiled, value);
            }
            dataGridData.setFooter(footer);
        }

        return dataGridData;
    }


    /**
     * 计算指定列的合计
     *
     * @param filed 字段名
     * @param list  列表数据
     * @return
     */
    private static Object getTotalValue(String filed, List<?> list) {
        Double sum = 0D;
        try {
            for (int j = 0; j < list.size(); j++) {
                Double v = 0d;
                String vstr = String.valueOf(fieldNameToValues(filed, list.get(j)));
                if (!StringUtils.isEmpty(vstr)) {
                    v = Double.valueOf(vstr);
                }
                sum += v;
            }
        } catch (Exception e) {
            return "";
        }
        return sum;
    }


    /**
     * 获取转换的json
     *
     * @param dg
     * @return
     */
    public static DataGridData getObject(DataGridParam dg) {
        DataGridData jObject;
        if (!StringUtils.isEmpty(dg.getFooter())) {
            jObject = getDataObject(dg.getField().split(","), dg.getTotal(), dg.getResults(), dg.getFooter().split(","));
        } else {
            jObject = getDataObject(dg.getField().split(","), dg.getTotal(), dg.getResults(), null);
        }
        return jObject;
    }


    /**
     * 获取自定义函数名
     *
     * @param functionname
     * @return
     */
    public static String getFunction(String functionname) {
        int index = functionname.indexOf("(");
        if (index == -1) {
            return functionname;
        } else {
            return functionname.substring(0, functionname.indexOf("("));
        }
    }

    /**
     * 获取自定义函数的参数
     *
     * @param functionname
     * @return
     */
    public static String getFunParams(String functionname) {
        int index = functionname.indexOf("(");
        StringBuilder param = new StringBuilder();
        if (index != -1) {
            String tempParam = functionname.substring(functionname.indexOf("(") + 1, functionname.length() - 1);
            if (!StringUtils.isEmpty(tempParam)) {
                String[] params = tempParam.split(",");
                for (String string : params) {
                    String s=(string.indexOf("{") != -1) ? ("'\"+"
                            + string.substring(1, string.length() - 1) + "+\"',")
                            : ("'\"+rec." + string + "+\"',");
                    param.append(s);
                }
            }
        }
        param.append( "'\"+index+\"'");// 传出行索引号参数
        return param.toString();
    }

    /**
     * 获取对象内对应字段的值
     *
     * @param fields
     */
    public static Object fieldNameToValues(String fields, Object o) {
        Object value = "";
        String fieldName = "";
        String childFieldName = null;
        ReflectHelper reflectHelper = new ReflectHelper(o);
        if (fields.indexOf("_") == -1) {
            if (fields.indexOf(".") == -1) {
                fieldName = fields;
            } else {
                fieldName = fields.substring(0, fields.indexOf("."));//外键字段引用名
                childFieldName = fields.substring(fields.indexOf(".") + 1);//外键字段名
            }
        } else {
            fieldName = fields.substring(0, fields.indexOf("_"));//外键字段引用名
            childFieldName = fields.substring(fields.indexOf("_") + 1);//外键字段名
        }
        value = reflectHelper.getMethodValue(fieldName) == null ? "" : reflectHelper.getMethodValue(fieldName);
        if (!StringUtils.isEmpty(value) && (fields.indexOf("_") != -1 || fields.indexOf(".") != -1)) {
            if (value instanceof List) {
                Object tempValue = "";
                for (Object listValue : (List) value) {
                    tempValue = tempValue.toString() + fieldNameToValues(childFieldName, listValue) + ",";
                }
                value = tempValue;
            } else {
                value = fieldNameToValues(childFieldName, value);
            }
        }
        if (!StringUtils.isEmpty(value)) {
            //无特殊字符
            value = value.toString();
        }
        return value;
    }

    /**
     * getFiled(获得实体Bean中所有属性)
     *
     * @param objClass
     * @return
     * @throws ClassNotFoundException
     */
    public static Field[] getFiled(Class<?> objClass) throws ClassNotFoundException {
        Field[] field = null;
        if (objClass != null) {
            Class<?> class1 = Class.forName(objClass.getName());
            field = class1.getDeclaredFields();// 这里便是获得实体Bean中所有属性的方法
            return field;
        } else {
            return field;
        }
    }


    /**
     * 获取指定字段类型 getColumnType(请用一句话描述这个方法的作用)
     *
     * @param fileName
     * @param fields
     * @param node
     * @return
     */
    public static Object getColumnType(String fileName, Field[] fields, Element node) {
        for (Field field : fields) {
            String name = field.getName(); // 获取属性的名字
            String filedType =field.getGenericType().toString(); // 获取属性的类型
            if (fileName.equals(name)) {
                if (filedType.equals("class java.lang.Integer")) {
                    return  Integer.valueOf(node.getText());
                } else if (filedType.equals("class java.lang.Short")) {
                    return Short.parseShort(node.getText());
                } else if (filedType.equals("class java.lang.Double")) {
                    return  Double.valueOf(node.getText());
                } else if (filedType.equals("class java.util.Date")) {
                    return  Timestamp.valueOf(node.getText());
                } else if (filedType.equals("class java.lang.String")) {
                    return node.getText();
                } else if (filedType.equals("class java.sql.Timestamp")) {
                    return  Timestamp.valueOf(node.getText());
                } else if (filedType.equals("class java.lang.Boolean")) {
                    return Boolean.valueOf(node.getText());
                } else if (filedType.equals("class java.lang.Long")) {
                    return Long.valueOf(node.getText());
                } else {
                    return node.getText();
                }
            }
        }
        return node.getText();
    }

    /**
     * 根据模型生成JSON
     *
     * @param all    全部对象
     * @param in     已拥有的对象
     * @param fields 模型
     * @return
     */
    public static List<ComboBox> getComboBox(List<Object> all, List<Object> in, String[] fields) {
        List<ComboBox> comboBoxes = new ArrayList<>();
        Object[] values = new Object[fields.length];
        for (Object node : all) {
            ComboBox box = new ComboBox();
            ReflectHelper reflectHelper = new ReflectHelper(node);
            for (int i = 0; i < fields.length; i++) {
                String fieldName = fields[i];
                values[i] = reflectHelper.getMethodValue(fieldName);
            }
            box.setId(values[0].toString());
            box.setText(values[1].toString());
            if (in != null) {
                for (Object node1 : in) {
                    ReflectHelper reflectHelper2 = new ReflectHelper(node);
                    if (node1 != null) {
                        String fieldName = fields[0];
                        String test = reflectHelper2.getMethodValue(fieldName).toString();
                        if (values[0].toString().equals(test)) {
                            box.setSelected(true);
                        }
                    }
                }
            }
            comboBoxes.add(box);
        }
        return comboBoxes;

    }

}
