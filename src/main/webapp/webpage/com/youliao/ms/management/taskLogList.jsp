<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:1px;">
  <t:datagrid name="taskLogList" title="任务执行日志" actionUrl="taskLogController.do?datagrid" idField="id" fit="true" queryMode="group">
   <t:dgCol title="编号" field="id" hidden="true"></t:dgCol>
   <t:dgCol title="任务ID" field="taskId" query="true"></t:dgCol>
   <t:dgCol title="分组ID" field="groupNumber" query="true"></t:dgCol>
   <t:dgCol title="执行轮次" field="orderIndex" ></t:dgCol>
   <t:dgCol title="创建时间" field="createTime" formatter="yyyy-MM-dd hh:mm:ss"></t:dgCol>
   <t:dgCol title="执行结果" replace="失败_0,成功_1" field="isSuccess" ></t:dgCol>
   <t:dgCol title="结果描述" field="description" width="30"></t:dgCol>
   <%-- <t:dgCol title="操作" field="opt" width="100"></t:dgCol>
   <t:dgDelOpt title="删除" url="taskLogController.do?del&id={id}" />
   <t:dgToolBar title="录入" icon="icon-add" url="taskLogController.do?addorupdate" funname="add"></t:dgToolBar>
   <t:dgToolBar title="编辑" icon="icon-edit" url="taskLogController.do?addorupdate" funname="update"></t:dgToolBar>
    --%>
    <t:dgToolBar title="详情" icon="icon-search" url="taskLogController.do?addorupdate" funname="detail"></t:dgToolBar>
  </t:datagrid>
  </div>
 </div>