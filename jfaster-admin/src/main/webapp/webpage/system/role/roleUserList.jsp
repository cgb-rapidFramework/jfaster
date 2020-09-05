<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<%--<t:datagrid name="userList" title="user.manage" actionUrl="roleController.do?roleUserDatagrid&roleId=${roleId}" fit="true" fitColumns="true" idField="id">--%>
<%--	<t:dgCol title="common.id" field="id" hidden="true" ></t:dgCol>--%>
<%--	<t:dgCol title="common.username" sortable="false" field="username" width="5"></t:dgCol>--%>
<%--	<t:dgCol title="common.real.name" field="realName" width="5"></t:dgCol>--%>
<%--</t:datagrid>--%>

<t:datagrid name="roleUserList" title="common.operation"
            actionUrl="roleController.do?roleUserDatagrid&roleId=${roleId}" fit="true" fitColumns="true" idField="id" queryMode="group">
	<t:dgCol title="common.code" field="id" hidden="true"></t:dgCol>
	<t:dgCol title="common.username" sortable="false" field="username" query="true" width="10"></t:dgCol>
	<t:dgCol title="common.real.name" field="realName" query="true" width="16"></t:dgCol>
	<t:dgCol title="common.status" sortable="true" field="status" replace="common.active_1,common.inactive_0,super.admin_-1" width="8"></t:dgCol>
	<t:dgCol title="common.operation" field="opt" width="10"></t:dgCol>
	<t:dgDelOpt title="common.delete" url="userController.do?del&id={id}&username={username}" />
	<t:dgToolBar title="common.add.param" langArg="common.user" icon="icon-add" url="userController.do?detail&roleId=${roleId}" function="add"></t:dgToolBar>
	<t:dgToolBar title="common.edit.param" langArg="common.user" icon="icon-edit" url="userController.do?detail&roleId=${roleId}" function="update"></t:dgToolBar>
	<t:dgToolBar title="common.add.exist.user" icon="icon-add" url="roleController.do?goAddUserToRole&roleId=${roleId}" function="add" width="500"></t:dgToolBar>
</t:datagrid>

