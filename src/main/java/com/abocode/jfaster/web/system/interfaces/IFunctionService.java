package com.abocode.jfaster.web.system.interfaces;

import com.abocode.jfaster.web.system.domain.entity.User;
import com.abocode.jfaster.web.system.interfaces.view.FunctionView;

import java.util.List;
import java.util.Map;

public interface IFunctionService {
    Map<Integer, List<FunctionView>>  getFunctionMap(User user);
}
