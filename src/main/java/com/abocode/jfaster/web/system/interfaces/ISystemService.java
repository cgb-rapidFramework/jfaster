package com.abocode.jfaster.web.system.interfaces;


import com.abocode.jfaster.web.system.interfaces.bean.DuplicateBean;

public interface ISystemService {
    Long findCountByTable(DuplicateBean table);
}
