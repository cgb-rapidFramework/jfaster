<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:1px;">
  <t:datagrid name="photoAlbumList" title="有聊用户相册" actionUrl="photoAlbumController.do?auditedDatagrid&auditstatus=1" idField="id" fit="true" checkbox="true" queryMode="group">
   <t:dgCol title="编号" field="id" hidden="true"></t:dgCol>
   <t:dgCol title="照片标识" field="identifiers"  hidden="true"></t:dgCol>
   <t:dgCol title="照片" field="sourceurl" width="7" funname="viewImage"  image="true" imageSize="80,80"></t:dgCol>
   <t:dgCol title="手机号码" field="msisdn"  width="10" sortable="false" query="true"></t:dgCol>
   <t:dgCol title="上传时间" field="createtime" width="16" formatter="yyyy-MM-dd hh:mm:ss" query="true" queryMode="group"></t:dgCol>
   <t:dgCol title="审核时间" field="lastAuditTime" width="16"   formatter="yyyy-MM-dd hh:mm:ss"></t:dgCol>
    <t:dgCol title="审核人" field="LastAuditorName" width="10" sortable="false"></t:dgCol> 
   <t:dgCol title="审核状态" field="auditstatus" width="10" replace="待审核_0,审核通过_1,审核不通过_2"></t:dgCol>
   <t:dgCol title="用户状态" field="userStatus" width="10"  sortable="false" replace="已封号_0,正常_1" query="true"></t:dgCol>
   <t:dgCol title="操作" field="opt" width="8"></t:dgCol>
     <t:dgConfOpt title="封号" url="photoAlbumController.do?deletePhotoAlbum&msisdns={msisdn}&identifiers={identifiers}&type=-1" message="确认封号吗"/>
   <t:dgToolBar title="批量封号" icon="icon-search" url="photoAlbumController.do?deletePhotoAlbum&type=-1" funname="deletePhotoAlbum"></t:dgToolBar>
   <%--
    <t:dgConfOpt title="撤销审核" url="auditLogController.do?cancelAudit&auditType=Album&msisdn={msisdn}&identifiers={identifiers}" message="确认撤销审核吗？"/>
   --%>
  </t:datagrid>
  </div>
 </div>
 
  <script type="text/javascript">
  $(document).ready(function(){
		  <c:choose>
		   <c:when test="${hidden=='true'}">     
		      $('#photoAlbumList').datagrid('hideColumn','opt');
		   </c:when>
		  </c:choose>
	  
		$("input[name='createtime_begin']").attr("class","Wdate").attr("style","height:20px;width:135px;").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});});
		$("input[name='createtime_end']").attr("class","Wdate").attr("style","height:20px;width:135px;").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});});
	});
  </script>
  
 <script type="text/javascript">
 function viewImage(title,id,url){
     createdetailwindow(title+"列表", url,600,520);	
}
</script>
 
  
 <script type="text/javascript">

 
 /**
  * 批量删除数据审核
  */
 function  deletePhotoAlbum(title,url,gname) {
 	gridname=gname;
     var msisdns = [];
     var identifiers = [];
     var rows = $("#"+gname).datagrid('getSelections');
     if (rows.length > 0) {
     	$.dialog.confirm('确定操作吗？', function(r) {
 		   if (r) {
 				for ( var i = 0; i < rows.length; i++) {
 					msisdns.push(rows[i].msisdn);
 					identifiers.push(rows[i].identifiers);
 				}
 				
 				$.ajax({
 					url : url,
 					type : 'post',
 					data : {
 						msisdns : msisdns.join(','),
 						identifiers : identifiers.join(','),
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
 		tip("请选择需要操作的数据");
 	}
 } 
  </script>