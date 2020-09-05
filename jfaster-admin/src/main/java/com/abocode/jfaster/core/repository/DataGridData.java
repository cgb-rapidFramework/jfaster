package com.abocode.jfaster.core.repository;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
@Getter
@Setter
public class DataGridData {
    private List<Map<String, Object>> rows;
    private Map<String, Object> footer;
    private int total;
}
