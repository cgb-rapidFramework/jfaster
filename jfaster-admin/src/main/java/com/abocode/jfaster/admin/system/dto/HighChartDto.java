package com.abocode.jfaster.admin.system.dto;

import lombok.Data;

import java.util.List;

/**
 * 统计报表模型
 */
@Data
public class HighChartDto {
    private String name;
    private String type;//类型
    private List data;//数据
}
