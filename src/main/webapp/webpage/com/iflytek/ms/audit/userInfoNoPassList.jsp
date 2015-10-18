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
  <t:datagrid name="userInfoList" title="有聊待审核用户信息" actionUrl="userInfoController.do?auditedDatagrid&auditstatus=2" idField="id"  checkbox="true" fit="true" queryMode="group">
   <t:dgCol title="编号" field="id" hidden="true"></t:dgCol>
   <t:dgCol title="手机号码" field="msisdn" width="15" query="true"></t:dgCol>
   <t:dgCol title="昵称" field="nickName" width="10" ></t:dgCol>
   <t:dgCol title="自我介绍" field="selfsay" width="15"></t:dgCol>
   <t:dgCol title="上传时间" field="lastUpdateTime" width="15"  formatter="yyyy-MM-dd hh:mm:ss" query="true" queryMode="group"></t:dgCol>
   <t:dgCol title="审核时间" field="audittime" width="15" formatter="yyyy-MM-dd hh:mm:ss" ></t:dgCol>
   <t:dgCol title="审核人" field="auditorname" width="8" sortable="false"></t:dgCol> 
   <t:dgCol title="审核状态" field="auditstatus" width="10" replace="待审核_0,通过_1,不通过_2"></t:dgCol>
   <t:dgCol title="不通过次数" field="noPassNum" width="12" sortable="false" funname="veiwNum" url="auditLogController.do?auditLog&msisdn={msisdn}"></t:dgCol>
   <t:dgCol title="帐号状态" field="status" width="10" replace="已封号_0,正常_1" query="true"></t:dgCol>
    <t:dgCol title="操作" field="opt" width="10"></t:dgCol>
  
    <t:dgConfOpt title="封号" url="userInfoController.do?deleteUserInfo&ids={id}&type=-1" message="确认封号吗？"/>
    <t:dgToolBar title="批量封号" icon="icon-search" url="userInfoController.do?deleteUserInfo&type=-1" funname="deleteUserInfo"></t:dgToolBar>
    <%-- 
  <t:dgConfOpt title="删除昵称" url="userInfoController.do?deleteUserInfo&ids={id}&type=2" message="确认删除昵称？"/>
    <t:dgConfOpt title="删除介绍 " url="userInfoController.do?deleteUserInfo&ids={id}&type=4" message="确认删除自我介绍？"/>
    <t:dgToolBar title="批量删除昵称" icon="icon-search" url="userInfoController.do?deleteUserInfo&type=2" funname="deleteUserInfo"></t:dgToolBar>
    <t:dgToolBar title="批量删除自我介绍" icon="icon-search" url="userInfoController.do?deleteUserInfo&type=4" funname="deleteUserInfo"></t:dgToolBar>
     --%>
  </t:datagrid>

  </div>
 </div>
 
  <script type="text/javascript">
  $(document).ready(function(){
	  <c:choose>
	   <c:when test="${hidden=='true'}">     
	      $('#userInfoList').datagrid('hideColumn','opt');
	   </c:when>
	  </c:choose>
		$("input[name='lastUpdateTime_begin']").attr("class","Wdate").attr("style","height:20px;width:135px;").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});});
		$("input[name='lastUpdateTime_end']").attr("class","Wdate").attr("style","height:20px;width:135px;").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});});
	});
  </script>
  
    
 <script type="text/javascript">
function veiwNum(title,url){
	createdetailwindow(title+"列表", url, 800,400);
}
</script>

 <script type="text/javascript">

 
 /**
  * 批量删除数据审核
  */
 function  deleteUserInfo(title,url,gname) {
 	gridname=gname;
     var ids = [];
     var rows = $("#"+gname).datagrid('getSelections');
     if (rows.length > 0) {
     	$.dialog.confirm('确定操作吗？', function(r) {
 		   if (r) {
 				for ( var i = 0; i < rows.length; i++) {
 					ids.push(rows[i].id);
 				}
 				$.ajax({
 					url : url,
 					type : 'post',
 					data : {
 						ids : ids.join(','),
 						flag:0
 					},
 					cache : false,
 					success : function(data) {
 						var d = $.parseJSON(data);
 						if (d.success) {
 							var msg = d.msg;
 							tip(msg);
 							reloadTable();
 							$("#"+gname).datagrid('unselectAll');
 							ids='';
 						}
 					}
 				});
 			}
 		});
 	} else {
 		tip("请选择数据");
 	}
 } 
  </script>