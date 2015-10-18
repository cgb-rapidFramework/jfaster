<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:1px;">
     <t:datagrid name="auditLogStatisticsList" title="审核日志" actionUrl="auditLogController.do?auditLogStatisticsDatagrid"  fit="true" fitColumns="true"  idField="id" queryMode="group">
   <t:dgCol title="编号" field="id" hidden="true"></t:dgCol>
   <t:dgCol title="审核人id" field="auditorId" hidden="true"></t:dgCol>
   <t:dgCol title="审核人" field="auditorName" query="true"></t:dgCol>
   <t:dgCol title="审核时间" field="auditorTime" query="true" hidden="true"></t:dgCol>
   <t:dgCol title="审核数量" field="auditNum"></t:dgCol>
   <t:dgCol title="操作" field="opt" width="30"></t:dgCol>
    <t:dgToolBar title="查看" icon="icon-search" url="auditLogController.do?auditLog" funname="detail"></t:dgToolBar>
   	<t:dgFunOpt  title="审核" operationCode="audit" funname="audit(id)" ></t:dgFunOpt> 
   
  </t:datagrid>
  </div>
 </div>
  <script type="text/javascript">
  $(document).ready(function(){
		$("input[name='auditorTime_begin']").attr("class","Wdate").attr("style","height:20px;width:90px;").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
		$("input[name='auditorTime_end']").attr("class","Wdate").attr("style","height:20px;width:90px;").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
	});
  
  
	function szqm(id) {
		createwindow('审核', 'jeecgDemoController.do?doCheck&id=' + id);
	}
  </script>