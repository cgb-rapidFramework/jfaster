package com.abocode.jfaster.admin.system.web;

import com.abocode.jfaster.core.common.model.common.UploadFile;
import com.abocode.jfaster.core.common.model.json.AjaxJson;
import com.abocode.jfaster.core.common.model.json.DataGrid;
import com.abocode.jfaster.core.common.constants.Globals;
import com.abocode.jfaster.core.common.util.*;
import com.abocode.jfaster.core.web.hqlsearch.HqlGenerateUtil;
import com.abocode.jfaster.system.entity.Function;
import com.abocode.jfaster.system.entity.Icon;
import com.abocode.jfaster.system.entity.Operation;
import com.abocode.jfaster.admin.system.repository.ResourceRepository;
import com.abocode.jfaster.admin.system.repository.SystemRepository;
import com.abocode.jfaster.core.persistence.hibernate.qbc.CriteriaQuery;
import com.abocode.jfaster.core.platform.view.widgets.easyui.TagUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
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
	private SystemRepository systemService;
	@Autowired
	private ResourceRepository resourceService;
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Autowired
	public void setSystemService(SystemRepository systemService) {
		this.systemService = systemService;
	}

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
	public AjaxJson saveOrUpdateIcon(HttpServletRequest request) throws Exception {
		AjaxJson j = new AjaxJson();
		Icon icon = new Icon();
		Short iconType = ConvertUtils.getShort(request.getParameter("iconType"));
		String iconName = ConvertUtils.getString(request.getParameter("iconName"));
		String id = request.getParameter("id");
		icon.setId(id);
		icon.setIconName(iconName);
		icon.setIconType(iconType);
		UploadFile uploadFile = new UploadFile(request, icon);
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
		write(request, css);
		message = MutiLangUtils.paramAddSuccess("common.icon");
		j.setMsg(message);
		return j;
	}	
	/**
	 * 没有上传文件时更新信息
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping(params = "update", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson update(HttpServletRequest request) throws Exception {
		AjaxJson j = new AjaxJson();
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
		write(request, css);
		message = "更新成功";
		j.setMsg(message);
		return j;
	}
	/**
	 * 添加图标样式
	 * 
	 * @param request
	 * @param css
	 */
	protected void write(HttpServletRequest request, String css) {
		try {
			String path = request.getSession().getServletContext().getRealPath("/plug-in/accordion/css/icons.css");
			File file = new File(path);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter out = new FileWriter(file, true);
			out.write("\r\n");
			out.write(css);
			out.close();
		} catch (Exception e) {
		}
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
		AjaxJson json = new AjaxJson();
		List<Icon> icons = systemService.findAll(Icon.class);
		String rootpath = request.getSession().getServletContext().getRealPath("/");
		String csspath = request.getSession().getServletContext().getRealPath("/plug-in/accordion/css/icons.css");
		// 清空CSS文件内容
		clearFile(csspath);
		for (Icon c : icons) {
			File file = new File(rootpath + c.getIconPath());
			if (!file.exists()) {
				byte[] content = c.getIconContent();
				if (content != null) {
					BufferedImage imag = ImageIO.read(new ByteArrayInputStream(content));
					ImageIO.write(imag, "PNG", file);// 输出到 png 文件
				}
			}
			String css = "." + c.getIconClazz() + "{background:url('../images/" + c.getIconClazz() + "." + c.getIconExtend() + "') no-repeat}";
			write(request, css);
		}
        json.setMsg(MutiLangUtils.paramAddSuccess("common.icon.style"));
        json.setSuccess(true);
		return json;
	}

	/**
	 * 清空文件内容
	 * 
	 * @param path
	 */
	protected void clearFile(String path) {
		try {
			FileOutputStream fos = new FileOutputStream(new File(path));
			fos.write("".getBytes());
			fos.close();
		} catch (FileNotFoundException e) {
			LogUtils.error(e.getMessage());
		} catch (IOException e) {
			LogUtils.error(e.getMessage());
		}
	}

	/**
	 * 删除图标
	 * 
	 * @param icon
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(Icon icon, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		
		icon = systemService.findEntity(Icon.class, icon.getId());
		
		boolean isPermit=isPermitDel(icon);
		
		if(isPermit){
			systemService.delete(icon);

            message = MutiLangUtils.paramDelSuccess("common.icon");
			
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);

            j.setMsg(message);

            return j;
		}
		
        message = MutiLangUtils.paramDelFail("common.icon,common.icon.isusing");
        j.setMsg(message);
		
		return j;
	}

	/**
	 * 检查是否允许删除该图标。
	 * @param icon 图标。
	 * @return true允许；false不允许；
	 */
	private boolean isPermitDel(Icon icon) {
		List<Function> functions = systemService.findAllByProperty(Function.class, "Icon.id", icon.getId());
		if (functions==null||functions.isEmpty()) {
			return true;
		}
		return false;
	}

	/**	 *
	 * @param icon
     */
	@Deprecated
	public void upEntity(Icon icon) {
		List<Function> functions = systemService.findAllByProperty(Function.class, "Icon.id", icon.getId());
		if (functions.size() > 0) {
			for (Function tsFunction : functions) {
				tsFunction.setIcon(null);
				systemService.saveOrUpdate(tsFunction);
			}
		}
		List<Operation> operations = systemService.findAllByProperty(Operation.class, "Icon.id", icon.getId());
		if (operations.size() > 0) {
			for (Operation tsOperation : operations) {
				tsOperation.setIcon(null);
				systemService.saveOrUpdate(tsOperation);
			}
		}
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
