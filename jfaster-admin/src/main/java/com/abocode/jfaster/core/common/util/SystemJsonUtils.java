package com.abocode.jfaster.core.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;
import com.abocode.jfaster.core.repository.TagUtil;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SystemJsonUtils {


    /**
     * @param objList
     * @param perFieldName
     * @param sufFieldName
     * @return 格式：old_new,old2_new2
     * 返回类型： String
     */
    public static String listToReplaceStr(List<?> objList, String perFieldName, String sufFieldName) {
        List<String> strList = new ArrayList();
        for (Object object : objList) {
            String perStr = null;
            String sufStr = null;
            try {
                perStr = (String) PropertyUtils.getProperty(object, perFieldName);
                sufStr = (String) PropertyUtils.getProperty(object, sufFieldName);
            } catch (InvocationTargetException e) {
                log.error(e.getMessage());
            } catch (NoSuchMethodException e) {
                log.error(e.getMessage());
            } catch (IllegalAccessException e) {
                log.error(e.getMessage());
            }
            strList.add(perStr + "_" + sufStr);
        }
        return StrUtils.join(strList, ',');
    }


    public static String listToJson(String[] fields, int total, List list) {
        Object[] values = new Object[fields.length];
        String jsonTemp = "{\"total\":" + total + ",\"rows\":[";
        for (int j = 0; j < list.size(); j++) {
            jsonTemp = jsonTemp + "{\"state\":\"closed\",";
            for (int i = 0; i < fields.length; i++) {
                String fieldName = fields[i].toString();
                values[i] = TagUtil.fieldNameToValues(fieldName, list.get(j));
                jsonTemp = jsonTemp + "\"" + fieldName + "\"" + ":\"" + values[i] + "\"";
                if (i != fields.length - 1) {
                    jsonTemp = jsonTemp + ",";
                }
            }
            if (j != list.size() - 1) {
                jsonTemp = jsonTemp + "},";
            } else {
                jsonTemp = jsonTemp + "}";
            }
        }
        jsonTemp = jsonTemp + "]}";
        return jsonTemp;
    }
}
