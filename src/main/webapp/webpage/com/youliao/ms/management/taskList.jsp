<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<style type="text/css">
<!--
  .datagrid-cell, .datagrid-cell-group, .datagrid-header-rownumber, .datagrid-cell-rownumber{
      white-space:pre-wrap;
   }
-->
</style>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:1px;">
  <t:datagrid name="taskList" title="任务执行日志" actionUrl="taskController.do?datagrid" idField="id" fit="true" queryMode="group">
   <t:dgCol title="任务ID" field="id" width="10"></t:dgCol>
   <t:dgCol title="任务名称" field="taskName" width="20" query="true"></t:dgCol>
   <t:dgCol title="用户总数" field="totalNumber"  width="13"></t:dgCol>
   <t:dgCol title="每次刷新人数" field="topNumber"  width="18"></t:dgCol>
   <t:dgCol title="刷新间隔时间(分钟)" field="topSpaceTime"  width="25"></t:dgCol>
   <t:dgCol title="求聊有效时间(分钟)" field="limitTime"  width="25"></t:dgCol>
   <t:dgCol title="开始时间" field="beginTime" formatter="yyyy-MM-dd hh:mm:ss"  width="15"></t:dgCol>
   <t:dgCol title="结束时间" field="endTime" formatter="yyyy-MM-dd hh:mm:ss"  width="15"></t:dgCol>
   <t:dgCol title="操作人" field="createBy"   width="10" query="true"></t:dgCol>
   <t:dgCol title="提交时间" field="createTime" formatter="yyyy-MM-dd hh:mm:ss"  width="15" query="true" queryMode="group"></t:dgCol>
   <t:dgCol title="状态" field="status" replace="未开始_0,进行中_1,暂停_2,已停止_3"  width="10"></t:dgCol>
   <t:dgCol title="执行轮次" field="orderIndex"  width="13"></t:dgCol>
   <t:dgCol title="操作" field="opt" width="28"></t:dgCol>
   <t:dgToolBar title="录入" icon="icon-add" url="taskController.do?addorupdate" funname="add"></t:dgToolBar>
   
    <t:dgFunOpt funname="veiwNo(id)" title="查看号码"></t:dgFunOpt>
   <t:dgConfOpt title="停止" url="taskController.do?stopTask&id={id}&status=1" message="确认停止吗？" exp="status#ne#3"/> 
   <t:dgDelOpt title="删除" url="taskController.do?del&id={id}" message="确认删除吗？"/>
  </t:datagrid>
  </div>
 </div>
 
  <script type="text/javascript">
 $(document).ready(function(){
		$("input[name='createTime_begin']").attr("class","Wdate").attr("style","height:20px;width:135px;").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});});
		$("input[name='createTime_end']").attr("class","Wdate").attr("style","height:20px;width:135px;").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});});
	});
 </script>
 
  <script type="text/javascript">
  function veiwNo(id){
	  var url="taskUserController.do?findTelNo&taskId="+id;
	  var title="号码";
	 createdetailwindow(title+"列表", url, 400,400);
	}
 </script>
 

 