package com.abocode.jfaster.admin.system.service;


import com.abocode.jfaster.admin.system.dto.bean.DuplicateBean;

public interface SystemService {
    Long findCountByTable(DuplicateBean table);
}
