package com.abocode.jfaster.admin.system.service;

import com.abocode.jfaster.admin.system.dto.HighChartDto;

import java.util.List;

/**
 * Description:
 *
 * @author: guanxf
 * @date: 2020/5/18
 */
public interface ChartService {
    List<HighChartDto> buildChart(String reportType);
}
