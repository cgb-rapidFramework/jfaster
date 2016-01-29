<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html >
<html>
<head>
	<title>上传文件支持多附件</title>
	<%--uploader参数说明：
	1.sessionKey,每次上传附件产生的key
	2。multi：多附件为true,t:upload中的multi无效
	--%>
	<t:base type="jquery,easyui,tools"></t:base>
</head>
<body style="overflow-y: hidden" scroll="no">
<t:formvalid formid="formobj" layout="table" refresh="false" dialog="true"   action="bUserController.do?saveAvatar">
	<input id="id" type="hidden" name="id" value="${user.id }">
	<input id="documentTitle" type="hidden" name="documentTitle" value="blank"/>
	<input id="fileUrl" type="hidden"/>
	<input id="fileName" type="hidden"/>

	<table cellpadding="0" cellspacing="1" class="formtable">
		<tbody>
		<tr>
			<td align="right"><label class="Validform_label">上传头像:</label></td>
			<td class="value"><t:upload name="instruction" dialog="false" queueID="instructionfile" view="true" auto="true" uploader="resourceController.do?saveFiles&multi=true&sessionKey=${sessionKey}" extend="pic" id="instruction"
							 			 formData="documentTitle" multi="false" onUploadSuccess="uploadsuccess"></t:upload></td>
		</tr>
		<tr>
			<td colspan="2" id="instructionfile" class="value"></td>
		</tr>
		<tr>
			<td colspan="2" class="value">
				<div id="fileShow" >
				</div>
			</td>
		</tr>
		</tbody>
	</table>
</t:formvalid>

<%--<script type="text/javascript">
	function uploadsuccess(json) {
		var status = json.success;
		if(status){
			tip(json.attributes.name+'文件上传成功');
			$('#fileurl').val(json.attributes.url);
			$('#filekey').val(json.attributes.filekey);
			$('#viewmsg').html('');
		}else{
			tip(json.msg);
		}
	}
</script>--%>


<script type="text/javascript">

	function uploadsuccess(d,file,response){
		$("#fileUrl").val(d.attributes.url);
		$("#fileName").val(d.attributes.name);
		var url = $("#fileUrl").val();
		var html="";
		if(url.indexOf(".gif")!=-1 ||
				url.indexOf(".jpg")!=-1        ||
				url.indexOf(".png")!=-1 ||
				url.indexOf(".bmp")!=-1){
			html += "<img src='"+url+"' width =400 height=300 />";
		}else{
			html += "<a href='"+url+"' target=_blank >下载:"+d.attributes.name+"</a>";
		}
		$("#fileShow").html(html);
	}
	function uploadCallback(callback,inputId){
		var url = $("#fileUrl").val();
		var name= $("#fileName").val();
		callback(url,name,inputId);

	}
</script>
</body>
</html>
