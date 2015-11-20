package org.jeecgframework.web.system.service.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.jeecgframework.core.common.service.impl.CommonServiceImpl;


import org.jeecgframework.web.system.entity.base.TSAttachment;
import org.jeecgframework.web.system.entity.base.TSDemo;
import org.jeecgframework.web.system.entity.base.TSDepart;
import org.jeecgframework.web.system.entity.base.TSFunction;
import org.jeecgframework.web.system.entity.base.TSIcon;
import org.jeecgframework.web.system.entity.base.TSLog;
import org.jeecgframework.web.system.entity.base.TSOperation;
import org.jeecgframework.web.system.entity.base.TSRole;
import org.jeecgframework.web.system.entity.base.TSRoleFunction;
import org.jeecgframework.web.system.entity.base.TSRoleUser;
import org.jeecgframework.web.system.entity.base.TSTimeTaskEntity;
import org.jeecgframework.web.system.entity.base.TSType;
import org.jeecgframework.web.system.entity.base.TSTypegroup;
import org.jeecgframework.web.system.entity.base.TSUser;
import org.jeecgframework.web.system.entity.base.TSUserOrg;
import org.jeecgframework.web.system.service.RepairService;
import org.jeecgframework.web.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description 修复数据库Service
 * @ClassName: RepairService
 * @author tanghan
 * @date 2013-7-19 下午01:31:00
 */
@Service("repairService")
@Transactional
public class RepairServiceImpl extends CommonServiceImpl implements
		RepairService {


	/**
	 * @Description 先清空数据库，然后再修复数据库
	 * @author tanghan 2013-7-19
	 */
	
	public void deleteAndRepair() {
		// 由于表中有主外键关系，清空数据库需注意
		commonDao.executeHql("delete TSLog");
		commonDao.executeHql("delete CKEditorEntity");
		commonDao.executeHql("delete CgformEnhanceJsEntity");
		commonDao.executeHql("delete CgFormFieldEntity");
		commonDao.executeHql("delete CgFormHeadEntity");
		commonDao.executeHql("delete TSAttachment");
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
		commonDao.executeHql("delete JeecgDemoCkfinderEntity");
		commonDao.executeHql("delete TSTimeTaskEntity");
		commonDao.executeHql("update TSTerritory t set t.TSTerritory = null");
		commonDao.executeHql("delete TSTerritory");
		commonDao.executeHql("delete StudentEntity");
		commonDao.executeHql("delete CourseEntity");
		commonDao.executeHql("delete TeacherEntity");
		commonDao.executeHql("delete JeecgJdbcEntity ");
		commonDao.executeHql("delete JeecgOrderMainEntity ");
		commonDao.executeHql("delete JeecgOrderProductEntity ");
		commonDao.executeHql("delete JeecgOrderCustomEntity ");
		commonDao.executeHql("delete JeecgNoteEntity ");
		commonDao.executeHql("update JeecgMatterBom mb set mb.parent = null");
		commonDao.executeHql("delete JeecgMatterBom ");
		repair();
	}

	/**
	 * @Description 修复数据库
	 * @author tanghan 2013-7-19
	 */
	
	synchronized public void repair() {
		//repairCkFinder();// 修复智能表单ck_finder数据库
		repaireIcon(); // 修复图标
		repairAttachment(); // 修改附件
		repairDepart();// 修复部门表
		repairMenu();// 修复菜单权限
		repairRole();// 修复角色
		repairUser(); // 修复基本用户
		repairTypeAndGroup();// 修复字典类型
		repairType();// 修复字典值
		repairOperation(); // 修复操作表
		repairRoleFunction();// 修复角色和权限的关系
		repairUserRole();// 修复用户和角色的关系
		repairLog();// 修复日志表
		repairTask();//修复任务管理

	}



	/**
	 * 修复任务管理
	 * @author JueYue
	 * @serialData 2013年11月5日
	 */
	private void repairTask() {
		TSTimeTaskEntity task = new TSTimeTaskEntity();
		task.setTaskId("taskDemoServiceTaskCronTrigger");
		task.setTaskDescribe("测试Demo");
		task.setCronExpression("0 0/1 * * * ?");
		task.setIsEffect("0");
		task.setIsStart("0");
		commonDao.saveOrUpdate(task);
	}






	/**
	 * @Description 修复日志表
	 * @author tanghan 2013-7-28
	 */
	private void repairLog() {/*
		TSUser admin = commonDao.findAllByProperty(TSUser.class, "signatureFile",
				"images/renfang/qm/licf.gif").get(0);
		try {
			TSLog log1 = new TSLog();
			log1.setLogcontent("用户: admin登录成功");
			log1.setBroswer("Chrome");
			log1.setNote("169.254.200.136");
			log1.setTSUser(admin);
			log1.setOperatetime(DateUtils.parseTimestamp("2013-4-24 16:22:40",
					"yyyy-MM-dd HH:mm"));
			log1.setOperatetype((short) 1);
			log1.setLoglevel((short) 1);
			commonDao.saveOrUpdate(log1);

			TSLog log2 = new TSLog();
			log2.setLogcontent("用户: admin登录成功");
			log2.setBroswer("Chrome");
			log2.setNote("10.10.10.1");
			log2.setTSUser(admin);
			log2.setOperatetime(DateUtils.parseTimestamp("2013-4-24 17:12:22",
					"yyyy-MM-dd HH:mm"));
			log2.setOperatetype((short) 1);
			log2.setLoglevel((short) 1);
			commonDao.saveOrUpdate(log2);

			TSLog log3 = new TSLog();
			log3.setLogcontent("用户: admin登录成功");
			log3.setBroswer("Chrome");
			log3.setNote("169.254.218.201");
			log3.setTSUser(admin);
			log3.setOperatetime(DateUtils.parseTimestamp("2013-3-10 15:37:11",
					"yyyy-MM-dd HH:mm"));
			log3.setOperatetype((short) 1);
			log3.setLoglevel((short) 1);
			commonDao.saveOrUpdate(log3);

			TSLog log4 = new TSLog();
			log4.setLogcontent("用户admin已退出");
			log4.setBroswer("Chrome");
			log4.setNote("169.254.218.201");
			log4.setTSUser(admin);
			log4.setOperatetime(DateUtils.parseTimestamp("2013-3-10 15:38:33",
					"yyyy-MM-dd HH:mm"));
			log4.setOperatetype((short) 1);
			log4.setLoglevel((short) 2);
			commonDao.saveOrUpdate(log4);

			TSLog log5 = new TSLog();
			log5.setLogcontent("用户: admin登录成功");
			log5.setBroswer("MSIE 9.0");
			log5.setNote("169.254.218.201");
			log5.setTSUser(admin);
			log5.setOperatetime(DateUtils.parseTimestamp("2013-3-10 15:38:42",
					"yyyy-MM-dd HH:mm"));
			log5.setOperatetype((short) 1);
			log5.setLoglevel((short) 1);
			commonDao.saveOrUpdate(log5);

			TSLog log6 = new TSLog();
			log6.setLogcontent("JeecgDemo例子: 12被删除 成功");
			log6.setBroswer("MSIE 9.0");
			log6.setNote("169.254.218.201");
			log6.setTSUser(admin);
			log6.setOperatetime(DateUtils.parseTimestamp("2013-3-10 15:39:00",
					"yyyy-MM-dd HH:mm"));
			log6.setOperatetype((short) 1);
			log6.setLoglevel((short) 4);
			commonDao.saveOrUpdate(log6);

			TSLog log7 = new TSLog();
			log7.setLogcontent("JeecgDemo例子: 12被删除 成功");
			log7.setBroswer("MSIE 9.0");
			log7.setNote("169.254.218.201");
			log7.setTSUser(admin);
			log7.setOperatetime(DateUtils.parseTimestamp("2013-3-10 15:39:02",
					"yyyy-MM-dd HH:mm"));
			log7.setOperatetype((short) 1);
			log7.setLoglevel((short) 4);
			commonDao.saveOrUpdate(log7);

			TSLog log8 = new TSLog();
			log8.setLogcontent("JeecgDemo例子: 12被删除 成功");
			log8.setBroswer("Chrome");
			log8.setNote("169.254.218.201");
			log8.setTSUser(admin);
			log8.setOperatetime(DateUtils.parseTimestamp("2013-3-10 15:39:04",
					"yyyy-MM-dd HH:mm"));
			log8.setOperatetype((short) 1);
			log8.setLoglevel((short) 4);
			commonDao.saveOrUpdate(log8);

			TSLog log9 = new TSLog();
			log9.setLogcontent("权限: 单表模型被更新成功");
			log9.setBroswer("MSIE 9.0");
			log9.setNote("169.254.218.201");
			log9.setTSUser(admin);
			log9.setOperatetime(DateUtils.parseTimestamp("2013-3-10 15:39:30",
					"yyyy-MM-dd HH:mm"));
			log9.setOperatetype((short) 1);
			log9.setLoglevel((short) 5);
			commonDao.saveOrUpdate(log9);

			TSLog log10 = new TSLog();
			log10.setLogcontent("删除成功");
			log10.setBroswer("Chrome");
			log10.setNote("169.254.218.201");
			log10.setTSUser(admin);
			log10.setOperatetime(DateUtils.parseTimestamp("2013-3-10 15:39:38",
					"yyyy-MM-dd HH:mm"));
			log10.setOperatetype((short) 1);
			log10.setLoglevel((short) 4);
			commonDao.saveOrUpdate(log10);

			TSLog log11 = new TSLog();
			log11.setLogcontent("删除成功");
			log11.setBroswer("MSIE 9.0");
			log11.setNote("169.254.218.201");
			log11.setTSUser(admin);
			log11.setOperatetime(DateUtils.parseTimestamp("2013-3-10 15:39:40",
					"yyyy-MM-dd HH:mm"));
			log11.setOperatetype((short) 1);
			log11.setLoglevel((short) 4);
			commonDao.saveOrUpdate(log11);

			TSLog log12 = new TSLog();
			log12.setLogcontent("删除成功");
			log12.setBroswer("Chrome");
			log12.setNote("169.254.218.201");
			log12.setTSUser(admin);
			log12.setOperatetime(DateUtils.parseTimestamp("2013-3-10 15:39:41",
					"yyyy-MM-dd HH:mm"));
			log12.setOperatetype((short) 1);
			log12.setLoglevel((short) 4);
			commonDao.saveOrUpdate(log12);

			TSLog log13 = new TSLog();
			log13.setLogcontent("删除成功");
			log13.setBroswer("Firefox");
			log13.setNote("169.254.218.201");
			log13.setTSUser(admin);
			log13.setOperatetime(DateUtils.parseTimestamp("2013-3-10 15:39:42",
					"yyyy-MM-dd HH:mm"));
			log13.setOperatetype((short) 1);
			log13.setLoglevel((short) 4);
			commonDao.saveOrUpdate(log13);

			TSLog log14 = new TSLog();
			log14.setLogcontent("添加成功");
			log14.setBroswer("Chrome");
			log14.setNote("169.254.218.201");
			log14.setTSUser(admin);
			log14.setOperatetime(DateUtils.parseTimestamp("2013-3-10 15:40:00",
					"yyyy-MM-dd HH:mm"));
			log14.setOperatetype((short) 1);
			log14.setLoglevel((short) 3);
			commonDao.saveOrUpdate(log14);

			TSLog log15 = new TSLog();
			log15.setLogcontent("更新成功");
			log15.setBroswer("Chrome");
			log15.setNote("169.254.218.201");
			log15.setTSUser(admin);
			log15.setOperatetime(DateUtils.parseTimestamp("2013-3-10 15:40:04",
					"yyyy-MM-dd HH:mm"));
			log15.setOperatetype((short) 1);
			log15.setLoglevel((short) 5);
			commonDao.saveOrUpdate(log15);

			TSLog log16 = new TSLog();
			log16.setLogcontent("JeecgDemo例子: 12被添加成功");
			log16.setBroswer("Chrome");
			log16.setNote("169.254.218.201");
			log16.setTSUser(admin);
			log16.setOperatetime(DateUtils.parseTimestamp("2013-3-10 15:40:44",
					"yyyy-MM-dd HH:mm"));
			log16.setOperatetype((short) 1);
			log16.setLoglevel((short) 3);
			commonDao.saveOrUpdate(log16);

			TSLog log17 = new TSLog();
			log17.setLogcontent("部门: 信息部被更新成功");
			log17.setBroswer("Chrome");
			log17.setNote("169.254.218.201");
			log17.setTSUser(admin);
			log17.setOperatetime(DateUtils.parseTimestamp("2013-3-10 15:41:26",
					"yyyy-MM-dd HH:mm"));
			log17.setOperatetype((short) 1);
			log17.setLoglevel((short) 5);
			commonDao.saveOrUpdate(log17);

			TSLog log18 = new TSLog();
			log18.setLogcontent("部门: 设计部被更新成功");
			log18.setBroswer("Chrome");
			log18.setNote("169.254.218.201");
			log18.setTSUser(admin);
			log18.setOperatetime(DateUtils.parseTimestamp("2013-3-10 15:41:38",
					"yyyy-MM-dd HH:mm"));
			log18.setOperatetype((short) 1);
			log18.setLoglevel((short) 5);
			commonDao.saveOrUpdate(log18);

			TSLog log19 = new TSLog();
			log19.setLogcontent("类型: 信息部流程被更新成功");
			log19.setBroswer("Chrome");
			log19.setNote("169.254.218.201");
			log19.setTSUser(admin);
			log19.setOperatetime(DateUtils.parseTimestamp("2013-3-10 15:46:55",
					"yyyy-MM-dd HH:mm"));
			log19.setOperatetype((short) 1);
			log19.setLoglevel((short) 5);
			commonDao.saveOrUpdate(log19);

			TSLog log20 = new TSLog();
			log20.setLogcontent("用户: admin登录成功");
			log20.setBroswer("Chrome");
			log20.setNote("169.254.218.201");
			log20.setTSUser(admin);
			log20.setOperatetime(DateUtils.parseTimestamp("2013-3-10 15:48:47",
					"yyyy-MM-dd HH:mm"));
			log20.setOperatetype((short) 1);
			log20.setLoglevel((short) 1);
			commonDao.saveOrUpdate(log20);

			TSLog log21 = new TSLog();
			log21.setLogcontent("用户: admin登录成功");
			log21.setBroswer("Firefox");
			log21.setNote("169.254.218.201");
			log21.setTSUser(admin);
			log21.setOperatetime(DateUtils.parseTimestamp("2013-3-21 23:23:52",
					"yyyy-MM-dd HH:mm"));
			log21.setOperatetype((short) 1);
			log21.setLoglevel((short) 1);
			commonDao.saveOrUpdate(log21);

			TSLog log22 = new TSLog();
			log22.setLogcontent("用户: admin登录成功");
			log22.setBroswer("Chrome");
			log22.setNote("169.254.218.201");
			log22.setTSUser(admin);
			log22.setOperatetime(DateUtils.parseTimestamp("2013-3-21 23:26:22",
					"yyyy-MM-dd HH:mm"));
			log22.setOperatetype((short) 1);
			log22.setLoglevel((short) 1);
			commonDao.saveOrUpdate(log22);

			TSLog log23 = new TSLog();
			log23.setLogcontent("权限: 一对多实例被添加成功");
			log23.setBroswer("Chrome");
			log23.setNote("169.254.218.201");
			log23.setTSUser(admin);
			log23.setOperatetime(DateUtils.parseTimestamp("2013-3-21 23:28:34",
					"yyyy-MM-dd HH:mm"));
			log23.setOperatetype((short) 1);
			log23.setLoglevel((short) 3);
			commonDao.saveOrUpdate(log23);

			TSLog log24 = new TSLog();
			log24.setLogcontent("用户: admin登录成功");
			log24.setBroswer("Chrome");
			log24.setNote("169.254.218.201");
			log24.setTSUser(admin);
			log24.setOperatetime(DateUtils.parseTimestamp("2013-3-22 8:25:07",
					"yyyy-MM-dd HH:mm"));
			log24.setOperatetype((short) 1);
			log24.setLoglevel((short) 1);
			commonDao.saveOrUpdate(log24);

			TSLog log25 = new TSLog();
			log25.setLogcontent("用户: admin登录成功");
			log25.setBroswer("Firefox");
			log25.setNote("169.254.218.201");
			log25.setTSUser(admin);
			log25.setOperatetime(DateUtils.parseTimestamp("2013-3-22 9:05:25",
					"yyyy-MM-dd HH:mm"));
			log25.setOperatetype((short) 1);
			log25.setLoglevel((short) 1);
			commonDao.saveOrUpdate(log25);

			TSLog log26 = new TSLog();
			log26.setLogcontent("用户: admin登录成功");
			log26.setBroswer("Chrome");
			log26.setNote("169.254.218.201");
			log26.setTSUser(admin);
			log26.setOperatetime(DateUtils.parseTimestamp("2013-3-22 9:09:05",
					"yyyy-MM-dd HH:mm"));
			log26.setOperatetype((short) 1);
			log26.setLoglevel((short) 1);
			commonDao.saveOrUpdate(log26);

			TSLog log27 = new TSLog();
			log27.setLogcontent("用户: admin登录成功");
			log27.setBroswer("MSIE 8.0");
			log27.setNote("169.254.218.201");
			log27.setTSUser(admin);
			log27.setOperatetime(DateUtils.parseTimestamp("2013-3-22 9:28:50",
					"yyyy-MM-dd HH:mm"));
			log27.setOperatetype((short) 1);
			log27.setLoglevel((short) 1);
			commonDao.saveOrUpdate(log27);

			TSLog log28 = new TSLog();
			log28.setLogcontent("用户: admin登录成功");
			log28.setBroswer("Firefox");
			log28.setNote("169.254.218.201");
			log28.setTSUser(admin);
			log28.setOperatetime(DateUtils.parseTimestamp("2013-3-22 10:32:59",
					"yyyy-MM-dd HH:mm"));
			log28.setOperatetype((short) 1);
			log28.setLoglevel((short) 1);
			commonDao.saveOrUpdate(log28);

			TSLog log29 = new TSLog();
			log29.setLogcontent("物品: 笔记本添加成功");
			log29.setBroswer("Chrome");
			log29.setNote("169.254.218.201");
			log29.setTSUser(admin);
			log29.setOperatetime(DateUtils.parseTimestamp("2013-3-22 10:35:44",
					"yyyy-MM-dd HH:mm"));
			log29.setOperatetype((short) 1);
			log29.setLoglevel((short) 3);
			commonDao.saveOrUpdate(log29);

			TSLog log30 = new TSLog();
			log30.setLogcontent("用户: admin登录成功");
			log30.setBroswer("Firefox");
			log30.setNote("169.254.218.201");
			log30.setTSUser(admin);
			log30.setOperatetime(DateUtils.parseTimestamp("2013-3-22 10:41:46",
					"yyyy-MM-dd HH:mm"));
			log30.setOperatetype((short) 1);
			log30.setLoglevel((short) 1);
			commonDao.saveOrUpdate(log30);

			TSLog log31 = new TSLog();
			log31.setLogcontent("用户: admin登录成功");
			log31.setBroswer("Firefox");
			log31.setNote("169.254.218.201");
			log31.setTSUser(admin);
			log31.setOperatetime(DateUtils.parseTimestamp("2013-3-22 16:11:14",
					"yyyy-MM-dd HH:mm"));
			log31.setOperatetype((short) 1);
			log31.setLoglevel((short) 1);
			commonDao.saveOrUpdate(log31);

			TSLog log32 = new TSLog();
			log32.setLogcontent("用户: admin登录成功");
			log32.setBroswer("Chrome");
			log32.setNote("169.254.218.201");
			log32.setTSUser(admin);
			log32.setOperatetime(DateUtils.parseTimestamp("2013-3-22 21:49:43",
					"yyyy-MM-dd HH:mm"));
			log32.setOperatetype((short) 1);
			log32.setLoglevel((short) 1);
			commonDao.saveOrUpdate(log32);

			TSLog log33 = new TSLog();
			log33.setLogcontent("用户: admin登录成功");
			log33.setBroswer("Chrome");
			log33.setNote("169.254.218.201");
			log33.setTSUser(admin);
			log33.setOperatetime(DateUtils.parseTimestamp("2013-3-22 23:17:12",
					"yyyy-MM-dd HH:mm"));
			log33.setOperatetype((short) 1);
			log33.setLoglevel((short) 1);
			commonDao.saveOrUpdate(log33);

			TSLog log34 = new TSLog();
			log34.setLogcontent("用户: admin登录成功");
			log34.setBroswer("Chrome");
			log34.setNote("169.254.218.201");
			log34.setTSUser(admin);
			log34.setOperatetime(DateUtils.parseTimestamp("2013-3-22 23:27:22",
					"yyyy-MM-dd HH:mm"));
			log34.setOperatetype((short) 1);
			log34.setLoglevel((short) 1);
			commonDao.saveOrUpdate(log34);

			TSLog log35 = new TSLog();
			log35.setLogcontent("用户: admin登录成功");
			log35.setBroswer("Chrome");
			log35.setNote("169.254.218.201");
			log35.setTSUser(admin);
			log35.setOperatetime(DateUtils.parseTimestamp("2013-3-23 0:16:10",
					"yyyy-MM-dd HH:mm"));
			log35.setOperatetype((short) 1);
			log35.setLoglevel((short) 1);
			commonDao.saveOrUpdate(log35);

			TSLog log36 = new TSLog();
			log36.setLogcontent("用户: admin登录成功");
			log36.setBroswer("Chrome");
			log36.setNote("169.254.218.201");
			log36.setTSUser(admin);
			log36.setOperatetime(DateUtils.parseTimestamp("2013-3-23 0:22:46",
					"yyyy-MM-dd HH:mm"));
			log36.setOperatetype((short) 1);
			log36.setLoglevel((short) 1);
			commonDao.saveOrUpdate(log36);

			TSLog log37 = new TSLog();
			log37.setLogcontent("用户: admin登录成功");
			log37.setBroswer("Firefox");
			log37.setNote("169.254.218.201");
			log37.setTSUser(admin);
			log37.setOperatetime(DateUtils.parseTimestamp("2013-3-23 0:31:11",
					"yyyy-MM-dd HH:mm"));
			log37.setOperatetype((short) 1);
			log37.setLoglevel((short) 1);
			commonDao.saveOrUpdate(log37);

			TSLog log38 = new TSLog();
			log38.setLogcontent("用户: admin登录成功");
			log38.setBroswer("Chrome");
			log38.setNote("169.254.218.201");
			log38.setTSUser(admin);
			log38.setOperatetime(DateUtils.parseTimestamp("2013-3-23 14:23:36",
					"yyyy-MM-dd HH:mm"));
			log38.setOperatetype((short) 1);
			log38.setLoglevel((short) 1);
			commonDao.saveOrUpdate(log38);

			TSLog log39 = new TSLog();
			log39.setLogcontent("流程参数: 主任审批被添加成功");
			log39.setBroswer("Chrome");
			log39.setNote("169.254.218.201");
			log39.setTSUser(admin);
			log39.setOperatetime(DateUtils.parseTimestamp("2013-3-23 15:05:30",
					"yyyy-MM-dd HH:mm"));
			log39.setOperatetype((short) 1);
			log39.setLoglevel((short) 3);
			commonDao.saveOrUpdate(log39);

			TSLog log40 = new TSLog();
			log40.setLogcontent("业务参数: 入职申请被添加成功");
			log40.setBroswer("Firefox");
			log40.setNote("169.254.218.201");
			log40.setTSUser(admin);
			log40.setOperatetime(DateUtils.parseTimestamp("2013-3-23 15:05:42",
					"yyyy-MM-dd HH:mm"));
			log40.setOperatetype((short) 1);
			log40.setLoglevel((short) 3);
			commonDao.saveOrUpdate(log40);

			TSLog log41 = new TSLog();
			log41.setLogcontent("权限: 入职申请被添加成功");
			log41.setBroswer("Chrome");
			log41.setNote("169.254.218.201");
			log41.setTSUser(admin);
			log41.setOperatetime(DateUtils.parseTimestamp("2013-3-23 15:12:56",
					"yyyy-MM-dd HH:mm"));
			log41.setOperatetype((short) 1);
			log41.setLoglevel((short) 3);
			commonDao.saveOrUpdate(log41);

			TSLog log42 = new TSLog();
			log42.setLogcontent("权限: 入职办理被添加成功");
			log42.setBroswer("Firefox");
			log42.setNote("169.254.218.201");
			log42.setTSUser(admin);
			log42.setOperatetime(DateUtils.parseTimestamp("2013-3-23 15:13:23",
					"yyyy-MM-dd HH:mm"));
			log42.setOperatetype((short) 1);
			log42.setLoglevel((short) 3);
			commonDao.saveOrUpdate(log42);

			TSLog log43 = new TSLog();
			log43.setLogcontent("用户: admin登录成功");
			log43.setBroswer("Chrome");
			log43.setNote("10.10.10.1");
			log43.setTSUser(admin);
			log43.setOperatetime(DateUtils.parseTimestamp("2013-5-6 15:27:19",
					"yyyy-MM-dd HH:mm"));
			log43.setOperatetype((short) 1);
			log43.setLoglevel((short) 1);
			commonDao.saveOrUpdate(log43);

			TSLog log44 = new TSLog();
			log44.setLogcontent("用户: admin登录成功");
			log44.setBroswer("MSIE 8.0");
			log44.setNote("192.168.197.1");
			log44.setTSUser(admin);
			log44.setOperatetime(DateUtils.parseTimestamp("2013-7-7 15:16:05",
					"yyyy-MM-dd HH:mm"));
			log44.setOperatetype((short) 1);
			log44.setLoglevel((short) 1);
			commonDao.saveOrUpdate(log44);

			TSLog log45 = new TSLog();
			log45.setLogcontent("用户: admin登录成功");
			log45.setBroswer("MSIE 8.0");
			log45.setNote("192.168.197.1");
			log45.setTSUser(admin);
			log45.setOperatetime(DateUtils.parseTimestamp("2013-7-7 16:02:38",
					"yyyy-MM-dd HH:mm"));
			log45.setOperatetype((short) 1);
			log45.setLoglevel((short) 1);
			commonDao.saveOrUpdate(log45);

			TSLog log46 = new TSLog();
			log46.setLogcontent("用户: admin登录成功");
			log46.setBroswer("MSIE 8.0");
			log46.setNote("192.168.197.1");
			log46.setTSUser(admin);
			log46.setOperatetime(DateUtils.parseTimestamp("2013-7-7 16:07:49",
					"yyyy-MM-dd HH:mm"));
			log46.setOperatetype((short) 1);
			log46.setLoglevel((short) 1);
			commonDao.saveOrUpdate(log46);

			TSLog log47 = new TSLog();
			log47.setLogcontent("用户: admin登录成功");
			log47.setBroswer("MSIE 8.0");
			log47.setNote("192.168.197.1");
			log47.setTSUser(admin);
			log47.setOperatetime(DateUtils.parseTimestamp("2013-7-7 16:09:10",
					"yyyy-MM-dd HH:mm"));
			log47.setOperatetype((short) 1);
			log47.setLoglevel((short) 1);
			commonDao.saveOrUpdate(log47);

			TSLog log48 = new TSLog();
			log48.setLogcontent("用户: admin登录成功");
			log48.setBroswer("MSIE 8.0");
			log48.setNote("192.168.197.1");
			log48.setTSUser(admin);
			log48.setOperatetime(DateUtils.parseTimestamp("2013-7-7 16:11:49",
					"yyyy-MM-dd HH:mm"));
			log48.setOperatetype((short) 1);
			log48.setLoglevel((short) 1);
			commonDao.saveOrUpdate(log48);

			TSLog log49 = new TSLog();
			log49.setLogcontent("用户: admin登录成功");
			log49.setBroswer("MSIE 8.0");
			log49.setNote("192.168.197.1");
			log49.setTSUser(admin);
			log49.setOperatetime(DateUtils.parseTimestamp("2013-7-7 16:13:44",
					"yyyy-MM-dd HH:mm"));
			log49.setOperatetype((short) 1);
			log49.setLoglevel((short) 1);
			commonDao.saveOrUpdate(log49);

			TSLog log50 = new TSLog();
			log50.setLogcontent("用户: admin登录成功");
			log50.setBroswer("MSIE 8.0");
			log50.setNote("192.168.197.1");
			log50.setTSUser(admin);
			log50.setOperatetime(DateUtils.parseTimestamp("2013-7-7 16:16:52",
					"yyyy-MM-dd HH:mm"));
			log50.setOperatetype((short) 1);
			log50.setLoglevel((short) 1);
			commonDao.saveOrUpdate(log50);

			TSLog log51 = new TSLog();
			log51.setLogcontent("用户: admin登录成功");
			log51.setBroswer("MSIE 8.0");
			log51.setNote("192.168.197.1");
			log51.setTSUser(admin);
			log51.setOperatetime(DateUtils.parseTimestamp("2013-7-7 16:19:18",
					"yyyy-MM-dd HH:mm"));
			log51.setOperatetype((short) 1);
			log51.setLoglevel((short) 1);
			commonDao.saveOrUpdate(log51);

			TSLog log52 = new TSLog();
			log52.setLogcontent("用户: admin登录成功");
			log52.setBroswer("MSIE 8.0");
			log52.setNote("192.168.197.1");
			log52.setTSUser(admin);
			log52.setOperatetime(DateUtils.parseTimestamp("2013-7-7 16:27:05",
					"yyyy-MM-dd HH:mm"));
			log52.setOperatetype((short) 1);
			log52.setLoglevel((short) 1);
			commonDao.saveOrUpdate(log52);

			TSLog log53 = new TSLog();
			log53.setLogcontent("用户: admin登录成功");
			log53.setBroswer("MSIE 8.0");
			log53.setNote("192.168.197.1");
			log53.setTSUser(admin);
			log53.setOperatetime(DateUtils.parseTimestamp("2013-7-7 16:42:32",
					"yyyy-MM-dd HH:mm"));
			log53.setOperatetype((short) 1);
			log53.setLoglevel((short) 1);
			commonDao.saveOrUpdate(log53);

			TSLog log54 = new TSLog();
			log54.setLogcontent("用户: admin登录成功");
			log54.setBroswer("MSIE 8.0");
			log54.setNote("192.168.197.1");
			log54.setTSUser(admin);
			log54.setOperatetime(DateUtils.parseTimestamp("2013-7-7 16:44:38",
					"yyyy-MM-dd HH:mm"));
			log54.setOperatetype((short) 1);
			log54.setLoglevel((short) 1);
			commonDao.saveOrUpdate(log54);

			TSLog log55 = new TSLog();
			log55.setLogcontent("用户: admin登录成功");
			log55.setBroswer("MSIE 8.0");
			log55.setNote("192.168.197.1");
			log55.setTSUser(admin);
			log55.setOperatetime(DateUtils.parseTimestamp("2013-7-7 16:49:06",
					"yyyy-MM-dd HH:mm"));
			log55.setOperatetype((short) 1);
			log55.setLoglevel((short) 1);
			commonDao.saveOrUpdate(log55);

			TSLog log56 = new TSLog();
			log56.setLogcontent("用户: admin登录成功");
			log56.setBroswer("MSIE 8.0");
			log56.setNote("192.168.197.1");
			log56.setTSUser(admin);
			log56.setOperatetime(DateUtils.parseTimestamp("2013-7-7 16:50:51",
					"yyyy-MM-dd HH:mm"));
			log56.setOperatetype((short) 1);
			log56.setLoglevel((short) 1);
			commonDao.saveOrUpdate(log56);

			TSLog log57 = new TSLog();
			log57.setLogcontent("用户: admin登录成功");
			log57.setBroswer("MSIE 8.0");
			log57.setNote("192.168.197.1");
			log57.setTSUser(admin);
			log57.setOperatetime(DateUtils.parseTimestamp("2013-7-7 16:53:48",
					"yyyy-MM-dd HH:mm"));
			log57.setOperatetype((short) 1);
			log57.setLoglevel((short) 1);
			commonDao.saveOrUpdate(log57);

			TSLog log58 = new TSLog();
			log58.setLogcontent("修改成功");
			log58.setBroswer("MSIE 8.0");
			log58.setNote("192.168.197.1");
			log58.setTSUser(admin);
			log58.setOperatetime(DateUtils.parseTimestamp("2013-7-7 16:56:45",
					"yyyy-MM-dd HH:mm"));
			log58.setOperatetype((short) 1);
			log58.setLoglevel((short) 5);
			commonDao.saveOrUpdate(log58);

			TSLog log59 = new TSLog();
			log59.setLogcontent("用户: admin登录成功");
			log59.setBroswer("MSIE 8.0");
			log59.setNote("192.168.197.1");
			log59.setTSUser(admin);
			log59.setOperatetime(DateUtils.parseTimestamp("2013-7-7 16:59:22",
					"yyyy-MM-dd HH:mm"));
			log59.setOperatetype((short) 1);
			log59.setLoglevel((short) 1);
			commonDao.saveOrUpdate(log59);

			TSLog log60 = new TSLog();
			log60.setLogcontent("创建成功");
			log60.setBroswer("MSIE 8.0");
			log60.setNote("192.168.197.1");
			log60.setTSUser(admin);
			log60.setOperatetime(DateUtils.parseTimestamp("2013-7-7 17:22:42",
					"yyyy-MM-dd HH:mm"));
			log60.setOperatetype((short) 1);
			log60.setLoglevel((short) 3);
			commonDao.saveOrUpdate(log60);

			TSLog log61 = new TSLog();
			log61.setLogcontent("修改成功");
			log61.setBroswer("MSIE 8.0");
			log61.setNote("192.168.197.1");
			log61.setTSUser(admin);
			log61.setOperatetime(DateUtils.parseTimestamp("2013-7-7 17:26:03",
					"yyyy-MM-dd HH:mm"));
			log61.setOperatetype((short) 1);
			log61.setLoglevel((short) 5);
			commonDao.saveOrUpdate(log61);

			TSLog log62 = new TSLog();
			log62.setLogcontent("删除成功");
			log62.setBroswer("MSIE 8.0");
			log62.setNote("192.168.197.1");
			log62.setTSUser(admin);
			log62.setOperatetime(DateUtils.parseTimestamp("2013-7-7 17:31:00",
					"yyyy-MM-dd HH:mm"));
			log62.setOperatetype((short) 1);
			log62.setLoglevel((short) 4);
			commonDao.saveOrUpdate(log62);

			TSLog log63 = new TSLog();
			log63.setLogcontent("修改成功");
			log63.setBroswer("MSIE 8.0");
			log63.setNote("192.168.197.1");
			log63.setTSUser(admin);
			log63.setOperatetime(DateUtils.parseTimestamp("2013-7-7 17:35:02",
					"yyyy-MM-dd HH:mm"));
			log63.setOperatetype((short) 1);
			log63.setLoglevel((short) 5);
			commonDao.saveOrUpdate(log63);

			TSLog log64 = new TSLog();
			log64.setLogcontent("用户: admin登录成功");
			log64.setBroswer("MSIE 8.0");
			log64.setNote("192.168.197.1");
			log64.setTSUser(admin);
			log64.setOperatetime(DateUtils.parseTimestamp("2013-7-7 17:46:39",
					"yyyy-MM-dd HH:mm"));
			log64.setOperatetype((short) 1);
			log64.setLoglevel((short) 1);
			commonDao.saveOrUpdate(log64);

			TSLog log65 = new TSLog();
			log65.setLogcontent("用户: admin登录成功");
			log65.setBroswer("MSIE 8.0");
			log65.setNote("192.168.197.1");
			log65.setTSUser(admin);
			log65.setOperatetime(DateUtils.parseTimestamp("2013-7-7 17:55:01",
					"yyyy-MM-dd HH:mm"));
			log65.setOperatetype((short) 1);
			log65.setLoglevel((short) 1);
			commonDao.saveOrUpdate(log65);

			TSLog log66 = new TSLog();
			log66.setLogcontent("用户: admin登录成功");
			log66.setBroswer("MSIE 8.0");
			log66.setNote("192.168.197.1");
			log66.setTSUser(admin);
			log66.setOperatetime(DateUtils.parseTimestamp("2013-7-7 18:08:56",
					"yyyy-MM-dd HH:mm"));
			log66.setOperatetype((short) 1);
			log66.setLoglevel((short) 1);
			commonDao.saveOrUpdate(log66);

			TSLog log67 = new TSLog();
			log67.setLogcontent("用户: admin登录成功");
			log67.setBroswer("MSIE 8.0");
			log67.setNote("192.168.197.1");
			log67.setTSUser(admin);
			log67.setOperatetime(DateUtils.parseTimestamp("2013-7-7 18:13:02",
					"yyyy-MM-dd HH:mm"));
			log67.setOperatetype((short) 1);
			log67.setLoglevel((short) 1);
			commonDao.saveOrUpdate(log67);

			TSLog log68 = new TSLog();
			log68.setLogcontent("用户: admin登录成功");
			log68.setBroswer("MSIE 8.0");
			log68.setNote("192.168.197.1");
			log68.setTSUser(admin);
			log68.setOperatetime(DateUtils.parseTimestamp("2013-7-7 18:15:50",
					"yyyy-MM-dd HH:mm"));
			log68.setOperatetype((short) 1);
			log68.setLoglevel((short) 1);
			commonDao.saveOrUpdate(log68);

			TSLog log69 = new TSLog();
			log69.setLogcontent("修改成功");
			log69.setBroswer("MSIE 8.0");
			log69.setNote("192.168.197.1");
			log69.setTSUser(admin);
			log69.setOperatetime(DateUtils.parseTimestamp("2013-7-7 18:28:42",
					"yyyy-MM-dd HH:mm"));
			log69.setOperatetype((short) 1);
			log69.setLoglevel((short) 5);
			commonDao.saveOrUpdate(log69);

			TSLog log70 = new TSLog();
			log70.setLogcontent("修改成功");
			log70.setBroswer("MSIE 8.0");
			log70.setNote("192.168.197.1");
			log70.setTSUser(admin);
			log70.setOperatetime(DateUtils.parseTimestamp("2013-7-7 18:29:12",
					"yyyy-MM-dd HH:mm"));
			log70.setOperatetype((short) 1);
			log70.setLoglevel((short) 5);
			commonDao.saveOrUpdate(log70);

			TSLog log71 = new TSLog();
			log71.setLogcontent("修改成功");
			log71.setBroswer("MSIE 8.0");
			log71.setNote("192.168.197.1");
			log71.setTSUser(admin);
			log71.setOperatetime(DateUtils.parseTimestamp("2013-7-7 18:30:12",
					"yyyy-MM-dd HH:mm"));
			log71.setOperatetype((short) 1);
			log71.setLoglevel((short) 5);
			commonDao.saveOrUpdate(log71);

			TSLog log72 = new TSLog();
			log72.setLogcontent("修改成功");
			log72.setBroswer("MSIE 8.0");
			log72.setNote("192.168.197.1");
			log72.setTSUser(admin);
			log72.setOperatetime(DateUtils.parseTimestamp("2013-7-7 18:31:00",
					"yyyy-MM-dd HH:mm"));
			log72.setOperatetype((short) 1);
			log72.setLoglevel((short) 5);
			commonDao.saveOrUpdate(log72);

			TSLog log73 = new TSLog();
			log73.setLogcontent("修改成功");
			log73.setBroswer("MSIE 8.0");
			log73.setNote("192.168.197.1");
			log73.setTSUser(admin);
			log73.setOperatetime(DateUtils.parseTimestamp("2013-7-7 18:31:26",
					"yyyy-MM-dd HH:mm"));
			log73.setOperatetype((short) 1);
			log73.setLoglevel((short) 5);
			commonDao.saveOrUpdate(log73);

			TSLog log74 = new TSLog();
			log74.setLogcontent("物品: 555添加成功");
			log74.setBroswer("MSIE 9.0");
			log74.setNote("192.168.1.103");
			log74.setTSUser(admin);
			log74.setOperatetime(DateUtils.parseTimestamp("2013-3-20 23:03:06",
					"yyyy-MM-dd HH:mm"));
			log74.setOperatetype((short) 1);
			log74.setLoglevel((short) 3);
			commonDao.saveOrUpdate(log74);

			TSLog log75 = new TSLog();
			log75.setLogcontent("用户: admin登录成功");
			log75.setBroswer("MSIE 9.0");
			log75.setNote("192.168.1.103");
			log75.setTSUser(admin);
			log75.setOperatetime(DateUtils.parseTimestamp("2013-3-20 23:19:25",
					"yyyy-MM-dd HH:mm"));
			log75.setOperatetype((short) 1);
			log75.setLoglevel((short) 1);
			commonDao.saveOrUpdate(log75);

			TSLog log76 = new TSLog();
			log76.setLogcontent("用户: admin登录成功");
			log76.setBroswer("MSIE 9.0");
			log76.setNote("192.168.1.103");
			log76.setTSUser(admin);
			log76.setOperatetime(DateUtils.parseTimestamp("2013-3-21 20:09:02",
					"yyyy-MM-dd HH:mm"));
			log76.setOperatetype((short) 1);
			log76.setLoglevel((short) 1);
			commonDao.saveOrUpdate(log76);

			TSLog log77 = new TSLog();
			log77.setLogcontent("用户: admin登录成功");
			log77.setBroswer("MSIE 9.0");
			log77.setNote("192.168.1.103");
			log77.setTSUser(admin);
			log77.setOperatetime(DateUtils.parseTimestamp("2013-3-21 20:27:25",
					"yyyy-MM-dd HH:mm"));
			log77.setOperatetype((short) 1);
			log77.setLoglevel((short) 1);
			commonDao.saveOrUpdate(log77);

			TSLog log78 = new TSLog();
			log78.setLogcontent("用户: admin登录成功");
			log78.setBroswer("MSIE 9.0");
			log78.setNote("192.168.1.103");
			log78.setTSUser(admin);
			log78.setOperatetime(DateUtils.parseTimestamp("2013-3-21 20:44:40",
					"yyyy-MM-dd HH:mm"));
			log78.setOperatetype((short) 1);
			log78.setLoglevel((short) 1);
			commonDao.saveOrUpdate(log78);

			TSLog log79 = new TSLog();
			log79.setLogcontent("用户: admin登录成功");
			log79.setBroswer("MSIE 9.0");
			log79.setNote("192.168.1.103");
			log79.setTSUser(admin);
			log79.setOperatetime(DateUtils.parseTimestamp("2013-3-21 20:54:13",
					"yyyy-MM-dd HH:mm"));
			log79.setOperatetype((short) 1);
			log79.setLoglevel((short) 1);
			commonDao.saveOrUpdate(log79);

			TSLog log80 = new TSLog();
			log80.setLogcontent("用户: admin登录成功");
			log80.setBroswer("MSIE 9.0");
			log80.setNote("192.168.1.103");
			log80.setTSUser(admin);
			log80.setOperatetime(DateUtils.parseTimestamp("2013-3-21 21:01:54",
					"yyyy-MM-dd HH:mm"));
			log80.setOperatetype((short) 1);
			log80.setLoglevel((short) 1);
			commonDao.saveOrUpdate(log80);

			TSLog log81 = new TSLog();
			log81.setLogcontent("用户: admin登录成功");
			log81.setBroswer("MSIE 9.0");
			log81.setNote("192.168.1.103");
			log81.setTSUser(admin);
			log81.setOperatetime(DateUtils.parseTimestamp("2013-3-21 21:13:05",
					"yyyy-MM-dd HH:mm"));
			log81.setOperatetype((short) 1);
			log81.setLoglevel((short) 1);
			commonDao.saveOrUpdate(log81);

			TSLog log82 = new TSLog();
			log82.setLogcontent("物品: 55添加成功");
			log82.setBroswer("MSIE 9.0");
			log82.setNote("192.168.1.103");
			log82.setTSUser(admin);
			log82.setOperatetime(DateUtils.parseTimestamp("2013-3-21 21:15:07",
					"yyyy-MM-dd HH:mm"));
			log82.setOperatetype((short) 1);
			log82.setLoglevel((short) 3);
			commonDao.saveOrUpdate(log82);

			TSLog log83 = new TSLog();
			log83.setLogcontent("用户: admin登录成功");
			log83.setBroswer("MSIE 9.0");
			log83.setNote("192.168.1.103");
			log83.setTSUser(admin);
			log83.setOperatetime(DateUtils.parseTimestamp("2013-3-21 21:22:57",
					"yyyy-MM-dd HH:mm"));
			log83.setOperatetype((short) 1);
			log83.setLoglevel((short) 1);
			commonDao.saveOrUpdate(log83);

			TSLog log84 = new TSLog();
			log84.setLogcontent("物品: 55添加成功");
			log84.setBroswer("MSIE 9.0");
			log84.setNote("192.168.1.103");
			log84.setTSUser(admin);
			log84.setOperatetime(DateUtils.parseTimestamp("2013-3-21 21:23:12",
					"yyyy-MM-dd HH:mm"));
			log84.setOperatetype((short) 1);
			log84.setLoglevel((short) 3);
			commonDao.saveOrUpdate(log84);

			TSLog log85 = new TSLog();
			log85.setLogcontent("物品: 33添加成功");
			log85.setBroswer("MSIE 9.0");
			log85.setNote("192.168.1.103");
			log85.setTSUser(admin);
			log85.setOperatetime(DateUtils.parseTimestamp("2013-3-21 21:23:47",
					"yyyy-MM-dd HH:mm"));
			log85.setOperatetype((short) 1);
			log85.setLoglevel((short) 3);
			commonDao.saveOrUpdate(log85);

			TSLog log86 = new TSLog();
			log86.setLogcontent("用户: admin登录成功");
			log86.setBroswer("MSIE 9.0");
			log86.setNote("192.168.1.103");
			log86.setTSUser(admin);
			log86.setOperatetime(DateUtils.parseTimestamp("2013-3-21 21:25:09",
					"yyyy-MM-dd HH:mm"));
			log86.setOperatetype((short) 1);
			log86.setLoglevel((short) 1);
			commonDao.saveOrUpdate(log86);

			TSLog log87 = new TSLog();
			log87.setLogcontent("用户: admin登录成功");
			log87.setBroswer("MSIE 9.0");
			log87.setNote("192.168.1.103");
			log87.setTSUser(admin);
			log87.setOperatetime(DateUtils.parseTimestamp("2013-3-21 21:27:58",
					"yyyy-MM-dd HH:mm"));
			log87.setOperatetype((short) 1);
			log87.setLoglevel((short) 1);
			commonDao.saveOrUpdate(log87);

			TSLog log88 = new TSLog();
			log88.setLogcontent("权限: 采购审批被添加成功");
			log88.setBroswer("MSIE 9.0");
			log88.setNote("192.168.1.103");
			log88.setTSUser(admin);
			log88.setOperatetime(DateUtils.parseTimestamp("2013-3-21 21:29:04",
					"yyyy-MM-dd HH:mm"));
			log88.setOperatetype((short) 1);
			log88.setLoglevel((short) 3);
			commonDao.saveOrUpdate(log88);

			TSLog log89 = new TSLog();
			log89.setLogcontent("权限: 采购审批被更新成功");
			log89.setBroswer("MSIE 9.0");
			log89.setNote("192.168.1.103");
			log89.setTSUser(admin);
			log89.setOperatetime(DateUtils.parseTimestamp("2013-3-21 21:29:56",
					"yyyy-MM-dd HH:mm"));
			log89.setOperatetype((short) 1);
			log89.setLoglevel((short) 5);
			commonDao.saveOrUpdate(log89);

			TSLog log90 = new TSLog();
			log90.setLogcontent("权限: 采购审批被更新成功");
			log90.setBroswer("MSIE 9.0");
			log90.setNote("192.168.1.103");
			log90.setTSUser(admin);
			log90.setOperatetime(DateUtils.parseTimestamp("2013-3-21 21:30:08",
					"yyyy-MM-dd HH:mm"));
			log90.setOperatetype((short) 1);
			log90.setLoglevel((short) 5);
			commonDao.saveOrUpdate(log90);

			TSLog log91 = new TSLog();
			log91.setLogcontent("用户: admin更新成功");
			log91.setBroswer("MSIE 9.0");
			log91.setNote("192.168.1.103");
			log91.setTSUser(admin);
			log91.setOperatetime(DateUtils.parseTimestamp("2013-3-21 21:31:21",
					"yyyy-MM-dd HH:mm"));
			log91.setOperatetype((short) 1);
			log91.setLoglevel((short) 5);
			commonDao.saveOrUpdate(log91);

			TSLog log92 = new TSLog();
			log92.setLogcontent("流程参数: 采购审批员审批被添加成功");
			log92.setBroswer("MSIE 9.0");
			log92.setNote("192.168.1.103");
			log92.setTSUser(admin);
			log92.setOperatetime(DateUtils.parseTimestamp("2013-3-21 21:36:03",
					"yyyy-MM-dd HH:mm"));
			log92.setOperatetype((short) 1);
			log92.setLoglevel((short) 3);
			commonDao.saveOrUpdate(log92);

			TSLog log93 = new TSLog();
			log93.setLogcontent("流程参数: 采购审批员审批被更新成功");
			log93.setBroswer("MSIE 9.0");
			log93.setNote("192.168.1.103");
			log93.setTSUser(admin);
			log93.setOperatetime(DateUtils.parseTimestamp("2013-3-21 21:36:11",
					"yyyy-MM-dd HH:mm"));
			log93.setOperatetype((short) 1);
			log93.setLoglevel((short) 5);
			commonDao.saveOrUpdate(log93);

			TSLog log94 = new TSLog();
			log94.setLogcontent("流程参数: 采购审批员审批被更新成功");
			log94.setBroswer("MSIE 9.0");
			log94.setNote("192.168.1.103");
			log94.setTSUser(admin);
			log94.setOperatetime(DateUtils.parseTimestamp("2013-3-21 21:37:16",
					"yyyy-MM-dd HH:mm"));
			log94.setOperatetype((short) 1);
			log94.setLoglevel((short) 5);
			commonDao.saveOrUpdate(log94);

			TSLog log95 = new TSLog();
			log95.setLogcontent("流程类别: 采购审批员审批被删除 成功");
			log95.setBroswer("MSIE 9.0");
			log95.setNote("192.168.1.103");
			log95.setTSUser(admin);
			log95.setOperatetime(DateUtils.parseTimestamp("2013-3-21 21:38:20",
					"yyyy-MM-dd HH:mm"));
			log95.setOperatetype((short) 1);
			log95.setLoglevel((short) 4);
			commonDao.saveOrUpdate(log95);

			TSLog log96 = new TSLog();
			log96.setLogcontent("物品: 44添加成功");
			log96.setBroswer("MSIE 9.0");
			log96.setNote("192.168.1.103");
			log96.setTSUser(admin);
			log96.setOperatetime(DateUtils.parseTimestamp("2013-3-21 21:43:51",
					"yyyy-MM-dd HH:mm"));
			log96.setOperatetype((short) 1);
			log96.setLoglevel((short) 3);
			commonDao.saveOrUpdate(log96);

			TSLog log97 = new TSLog();
			log97.setLogcontent("用户: admin登录成功");
			log97.setBroswer("MSIE 9.0");
			log97.setNote("192.168.1.105");
			log97.setTSUser(admin);
			log97.setOperatetime(DateUtils.parseTimestamp("2013-2-7 10:10:29",
					"yyyy-MM-dd HH:mm"));
			log97.setOperatetype((short) 1);
			log97.setLoglevel((short) 1);
			commonDao.saveOrUpdate(log97);

			TSLog log98 = new TSLog();
			log98.setLogcontent("权限: 上传下载被添加成功");
			log98.setBroswer("MSIE 9.0");
			log98.setNote("192.168.1.105");
			log98.setTSUser(admin);
			log98.setOperatetime(DateUtils.parseTimestamp("2013-2-7 11:07:26",
					"yyyy-MM-dd HH:mm"));
			log98.setOperatetype((short) 1);
			log98.setLoglevel((short) 3);
			commonDao.saveOrUpdate(log98);

			TSLog log99 = new TSLog();
			log99.setLogcontent("权限: 插件演示被删除成功");
			log99.setBroswer("MSIE 9.0");
			log99.setNote("192.168.1.105");
			log99.setTSUser(admin);
			log99.setOperatetime(DateUtils.parseTimestamp("2013-2-7 11:07:39",
					"yyyy-MM-dd HH:mm"));
			log99.setOperatetype((short) 1);
			log99.setLoglevel((short) 4);
			commonDao.saveOrUpdate(log99);

			TSLog log100 = new TSLog();
			log100.setLogcontent("用户: admin登录成功");
			log100.setBroswer("MSIE 9.0");
			log100.setNote("192.168.1.105");
			log100.setTSUser(admin);
			log100.setOperatetime(DateUtils.parseTimestamp("2013-2-7 11:07:54",
					"yyyy-MM-dd HH:mm"));
			log100.setOperatetype((short) 1);
			log100.setLoglevel((short) 1);
			commonDao.saveOrUpdate(log100);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	*/}



	/**
	 * @Description 修复User表
	 * @author tanghan 2013-7-23
	 */
	private void repairUser() {
		TSDepart eiu = commonDao.findAllByProperty(TSDepart.class, "departname",
				"信息部").get(0);
		TSDepart RAndD = commonDao.findAllByProperty(TSDepart.class, "departname",
				"研发室").get(0);

//        update-start--Author:zhangguoming  Date:20140825 for：用户表字段修改后，用户与部门的关系为多对多，这里修改初始化数据
		TSUser admin = new TSUser();
		admin.setSignatureFile("images/renfang/qm/licf.gif");
		admin.setStatus((short) 1);
		admin.setRealName("管理员");
		admin.setUserName("admin");
		admin.setPassword("c44b01947c9e6e3f");
//		admin.setTSDepart(eiu);
		admin.setActivitiSync((short) 1);
		commonDao.saveOrUpdate(admin);
        TSUserOrg adminUserOrg = new TSUserOrg();
        adminUserOrg.setTsUser(admin);
        adminUserOrg.setTsDepart(eiu);
        commonDao.save(adminUserOrg);

		TSUser scott = new TSUser();
		scott.setMobilePhone("13426432910");
		scott.setOfficePhone("7496661");
		scott.setEmail("zhangdaiscott@163.com");
		scott.setStatus((short) 1);
		scott.setRealName("张代浩");
		scott.setUserName("scott");
		scott.setPassword("97c07a884bf272b5");
//		scott.setTSDepart(RAndD);
		scott.setActivitiSync((short) 0);
		commonDao.saveOrUpdate(scott);
        TSUserOrg scottUserOrg = new TSUserOrg();
        scottUserOrg.setTsUser(scott);
        scottUserOrg.setTsDepart(eiu);
        commonDao.save(scottUserOrg);

		TSUser buyer = new TSUser();
		buyer.setStatus((short) 1);
		buyer.setRealName("采购员");
		buyer.setUserName("cgy");
		buyer.setPassword("f2322ec2fb9f40d1");
//		buyer.setTSDepart(eiu);
		buyer.setActivitiSync((short) 0);
		commonDao.saveOrUpdate(buyer);
        TSUserOrg buyerUserOrg = new TSUserOrg();
        buyerUserOrg.setTsUser(buyer);
        buyerUserOrg.setTsDepart(eiu);
        commonDao.save(buyerUserOrg);

		TSUser approver = new TSUser();
		approver.setStatus((short) 1);
		approver.setRealName("采购审批员");
		approver.setUserName("cgspy");
		approver.setPassword("a324509dc1a3089a");
//		approver.setTSDepart(eiu);
		approver.setActivitiSync((short) 1);
		commonDao.saveOrUpdate(approver);
        TSUserOrg approverUserOrg = new TSUserOrg();
        approverUserOrg.setTsUser(approver);
        approverUserOrg.setTsDepart(eiu);
        commonDao.save(approverUserOrg);
//        update-end--Author:zhangguoming  Date:20140825 for：用户表字段修改后，用户与部门的关系为多对多，这里修改初始化数据
    }

	/**
	 * @Description 修复用户角色表
	 * @author tanghan 2013-7-23
	 */
	private void repairUserRole() {
		TSRole admin = commonDao.findAllByProperty(TSRole.class, "roleCode",
				"admin").get(0);
		TSRole manager = commonDao.findAllByProperty(TSRole.class, "roleCode",
				"manager").get(0);
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
		TSRole admin = commonDao.findAllByProperty(TSRole.class, "roleCode",
				"admin").get(0);
		TSRole manager = commonDao.findAllByProperty(TSRole.class, "roleCode",
				"manager").get(0);
		List<TSFunction> list = commonDao.findAll(TSFunction.class);
		for (int i = 0; i < list.size(); i++) {
			TSRoleFunction adminroleFunction = new TSRoleFunction();
			TSRoleFunction managerFunction = new TSRoleFunction();
			adminroleFunction.setTSFunction(list.get(i));
			managerFunction.setTSFunction(list.get(i));
			adminroleFunction.setTSRole(admin);
			managerFunction.setTSRole(manager);
			if (list.get(i).getFunctionName().equals("Demo示例")) {
				adminroleFunction.setOperation("add,szqm,");
			}
			commonDao.saveOrUpdate(adminroleFunction);
			commonDao.saveOrUpdate(managerFunction);
		}
	}

	/**
	 * @Description 修复操作按钮表
	 * @author tanghan 2013-7-23
	 */
	private void repairOperation() {
		TSIcon back = commonDao.findAllByProperty(TSIcon.class, "iconName", "返回")
				.get(0);
		TSFunction function = commonDao.findAllByProperty(TSFunction.class,
				"functionName", "Demo示例").get(0);

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
		TSTypegroup icontype = commonDao.findAllByProperty(TSTypegroup.class,
				"typegroupname", "图标类型").get(0);
		TSTypegroup ordertype = commonDao.findAllByProperty(TSTypegroup.class,
				"typegroupname", "订单类型").get(0);
		TSTypegroup custom = commonDao.findAllByProperty(TSTypegroup.class,
				"typegroupname", "客户类型").get(0);
		TSTypegroup servicetype = commonDao.findAllByProperty(TSTypegroup.class,
				"typegroupname", "服务项目类型").get(0);
		TSTypegroup datatable = commonDao.findAllByProperty(TSTypegroup.class,
				"typegroupname", "数据表").get(0);
		TSTypegroup filetype = commonDao.findAllByProperty(TSTypegroup.class,
				"typegroupname", "文档分类").get(0);
		TSTypegroup sex = commonDao.findAllByProperty(TSTypegroup.class,
				"typegroupname", "性别类").get(0);
		TSTypegroup searchmode = commonDao.findAllByProperty(TSTypegroup.class,
				"typegroupname", "查询模式").get(0);
		TSTypegroup yesorno = commonDao.findAllByProperty(TSTypegroup.class,
				"typegroupname", "逻辑条件").get(0);
		TSTypegroup fieldtype = commonDao.findAllByProperty(TSTypegroup.class,
				"typegroupname", "字段类型").get(0);

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

		// TSType leave = new TSType();
		// leave.setTypename("请假流程");
		// leave.setTypecode("leave");
		// commonDao.saveOrUpdate(leave);

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

		TSType workflow = new TSType();
		workflow.setTypename("工作流引擎表");
		workflow.setTypecode("act");
		workflow.setTSTypegroup(datatable);
		commonDao.saveOrUpdate(workflow);

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

		TSType customwork = new TSType();
		customwork.setTypename("自定义引擎表");
		customwork.setTypecode("t_p");
		customwork.setTSTypegroup(datatable);
		commonDao.saveOrUpdate(customwork);

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
	 * @Description 修复部门表
	 * @author tanghan 2013-7-20
	 */
	private void repairDepart() {
		TSDepart eiu = new TSDepart();
		eiu.setDepartname("信息部");
		eiu.setDescription("12");
		commonDao.saveOrUpdate(eiu);

		TSDepart desgin = new TSDepart();
		desgin.setDepartname("设计部");
		commonDao.saveOrUpdate(desgin);

		TSDepart RAndD = new TSDepart();
		RAndD.setDepartname("研发室");
		RAndD.setDescription("研发技术难题");
		RAndD.setTSPDepart(desgin);
		commonDao.saveOrUpdate(RAndD);
	}

	/**
	 * @Description 修复附件表
	 * @author tanghan 2013-7-20
	 */
	private void repairAttachment() {
		TSAttachment jro = new TSAttachment();
		jro.setAttachmenttitle("JR079839867R90000001000");
		jro.setRealpath("JR079839867R90000001000");
		jro.setSwfpath("upload/files/20130719201109hDr31jP1.swf");
		jro.setExtend("doc");
		commonDao.saveOrUpdate(jro);

		TSAttachment treaty = new TSAttachment();
		treaty.setAttachmenttitle("JEECG平台协议");
		treaty.setRealpath("JEECG平台协议");
		treaty.setSwfpath("upload/files/20130719201156sYHjSFJj.swf");
		treaty.setExtend("docx");
		commonDao.saveOrUpdate(treaty);

		TSAttachment analyse = new TSAttachment();
		analyse.setAttachmenttitle("分析JEECG与其他的开源项目的不足和优势");
		analyse.setRealpath("分析JEECG与其他的开源项目的不足和优势");
		analyse.setSwfpath("upload/files/20130719201727ZLEX1OSf.swf");
		analyse.setExtend("docx");
		commonDao.saveOrUpdate(analyse);

		TSAttachment DMS = new TSAttachment();
		DMS.setAttachmenttitle("DMS-T3第三方租赁业务接口开发说明");
		DMS.setRealpath("DMS-T3第三方租赁业务接口开发说明");
		DMS.setSwfpath("upload/files/20130719201841LzcgqUek.swf");
		DMS.setExtend("docx");
		commonDao.saveOrUpdate(DMS);

		TSAttachment sap = new TSAttachment();
		sap.setAttachmenttitle("SAP-需求说明书-金融服务公司-第三方租赁业务需求V1.7-研发");
		sap.setRealpath("SAP-需求说明书-金融服务公司-第三方租赁业务需求V1.7-研发");
		sap.setSwfpath("upload/files/20130719201925mkCrU47P.swf");
		sap.setExtend("doc");
		commonDao.saveOrUpdate(sap);

		TSAttachment standard = new TSAttachment();
		standard.setAttachmenttitle("JEECG团队开发规范");
		standard.setRealpath("JEECG团队开发规范");
		standard.setSwfpath("upload/files/20130724103633fvOTwNSV.swf");
		standard.setExtend("txt");
		commonDao.saveOrUpdate(standard);

		TSAttachment temple = new TSAttachment();
		temple.setAttachmenttitle("第一模板");
		temple.setRealpath("第一模板");
		temple.setSwfpath("upload/files/20130724104603pHDw4QUT.swf");
		temple.setExtend("doc");
		commonDao.saveOrUpdate(temple);

		TSAttachment githubhelp = new TSAttachment();
		githubhelp.setAttachmenttitle("github入门使用教程");
		githubhelp.setRealpath("github入门使用教程");
		githubhelp.setSwfpath("upload/files/20130704200345EakUH3WB.swf");
		githubhelp.setExtend("doc");
		commonDao.saveOrUpdate(githubhelp);

		TSAttachment githelp = new TSAttachment();
		githelp.setAttachmenttitle("github入门使用教程");
		githelp.setRealpath("github入门使用教程");
		githelp.setSwfpath("upload/files/20130704200651IE8wPdZ4.swf");
		githelp.setExtend("doc");
		commonDao.saveOrUpdate(githelp);

		TSAttachment settable = new TSAttachment();
		settable.setAttachmenttitle("（张代浩）-金融服务公司机构岗位职责与任职资格设置表(根据模板填写）");
		settable.setRealpath("（张代浩）-金融服务公司机构岗位职责与任职资格设置表(根据模板填写）");
		settable.setSwfpath("upload/files/20130704201022KhdRW1Gd.swf");
		settable.setExtend("xlsx");
		commonDao.saveOrUpdate(settable);

		TSAttachment eim = new TSAttachment();
		eim.setAttachmenttitle("EIM201_CN");
		eim.setRealpath("EIM201_CN");
		eim.setSwfpath("upload/files/20130704201046JVAkvvOt.swf");
		eim.setExtend("pdf");
		commonDao.saveOrUpdate(eim);

		TSAttachment github = new TSAttachment();
		github.setAttachmenttitle("github入门使用教程");
		github.setRealpath("github入门使用教程");
		github.setSwfpath("upload/files/20130704201116Z8NhEK57.swf");
		github.setExtend("doc");
		commonDao.saveOrUpdate(github);

		TSAttachment taghelp = new TSAttachment();
		taghelp.setAttachmenttitle("JEECGUI标签库帮助文档v3.2");
		taghelp.setRealpath("JEECGUI标签库帮助文档v3.2");
		taghelp.setSwfpath("upload/files/20130704201125DQg8hi2x.swf");
		taghelp.setExtend("pdf");
		commonDao.saveOrUpdate(taghelp);
	}

	/**
	 * @Description 修复图标表
	 * @author tanghan 2013-7-19
	 */
	private void repaireIcon() {
		org.jeecgframework.core.util.LogUtil.info("修复图标中");

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

//    update-begin--Author:zhangguoming  Date:20140522 for：云桌面默认图标初始化
    /**
     * 修复桌面默认图标
     * @param iconName 图标名称
     * @param iconLabelName 图标展示名称
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
//    update-end--Author:zhangguoming  Date:20140522 for：云桌面默认图标初始化

	/**
	 * @Description 修复菜单权限
	 * @author tanghan 2013-7-19
	 */
	private void repairMenu() {
		TSIcon defaultIcon = commonDao.findAllByProperty(TSIcon.class, "iconName", "默认图")
				.get(0);
		TSIcon group_add = commonDao.findAllByProperty(TSIcon.class, "iconName",
				"组").get(0);
		TSIcon pie = commonDao.findAllByProperty(TSIcon.class, "iconName", "饼图")
				.get(0);
		TSIcon folder = commonDao.findAllByProperty(TSIcon.class, "iconName",
				"文件夹").get(0);
		org.jeecgframework.core.util.LogUtil.info(defaultIcon.getIconPath());
		TSFunction autoinput = new TSFunction();
		autoinput.setFunctionName("Online 开发");
		autoinput.setFunctionUrl("");
		autoinput.setFunctionLevel((short) 0);
		autoinput.setFunctionOrder("1");
		autoinput.setTSIcon(folder);
		commonDao.saveOrUpdate(autoinput);

		TSFunction sys = new TSFunction();
		sys.setFunctionName("系统管理");
		sys.setFunctionUrl("");
		sys.setFunctionLevel((short) 0);
		sys.setFunctionOrder("5");
		sys.setTSIcon(group_add);
		commonDao.saveOrUpdate(sys);

		TSFunction state = new TSFunction();
		state.setFunctionName("统计查询");
		state.setFunctionUrl("");
		state.setFunctionLevel((short) 0);
		state.setFunctionOrder("3");
		state.setTSIcon(folder);
		commonDao.saveOrUpdate(state);

		TSFunction commondemo = new TSFunction();
		commondemo.setFunctionName("常用示例");
		commondemo.setFunctionUrl("");
		commondemo.setFunctionLevel((short) 0);
		commondemo.setFunctionOrder("8");
		commondemo.setTSIcon(defaultIcon);
		commonDao.saveOrUpdate(commondemo);

		TSFunction syscontrol = new TSFunction();
		syscontrol.setFunctionName("系统监控");
		syscontrol.setFunctionUrl("");
		syscontrol.setFunctionLevel((short) 0);
		syscontrol.setFunctionOrder("11");
		syscontrol.setTSIcon(defaultIcon);
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
		commonDao.saveOrUpdate(depart);
		
		TSFunction territory = new TSFunction();
		territory.setFunctionName("地域管理");
		territory.setFunctionUrl("territoryController.do?territory");
		territory.setFunctionLevel((short) 1);
		territory.setFunctionOrder("22");
		territory.setTSFunction(sys);
		territory.setTSIcon(pie);
		commonDao.saveOrUpdate(territory);
		TSFunction useranalyse = new TSFunction();
		useranalyse.setFunctionName("用户分析");
		useranalyse.setFunctionUrl("logController.do?statisticTabs&isIframe");
		useranalyse.setFunctionLevel((short) 1);
		useranalyse.setFunctionOrder("17");
		useranalyse.setTSFunction(state);
		useranalyse.setTSIcon(pie);
		useranalyse.setTSIconDesk(repairInconForDesk("User", "用户分析"));
		commonDao.saveOrUpdate(useranalyse);
		TSFunction formconfig = new TSFunction();
		formconfig.setFunctionName("表单配置");
		formconfig.setFunctionUrl("cgFormHeadController.do?cgFormHeadList");
		formconfig.setFunctionLevel((short) 1);
		formconfig.setFunctionOrder("1");
		formconfig.setTSFunction(autoinput);
		formconfig.setTSIcon(defaultIcon);
		formconfig.setTSIconDesk(repairInconForDesk("Applications Folder", "表单配置"));
		commonDao.saveOrUpdate(formconfig);
		TSFunction formconfig1 = new TSFunction();
		formconfig1.setFunctionName("动态报表配置");
		formconfig1.setFunctionUrl("cgreportConfigHeadController.do?cgreportConfigHead");
		formconfig1.setFunctionLevel((short) 1);
		formconfig1.setFunctionOrder("2");
		formconfig1.setTSFunction(autoinput);
		formconfig1.setTSIcon(defaultIcon);
		commonDao.saveOrUpdate(formconfig1);
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
		TSFunction formcheck = new TSFunction();
		formcheck.setFunctionName("表单验证");
		formcheck.setFunctionUrl("demoController.do?formTabs");
		formcheck.setFunctionLevel((short) 1);
		formcheck.setFunctionOrder("1");
		formcheck.setTSFunction(commondemo);
		formcheck.setTSIcon(defaultIcon);
		formcheck.setTSIconDesk(repairInconForDesk("qidianzhongwen", "表单验证"));
		commonDao.saveOrUpdate(formcheck);

		TSFunction demo = new TSFunction();
		demo.setFunctionName("Demo示例");
		demo.setFunctionUrl("jeecgDemoController.do?jeecgDemo");
		demo.setFunctionLevel((short) 1);
		demo.setFunctionOrder("2");
		demo.setTSFunction(commondemo);
		demo.setTSIcon(defaultIcon);
		commonDao.saveOrUpdate(demo);

		TSFunction onetable = new TSFunction();
		onetable.setFunctionName("单表模型");
		onetable.setFunctionUrl("jeecgNoteController.do?jeecgNote");
		onetable.setFunctionLevel((short) 1);
		onetable.setFunctionOrder("3");
		onetable.setTSFunction(commondemo);
		onetable.setTSIcon(defaultIcon);
		commonDao.saveOrUpdate(onetable);

		TSFunction onetoMany = new TSFunction();
		onetoMany.setFunctionName("一对多模型");
		onetoMany.setFunctionUrl("jeecgOrderMainController.do?jeecgOrderMain");
		onetoMany.setFunctionLevel((short) 1);
		onetoMany.setFunctionOrder("4");
		onetoMany.setTSFunction(commondemo);
		onetoMany.setTSIcon(defaultIcon);
		commonDao.saveOrUpdate(onetoMany);

		TSFunction excel = new TSFunction();
		excel.setFunctionName("Excel导入导出");
		excel.setFunctionUrl("courseController.do?course");
		excel.setFunctionLevel((short) 1);
		excel.setFunctionOrder("5");
		excel.setTSFunction(commondemo);
		excel.setTSIcon(defaultIcon);
		commonDao.saveOrUpdate(excel);

		TSFunction uploadownload = new TSFunction();
		uploadownload.setFunctionName("上传下载");
		uploadownload
				.setFunctionUrl("commonController.do?listTurn&turn=system/document/filesList");
		uploadownload.setFunctionLevel((short) 1);
		uploadownload.setFunctionOrder("6");
		uploadownload.setTSFunction(commondemo);
		uploadownload.setTSIcon(defaultIcon);
		commonDao.saveOrUpdate(uploadownload);
		
		TSFunction jqueryFileUpload = new TSFunction();
		jqueryFileUpload.setFunctionName("JqueryFileUpload示例");
		jqueryFileUpload.setFunctionUrl("fileUploadController.do?fileUploadSample&isIframe");
		jqueryFileUpload.setFunctionLevel((short) 1);
		jqueryFileUpload.setFunctionOrder("6");
		jqueryFileUpload.setTSFunction(commondemo);
		jqueryFileUpload.setTSIcon(defaultIcon);
		commonDao.saveOrUpdate(jqueryFileUpload);

		TSFunction nopaging = new TSFunction();
		nopaging.setFunctionName("无分页列表");
		nopaging.setFunctionUrl("userNoPageController.do?user");
		nopaging.setFunctionLevel((short) 1);
		nopaging.setFunctionOrder("7");
		nopaging.setTSIcon(defaultIcon);
		nopaging.setTSFunction(commondemo);
		commonDao.saveOrUpdate(nopaging);

		TSFunction jdbcdemo = new TSFunction();
		jdbcdemo.setFunctionName("jdbc示例");
		jdbcdemo.setFunctionUrl("jeecgJdbcController.do?jeecgJdbc");
		jdbcdemo.setFunctionLevel((short) 1);
		jdbcdemo.setFunctionOrder("8");
		jdbcdemo.setTSFunction(commondemo);
		jdbcdemo.setTSIcon(defaultIcon);
		commonDao.saveOrUpdate(jdbcdemo);
		
		TSFunction sqlsep = new TSFunction();
		sqlsep.setFunctionName("SQL分离");
		sqlsep.setFunctionUrl("jeecgJdbcController.do?dictParameter");
		sqlsep.setFunctionLevel((short) 1);
		sqlsep.setTSIcon(defaultIcon);
		sqlsep.setFunctionOrder("9");
		sqlsep.setTSFunction(commondemo);
		commonDao.saveOrUpdate(sqlsep);

		TSFunction dicttag = new TSFunction();
		dicttag.setFunctionName("字典标签");
		dicttag.setFunctionUrl("demoController.do?dictSelect");
		dicttag.setFunctionLevel((short) 1);
		dicttag.setFunctionOrder("10");
		dicttag.setTSFunction(commondemo);
		dicttag.setTSIcon(defaultIcon);
		commonDao.saveOrUpdate(dicttag);

		TSFunction demomaintain = new TSFunction();
		demomaintain.setFunctionName("表单弹出风格");
		demomaintain.setFunctionUrl("demoController.do?demoList");
		demomaintain.setFunctionLevel((short) 1);
		demomaintain.setFunctionOrder("11");
		demomaintain.setTSFunction(commondemo);
		demomaintain.setTSIcon(defaultIcon);
		commonDao.saveOrUpdate(demomaintain);

		TSFunction democlassify = new TSFunction();
		democlassify.setFunctionName("特殊布局");
		democlassify.setFunctionUrl("demoController.do?demoIframe");
		democlassify.setFunctionLevel((short) 1);
		democlassify.setFunctionOrder("12");
		democlassify.setTSFunction(commondemo);
		democlassify.setTSIcon(defaultIcon);
		democlassify.setTSIconDesk(repairInconForDesk("xiami", "特殊布局"));
		commonDao.saveOrUpdate(democlassify);

		TSFunction notag1 = new TSFunction();
		notag1.setFunctionName("单表例子(无Tag)");
		notag1.setFunctionUrl("jeecgEasyUIController.do?jeecgEasyUI");
		notag1.setFunctionLevel((short) 1);
		notag1.setFunctionOrder("13");
		notag1.setTSFunction(commondemo);
		notag1.setTSIcon(defaultIcon);
		commonDao.saveOrUpdate(notag1);

		TSFunction notag2 = new TSFunction();
		notag2.setFunctionName("一对多例子(无Tag)");
		notag2.setFunctionUrl("jeecgOrderMainNoTagController.do?jeecgOrderMainNoTag");
		notag2.setFunctionLevel((short) 1);
		notag2.setFunctionOrder("14");
		notag2.setTSFunction(commondemo);
		notag2.setTSIcon(defaultIcon);
		commonDao.saveOrUpdate(notag2);

		TSFunction htmledit = new TSFunction();
		htmledit.setFunctionName("HTML编辑器");
		htmledit.setFunctionUrl("jeecgDemoController.do?ckeditor&isIframe");
		htmledit.setFunctionLevel((short) 1);
		htmledit.setFunctionOrder("15");
		htmledit.setTSFunction(commondemo);
		htmledit.setTSIcon(defaultIcon);
		commonDao.saveOrUpdate(htmledit);

		TSFunction weboffice = new TSFunction();
		weboffice.setFunctionName("在线word(IE)");
		weboffice.setFunctionUrl("webOfficeController.do?webOffice");
		weboffice.setFunctionLevel((short) 1);
		weboffice.setFunctionOrder("16");
		weboffice.setTSFunction(commondemo);
		weboffice.setTSIcon(defaultIcon);
		commonDao.saveOrUpdate(weboffice);

		TSFunction Office = new TSFunction();
		Office.setFunctionName("WebOffice官方例子");
		Office.setFunctionUrl("webOfficeController.do?webOfficeSample&isIframe");
		Office.setFunctionLevel((short) 1);
		Office.setFunctionOrder("17");
		Office.setTSIcon(defaultIcon);
		Office.setTSFunction(commondemo);
		commonDao.saveOrUpdate(Office);

		TSFunction finance = new TSFunction();
		finance.setFunctionName("多附件管理");
		finance.setFunctionUrl("tFinanceController.do?tFinance");
		finance.setFunctionLevel((short) 1);
		finance.setFunctionOrder("18");
		finance.setTSFunction(commondemo);
		finance.setTSIcon(defaultIcon);
        finance.setTSIconDesk(repairInconForDesk("vadio", "多附件管理"));
		commonDao.saveOrUpdate(finance);

		TSFunction userdemo = new TSFunction();
		userdemo.setFunctionName("Datagrid手工Html");
		userdemo.setFunctionUrl("userController.do?userDemo");
		userdemo.setFunctionLevel((short) 1);
		userdemo.setFunctionOrder("19");
		userdemo.setTSFunction(commondemo);
		userdemo.setTSIcon(defaultIcon);
		commonDao.saveOrUpdate(userdemo);
		TSFunction matterBom = new TSFunction();
		matterBom.setFunctionName("物料Bom");
		matterBom.setFunctionUrl("jeecgMatterBomController.do?goList");
		matterBom.setFunctionLevel((short) 1);
		matterBom.setFunctionOrder("20");
		matterBom.setTSFunction(commondemo);
		matterBom.setTSIcon(defaultIcon);
		commonDao.saveOrUpdate(matterBom);
		TSFunction reportdemo = new TSFunction();
		reportdemo.setFunctionName("报表示例");
		reportdemo.setFunctionUrl("reportDemoController.do?studentStatisticTabs&isIframe");
		reportdemo.setFunctionLevel((short) 1);
		reportdemo.setFunctionOrder("21");
		reportdemo.setTSFunction(state);
		reportdemo.setTSIcon(pie);
		commonDao.saveOrUpdate(reportdemo);
		
		TSFunction ckfinder = new TSFunction();
		ckfinder.setFunctionName("ckfinder例子");
		ckfinder.setFunctionUrl("jeecgDemoCkfinderController.do?jeecgDemoCkfinder");
		ckfinder.setFunctionLevel((short) 1);
		ckfinder.setFunctionOrder("100");
		ckfinder.setTSFunction(commondemo);
		ckfinder.setTSIcon(defaultIcon);
		commonDao.saveOrUpdate(ckfinder);
	}
}
