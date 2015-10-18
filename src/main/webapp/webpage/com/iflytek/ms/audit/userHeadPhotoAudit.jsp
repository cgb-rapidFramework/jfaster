<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE meta PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>头像审核管理</title>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<script type="text/javascript" src="plug-in/tools/json2.js"></script>
<script type="text/javascript">
$(document).ready(function(){
  var textarea=document.getElementById("mark");
	textarea.value=""; 
})
</script>
</head>
 <body style="overflow-x: hidden" scroll="yes">
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="userInfoController.do?saveHeadPhotoAudit">
			<input id="auditstatus" name="auditstatus" type="hidden" value="2">
			<input id="ids" name="ids" type="hidden" value="${ids}">
			<input id="auditType" name="auditType" type="hidden" value="${auditType}">
			<table style="width:600px;" cellpadding="0" cellspacing="1" class="formtable">
				<tr>
					<td align="right">
						<label class="Validform_label">
							备注:
						</label>
					</td>
					<td class="value">
						<textarea cols="70"	id="mark" name="mark" rows="10" datatype="*1-500" errormsg="内容为空或超出500个字符">
					   </textarea>
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				
			</table>
			
		</t:formvalid>
 </body> 
</body>