package com.abocode.jfaster.admin.system.web;

import com.abocode.jfaster.admin.system.repository.SystemRepository;
import com.abocode.jfaster.admin.system.service.TerritoryService;
import com.abocode.jfaster.core.common.model.json.AjaxJson;
import com.abocode.jfaster.core.common.model.json.AjaxJsonBuilder;
import com.abocode.jfaster.core.common.model.json.ComboTree;
import com.abocode.jfaster.core.common.model.json.TreeGrid;
import com.abocode.jfaster.system.entity.Territory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * 地域处理类
 */
@Scope("prototype")
@Controller
@RequestMapping("/territoryController")
public class TerritoryController {
    @Autowired
    private SystemRepository systemRepository;
    @Autowired
    private TerritoryService territoryService;

    /**
     * 地域列表页面跳转
     * @return
     */
    @RequestMapping(params = "territory")
    public ModelAndView function() {
        return new ModelAndView("system/territory/territoryList");
    }

    /***
     * 地域列表
     * @param treegrid
     * @return
     */
    @RequestMapping(params = "territoryGrid")
    @ResponseBody
    public List<TreeGrid> territoryGrid(TreeGrid treegrid) {
        return territoryService.findById(treegrid.getId());
    }

    /**
     * 地域列表页面跳转
     * @return
     */
    @RequestMapping(params = "detail")
    public ModelAndView detail(Territory territory, HttpServletRequest request) {
        String functionid = request.getParameter("id");
        if (functionid != null) {
            territory = systemRepository.findEntity(Territory.class, functionid);
            request.setAttribute("territoryView", territory);
        }
        if (territory.getParentTerritory() != null && territory.getParentTerritory().getId() != null) {
            territory.setParentTerritory((Territory) systemRepository.findEntity(Territory.class, territory.getParentTerritory().getId()));
            request.setAttribute("territoryView", territory);
        }
        return new ModelAndView("system/territory/territory");
    }

    /**
     * 地域父级下拉菜单
     */
    @RequestMapping(params = "setPTerritory")
    @ResponseBody
    public List<ComboTree> setPTerritory(ComboTree comboTree) {
        return territoryService.findComboTree(comboTree.getId());
    }

    /**
     * 地域保存
     */
    @RequestMapping(params = "saveTerritory")
    @ResponseBody
    public AjaxJson saveTerritory(Territory territory) {
        territoryService.save(territory);
        return AjaxJsonBuilder.success();
    }

    /**
     * 地域删除
     * @param territory
     * @return
     */
    @RequestMapping(params = "del")
    @ResponseBody
    public AjaxJson del(Territory territory) {
        territoryService.del(territory.getId());
        return AjaxJsonBuilder.success();
    }

}
