<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:1px;">
       <t:datagrid name="jobList" title="定时任务" actionUrl="jobController.do?datagrid" idField="id" fit="true" queryMode="group">
       <t:dgCol title="ID" field="id" hidden="true"></t:dgCol>
       <t:dgCol title="名称" field="name" query="true" width="12"></t:dgCol>
       <t:dgCol title="分组" field="group" query="true" width="12"></t:dgCol>
       <t:dgCol title="状态" field="status" query="true" replace="暂停_1,运行_0,废止_2" width="10" ></t:dgCol>
       <t:dgCol title="表达式" field="expression" width="15"></t:dgCol>
       <t:dgCol title="实现类" field="clazz" width="45"></t:dgCol>
       <t:dgCol title="描述" field="description" width="18"></t:dgCol>
       <t:dgCol title="操作" field="opt" width="30"></t:dgCol>
       <t:dgConfOpt title="暂停" url="jobController.do?pause&id={id}" message="确定暂停吗" />
       <t:dgConfOpt title="恢复" url="jobController.do?resume&id={id}" message="确定恢复吗" />
       <t:dgConfOpt title="废止" url="jobController.do?del&id={id}" message="确定废止吗" />
       <t:dgConfOpt title="立即运行一次" url="jobController.do?runOnce&id={id}" message="确定立即运行一次吗" />
       <t:dgToolBar title="录入" icon="icon-add" url="jobController.do?addorupdate" funname="add"></t:dgToolBar>
       <t:dgToolBar title="编辑" icon="icon-edit" url="jobController.do?addorupdate" funname="update"></t:dgToolBar>
       <t:dgToolBar title="查看" icon="icon-search" url="jobController.do?addorupdate" funname="detail"></t:dgToolBar>
      </t:datagrid>
  </div>
 </div>