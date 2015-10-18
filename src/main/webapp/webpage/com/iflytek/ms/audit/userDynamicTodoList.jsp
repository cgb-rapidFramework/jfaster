<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">

  <div region="center" style="padding:1px;">
  <t:datagrid name="userDynamicList" title="用户动态" actionUrl="userDynamicController.do?datagrid&auditstatus=0" idField="id"  checkbox="true" fit="true" queryMode="group" > 
   <t:dgCol title="编号" field="id" hidden="true"></t:dgCol>
   <t:dgCol title="手机号码" field="msisdn"  width="15" query="true"></t:dgCol>
   <t:dgCol title="图片" field="imageurl" width="12" funname="viewImage"  image="true" imageSize="80,80"></t:dgCol>
   <t:dgCol title="内容" field="content" width="30"></t:dgCol>
   <t:dgCol title="录音" field="voicetext" width="10"  funname="playVoice" url="commandInfoController.do?playVoice&voiceurl={voiceurl}"></t:dgCol>
   <t:dgCol title="录音地址" field="voiceurl" width="10" hidden="true"></t:dgCol>
   <t:dgCol title="录音时长" field="voicetime" width="10"></t:dgCol>
   <t:dgCol title="上传时间" field="createtime" width="15" formatter="yyyy-MM-dd hh:mm:ss" query="true" queryMode="group"></t:dgCol>   
   <t:dgCol title="操作" field="opt" width="20"></t:dgCol>
    <t:dgConfOpt title="审核通过" url="userDynamicController.do?saveUserDynamicAudit&ids={id}&auditstatus=1&auditType=userDynamicInfo" message="确认通过？"/>
   <t:dgFunOpt title="审批不通过" funname="audit(id)" />   
   <t:dgToolBar title="批量通过" icon="icon-search" url="userDynamicController.do?saveUserDynamicAudit&auditstatus=1&auditType=userDynamicInfo" funname="auditAllToPass"></t:dgToolBar>
   <t:dgToolBar title="批量不通过" icon="icon-search" url=""  funname="auditAllToNoPass"></t:dgToolBar>
  </t:datagrid>
  </div>
 </div>


 <script type="text/javascript">
 $(document).ready(function(){
	  <c:choose>
	   <c:when test="${hidden=='true'}">     
	      $('#userDynamicList').datagrid('hideColumn','opt');
	   </c:when>
	  </c:choose>
		$("input[name='createtime_begin']").attr("class","Wdate").attr("style","height:20px;width:135px;").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});});
		$("input[name='createtime_end']").attr("class","Wdate").attr("style","height:20px;width:135px;").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});});
	});
 </script>
 <script type="text/javascript">
function playVoice(title,url){
	createdetailwindow(title+"列表", url,600,200);
}

/**
 * 查看大图
 */
function viewImage(title,id,url){
	 var url="userDynamicController.do?viewImage&id="+id;
     createdetailwindow(title+"列表", url,600,520);	
}
</script>

 <script type="text/javascript">
  /**
   * 批量通过审核
   */
  function auditAllToPass(title,url,gname) {
  	gridname=gname;
      var ids = [];
      var rows = $("#"+gname).datagrid('getSelections');
      if (rows.length > 0) {
      	$.dialog.confirm('确定通过吗？', function(r) {
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
  		tip("请选择需要审核的数据");
  	}
  }
  
  
  /**
   * 批量拒绝通过审核
   */
  function auditAllToNoPass(title,url,gname) {
	  	gridname=gname;
	      var ids = [];
	      var rows = $("#"+gname).datagrid('getSelections');
	      if (rows.length > 0) {
	      	$.dialog.confirm('确定不通过吗？', function(r) {
	  		   if (r) {
	  			    for ( var i = 0; i < rows.length; i++) {
	  					ids.push(rows[i].id);
	  				}
	  			  audit(ids);
	  			}
	  		});
	  	} else {
	  		tip("请选择需要审核的数据");
	  	}
  }
 
  /**
   *批量不通过时填写原因
  **/
 function audit(ids,flag) {
		var url="userDynamicController.do?auditDynamic&auditstatus=2&auditType=userDynamicInfo&ids="+ids;
		var random = Math.random();
		var winId = 'approveWin' + random;
		$.dialog({
			id : winId,
			title :'填写备注',
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