<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:1px;">
  <t:datagrid name="userGiftList" title="用户的鲜花信息" actionUrl="userGiftController.do?datagrid" idField="id" fit="true" queryMode="group">
   <t:dgCol title="编号" field="id" hidden="true"></t:dgCol>
   <t:dgCol title="用户号码" field="msisdn" query="true" width="20"></t:dgCol>
   <t:dgCol title="剩余免费鲜花数" field="freeFlowerNum" width="20"></t:dgCol>
   <t:dgCol title="剩余付费鲜花数" field="costFlowerNum" width="20"></t:dgCol>
   <t:dgCol title="操作" field="opt" width="30"></t:dgCol>
   <t:dgFunOpt title="添加鲜花" funname="addGift(id)" />
  </t:datagrid>
  </div>
 </div>
 
 <script type="text/javascript">
  /**
   *批量不通过时填写原因
  **/
 function addGift(id) {
		var url="userGiftController.do?addorupdate&id="+id;
		var random = Math.random();
		var winId = 'approveWin' + random;
		$.dialog({
			id : winId,
			title :'请输入鲜花数量',
			lock : true,
			width : 600,
			height : 350,
			content : "url:" + url,
			button : [ {
				name : '提交',
			 	callback : function() {
			 		iframe = this.iframe.contentWindow;
		    		$('#btn_sub', iframe.document).click();
		 			//window.location.reload();   	
		    		return false;
				},
				focus : true,
			} ],
			cancelVal : '取消',
			cancel : true
		});
	}
  </script>