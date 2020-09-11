package com.abocode.jfaster.admin.system.web;

import com.abocode.jfaster.admin.system.repository.SystemRepository;
import com.abocode.jfaster.api.system.ConfigDto;
import com.abocode.jfaster.core.common.constants.Globals;
import com.abocode.jfaster.core.common.model.json.AjaxJson;
import com.abocode.jfaster.core.common.model.json.AjaxJsonBuilder;
import com.abocode.jfaster.core.persistence.hibernate.qbc.CriteriaQuery;
import com.abocode.jfaster.core.repository.DataGridData;
import com.abocode.jfaster.core.repository.DataGridParam;
import com.abocode.jfaster.core.web.manager.SessionHolder;
import com.abocode.jfaster.system.entity.Config;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;


/**
 * 配置信息处理类 *
 *
 * @author franky
 */
@Controller
@RequestMapping("/configController")
public class ConfigController {
    private static final String NAME = "配置信息: ";
    @Autowired
    private SystemRepository systemRepository;

    /**
     * 配置列表页面跳转
     *
     * @return
     */
    @RequestMapping(params = "config")
    public ModelAndView config() {
        return new ModelAndView("system/config/configList");
    }

    /**
     * easyuiAjax表单请求
     *
     * @param dataGridParam
     * @return
     */
    @RequestMapping(params = "findDataGridData")
    @ResponseBody
    public DataGridData findDataGridData(DataGridParam dataGridParam) {
        CriteriaQuery cq = new CriteriaQuery(Config.class).buildDataGrid(dataGridParam);
        return this.systemRepository.findDataGridData(cq);
    }

    /**
     * 删除配置信息
     *
     * @param id
     * @return
     */
    @RequestMapping(params = "del")
    @ResponseBody
    public AjaxJson del(@RequestParam String id) {
        Config entity = systemRepository.find(Config.class, id);
        String message = NAME + entity.getName() + "被删除 成功";
        systemRepository.delete(id);
        systemRepository.addLog(message, Globals.LOG_TYPE_DEL, Globals.LOG_LEVEL);
        return AjaxJsonBuilder.success(message);
    }

    /**
     * 添加和更新配置信息
     * @return
     */
    @RequestMapping(params = "save")
    @ResponseBody
    public AjaxJson save(ConfigDto configDto) {
        String message;
        if (StringUtils.isEmpty(configDto.getId())) {
            Config tsConfig = systemRepository.findUniqueByProperty(Config.class, "code", configDto.getCode());
            Assert.notNull(tsConfig, "编码为: " + tsConfig.getCode() + "的配置信息已存在");

            tsConfig = systemRepository.find(Config.class, configDto.getId());
            tsConfig.setUser(SessionHolder.getCurrentUser());
            systemRepository.save(tsConfig);
            message = NAME + tsConfig.getName() + "被添加成功";
            systemRepository.addLog(message, Globals.LOG_TYPE_INSERT,
                    Globals.LOG_LEVEL);

        } else {
            Config tsConfig = systemRepository.find(Config.class, configDto.getId());
            message = NAME + tsConfig.getName() + "被修改成功";
            systemRepository.update(tsConfig);
            systemRepository.addLog(message, Globals.LOG_TYPE_INSERT, Globals.LOG_LEVEL);
        }
        return AjaxJsonBuilder.success(message);
    }

    /**
     * 添加和更新配置信息页面
     * @param id
     * @return
     */
    @RequestMapping(params = "detail")
    public ModelAndView detail(@RequestParam String id, HttpServletRequest request) {
        if (!StringUtils.isEmpty(id)) {
            Config config = systemRepository.find(Config.class, id);
            ConfigDto configDto = new ConfigDto();
            BeanUtils.copyProperties(config, configDto);
            request.setAttribute("configView", configDto);
        }
        return new ModelAndView("system/config/config");
    }

}
