<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>用户信息</title>
<t:base type="jquery,easyui,tools"></t:base>
</head>
<body style="overflow-y: hidden" scroll="no">
<t:formvalid formid="formobj" refresh="false" dialog="true" action="bUserController.do?saveAccount" usePlugin="password" layout="table">
	<input id="id" type="hidden" name="id" value="${user.id }">
	<table style="width: 550px" cellpadding="0" cellspacing="1" class="formtable">
		<tbody>
		<tr>
			<td align="right">
				<label class="Validform_label">
					个性化帐号:
				</label>
			</td>
			<td class="value">
				<input class="inputxt" id="account" name="account" ignore="ignore" datatype="*6-18"
					   value="${user.account}"  <c:if test="${!empty user.account}"> disabled="disabled"</c:if>>
				<span class="Validform_checktip">请输入字母和数字</span>
			</td>
		</tr>
		</tbody>
	</table>
</t:formvalid>
</body>