<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
<div region="center" style="padding: 1px;">
	<t:datagrid name="timeTaskList" title="schedule.task.manage" actionUrl="timeTaskController.do?datagrid" idField="id" fit="true">
	<t:dgCol title="common.id" field="id" hidden="true"></t:dgCol>
	<t:dgCol title="common.taskid" field="taskId" width="20"></t:dgCol>
	<t:dgCol title="cron.expression" field="cronExpression" width="10"></t:dgCol>
	<t:dgCol title="common.task.desc" field="taskDescribe" width="30"></t:dgCol>
	<t:dgCol title="common.iseffect" field="isEffect" replace="未生效_0,已生效_1" width="8"></t:dgCol>
	<t:dgCol title="running.state" field="isStart" replace="停止_0,运行_1" width="8"></t:dgCol>

	<%--<t:dgCol title="common.createby" field="createBy" width="8"></t:dgCol>
	<t:dgCol title="common.createtime" field="createDate" formatter="yyyy-MM-dd"></t:dgCol>
	<t:dgCol title="common.updateby" field="updateBy"></t:dgCol>
	<t:dgCol title="common.updatetime" field="updateDate" formatter="yyyy-MM-dd"></t:dgCol>--%>
	<t:dgCol title="common.operation" field="opt" width="30"></t:dgCol>
	<t:dgConfOpt title="启动任务" url="timeTaskController.do?startOrStopTask&id={id}&isStart=1" message="确认运行任务吗" exp="isStart#eq#0"/>
	<t:dgConfOpt title="停止任务" url="timeTaskController.do?startOrStopTask&id={id}&isStart=0" message="确认停止任务吗" exp="isStart#eq#1"/>
	<t:dgConfOpt title="生效任务" url="timeTaskController.do?updateTime&id={id}" message="确认生效该任务吗" exp="isEffect#eq#0"/>
	<t:dgDelOpt title="common.delete" url="timeTaskController.do?del&id={id}" />
	<t:dgToolBar title="common.add" icon="icon-add" url="timeTaskController.do?addorupdate" funname="add"></t:dgToolBar>
	<t:dgToolBar title="common.edit" icon="icon-edit" url="timeTaskController.do?addorupdate" funname="update"></t:dgToolBar>
	<t:dgToolBar title="common.view" icon="icon-search" url="timeTaskController.do?addorupdate" funname="detail"></t:dgToolBar>
</t:datagrid></div>
</div>
