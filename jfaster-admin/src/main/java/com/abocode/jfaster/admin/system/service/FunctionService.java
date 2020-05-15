package com.abocode.jfaster.admin.system.service;

import com.abocode.jfaster.system.entity.User;
import com.abocode.jfaster.admin.system.dto.view.FunctionView;

import java.util.List;
import java.util.Map;

public interface FunctionService {
    Map<Integer, List<FunctionView>>  getFunctionMap(User user);
}
