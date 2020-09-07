package com.abocode.jfaster.core.persistence.hibernate.qbc;

import java.util.Map;

/**
 * 类描述：分页工具类
 * 张代浩
 *
 * @version 1.0
 * @date： 日期：2012-12-7 时间：上午10:19:14
 */
public class PagerUtil {
    private PagerUtil() {

    }

    public static String getBar(String action, String form, int allCounts, int curPageNO, int pageSize, Map<String, Object> map) {
        Pager pager = buildPager(allCounts, curPageNO, pageSize, map);
        return pager.getToolBar(action, form);
    }

    private static Pager buildPager(int allCounts, int curPageNO, int pageSize, Map<String, Object> map) {
        if (curPageNO > (int) Math.ceil((double) allCounts / pageSize))
            curPageNO = (int) Math.ceil((double) allCounts / pageSize);
        if (curPageNO <= 1)
            curPageNO = 1;
        // 得到offset
        return new Pager(allCounts, curPageNO, pageSize, map);
    }

    public static String getBar(String url, int allCounts, int curPageNO, int pageSize, Map<String, Object> map) {
        Pager pager = buildPager(allCounts, curPageNO, pageSize, map);
        return pager.getToolBar(url);
    }

    public static int getOffset(int rowCounts, int curPageNO, int pageSize) {
        if (curPageNO > (int) Math.ceil((double) rowCounts / pageSize))
            curPageNO = (int) Math.ceil((double) rowCounts / pageSize);
        // 得到第几页
        if (curPageNO <= 1)
            curPageNO = 1;
        // 得到offset
        return (curPageNO - 1) * pageSize;
    }

    public static int getcurPageNo(int rowCounts, int curPageNO, int pageSize) {
        // 得到第几页
        if (curPageNO > (int) Math.ceil((double) rowCounts / pageSize))
            curPageNO = (int) Math.ceil((double) rowCounts / pageSize);
        if (curPageNO <= 1)
            curPageNO = 1;
        return curPageNO;
    }
}