package com.abocode.jfaster.web.system.application;


import com.abocode.jfaster.web.system.application.dto.bean.DuplicateBean;

public interface ISystemService {
    Long findCountByTable(DuplicateBean table);
}
