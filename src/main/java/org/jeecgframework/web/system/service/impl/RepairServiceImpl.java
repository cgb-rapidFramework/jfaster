package org.jeecgframework.web.system.service.impl;

import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.LogUtils;
import org.jeecgframework.web.system.constant.core.TemplateConstant;
import org.jeecgframework.web.system.entity.base.*;
import org.jeecgframework.web.system.entity.core.TemplateEntity;
import org.jeecgframework.web.system.service.MutiLangService;
import org.jeecgframework.web.system.service.RepairService;
import org.jeecgframework.web.system.service.UserService;
import org.jeecgframework.web.utils.ConfigUtils;
import org.jeecgframework.web.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.List;

/**
 * @Description 修复数据库Service
 * @ClassName: RepairService
 * @author tanghan
 * @date 2013-7-19 下午01:31:00
 */
@Service("repairService")
@Transactional
public class RepairServiceImpl extends CommonServiceImpl implements RepairService {

	/**
	 * @Description 先清空数据库，然后再修复数据库
	 * @author tanghan 2013-7-19
	 */

	@Autowired
	private MutiLangService mutiLangService;

	public void deleteAndRepair() {
		// 由于表中有主外键关系，清空数据库需注意
		commonDao.executeHql("delete TSLog");
		commonDao.executeHql("delete TSOperation");
		commonDao.executeHql("delete TSRoleFunction");
		commonDao.executeHql("delete TSRoleUser");
		commonDao.executeHql("delete TSUser");
		commonDao.executeHql("delete TSBaseUser");
		commonDao.executeHql("update TSFunction ts set ts.TSFunction = null");
		commonDao.executeHql("delete TSFunction");
		commonDao.executeHql("update TSDepart t set t.TSPDepart = null");
		commonDao.executeHql("delete TSDepart");
		commonDao.executeHql("delete TSIcon");
		commonDao.executeHql("delete TSRole");
		commonDao.executeHql("delete TSType");
		commonDao.executeHql("delete TSTypegroup");
		commonDao.executeHql("update TSDemo t set t.TSDemo = null");
		commonDao.executeHql("delete TSDemo");
		commonDao.executeHql("delete TSTimeTaskEntity");
		commonDao.executeHql("update TSTerritory t set t.TSTerritory = null");
		commonDao.executeHql("delete TSTerritory");
		commonDao.executeHql("delete TemplateEntity");
		commonDao.executeHql("delete MutiLangEntity");
		repair();
	}

	/**
	 * @Description 修复数据库
	 * @author tanghan 2013-7-19
	 */

	synchronized public void repair() {
		repaireIcon(); // 修复图标
		repairDepart();// 修复部门表
		repairRole();// 修复角色
		repairUser(); // 修复基本用户
		repairUserRole();// 修复用户和角色的关系
		repairTypeAndGroup();// 修复字典类型
		repairType();// 修复字典值
		repairTask();// 修复任务管理
		repairLog();// 修复日志表
		repairMenu();// 修复菜单权限
		repairOperation(); // 修复操作表
		repairRoleFunction();// 修复角色和权限的关系
		repairTemplate();// 修复模版
		repairMutilang();// 修复多国语言
		repairTerritory();// 修复地域
	}

	private void repairTerritory() {
		try {
			ClassPathResource sqlFile = new ClassPathResource("sql/repair/RepairDao_batchRepairTerritory.sql");
			//InputStream sqlFile = getClass().getResourceAsStream("../../sql/repair/RepairDao_batchRepairTerritory.sql");
			InputStreamReader isr = null;
			isr = new InputStreamReader(sqlFile.getInputStream(),"utf-8");
			BufferedReader br = new BufferedReader(isr);
			String str = "";
			while ((str = br.readLine()) != null) {
				commonDao.updateBySql(str);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void repairMutilang() {
		try {
			ClassPathResource sqlFile = new ClassPathResource("sql/repair/RepairDao_batchRepairMutilang.sql");
			//InputStream sqlFile = getClass().getResourceAsStream("../../sql/repair/RepairDao_batchRepairMutilang.sql");
			InputStreamReader isr = null;
			isr = new InputStreamReader(sqlFile.getInputStream(),"utf-8");
			BufferedReader br = new BufferedReader(isr);
			String str = "";
			while ((str = br.readLine()) != null) {
				commonDao.updateBySql(str);
			}
			mutiLangService.refleshMutiLangCach();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void repairTemplate() {
		TemplateEntity templateEntity = new TemplateEntity();
		templateEntity.setStatus(TemplateConstant.TEMPLATE_STATUS_IS_UNAVAILABLE);
		templateEntity.setCode("经典风格");
		templateEntity.setImage("resource/image/template/default.png");
		templateEntity.setName("经典风格");
		commonDao.save(templateEntity);

		templateEntity = new TemplateEntity();
		templateEntity.setStatus(TemplateConstant.TEMPLATE_STATUS_IS_UNAVAILABLE);
		templateEntity.setCode("shortcut");
		templateEntity.setImage("resource/image/template/shortcut.png");
		templateEntity.setName("shortcut风格");
		commonDao.save(templateEntity);

		templateEntity = new TemplateEntity();
		templateEntity.setStatus(TemplateConstant.TEMPLATE_STATUS_IS_UNAVAILABLE);
		templateEntity.setCode("sliding");
		templateEntity.setImage("resource/image/template/sliding.png");
		templateEntity.setName("Sliding云桌面");
		commonDao.save(templateEntity);

		templateEntity = new TemplateEntity();
		templateEntity.setStatus(TemplateConstant.TEMPLATE_STATUS_IS_AVAILABLE);
		templateEntity.setCode("ace");
		templateEntity.setImage("resource/image/template/ace.png");
		templateEntity.setName("ACE平面风格");
		commonDao.save(templateEntity);

		templateEntity = new TemplateEntity();
		templateEntity.setStatus(TemplateConstant.TEMPLATE_STATUS_IS_UNAVAILABLE);
		templateEntity.setCode("bootstrap");
		templateEntity.setImage("resource/image/template/bootstrap.png");
		templateEntity.setName("bootstrap风格");
		commonDao.save(templateEntity);
	}

	/**
	 * 修复任务管理
	 *
	 * @author JueYue
	 * @serialData 2013年11月5日
	 */
	private void repairTask() {
		TSTimeTaskEntity task = new TSTimeTaskEntity();
		task.setTaskId("taskDemoServiceTaskCronTrigger");
		task.setTaskDescribe("测试taskDemo");
		task.setCronExpression("0 0/1 * * * ?");
		task.setIsEffect("0");
		task.setIsStart("0");
		commonDao.saveOrUpdate(task);
	}

	/**
	 * @Description 修复日志表
	 * @author tanghan 2013-7-28
	 */
	private void repairLog() {
		TSUser admin = commonDao.findAllByProperty(TSUser.class, "signatureFile", "images/renfang/qm/licf.gif").get(0);
		try {
			TSLog log1 = new TSLog();
			log1.setLogcontent("用户: admin登录成功");
			log1.setBroswer("Chrome");
			log1.setNote("169.254.200.136");
			log1.setTSUser(admin);
			log1.setOperatetime(DateUtils.parseTimestamp("2015-04-25 16:22:40", "yyyy-MM-dd HH:mm"));
			log1.setOperatetype((short) 1);
			log1.setLoglevel((short) 1);
			commonDao.saveOrUpdate(log1);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @Description 修复部门表
	 * @author tanghan 2013-7-20
	 */
	private void repairDepart() {
		TSDepart eiu = new TSDepart();
		eiu.setDepartname("系统管理");
		eiu.setDescription("12");
		commonDao.saveOrUpdate(eiu);
	}

	/**
	 * @Description 修复User表
	 * @author tanghan 2013-7-23
	 */
	private void repairUser() {
		this.commonDao.getSession().clear();
		TSDepart eiu = this.commonDao.findAllByProperty(TSDepart.class, "departname", "系统管理").get(0);

		TSUser admin = new TSUser();
		admin.setSignatureFile("images/renfang/qm/licf.gif");
		admin.setStatus((short) 1);
		admin.setRealName("管理员");
		admin.setUserName("admin");
		admin.setPassword("c44b01947c9e6e3f");
		admin.setActivitiSync((short) 1);
		commonDao.save(admin);

		TSUserOrg adminUserOrg = new TSUserOrg();
		adminUserOrg.setTsUser(admin);
		adminUserOrg.setTsDepart(eiu);
		commonDao.save(adminUserOrg);

		TSUser scott = new TSUser();
		scott.setEmail("guanxf_m@126.com");
		scott.setStatus((short) 1);
		scott.setRealName("scott");
		scott.setUserName("scott");
		scott.setPassword("97c07a884bf272b5");
		// scott.setTSDepart(RAndD);
		scott.setActivitiSync((short) 0);
		commonDao.saveOrUpdate(scott);
		TSUserOrg scottUserOrg = new TSUserOrg();
		scottUserOrg.setTsUser(scott);
		scottUserOrg.setTsDepart(eiu);
		commonDao.save(scottUserOrg);

	}

	/**
	 * @Description 修复用户角色表
	 * @author tanghan 2013-7-23
	 */
	private void repairUserRole() {
		TSRole admin = commonDao.findAllByProperty(TSRole.class, "roleCode", "admin").get(0);
		TSRole manager = commonDao.findAllByProperty(TSRole.class, "roleCode", "manager").get(0);
		List<TSUser> user = commonDao.findAll(TSUser.class);
		for (int i = 0; i < user.size(); i++) {
			if (user.get(i).getEmail() != null) {
				TSRoleUser roleuser = new TSRoleUser();
				roleuser.setTSUser(user.get(i));
				roleuser.setTSRole(manager);
				commonDao.saveOrUpdate(roleuser);
			} else {
				TSRoleUser roleuser = new TSRoleUser();
				roleuser.setTSUser(user.get(i));
				roleuser.setTSRole(admin);
				commonDao.saveOrUpdate(roleuser);
			}
			if (user.get(i).getSignatureFile() != null) {
				TSRoleUser roleuser = new TSRoleUser();
				roleuser.setTSUser(user.get(i));
				roleuser.setTSRole(admin);
				commonDao.saveOrUpdate(roleuser);
			}
		}

	}

	/**
	 * @Description 修复角色权限表
	 * @author tanghan 2013-7-23
	 */
	private void repairRoleFunction() {
		TSRole admin = commonDao.findAllByProperty(TSRole.class, "roleCode", "admin").get(0);
		TSRole manager = commonDao.findAllByProperty(TSRole.class, "roleCode", "manager").get(0);
		List<TSFunction> list = commonDao.findAll(TSFunction.class);
		for (int i = 0; i < list.size(); i++) {
			TSRoleFunction adminroleFunction = new TSRoleFunction();
			TSRoleFunction managerFunction = new TSRoleFunction();
			adminroleFunction.setTSFunction(list.get(i));
			managerFunction.setTSFunction(list.get(i));
			adminroleFunction.setTSRole(admin);
			managerFunction.setTSRole(manager);
			commonDao.saveOrUpdate(adminroleFunction);
			commonDao.saveOrUpdate(managerFunction);
		}
	}

	/**
	 * @Description 修复操作按钮表
	 * @author tanghan 2013-7-23
	 */
	private void repairOperation() {
		TSIcon back = commonDao.findAllByProperty(TSIcon.class, "iconName", "返回").get(0);
		TSFunction function = commonDao.findAllByProperty(TSFunction.class, "functionName", "系统管理").get(0);

		TSOperation add = new TSOperation();
		add.setOperationname("录入");
		add.setOperationcode("add");
		add.setTSIcon(back);
		add.setTSFunction(function);
		commonDao.saveOrUpdate(add);

		TSOperation edit = new TSOperation();
		edit.setOperationname("编辑");
		edit.setOperationcode("edit");
		edit.setTSIcon(back);
		edit.setTSFunction(function);
		commonDao.saveOrUpdate(edit);

		TSOperation del = new TSOperation();
		del.setOperationname("删除");
		del.setOperationcode("del");
		del.setTSIcon(back);
		del.setTSFunction(function);
		commonDao.saveOrUpdate(del);

		TSOperation szqm = new TSOperation();
		szqm.setOperationname("审核");
		szqm.setOperationcode("szqm");
		szqm.setTSIcon(back);
		szqm.setTSFunction(function);
		commonDao.saveOrUpdate(szqm);
	}

	/**
	 * @Description 修复类型分组表
	 * @author tanghan 2013-7-20
	 */
	private void repairTypeAndGroup() {
		TSTypegroup icontype = new TSTypegroup();
		icontype.setTypegroupname("图标类型");
		icontype.setTypegroupcode("icontype");
		commonDao.saveOrUpdate(icontype);

		TSTypegroup ordertype = new TSTypegroup();
		ordertype.setTypegroupname("订单类型");
		ordertype.setTypegroupcode("order");
		commonDao.saveOrUpdate(ordertype);

		TSTypegroup custom = new TSTypegroup();
		custom.setTypegroupname("客户类型");
		custom.setTypegroupcode("custom");
		commonDao.saveOrUpdate(custom);

		TSTypegroup servicetype = new TSTypegroup();
		servicetype.setTypegroupname("服务项目类型");
		servicetype.setTypegroupcode("service");
		commonDao.saveOrUpdate(servicetype);

		TSTypegroup searchMode = new TSTypegroup();
		searchMode.setTypegroupname("查询模式");
		searchMode.setTypegroupcode("searchmode");
		commonDao.saveOrUpdate(searchMode);

		TSTypegroup yesOrno = new TSTypegroup();
		yesOrno.setTypegroupname("逻辑条件");
		yesOrno.setTypegroupcode("yesorno");
		commonDao.saveOrUpdate(yesOrno);

		TSTypegroup fieldtype = new TSTypegroup();
		fieldtype.setTypegroupname("字段类型");
		fieldtype.setTypegroupcode("fieldtype");
		commonDao.saveOrUpdate(fieldtype);

		TSTypegroup datatable = new TSTypegroup();
		datatable.setTypegroupname("数据表");
		datatable.setTypegroupcode("database");
		commonDao.saveOrUpdate(datatable);

		TSTypegroup filetype = new TSTypegroup();
		filetype.setTypegroupname("文档分类");
		filetype.setTypegroupcode("fieltype");
		commonDao.saveOrUpdate(filetype);

		TSTypegroup sex = new TSTypegroup();
		sex.setTypegroupname("性别类");
		sex.setTypegroupcode("sex");
		commonDao.saveOrUpdate(sex);
	}

	/**
	 * @Description 修复类型表
	 * @author tanghan 2013-7-22
	 */
	private void repairType() {
		TSTypegroup icontype = commonDao.findAllByProperty(TSTypegroup.class, "typegroupname", "图标类型").get(0);
		TSTypegroup ordertype = commonDao.findAllByProperty(TSTypegroup.class, "typegroupname", "订单类型").get(0);
		TSTypegroup custom = commonDao.findAllByProperty(TSTypegroup.class, "typegroupname", "客户类型").get(0);
		TSTypegroup servicetype = commonDao.findAllByProperty(TSTypegroup.class, "typegroupname", "服务项目类型").get(0);
		TSTypegroup datatable = commonDao.findAllByProperty(TSTypegroup.class, "typegroupname", "数据表").get(0);
		TSTypegroup filetype = commonDao.findAllByProperty(TSTypegroup.class, "typegroupname", "文档分类").get(0);
		TSTypegroup sex = commonDao.findAllByProperty(TSTypegroup.class, "typegroupname", "性别类").get(0);
		TSTypegroup searchmode = commonDao.findAllByProperty(TSTypegroup.class, "typegroupname", "查询模式").get(0);
		TSTypegroup yesorno = commonDao.findAllByProperty(TSTypegroup.class, "typegroupname", "逻辑条件").get(0);
		TSTypegroup fieldtype = commonDao.findAllByProperty(TSTypegroup.class, "typegroupname", "字段类型").get(0);

		TSType menu = new TSType();
		menu.setTypename("菜单图标");
		menu.setTypecode("2");
		menu.setTSTypegroup(icontype);
		commonDao.saveOrUpdate(menu);

		TSType systemicon = new TSType();
		systemicon.setTypename("系统图标");
		systemicon.setTypecode("1");
		systemicon.setTSTypegroup(icontype);
		commonDao.saveOrUpdate(systemicon);

		TSType file = new TSType();
		file.setTypename("附件");
		file.setTypecode("files");
		file.setTSTypegroup(filetype);
		commonDao.saveOrUpdate(file);

		TSType goodorder = new TSType();
		goodorder.setTypename("优质订单");
		goodorder.setTypecode("1");
		goodorder.setTSTypegroup(ordertype);
		commonDao.saveOrUpdate(goodorder);

		TSType general = new TSType();
		general.setTypename("普通订单");
		general.setTypecode("2");
		general.setTSTypegroup(ordertype);
		commonDao.saveOrUpdate(general);

		TSType sign = new TSType();
		sign.setTypename("签约客户");
		sign.setTypecode("1");
		sign.setTSTypegroup(custom);
		commonDao.saveOrUpdate(sign);

		TSType commoncustom = new TSType();
		commoncustom.setTypename("普通客户");
		commoncustom.setTypecode("2");
		commoncustom.setTSTypegroup(custom);
		commonDao.saveOrUpdate(commoncustom);

		TSType vipservice = new TSType();
		vipservice.setTypename("特殊服务");
		vipservice.setTypecode("1");
		vipservice.setTSTypegroup(servicetype);
		commonDao.saveOrUpdate(vipservice);

		TSType commonservice = new TSType();
		commonservice.setTypename("普通服务");
		commonservice.setTypecode("2");
		commonservice.setTSTypegroup(servicetype);
		commonDao.saveOrUpdate(commonservice);

		TSType single = new TSType();
		single.setTypename("单条件查询");
		single.setTypecode("single");
		single.setTSTypegroup(searchmode);
		commonDao.saveOrUpdate(single);

		TSType group = new TSType();
		group.setTypename("范围查询");
		group.setTypecode("group");
		group.setTSTypegroup(searchmode);
		commonDao.saveOrUpdate(group);

		TSType yes = new TSType();
		yes.setTypename("是");
		yes.setTypecode("Y");
		yes.setTSTypegroup(yesorno);
		commonDao.saveOrUpdate(yes);

		TSType no = new TSType();
		no.setTypename("否");
		no.setTypecode("N");
		no.setTSTypegroup(yesorno);
		commonDao.saveOrUpdate(no);

		TSType type_integer = new TSType();
		type_integer.setTypename("Integer");
		type_integer.setTypecode("Integer");
		type_integer.setTSTypegroup(fieldtype);
		commonDao.saveOrUpdate(type_integer);

		TSType type_date = new TSType();
		type_date.setTypename("Date");
		type_date.setTypecode("Date");
		type_date.setTSTypegroup(fieldtype);
		commonDao.saveOrUpdate(type_date);

		TSType type_string = new TSType();
		type_string.setTypename("String");
		type_string.setTypecode("String");
		type_string.setTSTypegroup(fieldtype);
		commonDao.saveOrUpdate(type_string);

		TSType type_long = new TSType();
		type_long.setTypename("Long");
		type_long.setTypecode("Long");
		type_long.setTSTypegroup(fieldtype);
		commonDao.saveOrUpdate(type_long);

		TSType systable = new TSType();
		systable.setTypename("系统基础表");
		systable.setTypecode("t_s");
		systable.setTSTypegroup(datatable);
		commonDao.saveOrUpdate(systable);

		TSType business = new TSType();
		business.setTypename("业务表");
		business.setTypecode("t_b");
		business.setTSTypegroup(datatable);
		commonDao.saveOrUpdate(business);

		TSType news = new TSType();
		news.setTypename("新闻");
		news.setTypecode("news");
		news.setTSTypegroup(filetype);
		commonDao.saveOrUpdate(news);

		TSType man = new TSType();
		man.setTypename("男性");
		man.setTypecode("0");
		man.setTSTypegroup(sex);
		commonDao.saveOrUpdate(man);

		TSType woman = new TSType();
		woman.setTypename("女性");
		woman.setTypecode("1");
		woman.setTSTypegroup(sex);
		commonDao.saveOrUpdate(woman);
	}

	/**
	 * @Description 修复角色表
	 * @author tanghan 2013-7-20
	 */
	private void repairRole() {
		TSRole admin = new TSRole();
		admin.setRoleName("管理员");
		admin.setRoleCode("admin");
		commonDao.saveOrUpdate(admin);

		TSRole manager = new TSRole();
		manager.setRoleName("普通用户");
		manager.setRoleCode("manager");
		commonDao.saveOrUpdate(manager);

	}

	/**
	 * @Description 修复图标表
	 * @author tanghan 2013-7-19
	 */
	private void repaireIcon() {
		LogUtils.info("修复图标中");

		TSIcon defaultIcon = new TSIcon();
		defaultIcon.setIconName("默认图");
		defaultIcon.setIconType((short) 1);
		defaultIcon.setIconPath("plug-in/accordion/images/default.png");
		defaultIcon.setIconClas("default");
		defaultIcon.setExtend("png");
		commonDao.saveOrUpdate(defaultIcon);

		TSIcon back = new TSIcon();
		back.setIconName("返回");
		back.setIconType((short) 1);
		back.setIconPath("plug-in/accordion/images/back.png");
		back.setIconClas("back");
		back.setExtend("png");
		commonDao.saveOrUpdate(back);

		TSIcon pie = new TSIcon();

		pie.setIconName("饼图");
		pie.setIconType((short) 1);
		pie.setIconPath("plug-in/accordion/images/pie.png");
		pie.setIconClas("pie");
		pie.setExtend("png");
		commonDao.saveOrUpdate(pie);

		TSIcon pictures = new TSIcon();
		pictures.setIconName("图片");
		pictures.setIconType((short) 1);
		pictures.setIconPath("plug-in/accordion/images/pictures.png");
		pictures.setIconClas("pictures");
		pictures.setExtend("png");
		commonDao.saveOrUpdate(pictures);

		TSIcon pencil = new TSIcon();
		pencil.setIconName("笔");
		pencil.setIconType((short) 1);
		pencil.setIconPath("plug-in/accordion/images/pencil.png");
		pencil.setIconClas("pencil");
		pencil.setExtend("png");
		commonDao.saveOrUpdate(pencil);

		TSIcon map = new TSIcon();
		map.setIconName("地图");
		map.setIconType((short) 1);
		map.setIconPath("plug-in/accordion/images/map.png");
		map.setIconClas("map");
		map.setExtend("png");
		commonDao.saveOrUpdate(map);

		TSIcon group_add = new TSIcon();
		group_add.setIconName("组");
		group_add.setIconType((short) 1);
		group_add.setIconPath("plug-in/accordion/images/group_add.png");
		group_add.setIconClas("group_add");
		group_add.setExtend("png");
		commonDao.saveOrUpdate(group_add);

		TSIcon calculator = new TSIcon();
		calculator.setIconName("计算器");
		calculator.setIconType((short) 1);
		calculator.setIconPath("plug-in/accordion/images/calculator.png");
		calculator.setIconClas("calculator");
		calculator.setExtend("png");
		commonDao.saveOrUpdate(calculator);

		TSIcon folder = new TSIcon();
		folder.setIconName("文件夹");
		folder.setIconType((short) 1);
		folder.setIconPath("plug-in/accordion/images/folder.png");
		folder.setIconClas("folder");
		folder.setExtend("png");
		commonDao.saveOrUpdate(folder);
	}

	/**
	 * 修复桌面默认图标
	 *
	 * @param iconName
	 *            图标名称
	 * @param iconLabelName
	 *            图标展示名称
	 * @return 图标实体
	 */
	private TSIcon repairInconForDesk(String iconName, String iconLabelName) {
		String iconPath = "plug-in/sliding/icon/" + iconName + ".png";
		TSIcon deskIncon = new TSIcon();
		deskIncon.setIconName(iconLabelName);
		deskIncon.setIconType((short) 3);
		deskIncon.setIconPath(iconPath);
		deskIncon.setIconClas("deskIcon");
		deskIncon.setExtend("png");
		commonDao.saveOrUpdate(deskIncon);

		return deskIncon;
	}

	/**
	 * 修复桌面默认图标
	 *
	 * @return 图标实体
	 */
	private TSIcon getDefaultInconForDesk() {
		String iconPath = "plug-in/sliding/icon/default.png";
		TSIcon deskIncon = new TSIcon();
		deskIncon.setIconName("默认图标");
		deskIncon.setIconType((short) 3);
		deskIncon.setIconPath(iconPath);
		deskIncon.setIconClas("deskIcon");
		deskIncon.setExtend("png");
		commonDao.saveOrUpdate(deskIncon);

		return deskIncon;
	}

	/**
	 * @Description 修复菜单权限
	 * @author tanghan 2013-7-19
	 */
	private void repairMenu() {
		TSIcon defaultIcon = commonDao.findAllByProperty(TSIcon.class, "iconName", "默认图").get(0);
		TSIcon group_add = commonDao.findAllByProperty(TSIcon.class, "iconName", "组").get(0);
		TSIcon pie = commonDao.findAllByProperty(TSIcon.class, "iconName", "饼图").get(0);
		TSIcon folder = commonDao.findAllByProperty(TSIcon.class, "iconName", "文件夹").get(0);

		TSFunction sys = new TSFunction();
		sys.setFunctionName("系统管理");
		sys.setFunctionUrl("");
		sys.setFunctionLevel((short) 0);
		sys.setFunctionOrder("5");
		sys.setTSIconDesk(getDefaultInconForDesk());
		sys.setTSIcon(group_add);
		commonDao.saveOrUpdate(sys);

		TSFunction state = new TSFunction();
		state.setFunctionName("统计查询");
		state.setFunctionUrl("");
		state.setFunctionLevel((short) 0);
		state.setFunctionOrder("3");
		state.setTSIcon(folder);
		state.setTSIconDesk(getDefaultInconForDesk());
		commonDao.saveOrUpdate(state);

		TSFunction syscontrol = new TSFunction();
		syscontrol.setFunctionName("系统监控");
		syscontrol.setFunctionUrl("");
		syscontrol.setFunctionLevel((short) 0);
		syscontrol.setFunctionOrder("11");
		syscontrol.setTSIcon(defaultIcon);
		syscontrol.setTSIconDesk(getDefaultInconForDesk());
		commonDao.saveOrUpdate(syscontrol);

		TSFunction user = new TSFunction();
		user.setFunctionName("用户管理");
		user.setFunctionUrl("userController.do?user");
		user.setFunctionLevel((short) 1);
		user.setFunctionOrder("5");
		user.setTSFunction(sys);
		user.setTSIcon(defaultIcon);
		user.setTSIconDesk(repairInconForDesk("Finder", "用户管理"));
		commonDao.saveOrUpdate(user);

		TSFunction role = new TSFunction();
		role.setFunctionName("角色管理");
		role.setFunctionUrl("roleController.do?role");
		role.setFunctionLevel((short) 1);
		role.setFunctionOrder("6");
		role.setTSFunction(sys);
		role.setTSIcon(defaultIcon);
		role.setTSIconDesk(repairInconForDesk("friendgroup", "角色管理"));
		commonDao.saveOrUpdate(role);

		TSFunction menu = new TSFunction();
		menu.setFunctionName("菜单管理");
		menu.setFunctionUrl("functionController.do?function");
		menu.setFunctionLevel((short) 1);
		menu.setFunctionOrder("7");
		menu.setTSFunction(sys);
		menu.setTSIcon(defaultIcon);
		menu.setTSIconDesk(repairInconForDesk("kaikai", "菜单管理"));
		commonDao.saveOrUpdate(menu);

		TSFunction typegroup = new TSFunction();
		typegroup.setFunctionName("数据字典");
		typegroup.setFunctionUrl("systemController.do?typeGroupList");
		typegroup.setFunctionLevel((short) 1);
		typegroup.setFunctionOrder("6");
		typegroup.setTSFunction(sys);
		typegroup.setTSIcon(defaultIcon);
		typegroup.setTSIconDesk(repairInconForDesk("friendnear", "数据字典"));
		commonDao.saveOrUpdate(typegroup);

		TSFunction icon = new TSFunction();
		icon.setFunctionName("图标管理");
		icon.setFunctionUrl("iconController.do?icon");
		icon.setFunctionLevel((short) 1);
		icon.setFunctionOrder("18");
		icon.setTSFunction(sys);
		icon.setTSIcon(defaultIcon);
		icon.setTSIconDesk(repairInconForDesk("kxjy", "图标管理"));
		commonDao.saveOrUpdate(icon);

		TSFunction depart = new TSFunction();
		depart.setFunctionName("部门管理");
		depart.setFunctionUrl("departController.do?depart");
		depart.setFunctionLevel((short) 1);
		depart.setFunctionOrder("22");
		depart.setTSFunction(sys);
		depart.setTSIcon(defaultIcon);
		depart.setTSIconDesk(getDefaultInconForDesk());
		commonDao.saveOrUpdate(depart);

		TSFunction territory = new TSFunction();
		territory.setFunctionName("地域管理");
		territory.setFunctionUrl("territoryController.do?territory");
		territory.setFunctionLevel((short) 1);
		territory.setFunctionOrder("22");
		territory.setTSFunction(sys);
		territory.setTSIcon(pie);
		territory.setTSIconDesk(getDefaultInconForDesk());
		commonDao.saveOrUpdate(territory);

		TSFunction language = new TSFunction();
		language.setFunctionName("语言管理");
		language.setFunctionUrl("mutiLangController.do?mutiLang");
		language.setFunctionLevel((short) 1);
		language.setFunctionOrder("30");
		language.setTSFunction(sys);
		language.setTSIcon(pie);
		language.setTSIconDesk(getDefaultInconForDesk());
		commonDao.saveOrUpdate(language);

		TSFunction template = new TSFunction();
		template.setFunctionName("模版管理");
		template.setFunctionUrl("templateController.do?template");
		template.setFunctionLevel((short) 1);
		template.setFunctionOrder("28");
		template.setTSFunction(sys);
		template.setTSIcon(pie);
		template.setTSIconDesk(getDefaultInconForDesk());
		commonDao.saveOrUpdate(template);

		TSFunction useranalyse = new TSFunction();
		useranalyse.setFunctionName("用户分析");
		useranalyse.setFunctionUrl("logController.do?statisticTabs&isIframe");
		useranalyse.setFunctionLevel((short) 1);
		useranalyse.setFunctionOrder("17");
		useranalyse.setTSFunction(state);
		useranalyse.setTSIcon(pie);
		useranalyse.setTSIconDesk(repairInconForDesk("User", "用户分析"));
		commonDao.saveOrUpdate(useranalyse);

		TSFunction druid = new TSFunction();
		druid.setFunctionName("数据监控");
		druid.setFunctionUrl("dataSourceController.do?goDruid&isIframe");
		druid.setFunctionLevel((short) 1);
		druid.setFunctionOrder("11");
		druid.setTSFunction(syscontrol);
		druid.setTSIcon(defaultIcon);
		druid.setTSIconDesk(repairInconForDesk("Super Disk", "数据监控"));
		commonDao.saveOrUpdate(druid);

		TSFunction log = new TSFunction();
		log.setFunctionName("系统日志");
		log.setFunctionUrl("logController.do?log");
		log.setFunctionLevel((short) 1);
		log.setFunctionOrder("21");
		log.setTSFunction(syscontrol);
		log.setTSIcon(defaultIcon);
		log.setTSIconDesk(repairInconForDesk("fastsearch", "系统日志"));
		commonDao.saveOrUpdate(log);

		TSFunction timeTask = new TSFunction();
		timeTask.setFunctionName("定时任务");
		timeTask.setFunctionUrl("timeTaskController.do?timeTask");
		timeTask.setFunctionLevel((short) 1);
		timeTask.setFunctionOrder("21");
		timeTask.setTSFunction(syscontrol);
		timeTask.setTSIcon(defaultIcon);
		timeTask.setTSIconDesk(repairInconForDesk("Utilities", "定时任务"));
		commonDao.saveOrUpdate(timeTask);

		TSFunction reportdemo = new TSFunction();
		reportdemo.setFunctionName("报表示例");
		reportdemo.setFunctionUrl("reportDemoController.do?studentStatisticTabs&isIframe");
		reportdemo.setFunctionLevel((short) 1);
		reportdemo.setFunctionOrder("21");
		reportdemo.setTSFunction(state);
		reportdemo.setTSIcon(pie);
		reportdemo.setTSIconDesk(getDefaultInconForDesk());
		commonDao.saveOrUpdate(reportdemo);

	}
}
