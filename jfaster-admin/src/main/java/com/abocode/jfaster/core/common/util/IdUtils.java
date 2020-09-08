package com.abocode.jfaster.core.common.util;

import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class IdUtils {
    private IdUtils() {
    }

    private static final Random RANDOM = new Random();

    /**
     * 抽取由逗号分隔的主键列表
     *
     * @param ids 由逗号分隔的多个主键值
     * @return 主键类表
     */
    public static synchronized List<String> extractIdListByComma(String ids) {
        List<String> result = new ArrayList<>();
        if (!StringUtils.isEmpty(ids)) {
            for (String id : ids.split(",")) {
                if (StringUtils.hasLength(id)) {
                    result.add(id.trim());
                }
            }
        }
        return result;
    }

    public static synchronized int nextInt(int len) {
        return RANDOM.nextInt(len);
    }
}
