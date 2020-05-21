package com.abocode.jfaster.admin.system.service;


import com.abocode.jfaster.admin.system.dto.DuplicateBean;

public interface SystemService {
    Long findCountByTable(DuplicateBean table);
}
