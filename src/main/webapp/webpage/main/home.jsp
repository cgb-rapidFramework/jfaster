<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<%
	String lang =org.jeecgframework.web.utils.BrowserUtils.getBrowserLanguage(request);
	String langurl = "plug-in/mutiLang/" + lang +".js";
%>
<div style="margin-top: 14px;text-align: center;">
       欢迎使用，<t:mutiLang langKey="system.right"/>—<t:mutiLang langKey="system.name"/>
</div>
<!-- <div style="margin-top: 14px;">
<h3>Bms开源系统简介</h3>
<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;JEECG（J2EE Code Generation）是一款基于代码生成器的微云快速开发平台，引领新开发模式(智能开发\在线开发\插件开发)， 可以帮助解决Java项目60%的重复工作，让开发更多关注业务逻辑。既能快速提高开发效率，帮助公司节省人力成本，同时又不失灵活性。 <br>
&nbsp;&nbsp;&nbsp;&nbsp;JEECG快速开发宗旨是：简单功能由代码生成器生成使用; 复杂业务采用表单自定义，业务流程使用工作流来实现、扩展出任务接口，供开发编写业务逻辑。 实现了流程任务节点和任务接口的灵活配置，既保证了公司流程的保密性，又减少了开发人员的工作量</p>
<h3>架构说明</h3>
<ul>
	<li>
	表单处理等功能，再配合代码生成器的使用,将J2EE的开发效率提高6倍以上，可以将代码减少60%以上。</li>
	<li>JEECG 最新版本六大技术点: <b>1.代码生成器</b> <b>2.微信移动解决方案</b> <b>3.OnlineCoding在线开发</b> <b>4.在线流程设计</b> <b>5.Web GIS支持</b> <b>6.UI快速开发库</b></li>
	<li>技术点一：代码生成器，支持多种数据模型,根据表生成对应的Entity,Service,Dao,Action,JSP等,增删改查功能生成直接使用</li>
	<li>技术点二：微信移动解决方案，采用微信平台帮助企业实现移动解决方案 </li>
	<li>技术点三：在线流程定义，采用开源 Activiti流程引擎，实现在线画流程,自定义表单,表单挂接,业务流转，流程监控，流程跟踪，流程委托等</li>
	<li>技术点四：Web GIS支持，采用采用OpenLayers技术 </li>
	<li>技术点五：UI快速开发库，针对WEB UI进行标准封装，页面统一采用UI标签实现功能：数据datagrid,表单校验,Popup,Tab等，实现JSP页面零JS，开发维护非常高效</li>
	<li>JEECG 开发平台,经过了专业压力测试,性能测试，保证后台数据的准确性和页面访问速度</li>
	<li>支持多种浏览器: IE, 火狐, Google 等浏览器访问速度都很快</li>
	<li>支持数据库: Mysql,Oracle10g等</li>
	<li>基础权限: 用户，角色，菜单权限，按钮权限，数据权限</li>
	<li>智能报表集成: 简易的图像报表工具和Excel导入导出</li>
	<li>Web容器测试通过的有Jetty和Tomcat6</li>
	<li>待推出功能：分布式计算，云计算，微信管家开源项目，规则引擎</li>
	<li>要求JDK1.6+</li>
</ul>
</div>
<div style="margin-top: 20px;">
<h3>技术交流</h3>
<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;本系统由JEECG开源社区提供，JEECG微云快速开发平台，永久开源免费，为大家提供最好的<b>企业二次开发平台</b></p>
</div> -->