package com.abocode.jfaster.core.common.model.json;

import java.util.List;

import com.abocode.jfaster.core.platform.view.interactions.datatable.SortDirection;
import com.abocode.jfaster.core.common.util.ContextHolderUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * easyui的datagrid向后台传递参数使用的model
 *
 * @author
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DataGrid {

    private int page = 1;// 当前页
    private int rows = 10;// 每页显示记录数
    private String sort = null;// 排序字段名
    private SortDirection order = SortDirection.asc;// 按什么排序(asc,desc)
    private String field;//字段
    private String treefield;//树形数据表文本字段
    private List results;// 结果集
    private int total;//总记录数
    private String footer;//合计列

    public DataGrid(Integer total, List results) {
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
