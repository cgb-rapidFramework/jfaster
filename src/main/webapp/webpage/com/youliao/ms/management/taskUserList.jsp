<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:1px;">
  <t:datagrid name="taskUserList" title="任务用户" actionUrl="taskUserController.do?datagrid" idField="id" fit="true" queryMode="group" checkbox="true">
   <t:dgCol title="编号" field="id" hidden="true"></t:dgCol>
   <t:dgCol title="用户号码" field="telno" width="16" query="true"></t:dgCol>
   <t:dgCol title="任务名称" field="taskEntity.taskName" width="10" query="true" sortable="false"></t:dgCol>
   <t:dgCol title="任务ID" field="taskId"  width="5" query="true"></t:dgCol>
   <t:dgCol title="分组ID" field="groupNumber" width="10"></t:dgCol>
   <t:dgCol title="任务状态"  replace="未开始_0,进行中_1" field="taskEntity.status" query="true" width="10" sortable="false"></t:dgCol>
   <t:dgCol title="号码状态" replace="暂停刷新_1,正在刷新_0" field="status" width="15"></t:dgCol>
   <t:dgCol title="操作" field="opt" width="30"></t:dgCol>
    <t:dgConfOpt title="暂停刷新" url="taskUserController.do?pauseOrRefurbish&ids={id}&status=1" message="确认暂停刷新吗？"/>
    <t:dgConfOpt title="继续刷新" url="taskUserController.do?pauseOrRefurbish&ids={id}&status=0" message="确认继续刷新吗？"/>

    <t:dgToolBar title="批量暂停刷新" icon="icon-search" url="taskUserController.do?pauseOrRefurbish&&status=1" funname="pauseOrRefurbish"></t:dgToolBar>
    <t:dgToolBar title="批量继续刷新" icon="icon-search" url="taskUserController.do?pauseOrRefurbish&&status=0" funname="pauseOrRefurbish"></t:dgToolBar>
  </t:datagrid>
  </div>
 </div>
 
 <script type="text/javascript">
 
 /**
  * 批量删除数据审核
  */
 function  pauseOrRefurbish(title,url,gname) {
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