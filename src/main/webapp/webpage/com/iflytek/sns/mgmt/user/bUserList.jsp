<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<%--<style>
 <!--
 .datagrid-cell, .datagrid-cell-group, .datagrid-header-rownumber, .datagrid-cell-rownumber{
  white-space:pre-wrap;
 }
 -->
</style>--%>

<div class="easyui-layout" fit="true">
  <div region="center" style="padding:1px;">
  <t:datagrid name="bUserList" title="用户管理" actionUrl="bUserController.do?datagrid" idField="id" fit="true" queryMode="group">
   <t:dgCol title="UID" field="id" width="8"  query="true"></t:dgCol>
   <t:dgCol title="头像" field="avatarUrl" width="12" image="true" imageSize="20,20"></t:dgCol>
   <t:dgCol title="帐号" field="account" width="12" query="true"></t:dgCol>
   <t:dgCol title="昵称" field="nickname" width="12" query="true"></t:dgCol>
   <t:dgCol title="性别" field="sex" replace="女性_1,男性_2" width="8"></t:dgCol>
   <t:dgCol title="出生日期" field="birthday" formatter="yyyy-MM-dd" width="10"></t:dgCol>
   <t:dgCol title="省份" field="province" width="6"></t:dgCol>
   <t:dgCol title="城市" field="city" width="10"></t:dgCol>
   <t:dgCol title="个性签名" field="signature" width="20"></t:dgCol>
   <t:dgCol title="状态" field="status"  replace="不可用_0,可用_1" width="6" query="true"></t:dgCol>
   <t:dgCol title="类型" field="type"  replace="游客_0,正式用户_1,假资料_2" width="6" query="true"></t:dgCol>
   <t:dgCol title="资料完善" field="isfull"  replace="否_0,是_1" width="8"></t:dgCol>
   <t:dgCol title="创建时间" field="createTime" formatter="yyyy-MM-dd hh:mm:ss" width="10" query="true" queryMode="group"></t:dgCol>

   <%--
   <t:dgCol title="操作" field="opt" width="10"></t:dgCol>
  <t:dgDelOpt title="删除" url="bUserController.do?del&id={id}" />--%>
   <t:dgToolBar title="录入" icon="icon-add" url="bUserController.do?addorupdate" funname="add"></t:dgToolBar>
   <t:dgToolBar title="编辑" icon="icon-edit" url="bUserController.do?addorupdate" funname="update"></t:dgToolBar>
   <t:dgToolBar title="查看" icon="icon-search" url="bUserController.do?addorupdate" funname="detail"></t:dgToolBar>
   <t:dgToolBar title="重置密码" icon="icon-edit" url="bUserController.do?updatePassword" funname="update"></t:dgToolBar>
   <t:dgToolBar title="设置个性化帐号" icon="icon-edit" url="bUserController.do?updateAccount" funname="update"></t:dgToolBar>
   <t:dgToolBar title="上传头像" icon="icon-edit" url="bUserController.do?updateAvatar" funname="update"></t:dgToolBar>
   <t:dgConfOpt title="锁定帐号"  url="bUserController.do?lock" message="确定锁定吗？"></t:dgConfOpt>
  </t:datagrid>
  </div>
 </div>

<script type="text/javascript">
 $(document).ready(function(){
  $("input[name='createTime_begin']").attr("class","Wdate").attr("style","height:30px;width:140px;").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});});
  $("input[name='createTime_end']").attr("class","Wdate").attr("style","height:30px;width:140px;").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});});
 });
</script>