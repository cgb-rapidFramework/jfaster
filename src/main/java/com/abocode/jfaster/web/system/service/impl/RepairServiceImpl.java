package com.abocode.jfaster.web.system.service.impl;

import com.abocode.jfaster.core.common.service.impl.CommonServiceImpl;
import com.abocode.jfaster.web.system.service.RepairService;
import com.abocode.jfaster.web.utils.DateUtils;
import com.abocode.jfaster.core.util.LogUtils;
import com.abocode.jfaster.web.system.service.MutiLangService;
import com.abocode.jfaster.web.system.entity.*;
import com.abocode.jfaster.core.util.StreamUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author tanghan
 * @Description 修复数据库Service
 * @ClassName: RepairService
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
//		commonDao.executeHql("update TSDemo t set t.TSDemo = null");
//		commonDao.executeHql("delete TSDemo");
		commonDao.executeHql("delete JobEntity");
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
        repairJob();// 修复任务管理
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
            String str= StreamUtils.inputStreamTOString(sqlFile.getInputStream());
            commonDao.updateBySql(str);
        } catch (Exception e) {
            LogUtils.error(e.getMessage());
        }
    }

    private void repairMutilang() {
        try {
            ClassPathResource sqlFile = new ClassPathResource("sql/repair/RepairDao_batchRepairMutilang.sql");
            String str= StreamUtils.inputStreamTOString(sqlFile.getInputStream());
            commonDao.updateBySql(str);
            mutiLangService.refleshMutiLangCach();
        } catch (Exception e) {
            LogUtils.error(e.getMessage());
        }
    }

    private void repairTemplate() {
        try {
            ClassPathResource sqlFile = new ClassPathResource("sql/repair/RepairDao_batchRepairTemplate.sql");
            String str= StreamUtils.inputStreamTOString(sqlFile.getInputStream());
            commonDao.updateBySql(str);
        } catch (Exception e) {
            LogUtils.error(e.getMessage());
        }
    }

    /**
     * 修复任务管理
     *
     * @author JueYue
     * @serialData 2013年11月5日
     */
    private void repairJob() {
        Job job = new Job();
        job.setName("testjob1");
        job.setGroup("default");
        job.setExpression("0 0/5 * * * ?");
        job.setDescription("测试job1");
        job.setStatus("2");
        job.setClazz("DemoJob");
        commonDao.saveOrUpdate(job);
    }

    /**
     * @Description 修复日志表
     * @author tanghan 2013-7-28
     */
    private void repairLog() {
        User admin = commonDao.findAllByProperty(User.class, "signatureFile", "images/renfang/qm/licf.gif").get(0);
        Log log1 = new Log();
        log1.setLogcontent("用户: admin登录成功");
        log1.setBroswer("Chrome");
        log1.setNote("169.254.200.136");
        log1.setTSUser(admin);
        log1.setOperatetime(DateUtils.getTimestamp());
        log1.setOperatetype((short) 1);
        log1.setLoglevel((short) 1);
        commonDao.saveOrUpdate(log1);
    }

    /**
     * @Description 修复部门表
     * @author tanghan 2013-7-20
     */
    private void repairDepart() {
        Depart eiu = new Depart();
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
        Depart eiu = this.commonDao.findAllByProperty(Depart.class, "departname", "系统管理").get(0);

        User admin = new User();
        admin.setSignatureFile("images/renfang/qm/licf.gif");
        admin.setStatus((short) 1);
        admin.setRealName("管理员");
        admin.setUserName("admin");
        admin.setPassword("c44b01947c9e6e3f");
        commonDao.save(admin);

        UserOrg adminUserOrg = new UserOrg();
        adminUserOrg.setTsUser(admin);
        adminUserOrg.setTsDepart(eiu);
        commonDao.save(adminUserOrg);

        User scott = new User();
        scott.setEmail("guanxf_m@126.com");
        scott.setStatus((short) 1);
        scott.setRealName("scott");
        scott.setUserName("scott");
        scott.setPassword("97c07a884bf272b5");
        // scott.setTSDepart(RAndD);
        commonDao.saveOrUpdate(scott);
        UserOrg scottUserOrg = new UserOrg();
        scottUserOrg.setTsUser(scott);
        scottUserOrg.setTsDepart(eiu);
        commonDao.save(scottUserOrg);

    }

    /**
     * @Description 修复用户角色表
     * @author tanghan 2013-7-23
     */
    private void repairUserRole() {
        Role admin = commonDao.findAllByProperty(Role.class, "roleCode", "admin").get(0);
        Role manager = commonDao.findAllByProperty(Role.class, "roleCode", "manager").get(0);
        List<User> user = commonDao.findAll(User.class);
        for (int i = 0; i < user.size(); i++) {
            if (user.get(i).getEmail() != null) {
                RoleUser roleuser = new RoleUser();
                roleuser.setTSUser(user.get(i));
                roleuser.setTSRole(manager);
                commonDao.saveOrUpdate(roleuser);
            } else {
                RoleUser roleuser = new RoleUser();
                roleuser.setTSUser(user.get(i));
                roleuser.setTSRole(admin);
                commonDao.saveOrUpdate(roleuser);
            }
            if (user.get(i).getSignatureFile() != null) {
                RoleUser roleuser = new RoleUser();
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
        Role admin = commonDao.findAllByProperty(Role.class, "roleCode", "admin").get(0);
        Role manager = commonDao.findAllByProperty(Role.class, "roleCode", "manager").get(0);
        List<Function> list = commonDao.findAll(Function.class);
        for (int i = 0; i < list.size(); i++) {
            RoleFunction adminroleFunction = new RoleFunction();
            RoleFunction managerFunction = new RoleFunction();
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
        Icon back = commonDao.findAllByProperty(Icon.class, "iconName", "返回").get(0);
        Function function = commonDao.findAllByProperty(Function.class, "functionName", "系统管理").get(0);

        Operation add = new Operation();
        add.setOperationname("录入");
        add.setOperationcode("add");
        add.setTSIcon(back);
        add.setTSFunction(function);
        commonDao.saveOrUpdate(add);

        Operation edit = new Operation();
        edit.setOperationname("编辑");
        edit.setOperationcode("edit");
        edit.setTSIcon(back);
        edit.setTSFunction(function);
        commonDao.saveOrUpdate(edit);

        Operation del = new Operation();
        del.setOperationname("删除");
        del.setOperationcode("del");
        del.setTSIcon(back);
        del.setTSFunction(function);
        commonDao.saveOrUpdate(del);

        Operation szqm = new Operation();
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
        TypeGroup icontype = new TypeGroup();
        icontype.setTypegroupname("图标类型");
        icontype.setTypegroupcode("icontype");
        commonDao.saveOrUpdate(icontype);

        TypeGroup ordertype = new TypeGroup();
        ordertype.setTypegroupname("订单类型");
        ordertype.setTypegroupcode("order");
        commonDao.saveOrUpdate(ordertype);

        TypeGroup custom = new TypeGroup();
        custom.setTypegroupname("客户类型");
        custom.setTypegroupcode("custom");
        commonDao.saveOrUpdate(custom);

        TypeGroup servicetype = new TypeGroup();
        servicetype.setTypegroupname("服务项目类型");
        servicetype.setTypegroupcode("service");
        commonDao.saveOrUpdate(servicetype);

        TypeGroup searchMode = new TypeGroup();
        searchMode.setTypegroupname("查询模式");
        searchMode.setTypegroupcode("searchmode");
        commonDao.saveOrUpdate(searchMode);

        TypeGroup yesOrno = new TypeGroup();
        yesOrno.setTypegroupname("逻辑条件");
        yesOrno.setTypegroupcode("yesorno");
        commonDao.saveOrUpdate(yesOrno);

        TypeGroup fieldtype = new TypeGroup();
        fieldtype.setTypegroupname("字段类型");
        fieldtype.setTypegroupcode("fieldtype");
        commonDao.saveOrUpdate(fieldtype);

        TypeGroup datatable = new TypeGroup();
        datatable.setTypegroupname("数据表");
        datatable.setTypegroupcode("database");
        commonDao.saveOrUpdate(datatable);

        TypeGroup filetype = new TypeGroup();
        filetype.setTypegroupname("文档分类");
        filetype.setTypegroupcode("fieltype");
        commonDao.saveOrUpdate(filetype);

        TypeGroup sex = new TypeGroup();
        sex.setTypegroupname("性别类");
        sex.setTypegroupcode("sex");
        commonDao.saveOrUpdate(sex);
    }

    /**
     * @Description 修复类型表
     * @author tanghan 2013-7-22
     */
    private void repairType() {
        TypeGroup icontype = commonDao.findAllByProperty(TypeGroup.class, "typegroupname", "图标类型").get(0);
        TypeGroup ordertype = commonDao.findAllByProperty(TypeGroup.class, "typegroupname", "订单类型").get(0);
        TypeGroup custom = commonDao.findAllByProperty(TypeGroup.class, "typegroupname", "客户类型").get(0);
        TypeGroup servicetype = commonDao.findAllByProperty(TypeGroup.class, "typegroupname", "服务项目类型").get(0);
        TypeGroup datatable = commonDao.findAllByProperty(TypeGroup.class, "typegroupname", "数据表").get(0);
        TypeGroup filetype = commonDao.findAllByProperty(TypeGroup.class, "typegroupname", "文档分类").get(0);
        TypeGroup sex = commonDao.findAllByProperty(TypeGroup.class, "typegroupname", "性别类").get(0);
        TypeGroup searchmode = commonDao.findAllByProperty(TypeGroup.class, "typegroupname", "查询模式").get(0);
        TypeGroup yesorno = commonDao.findAllByProperty(TypeGroup.class, "typegroupname", "逻辑条件").get(0);
        TypeGroup fieldtype = commonDao.findAllByProperty(TypeGroup.class, "typegroupname", "字段类型").get(0);

        Type menu = new Type();
        menu.setTypename("菜单图标");
        menu.setTypecode("2");
        menu.setTSTypegroup(icontype);
        commonDao.saveOrUpdate(menu);

        Type systemicon = new Type();
        systemicon.setTypename("系统图标");
        systemicon.setTypecode("1");
        systemicon.setTSTypegroup(icontype);
        commonDao.saveOrUpdate(systemicon);

        Type file = new Type();
        file.setTypename("附件");
        file.setTypecode("files");
        file.setTSTypegroup(filetype);
        commonDao.saveOrUpdate(file);

        Type goodorder = new Type();
        goodorder.setTypename("优质订单");
        goodorder.setTypecode("1");
        goodorder.setTSTypegroup(ordertype);
        commonDao.saveOrUpdate(goodorder);

        Type general = new Type();
        general.setTypename("普通订单");
        general.setTypecode("2");
        general.setTSTypegroup(ordertype);
        commonDao.saveOrUpdate(general);

        Type sign = new Type();
        sign.setTypename("签约客户");
        sign.setTypecode("1");
        sign.setTSTypegroup(custom);
        commonDao.saveOrUpdate(sign);

        Type commoncustom = new Type();
        commoncustom.setTypename("普通客户");
        commoncustom.setTypecode("2");
        commoncustom.setTSTypegroup(custom);
        commonDao.saveOrUpdate(commoncustom);

        Type vipservice = new Type();
        vipservice.setTypename("特殊服务");
        vipservice.setTypecode("1");
        vipservice.setTSTypegroup(servicetype);
        commonDao.saveOrUpdate(vipservice);

        Type commonservice = new Type();
        commonservice.setTypename("普通服务");
        commonservice.setTypecode("2");
        commonservice.setTSTypegroup(servicetype);
        commonDao.saveOrUpdate(commonservice);

        Type single = new Type();
        single.setTypename("单条件查询");
        single.setTypecode("single");
        single.setTSTypegroup(searchmode);
        commonDao.saveOrUpdate(single);

        Type group = new Type();
        group.setTypename("范围查询");
        group.setTypecode("group");
        group.setTSTypegroup(searchmode);
        commonDao.saveOrUpdate(group);

        Type yes = new Type();
        yes.setTypename("是");
        yes.setTypecode("Y");
        yes.setTSTypegroup(yesorno);
        commonDao.saveOrUpdate(yes);

        Type no = new Type();
        no.setTypename("否");
        no.setTypecode("N");
        no.setTSTypegroup(yesorno);
        commonDao.saveOrUpdate(no);

        Type type_integer = new Type();
        type_integer.setTypename("Integer");
        type_integer.setTypecode("Integer");
        type_integer.setTSTypegroup(fieldtype);
        commonDao.saveOrUpdate(type_integer);

        Type type_date = new Type();
        type_date.setTypename("Date");
        type_date.setTypecode("Date");
        type_date.setTSTypegroup(fieldtype);
        commonDao.saveOrUpdate(type_date);

        Type type_string = new Type();
        type_string.setTypename("String");
        type_string.setTypecode("String");
        type_string.setTSTypegroup(fieldtype);
        commonDao.saveOrUpdate(type_string);

        Type type_long = new Type();
        type_long.setTypename("Long");
        type_long.setTypecode("Long");
        type_long.setTSTypegroup(fieldtype);
        commonDao.saveOrUpdate(type_long);

        Type systable = new Type();
        systable.setTypename("系统基础表");
        systable.setTypecode("t_s");
        systable.setTSTypegroup(datatable);
        commonDao.saveOrUpdate(systable);

        Type business = new Type();
        business.setTypename("业务表");
        business.setTypecode("t_b");
        business.setTSTypegroup(datatable);
        commonDao.saveOrUpdate(business);

        Type news = new Type();
        news.setTypename("新闻");
        news.setTypecode("news");
        news.setTSTypegroup(filetype);
        commonDao.saveOrUpdate(news);

        Type man = new Type();
        man.setTypename("男性");
        man.setTypecode("0");
        man.setTSTypegroup(sex);
        commonDao.saveOrUpdate(man);

        Type woman = new Type();
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
        Role admin = new Role();
        admin.setRoleName("管理员");
        admin.setRoleCode("admin");
        commonDao.saveOrUpdate(admin);

        Role manager = new Role();
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

        Icon defaultIcon = new Icon();
        defaultIcon.setIconName("默认图");
        defaultIcon.setIconType((short) 1);
        defaultIcon.setIconPath("plug-in/accordion/images/default.png");
        defaultIcon.setIconClas("default");
        defaultIcon.setExtend("png");
        commonDao.saveOrUpdate(defaultIcon);

        Icon back = new Icon();
        back.setIconName("返回");
        back.setIconType((short) 1);
        back.setIconPath("plug-in/accordion/images/back.png");
        back.setIconClas("back");
        back.setExtend("png");
        commonDao.saveOrUpdate(back);

        Icon pie = new Icon();

        pie.setIconName("饼图");
        pie.setIconType((short) 1);
        pie.setIconPath("plug-in/accordion/images/pie.png");
        pie.setIconClas("pie");
        pie.setExtend("png");
        commonDao.saveOrUpdate(pie);

        Icon pictures = new Icon();
        pictures.setIconName("图片");
        pictures.setIconType((short) 1);
        pictures.setIconPath("plug-in/accordion/images/pictures.png");
        pictures.setIconClas("pictures");
        pictures.setExtend("png");
        commonDao.saveOrUpdate(pictures);

        Icon pencil = new Icon();
        pencil.setIconName("笔");
        pencil.setIconType((short) 1);
        pencil.setIconPath("plug-in/accordion/images/pencil.png");
        pencil.setIconClas("pencil");
        pencil.setExtend("png");
        commonDao.saveOrUpdate(pencil);

        Icon map = new Icon();
        map.setIconName("地图");
        map.setIconType((short) 1);
        map.setIconPath("plug-in/accordion/images/map.png");
        map.setIconClas("map");
        map.setExtend("png");
        commonDao.saveOrUpdate(map);

        Icon group_add = new Icon();
        group_add.setIconName("组");
        group_add.setIconType((short) 1);
        group_add.setIconPath("plug-in/accordion/images/group_add.png");
        group_add.setIconClas("group_add");
        group_add.setExtend("png");
        commonDao.saveOrUpdate(group_add);

        Icon calculator = new Icon();
        calculator.setIconName("计算器");
        calculator.setIconType((short) 1);
        calculator.setIconPath("plug-in/accordion/images/calculator.png");
        calculator.setIconClas("calculator");
        calculator.setExtend("png");
        commonDao.saveOrUpdate(calculator);

        Icon folder = new Icon();
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
     * @param iconName      图标名称
     * @param iconLabelName 图标展示名称
     * @return 图标实体
     */
    private Icon repairInconForDesk(String iconName, String iconLabelName) {
        String iconPath = "plug-in/sliding/icon/" + iconName + ".png";
        Icon deskIncon = new Icon();
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
    private Icon getDefaultInconForDesk() {
        String iconPath = "plug-in/sliding/icon/default.png";
        Icon deskIncon = new Icon();
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
        Icon defaultIcon = commonDao.findAllByProperty(Icon.class, "iconName", "默认图").get(0);
        Icon group_add = commonDao.findAllByProperty(Icon.class, "iconName", "组").get(0);
        Icon pie = commonDao.findAllByProperty(Icon.class, "iconName", "饼图").get(0);
        Icon folder = commonDao.findAllByProperty(Icon.class, "iconName", "文件夹").get(0);

        Function sys = new Function();
        sys.setFunctionName("系统管理");
        sys.setFunctionUrl("");
        sys.setFunctionLevel((short) 0);
        sys.setFunctionOrder("5");
        sys.setTSIconDesk(getDefaultInconForDesk());
        sys.setTSIcon(group_add);
        commonDao.saveOrUpdate(sys);

        Function state = new Function();
        state.setFunctionName("统计查询");
        state.setFunctionUrl("");
        state.setFunctionLevel((short) 0);
        state.setFunctionOrder("3");
        state.setTSIcon(folder);
        state.setTSIconDesk(getDefaultInconForDesk());
        commonDao.saveOrUpdate(state);

        Function syscontrol = new Function();
        syscontrol.setFunctionName("系统监控");
        syscontrol.setFunctionUrl("");
        syscontrol.setFunctionLevel((short) 0);
        syscontrol.setFunctionOrder("11");
        syscontrol.setTSIcon(defaultIcon);
        syscontrol.setTSIconDesk(getDefaultInconForDesk());
        commonDao.saveOrUpdate(syscontrol);

        Function user = new Function();
        user.setFunctionName("用户管理");
        user.setFunctionUrl("userController.do?user");
        user.setFunctionLevel((short) 1);
        user.setFunctionOrder("5");
        user.setTSFunction(sys);
        user.setTSIcon(defaultIcon);
        user.setTSIconDesk(repairInconForDesk("Finder", "用户管理"));
        commonDao.saveOrUpdate(user);

        Function role = new Function();
        role.setFunctionName("角色管理");
        role.setFunctionUrl("roleController.do?role");
        role.setFunctionLevel((short) 1);
        role.setFunctionOrder("6");
        role.setTSFunction(sys);
        role.setTSIcon(defaultIcon);
        role.setTSIconDesk(repairInconForDesk("friendgroup", "角色管理"));
        commonDao.saveOrUpdate(role);

        Function menu = new Function();
        menu.setFunctionName("菜单管理");
        menu.setFunctionUrl("functionController.do?function");
        menu.setFunctionLevel((short) 1);
        menu.setFunctionOrder("7");
        menu.setTSFunction(sys);
        menu.setTSIcon(defaultIcon);
        menu.setTSIconDesk(repairInconForDesk("kaikai", "菜单管理"));
        commonDao.saveOrUpdate(menu);

        Function typegroup = new Function();
        typegroup.setFunctionName("数据字典");
        typegroup.setFunctionUrl("systemController.do?typeGroupList");
        typegroup.setFunctionLevel((short) 1);
        typegroup.setFunctionOrder("6");
        typegroup.setTSFunction(sys);
        typegroup.setTSIcon(defaultIcon);
        typegroup.setTSIconDesk(repairInconForDesk("friendnear", "数据字典"));
        commonDao.saveOrUpdate(typegroup);

        Function icon = new Function();
        icon.setFunctionName("图标管理");
        icon.setFunctionUrl("iconController.do?icon");
        icon.setFunctionLevel((short) 1);
        icon.setFunctionOrder("18");
        icon.setTSFunction(sys);
        icon.setTSIcon(defaultIcon);
        icon.setTSIconDesk(repairInconForDesk("kxjy", "图标管理"));
        commonDao.saveOrUpdate(icon);

        Function depart = new Function();
        depart.setFunctionName("部门管理");
        depart.setFunctionUrl("departController.do?depart");
        depart.setFunctionLevel((short) 1);
        depart.setFunctionOrder("22");
        depart.setTSFunction(sys);
        depart.setTSIcon(defaultIcon);
        depart.setTSIconDesk(getDefaultInconForDesk());
        commonDao.saveOrUpdate(depart);

        Function territory = new Function();
        territory.setFunctionName("地域管理");
        territory.setFunctionUrl("territoryController.do?territory");
        territory.setFunctionLevel((short) 1);
        territory.setFunctionOrder("22");
        territory.setTSFunction(sys);
        territory.setTSIcon(pie);
        territory.setTSIconDesk(getDefaultInconForDesk());
        commonDao.saveOrUpdate(territory);

        Function language = new Function();
        language.setFunctionName("语言管理");
        language.setFunctionUrl("mutiLangController.do?mutiLang");
        language.setFunctionLevel((short) 1);
        language.setFunctionOrder("30");
        language.setTSFunction(sys);
        language.setTSIcon(pie);
        language.setTSIconDesk(getDefaultInconForDesk());
        commonDao.saveOrUpdate(language);

        Function template = new Function();
        template.setFunctionName("模版管理");
        template.setFunctionUrl("templateController.do?template");
        template.setFunctionLevel((short) 1);
        template.setFunctionOrder("28");
        template.setTSFunction(sys);
        template.setTSIcon(pie);
        template.setTSIconDesk(getDefaultInconForDesk());
        commonDao.saveOrUpdate(template);

        Function useranalyse = new Function();
        useranalyse.setFunctionName("用户分析");
        useranalyse.setFunctionUrl("logController.do?statisticTabs&isIframe");
        useranalyse.setFunctionLevel((short) 1);
        useranalyse.setFunctionOrder("17");
        useranalyse.setTSFunction(state);
        useranalyse.setTSIcon(pie);
        useranalyse.setTSIconDesk(repairInconForDesk("User", "用户分析"));
        commonDao.saveOrUpdate(useranalyse);

        Function druid = new Function();
        druid.setFunctionName("数据监控");
        druid.setFunctionUrl("dataSourceController.do?goDruid&isIframe");
        druid.setFunctionLevel((short) 1);
        druid.setFunctionOrder("11");
        druid.setTSFunction(syscontrol);
        druid.setTSIcon(defaultIcon);
        druid.setTSIconDesk(repairInconForDesk("Super Disk", "数据监控"));
        commonDao.saveOrUpdate(druid);

        Function log = new Function();
        log.setFunctionName("系统日志");
        log.setFunctionUrl("logController.do?log");
        log.setFunctionLevel((short) 1);
        log.setFunctionOrder("21");
        log.setTSFunction(syscontrol);
        log.setTSIcon(defaultIcon);
        log.setTSIconDesk(repairInconForDesk("fastsearch", "系统日志"));
        commonDao.saveOrUpdate(log);

        Function timeTask = new Function();
        timeTask.setFunctionName("定时任务");
        timeTask.setFunctionUrl("jobController.do?job");
        timeTask.setFunctionLevel((short) 1);
        timeTask.setFunctionOrder("21");
        timeTask.setTSFunction(syscontrol);
        timeTask.setTSIcon(defaultIcon);
        timeTask.setTSIconDesk(repairInconForDesk("Utilities", "定时任务"));
        commonDao.saveOrUpdate(timeTask);

        Function reportdemo = new Function();
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
