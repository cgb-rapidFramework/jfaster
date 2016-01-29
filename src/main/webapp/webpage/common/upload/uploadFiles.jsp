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
	<table cellpadding="0" cellspacing="1" class="formtable">
		<tbody>
		<tr>
			<td align="right"><label class="Validform_label">上传头像:</label></td>
			<td class="value"><t:upload name="instruction" dialog="false" queueID="instructionfile" view="true" auto="true" uploader="resourceController.do?saveFiles&multi=true&sessionKey=${sessionKey}&fileType=${fileType}" extend="pic" id="instruction"
										formData="documentTitle" multi="false" onUploadSuccess="uploadsuccess"></t:upload></td>
		</tr>
		<tr>
			<td colspan="2" id="instructionfile" class="value"></td>
		</tr>
		</tbody>
	</table>
</t:formvalid>

<script type="text/javascript">
	function onuploadsuccess(json) {
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
</script>
</body>
</html>
