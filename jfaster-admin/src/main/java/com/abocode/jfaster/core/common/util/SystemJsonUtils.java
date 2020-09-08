package com.abocode.jfaster.core.common.util;

import com.abocode.jfaster.core.common.exception.BusinessException;
import com.abocode.jfaster.core.repository.TagUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SystemJsonUtils {
    private SystemJsonUtils() {
    }

    /**
     * @param objList
     * @param perFieldName
     * @param sufFieldName
     * @return 格式：old_new,old2_new2
     * 返回类型： String
     */
    public static String listToReplaceStr(List<?> objList, String perFieldName, String sufFieldName) {
        List<String> strList = new ArrayList<>();
        for (Object object : objList) {
            try {
                String perStr = (String) PropertyUtils.getProperty(object, perFieldName);
                String sufStr = (String) PropertyUtils.getProperty(object, sufFieldName);
                strList.add(perStr + "_" + sufStr);
                return StrUtils.join(strList, ',');
            } catch (Exception e) {
                throw new BusinessException("转换异常", e);
            }
        }
        return "";
    }


    public static String listToJson(String[] fields, int total, List<String> list) {
        Object[] values = new Object[fields.length];
        StringBuilder jsonTemp = new StringBuilder().append("{\"total\":" + total + ",\"rows\":[");
        for (int j = 0; j < list.size(); j++) {
            jsonTemp.append("{\"state\":\"closed\",");
            for (int i = 0; i < fields.length; i++) {
                String fieldName = fields[i];
                values[i] = TagUtil.fieldNameToValues(fieldName, list.get(j));
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
}
