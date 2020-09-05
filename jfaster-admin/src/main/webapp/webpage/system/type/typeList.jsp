<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools"></t:base>
<div class="easyui-layout" fit="true">
<div region="center" style="padding: 1px;"><t:datagrid name="${typegroup.typegroupcode}List" title="类型列表" actionUrl="typeController.do?typeGrid&typegroupid=${typegroup.id}" idField="id"
	queryMode="group">
	<t:dgCol title="编号" field="id" hidden="true"></t:dgCol>
	<t:dgCol title="类型名称" field="typename"></t:dgCol>
	<t:dgCol title="类型编码" field="typecode"></t:dgCol>
	<t:dgCol title="操作" field="opt"></t:dgCol>
	<t:dgDelOpt url="typeController.do?delType&id={id}" title="删除"></t:dgDelOpt>
	<t:dgToolBar title="${typegroup.typegroupname}录入" icon="icon-add" url="typeController.do?detailType&typegroupid=${typegroup.id}" function="add"></t:dgToolBar>
	<t:dgToolBar title="类别编辑" icon="icon-edit" url="typeController.do?detailType&typegroupid=${typegroup.id}" function="update"></t:dgToolBar>
</t:datagrid></div>
</div>

