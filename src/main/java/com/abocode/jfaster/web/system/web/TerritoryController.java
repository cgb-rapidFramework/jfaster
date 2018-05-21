package com.abocode.jfaster.web.system.web;

import com.abocode.jfaster.core.common.model.json.AjaxJson;
import com.abocode.jfaster.platform.view.interactions.datatable.SortDirection;
import com.abocode.jfaster.platform.view.interactions.easyui.ComboTreeModel;
import com.abocode.jfaster.core.common.constants.Globals;
import com.abocode.jfaster.core.common.util.MutiLangUtils;
import com.abocode.jfaster.web.system.domain.entity.Territory;
import com.abocode.jfaster.web.system.domain.repository.ResourceService;
import com.abocode.jfaster.web.system.domain.repository.SystemService;
import com.abocode.jfaster.core.persistence.hibernate.qbc.CriteriaQuery;
import com.abocode.jfaster.core.common.model.json.ComboTree;
import com.abocode.jfaster.core.common.model.json.TreeGrid;
import com.abocode.jfaster.platform.view.interactions.easyui.TreeGridModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


/**
 * 地域处理类
 * @author wushu
 */
@Scope("prototype")
@Controller
@RequestMapping("/territoryController")
public class TerritoryController extends BaseController {
	@Autowired
	private ResourceService resourceService;
	private String message = null;

	@Autowired
	private SystemService systemService;

	/**
	 * 地域列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "territory")
	public ModelAndView function() {
		return new ModelAndView("system/territory/territoryList");
	}

	
	/**
	 * 地域列表
	 */
	@RequestMapping(params = "territoryGrid")
	@ResponseBody
	public List<TreeGrid> territoryGrid(HttpServletRequest request, TreeGrid treegrid) {
		CriteriaQuery cq = new CriteriaQuery(Territory.class);
			if (treegrid.getId() != null) {
				cq.eq("TSTerritory.id", treegrid.getId());
			}
			if (treegrid.getId() == null) {
				cq.eq("TSTerritory.id","0");//这个是全国最高级
			}
		
		cq.addOrder("territorySort", SortDirection.asc);
		cq.add();
		List<Territory> territoryList = systemService.findListByCq(cq, false);
		List<TreeGrid> treeGrids = new ArrayList<TreeGrid>();
		TreeGridModel treeGridModel = new TreeGridModel();
		treeGridModel.setIcon("");
		treeGridModel.setTextField("territoryName");
		treeGridModel.setParentText("TSTerritory_territoryName");
		treeGridModel.setParentId("TSTerritory_id");
		treeGridModel.setSrc("territoryCode");
		treeGridModel.setIdField("id");
		treeGridModel.setChildList("TSTerritorys");
		treeGridModel.setOrder("territorySort");
		treeGrids = resourceService.treegrid(territoryList, treeGridModel);
		return treeGrids;
	}
	/**
	 *地域列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(Territory territory, HttpServletRequest req) {
		String functionid = req.getParameter("id");
		if (functionid != null) {
			territory = systemService.findEntity(Territory.class, functionid);
			req.setAttribute("territoryView", territory);
		}
		if(territory.getTSTerritory()!=null && territory.getTSTerritory().getId()!=null){
			territory.setTSTerritory((Territory)systemService.findEntity(Territory.class, territory.getTSTerritory().getId()));
			req.setAttribute("territoryView", territory);
		}
		return new ModelAndView("system/territory/territory");
	}
	/**
	 * 地域父级下拉菜单
	 */
	@RequestMapping(params = "setPTerritory")
	@ResponseBody
	public List<ComboTree> setPTerritory(HttpServletRequest request, ComboTree comboTree) {
		CriteriaQuery cq = new CriteriaQuery(Territory.class);
		if (comboTree.getId() != null) {
			cq.eq("TSTerritory.id", comboTree.getId());
		}
		if (comboTree.getId() == null) {
			cq.isNull("TSTerritory");
		}
		cq.add();
		List<Territory> territoryList = systemService.findListByCq(cq, false);
		ComboTreeModel comboTreeModel = new ComboTreeModel("id", "territoryName", "TSTerritorys");
		List<ComboTree> comboTrees  = resourceService.ComboTree(territoryList, comboTreeModel, null, false);
		return comboTrees;
	}
	/**
	 * 地域保存
	 */
	@RequestMapping(params = "saveTerritory")
	@ResponseBody
	public AjaxJson saveTerritory(Territory territory, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		String functionOrder = territory.getTerritorySort();
		if(StringUtils.isEmpty(functionOrder)){
			territory.setTerritorySort("0");
		}
		if (territory.getTSTerritory().getId().equals("")) {
			territory.setTSTerritory(null);
		}else{
			Territory parent = systemService.findEntity(Territory.class, territory.getTSTerritory().getId());
			territory.setTerritoryLevel(Short.valueOf(parent.getTerritoryLevel()+1+""));
		}
		if (!StringUtils.isEmpty(territory.getId())) {
			message = "地域: " + territory.getTerritoryName() + "被更新成功";
			systemService.saveOrUpdate(territory);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);

            message = MutiLangUtils.paramUpdSuccess("common.area");
		} else {
			territory.setTerritorySort(territory.getTerritorySort());
			message = "地域: " + territory.getTerritoryName() + "被添加成功";
			systemService.save(territory);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);

            message = MutiLangUtils.paramAddSuccess("common.area");
        }

        j.setMsg(message);
		
		return j;
	}

	/**
	 * 地域删除
	 * 
	 * @param territory
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(Territory territory, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		territory = systemService.findEntity(Territory.class, territory.getId());
		message = "地域: " + territory.getTerritoryName() + "被删除成功";
		systemService.delete(territory);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);

        message = MutiLangUtils.paramDelSuccess("common.area");
        j.setMsg(message);
		return j;
	}

}
