package com.abocode.jfaster.platform.common.tag.easyui;
import com.abocode.jfaster.core.common.model.json.ComboBox;
import com.abocode.jfaster.core.common.model.json.DataGrid;
import com.abocode.jfaster.core.tag.vo.datatable.DataTableReturn;
import com.abocode.jfaster.core.tag.vo.easyui.Autocomplete;
import com.abocode.jfaster.core.util.ConvertUtils;
import com.abocode.jfaster.platform.view.ReflectHelper;
import com.abocode.jfaster.platform.view.RoleView;
import com.google.gson.Gson;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类描述：标签工具类
 *
 * @author: guanxf
 * @date： 日期：2015-04-18
 */
public class TagUtil {


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
    private static DataObject getDataObject(String[] fields, int total, List<?> list, String[] footers) {
        DataObject dataObject = new DataObject();
        dataObject.setTotal(total);

        //设置行数
        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        Object[] values = new Object[fields.length];
        int i;
        String fieldName;
        for (int j = 0; j < list.size(); ++j) {
            Map<String, Object> row = new HashMap<String, Object>();
            row.put("state", "closed");
            for (i = 0; i < fields.length; ++i) {
                fieldName = fields[i].toString();
                if (list.get(j) instanceof Map)
                    values[i] = ((Map<?, ?>) list.get(j)).get(fieldName);
                else {
                    Object value= fieldNameToValues(fieldName, list.get(j));
                    if (value != "" && value != null) {
                        String strValue=value.toString();
                        if(strValue.endsWith(",")){
                            value = strValue.substring(0,strValue.length()-1);
                        }
                    }
                    values[i]=value;
                }
                row.put(fieldName, values[i]);
            }
            rows.add(row);
        }
        dataObject.setRows(rows);

        if (footers != null) {
            Map<String, Object> footer = new HashMap<String, Object>();
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
            dataObject.setFooter(footer);
        }

        return dataObject;
    }


    /**
     * 计算指定列的合计
     *
     * @param filed 字段名
     * @param list  列表数据
     * @return
     */
    private static Object getTotalValue(String filed, List list) {
        Double sum = 0D;
        try {
            for (int j = 0; j < list.size(); j++) {
                Double v = 0d;
                String vstr = String.valueOf(fieldNameToValues(filed, list.get(j)));
                if (!StringUtils.isEmpty(vstr)) {
                    v = Double.valueOf(vstr);
                } else {

                }
                sum += v;
            }
        } catch (Exception e) {
            return "";
        }
        return sum;
    }

    /**
     * 循环LIST对象拼接DATATABLE格式的JSON数据
     *
     * @param field
     * @param total
     * @param list
     */
    private static String datatable(String field, int total, List list) throws Exception {
        String[] fields = field.split(",");
        Object[] values = new Object[fields.length];
        StringBuffer jsonTemp = new StringBuffer();
        jsonTemp.append("{\"iTotalDisplayRecords\":" + total + ",\"iTotalRecords\":" + total + ",\"aaData\":[");
        for (int j = 0; j < list.size(); j++) {
            jsonTemp.append("{");
            for (int i = 0; i < fields.length; i++) {
                String fieldName = fields[i].toString();
                values[i] = fieldNameToValues(fieldName, list.get(j));
                jsonTemp.append("\"" + fieldName + "\"" + ":\"" + values[i] + "\"");
                if (i != fields.length - 1) {
                    jsonTemp.append(",");
                }
            }
            if (j != list.size() - 1) {
                jsonTemp.append("},");
            } else {
                jsonTemp.append("}");
            }
        }
        jsonTemp.append("]}");
        return jsonTemp.toString();
    }

    /**
     * 获取转换的json
     *
     * @param dg
     * @return
     */
    private static Object getObject(DataGrid dg) {
        Object jObject;
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
        String param = "";
        if (index != -1) {
            String testparam = functionname.substring(
                    functionname.indexOf("(") + 1, functionname.length() - 1);
            if (!StringUtils.isEmpty(testparam)) {
                String[] params = testparam.split(",");
                for (String string : params) {
                    param += (string.indexOf("{") != -1) ? ("'\"+"
                            + string.substring(1, string.length() - 1) + "+\"',")
                            : ("'\"+rec." + string + "+\"',");
                }
            }
        }
        param += "'\"+index+\"'";// 传出行索引号参数
        return param;
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
        if (value != "" && value != null && (fields.indexOf("_") != -1 || fields.indexOf(".") != -1)) {
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
        if (value != "" && value != null) {
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
     * 对象转数组
     *
     * @param fields
     * @param o
     * @return
     * @throws Exception
     */
    protected static Object[] field2Values(String[] fields, Object o) throws Exception {
        Object[] values = new Object[fields.length];
        for (int i = 0; i < fields.length; i++) {
            String fieldName = fields[i].toString();
            values[i] = fieldNameToValues(fieldName, o);
        }
        return values;
    }


    /**
     * 循环LIST对象拼接自动完成控件数据
     *
     * @param autocomplete
     * @param list
     * @throws Exception
     */
    public static String getAutoList(Autocomplete autocomplete, List list) throws Exception {
        String field = autocomplete.getLabelField() + "," + autocomplete.getValueField();
        String[] fields = field.split(",");
        Object[] values = new Object[fields.length];
        StringBuffer jsonTemp = new StringBuffer();
        jsonTemp.append("{\"totalResultsCount\":\"1\",\"geonames\":[");
        if (list.size() > 0) {
            for (int j = 0; j < list.size(); j++) {
                jsonTemp.append("{'nodate':'yes',");
                for (int i = 0; i < fields.length; i++) {
                    String fieldName = fields[i].toString();
                    values[i] = fieldNameToValues(fieldName, list.get(j));
                    jsonTemp.append("\"").append(fieldName).append("\"").append(":\"").append(values[i]).append("\"");
                    if (i != fields.length - 1) {
                        jsonTemp.append(",");
                    }
                }
                if (j != list.size() - 1) {
                    jsonTemp.append("},");
                } else {
                    jsonTemp.append("}");
                }
            }
        } else {
            jsonTemp.append("{'nodate':'数据不存在'}");
        }
        jsonTemp.append("]}");
        return  new Gson().toJson(jsonTemp);
    }


    /**
     * 获取指定字段类型 getColumnType(请用一句话描述这个方法的作用)
     *
     * @param fileName
     * @param fields
     * @return
     */
    public static String getColumnType(String fileName, Field[] fields) {
        String type = "";
        if (fields.length > 0) {
            for (int i = 0; i < fields.length; i++) {
                String name = fields[i].getName(); // 获取属性的名字
                String filedType = fields[i].getGenericType().toString(); // 获取属性的类型
                if (fileName.equals(name)) {
                    if (filedType.equals("class java.lang.Integer")) {
                        filedType = "int";
                        type = filedType;
                    } else if (filedType.equals("class java.lang.Short")) {
                        filedType = "short";
                        type = filedType;
                    } else if (filedType.equals("class java.lang.Double")) {
                        filedType = "double";
                        type = filedType;
                    } else if (filedType.equals("class java.util.Date")) {
                        filedType = "date";
                        type = filedType;
                    } else if (filedType.equals("class java.lang.String")) {
                        filedType = "string";
                        type = filedType;
                    } else if (filedType.equals("class java.sql.Timestamp")) {
                        filedType = "Timestamp";
                        type = filedType;
                    } else if (filedType.equals("class java.lang.Character")) {
                        filedType = "character";
                        type = filedType;
                    } else if (filedType.equals("class java.lang.Boolean")) {
                        filedType = "boolean";
                        type = filedType;
                    } else if (filedType.equals("class java.lang.Long")) {
                        filedType = "long";
                        type = filedType;
                    }

                }
            }
        }
        return type;
    }

    /**
     * getSortColumnIndex(获取指定字段索引)
     *
     * @param fileName
     * @param fieldString
     * @return
     */
    protected static String getSortColumnIndex(String fileName, String[] fieldString) {
        String index = "";
        if (fieldString.length > 0) {
            for (int i = 0; i < fieldString.length; i++) {
                if (fileName.equals(fieldString[i])) {
                    int j = i + 1;
                    index = ConvertUtils.getString(j);
                }
            }
        }
        return index;

    }
/*
    // JSON返回页面MAP方式
    public static void ListtoView(HttpServletResponse response, PageList pageList) {
        response.setContentType("application/json");
        response.setHeader("Cache-Control", "no-store");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("total", pageList.getCount());
        map.put("rows", pageList.getResultList());
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(response.getWriter(), map);
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    /**
     * 控件类型：easyui
     * 返回datagrid JSON数据
     *
     * @param response
     * @param dg
     */
    public static void datagrid(HttpServletResponse response, DataGrid dg) {
        response.setContentType("application/json");
        response.setHeader("Cache-Control", "no-store");
        String data = null;
        Gson gson = new Gson();
        try {
            Object object = getObject(dg);
            data = gson.toJson(object);
        } catch (Exception e) {
          e.printStackTrace();
        }
        try {
            PrintWriter pw = response.getWriter();
            pw.write(data);
            pw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 控件类型：datatable
     * 返回datatable JSON数据
     *
     * @param response
     */
    public static void datatable(HttpServletResponse response, DataTableReturn dataTableReturn, String field) {
        response.setContentType("application/json");
        response.setHeader("Cache-Control", "no-store");
        try {
            String  data= datatable(field, dataTableReturn.getiTotalDisplayRecords(), dataTableReturn.getAaData());
            response.getWriter().write(new Gson().toJson(data));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 手工拼接JSON
     */
    public static String getComboBoxJson(List<RoleView> list, List<RoleView> roles) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[");
        for (RoleView node : list) {
            if (roles.size() > 0) {
                buffer.append("{\"id\":" + node.getId() + ",\"text\":\"" + node.getRoleName() + "\"");
                for (RoleView node1 : roles) {
                    if (node.getId() == node1.getId()) {
                        buffer.append(",\"selected\":true");
                    }
                }
                buffer.append("},");
            } else {
                buffer.append("{\"id\":" + node.getId() + ",\"text\":\"" + node.getRoleName() + "\"},");
            }

        }
        buffer.append("]");
        // 将,\n]替换成\n]
        String tmp = buffer.toString();
        tmp = tmp.replaceAll(",]", "]");
        return tmp;

    }

    /**
     * 根据模型生成JSON
     *
     * @param all      全部对象
     * @param in       已拥有的对象
     * @param comboBox 模型
     * @return
     */
    public static List<ComboBox> getComboBox(List all, List in, ComboBox comboBox) {
        List<ComboBox> comboxBoxs = new ArrayList<ComboBox>();
        String[] fields = new String[]{comboBox.getId(), comboBox.getText()};
        Object[] values = new Object[fields.length];
        for (Object node : all) {
            ComboBox box = new ComboBox();
            ReflectHelper reflectHelper = new ReflectHelper(node);
            for (int i = 0; i < fields.length; i++) {
                String fieldName = fields[i].toString();
                values[i] = reflectHelper.getMethodValue(fieldName);
            }
            box.setId(values[0].toString());
            box.setText(values[1].toString());
            if (in != null) {
                for (Object node1 : in) {
                    ReflectHelper reflectHelper2 = new ReflectHelper(node);
                    if (node1 != null) {
                        String fieldName = fields[0].toString();
                        String test = reflectHelper2.getMethodValue(fieldName).toString();
                        if (values[0].toString().equals(test)) {
                            box.setSelected(true);
                        }
                    }
                }
            }
            comboxBoxs.add(box);
        }
        return comboxBoxs;

    }

}
