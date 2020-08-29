<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>用户的角色选择页面</title>
<t:base type="jquery,easyui,tools"></t:base>
</head>
<body style="overflow-y: hidden" scroll="no">
<t:formvalid formid="formobj" dialog="true" layout="table" action="loginController.do?login">
	<input id="id" name="id" type="hidden" value="${user.id }">
	<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
		<tr>
			<td align="right" width="15%" nowrap><label class="Validform_label">  <t:language langKey="common.username"/>: </label></td>
			<td class="value" width="85%">
                <c:if test="${user.id!=null }"> ${user.username } </c:if>
                <c:if test="${user.id==null }">
                    <input id="username" class="inputxt" name="username" validType="t_s_base_user,username,id" value="${user.username }" datatype="s2-10" />
                    <span class="Validform_checktip"> <t:language langKey="username.rang2to10"/></span>
                </c:if>
            </td>
		</tr>
		<tr>
			<td align="right"><label class="Validform_label"> <t:language langKey="common.department"/>: </label></td>
			<td class="value">
                <select id="orgId" name="orgId" datatype="*">
                    <c:forEach items="${orgList}" var="org">
                        <option value="${org.id }">${org.departname}</option>
                    </c:forEach>
                </select>
            <span class="Validform_checktip"><t:language langKey="please.select.department"/></span></td>
		</tr>
	</table>
</t:formvalid>
</body>