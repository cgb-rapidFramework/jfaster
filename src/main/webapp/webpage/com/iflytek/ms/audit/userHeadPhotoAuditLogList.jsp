<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<style>
<!--
  .datagrid-cell, .datagrid-cell-group, .datagrid-header-rownumber, .datagrid-cell-rownumber{
      white-space:pre-wrap;
   }
-->
</style>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:1px;">
  <t:datagrid name="auditLogList" title="用户头像信息审核日志" actionUrl="auditLogController.do?datagrid&auditType=userHeadPhotoInfo" idField="id" fit="true" queryMode="group">
   <t:dgCol title="编号" field="id" hidden="true"></t:dgCol>
   <t:dgCol title="手机号码" field="msisdn" width="15" query="true"></t:dgCol>
   <t:dgCol title="头像地址" field="photo"  width="10" funname="viewImage"   image="true"  imageSize="80,80"></t:dgCol>
   <t:dgCol title="上传时间" field="updateTime" width="18" formatter="yyyy-MM-dd hh:mm:ss"></t:dgCol>
   <t:dgCol title="审核时间" field="auditTime"  width="18" formatter="yyyy-MM-dd hh:mm:ss" query="true" queryMode="group"></t:dgCol>
   <t:dgCol title="审核人" field="auditorName"  width="10" query="true"></t:dgCol>
   <t:dgCol title="审核状态" field="auditstatus" width="10" replace="审核不通过_2,审核通过_1" query="true"></t:dgCol>
   <t:dgCol title="原因说明" field="mark" width="20"></t:dgCol>  
  </t:datagrid>
  </div>
 </div>
 
 <script type="text/javascript">
  $(document).ready(function(){
		$("input[name='auditTime_begin']").attr("class","Wdate").attr("style","height:20px;width:135px;").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});});
		$("input[name='auditTime_end']").attr("class","Wdate").attr("style","height:20px;width:135px;").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});});
	});
  </script>
  
  
  
 <script type="text/javascript">
 function viewImage(title,id,url){
     createdetailwindow(title+"列表", url,600,520);	
}
</script>