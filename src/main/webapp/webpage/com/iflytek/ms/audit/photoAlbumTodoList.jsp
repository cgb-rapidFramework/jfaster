<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:1px;">
  <t:datagrid name="photoAlbumList" title="有聊用户相册" actionUrl="photoAlbumController.do?datagrid&auditstatus=0" idField="id" fit="true" queryMode="group" checkbox="true">
  <t:dgCol title="编号" field="id" hidden="true" ></t:dgCol>
   <t:dgCol title="标识" field="identifiers" width="20" hidden="true"></t:dgCol>
   <t:dgCol title="手机" field="msisdn" width="20" hidden="true"></t:dgCol>
   <t:dgCol title="照片" field="sourceurl" width="6" funname="viewImage"  image="true" imageSize="80,80"></t:dgCol>
   <t:dgCol title="手机号码" field="id.msisdn" width="15" sortable="false" query="true"></t:dgCol>
   <t:dgCol title="上传时间" field="createtime" width="20" formatter="yyyy-MM-dd hh:mm:ss" query="true" queryMode="group"></t:dgCol>
   <t:dgCol title="审核状态" field="auditstatus" width="10" replace="待审核_0,审核通过_1,审核不通过_2"></t:dgCol>
   <%-- 
   <t:dgCol title="用户对自己照片的评论" field="comments" ></t:dgCol>
   <t:dgCol title="照片原图地址" field="sourceurl" imagae="true" style="width:150px; height:120px;"></t:dgCol>
    --%>
   <t:dgCol title="操作" field="opt" width="20"></t:dgCol>
   <t:dgConfOpt title="审核通过" url="photoAlbumController.do?saveAudit&identifiers={identifiers}&msisdns={msisdn}&auditstatus=1" message="确认通过？"/>
   <t:dgFunOpt title="审批不通过" funname="audit(identifiers,msisdn)" />
   
   <t:dgToolBar title="批量通过" icon="icon-search" url="photoAlbumController.do?saveAudit&auditstatus=1" funname="auditAllToPass"></t:dgToolBar>
   <t:dgToolBar title="批量不通过" icon="icon-search" url=""  funname="auditAllToNoPass"></t:dgToolBar>
  </t:datagrid>
  </div>
 </div>
 
 
 <script type="text/javascript">
  $(document).ready(function(){
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
   * 批量通过审核
   */
  function auditAllToPass(title,url,gname) {
      gridname=gname;
      var identifiers = [];
      var msisdns = [];
      var rows = $("#"+gname).datagrid('getSelections');
      if (rows.length > 0) {
      	$.dialog.confirm('确定通过吗？', function(r) {
  		   if (r) {
  				for ( var i = 0; i < rows.length; i++) {
  					identifiers.push(rows[i].identifiers);
  					msisdns.push(rows[i].msisdn);
  				}
  				$.ajax({
  					url : url,
  					type : 'post',
  					data : {
  						identifiers : identifiers.join(','),
  						msisdns : msisdns.join(','),
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
  		tip("请选择需要审核的数据");
  	}
  }
  
  
  /**
   * 批量拒绝通过审核
   */
  function auditAllToNoPass(title,url,gname) {
	     gridname=gname;
	      var identifiers = [];
	      var msisdns = [];
	      var rows = $("#"+gname).datagrid('getSelections');
	      if (rows.length > 0) {
	      	$.dialog.confirm('确定不通过吗？', function(r) {
	  		   if (r) {
	  			 for ( var i = 0; i < rows.length; i++) {
	  					identifiers.push(rows[i].identifiers);
	  					msisdns.push(rows[i].msisdn);
	  			  }
	  			  audit(identifiers,msisdns);
	  			}
	  		});
	  	} else {
	  		tip("请选择需要审核的数据");
	  	}
  }
 
  /**
   *批量不通过时填写原因
  **/
 function audit(identifiers,msisdns,flag) {
		var url=" photoAlbumController.do?audit&auditstatus=2&identifiers="+identifiers+"&msisdns="+msisdns;
		var random = Math.random();
		var winId = 'approveWin' + random;
		$.dialog({
			id : winId,
			title : '填写备注',
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