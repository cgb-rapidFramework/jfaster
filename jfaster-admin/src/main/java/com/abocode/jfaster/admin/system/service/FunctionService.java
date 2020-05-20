package com.abocode.jfaster.admin.system.service;

import com.abocode.jfaster.core.common.model.json.ComboTree;
import com.abocode.jfaster.core.common.model.json.TreeGrid;
import com.abocode.jfaster.system.entity.Function;
import com.abocode.jfaster.system.entity.User;
import com.abocode.jfaster.core.platform.view.FunctionView;

import java.util.List;
import java.util.Map;

public interface FunctionService {
    Map<Integer, List<FunctionView>>  getFunctionMap(User user);

    void delById(String id);

    void delByLopId(String id);

    void saveFunction(Function function);

    String search(String name);

    List<ComboTree> setParentFunction(String selfId, String id);

    List<TreeGrid> findTreeGrid(String selfId, String id);

    String getPrimaryMenu(User user);
}
