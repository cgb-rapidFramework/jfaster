<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:1px;">
       <t:datagrid name="jobList" title="任务管理" actionUrl="jobController.do?datagrid" idField="id" fit="true" queryMode="group">
       <t:dgCol title="编号" field="id" hidden="true"></t:dgCol>
       <t:dgCol title="任务名称" field="name" query="true" ></t:dgCol>
       <t:dgCol title="任务分组" field="group" query="true" ></t:dgCol>
       <t:dgCol title="任务状态" field="status" query="true" dictionary="jobstatus" width="50" ></t:dgCol>
       <t:dgCol title="任务运行时间表达式" field="expression" ></t:dgCol>
       <t:dgCol title="是否异步" field="isSync" dictionary="jobissync" ></t:dgCol>
       <t:dgCol title="任务描述" field="description" ></t:dgCol>
       <t:dgCol title="操作" field="opt" width="100"></t:dgCol>
       <t:dgDelOpt title="删除" url="jobController.do?del&id={id}" />
       <t:dgDelOpt title="暂停" url="jobController.do?pause&id={id}" />
       <t:dgDelOpt title="恢复" url="jobController.do?resume&id={id}" />
       <t:dgDelOpt title="立即运行一次" url="jobController.do?runOnce&id={id}" />
       <t:dgToolBar title="录入" icon="icon-add" url="jobController.do?addorupdate" funname="add"></t:dgToolBar>
       <t:dgToolBar title="编辑" icon="icon-edit" url="jobController.do?addorupdate" funname="update"></t:dgToolBar>
       <t:dgToolBar title="查看" icon="icon-search" url="jobController.do?addorupdate" funname="detail"></t:dgToolBar>
      </t:datagrid>
  </div>
 </div>