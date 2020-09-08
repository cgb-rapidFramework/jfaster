package com.abocode.jfaster.core.repository;

import com.abocode.jfaster.core.common.util.ContextHolderUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 *DataGridData
 * @author
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DataGridParam {
    private int page = 1;// 当前页
    private int rows = 10;// 每页显示记录数
    private String sort = null;// 排序字段名
    private SortDirection order = SortDirection.ASC;// 按什么排序(asc,desc)
    private String field;//字段
    private String treeField;//树形数据表文本字段
    private List<?> results;// 结果集
    private int total;//总记录数
    private String footer;//合计列

    public DataGridParam(Integer total, List<?> results) {
        this.total = total;
        this.results = results;
    }

    public int getRows() {
        if (ContextHolderUtils.getRequest() != null && ContextHolderUtils.getRequest().getParameter("rows") != null) {
            return rows;
        }
        return 10000;
    }

}
