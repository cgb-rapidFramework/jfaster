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
    <t:datagrid name="auditLogList" title="审核日志列表" actionUrl="auditLogController.do?datagrid&msisdn=${msisdn}&auditstatus=2" idField="id"  queryMode="group" >
   <t:dgCol title="编号" field="id" hidden="true"></t:dgCol>
   <t:dgCol title="图片/头像" field="photo"  width="10"  funname="viewImage" image="true" imageSize="80,80"></t:dgCol>
   <t:dgCol title="手机号码" field="msisdn" width="16"></t:dgCol>
   <t:dgCol title="用户昵称" field="nickName" width="10" ></t:dgCol>
   <t:dgCol title="交友宣言" field="signature" width="20"></t:dgCol>
   <t:dgCol title="自我介绍" field="selfsay" width="20"></t:dgCol>
   <t:dgCol title="上传时间" field="updateTime" width="18" formatter="yyyy-MM-dd hh:mm:ss"></t:dgCol>
   <t:dgCol title="审核时间" field="auditTime"  width="18" formatter="yyyy-MM-dd hh:mm:ss"></t:dgCol>
   <t:dgCol title="审核类型" field="auditType"  width="16"  replace="相册审核_Album,用户信息审核_UserInfo,用户文字审核_userTextInfo,用户动态审核_userDynamicInfo,用户头像审核_userHeadPhotoInfo"  query="true"></t:dgCol>
   <t:dgCol title="审核状态" field="auditstatus" width="12" replace="审核不通过_2,审核通过_1"></t:dgCol>
   <t:dgCol title="审核人" field="auditorName"  width="12"></t:dgCol>
   <t:dgCol title="原因说明" field="mark" width="20"></t:dgCol>
  </t:datagrid>
  </div>
 </div>
 
 
 <script type="text/javascript">
 function viewImage(title,id,url){
     createdetailwindow(title+"列表", url,600,520);	
}
</script>