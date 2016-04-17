<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:1px;">
  <t:datagrid name="templateList" title="模版管理" actionUrl="templateController.do?datagrid" idField="id" fit="true">
   <t:dgCol title="编号" field="id" hidden="true"></t:dgCol>
   <t:dgCol title="模版编码" field="code" width="20"></t:dgCol>
   <t:dgCol title="模版名称" field="name"  width="20"></t:dgCol>
   <t:dgCol title="模版预览" field="image" image="true" imageSize="72,72"></t:dgCol>
   <t:dgCol title="状态" field="status"  replace="未设置_0,已设置_1" width="8"></t:dgCol>
   <t:dgCol title="操作" field="opt" width="10"></t:dgCol>
   <t:dgConfOpt title="设置" url="templateController.do?setting&id={id}"  exp="status#eq#0" message="确定设置吗" />
   <t:dgConfOpt title="设置" url=""  exp="status#eq#1" message="必须保留一个" />
   <t:dgToolBar title="录入" icon="icon-add" url="templateController.do?addorupdate" funname="add"></t:dgToolBar>
   <t:dgToolBar title="编辑" icon="icon-edit" url="templateController.do?addorupdate" funname="update"></t:dgToolBar>
   <t:dgToolBar title="查看" icon="icon-search" url="templateController.do?addorupdate" funname="detail"></t:dgToolBar>
  </t:datagrid>
  </div>
 </div>