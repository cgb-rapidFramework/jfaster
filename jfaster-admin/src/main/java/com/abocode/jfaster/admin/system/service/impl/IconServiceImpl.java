package com.abocode.jfaster.admin.system.service.impl;

import com.abocode.jfaster.admin.system.repository.SystemRepository;
import com.abocode.jfaster.admin.system.service.IconService;
import com.abocode.jfaster.core.common.constants.Globals;
import com.abocode.jfaster.core.platform.utils.MutiLangUtils;
import com.abocode.jfaster.system.entity.Function;
import com.abocode.jfaster.system.entity.Icon;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Description:
 *
 * @author: guanxf
 * @date: 2020/5/18
 */
@Service
public class IconServiceImpl implements IconService {
    @Resource
    private SystemRepository systemService;

    @Override
    public void save(Icon icon) {
        systemService.delete(icon);
        String message = MutiLangUtils.paramDelSuccess("common.icon");
        systemService.addLog(message, Globals.LOG_TYPE_DEL, Globals.LOG_LEVEL);
    }


    /**
     * 检查是否允许删除该图标。
     * @param icon 图标。
     * @return true允许；false不允许；
     */
    @Override
    public boolean isPermitDel(Icon icon) {
        List<Function> functions = systemService.findAllByProperty(Function.class, "icon.id", icon.getId());
        return functions==null||functions.isEmpty();
    }
}
