package com.abocode.jfaster.admin.system.service;

import com.abocode.jfaster.system.entity.Icon;

/**
 * Description:
 *
 * @author: guanxf
 * @date: 2020/5/18
 */
public interface IconService {
    void save(Icon icon);

    boolean isPermitDel(Icon icon);
}
