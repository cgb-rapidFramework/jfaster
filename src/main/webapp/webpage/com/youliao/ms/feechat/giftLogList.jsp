<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:1px;">
  <t:datagrid name="giftLogList" title="手动加花日志表" actionUrl="giftLogController.do?datagrid&giftType=flower" idField="id" fit="true" queryMode="group">
   <t:dgCol title="编号" field="id" hidden="true"></t:dgCol>
   <t:dgCol title="用户号码" field="msisdn" width="15" query="true" ></t:dgCol>
   <t:dgCol title="添加免费鲜花数" field="freeFlowerNum" width="15"></t:dgCol>
   <t:dgCol title="添加付费鲜花数" field="costFlowerNum" width="15"></t:dgCol>
   <t:dgCol title="操作人" field="createBy" width="15" query="true" ></t:dgCol>
   <t:dgCol title="操作时间" field="createTime" width="25" formatter="yyyy-MM-dd hh:mm:ss" query="true" queryMode="group" ></t:dgCol>
  </t:datagrid>
  </div>
 </div>
 
 <script type="text/javascript">
 $(document).ready(function(){
		$("input[name='createTime_begin']").attr("class","Wdate").attr("style","height:20px;width:135px;").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});});
		$("input[name='createTime_end']").attr("class","Wdate").attr("style","height:20px;width:135px;").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});});
	});
 </script>