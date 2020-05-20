package com.abocode.jfaster.admin.system.web;

import com.abocode.jfaster.admin.system.service.IconService;
import com.abocode.jfaster.admin.system.dto.UploadFileDto;
import com.abocode.jfaster.core.common.model.json.AjaxJson;
import com.abocode.jfaster.core.common.model.json.AjaxJsonBuilder;
import com.abocode.jfaster.core.common.model.json.DataGrid;
import com.abocode.jfaster.core.common.util.*;
import com.abocode.jfaster.core.platform.utils.MutiLangUtils;
import com.abocode.jfaster.core.platform.utils.SysThemesUtils;
import com.abocode.jfaster.core.web.hqlsearch.HqlGenerateUtil;
import com.abocode.jfaster.system.entity.Icon;
import com.abocode.jfaster.admin.system.repository.ResourceRepository;
import com.abocode.jfaster.admin.system.repository.SystemRepository;
import com.abocode.jfaster.core.persistence.hibernate.qbc.CriteriaQuery;
import com.abocode.jfaster.core.platform.view.widgets.easyui.TagUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;


/**
 * 图标信息处理类
 * 
 * @author 张代浩
 * 
 */
@Scope("prototype")
@Controller
@RequestMapping("/iconController")
public class IconController{
	@Autowired
	private SystemRepository systemService;
	@Autowired
	private ResourceRepository resourceService;
	@Autowired
	private IconService  iconService;
	/**
	 * 图标列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "icon")
	public ModelAndView icon() {
		return new ModelAndView("system/icon/iconList");
	}

	/**
	 * easyuiAJAX请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "datagrid")
	public void datagrid(Icon icon, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(Icon.class, dataGrid);
		HqlGenerateUtil.installHql(cq, icon);
		cq.add();
		this.systemService.findDataGridReturn(cq, true);
        FileUtils.convertDataGrid(dataGrid, request);//先把数据库的byte存成图片到临时目录，再给每个TsIcon设置目录路径
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 上传图标
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "saveOrUpdateIcon", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson saveOrUpdateIcon(HttpServletRequest request) {
		Icon icon = new Icon();
		Short iconType = ConvertUtils.getShort(request.getParameter("iconType"));
		String iconName = ConvertUtils.getString(request.getParameter("iconName"));
		String id = request.getParameter("id");
		icon.setId(id);
		icon.setIconName(iconName);
		icon.setIconType(iconType);
		UploadFileDto uploadFile = new UploadFileDto(request, icon);
		uploadFile.setCusPath("plug-in/accordion/images");
		uploadFile.setIconExtend("extend");
		uploadFile.setTitleField("iconclas");
		uploadFile.setRealPath("iconPath");
		uploadFile.setObject(icon);
		uploadFile.setByteField("iconContent");
		uploadFile.setRename(false);
		resourceService.uploadFile(uploadFile);
		// 图标的css样式
		String css = "." + icon.getIconClazz() + "{background:url('../images/" + icon.getIconClazz() + "." + icon.getIconExtend() + "') no-repeat}";
		String path = request.getSession().getServletContext().getRealPath("/plug-in/accordion/css/icons.css");
		SysThemesUtils.write(path, css);
		String message = MutiLangUtils.paramAddSuccess("common.icon");
		return AjaxJsonBuilder.success(message);
	}	
	/**
	 * 没有上传文件时更新信息
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "update", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson update(HttpServletRequest request) throws Exception {
		Short iconType = ConvertUtils.getShort(request.getParameter("iconType"));
		String iconName = java.net.URLDecoder.decode(ConvertUtils.getString(request.getParameter("iconName")));
		String id = request.getParameter("id");
		Icon icon = new Icon();
		if (StringUtils.isNotEmpty(id)) {
			icon = systemService.find(Icon.class, id);
			icon.setId(id);
		}
		icon.setIconName(iconName);
		icon.setIconType(iconType);
		systemService.saveOrUpdate(icon);
		// 图标的css样式
		String css = "." + icon.getIconClazz() + "{background:url('../images/" + icon.getIconClazz() + "." + icon.getIconExtend() + "') no-repeat}";
		String path = request.getSession().getServletContext().getRealPath("/plug-in/accordion/css/icons.css");
		SysThemesUtils.write(path, css);
		return AjaxJsonBuilder.success();
	}


	/**
	 * 恢复图标（将数据库图标数据写入图标存放的路径下）
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "repair")
	@ResponseBody
	public AjaxJson repair(HttpServletRequest request) throws Exception {
		List<Icon> icons = systemService.findAll(Icon.class);
		String rootPath = request.getSession().getServletContext().getRealPath("/");
		String cssPath = request.getSession().getServletContext().getRealPath("/plug-in/accordion/css/icons.css");
		// 清空CSS文件内容
		SysThemesUtils.clearFile(cssPath);
		for (Icon c : icons) {
			File file = new File(rootPath.concat(c.getIconPath()));
			if (!file.exists()) {
				byte[] content = c.getIconContent();
				if (content != null) {
					BufferedImage imag = ImageIO.read(new ByteArrayInputStream(content));
					ImageIO.write(imag, "PNG", file);// 输出到 png 文件
				}
			}
			String css = "." + c.getIconClazz() + "{background:url('../images/" + c.getIconClazz() + "." + c.getIconExtend() + "') no-repeat}";
			String path = request.getSession().getServletContext().getRealPath("/plug-in/accordion/css/icons.css");
			SysThemesUtils.write(path, css);
		}
		return AjaxJsonBuilder.success(MutiLangUtils.paramAddSuccess("common.icon.style"));
	}


	/**
	 * 删除图标
	 * 
	 * @param icon
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(Icon icon) {
		icon = systemService.findEntity(Icon.class, icon.getId());
		boolean isPermit=iconService.isPermitDel(icon);
		Assert.isTrue(isPermit,MutiLangUtils.paramDelFail("common.icon,common.icon.isusing"));
		iconService.save(icon);
		return AjaxJsonBuilder.success();
	}

	/**
	 * 图标页面跳转
	 * 
	 * @param icon
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(Icon icon, HttpServletRequest req) {
		if (StringUtils.isNotEmpty(icon.getId())) {
			icon = systemService.findEntity(Icon.class, icon.getId());
			req.setAttribute("icon", icon);
		}
		return new ModelAndView("system/icon/icons");
	}
}
