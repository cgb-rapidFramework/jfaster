package com.abocode.jfaster.admin.system.service;

import com.abocode.jfaster.core.common.model.json.HighChart;

import java.util.List;

/**
 * Description:
 *
 * @author: guanxf
 * @date: 2020/5/18
 */
public interface ChartService {
    List<HighChart> buildChart(String reportType);
}
