package com.abocode.jfaster.admin.system.service;

import com.abocode.jfaster.admin.system.dto.DataRuleDto;
import com.abocode.jfaster.core.common.model.json.ComboTree;
import com.abocode.jfaster.core.common.model.json.TreeGrid;
import com.abocode.jfaster.core.platform.view.FunctionView;
import com.abocode.jfaster.system.entity.Function;
import com.abocode.jfaster.system.entity.Operation;
import com.abocode.jfaster.system.entity.User;

import java.util.List;
import java.util.Map;

public interface FunctionService {
    void initMenu();

    Map<Integer, List<FunctionView>>  getFunctionMap(User user);

    void delById(String id);

    void delByLopId(String id);

    void saveFunction(Function function);

    String search(String name);

    List<ComboTree> setParentFunction(String selfId, String id);

    List<TreeGrid> findTreeGrid(String selfId, String id);

    String getPrimaryMenu(User user);

    List<Operation> findById(String functionId, String userId);

    DataRuleDto installDataRule(String[] dataRuleCodes);

    boolean hasMenuAuth(String requestPath, String clickFunctionId);
}
