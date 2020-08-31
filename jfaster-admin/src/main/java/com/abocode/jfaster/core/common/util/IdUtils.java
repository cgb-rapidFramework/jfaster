package com.abocode.jfaster.core.common.util;

import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class IdUtils {

    /**
     * 抽取由逗号分隔的主键列表
     * @param ids 由逗号分隔的多个主键值
     * @return 主键类表
     */
    public static synchronized List<String> extractIdListByComma(String ids) {
        List<String> result = new ArrayList();
        if (org.springframework.util.StringUtils.hasText(ids)) {
            for (String id : ids.split(",")) {
                if (StringUtils.hasLength(id)) {
                    result.add(id.trim());
                }
            }
        }
        return result;
    }
}
