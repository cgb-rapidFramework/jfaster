package com.abocode.jfaster.core.platform.view.widgets.easyui;

import java.util.List;
import java.util.Map;

public class DataObject {
    private List<Map<String, Object>> rows;
    private Map<String, Object> footer;
    private int total;

    public List<Map<String, Object>> getRows() {
        return rows;
    }

    public void setRows(List<Map<String, Object>> rows) {
        this.rows = rows;
    }

    public Map<String, Object> getFooter() {
        return footer;
    }

    public void setFooter(Map<String, Object> footer) {
        this.footer = footer;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

}
