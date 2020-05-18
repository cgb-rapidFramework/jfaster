package com.abocode.jfaster.admin.system.service;

import com.abocode.jfaster.system.entity.DataRule;

/**
 * Description:
 *
 * @author: guanxf
 * @date: 2020/5/18
 */
public interface RuleService {
    void del(DataRule operation);

    void save(DataRule operation);
}
