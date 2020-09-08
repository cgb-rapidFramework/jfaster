<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title><t:language langKey="common.roleView.info"/></title>
<t:base type="jquery,easyui,tools"></t:base>
</head>
<body style="overflow-y: hidden" scroll="no">
<t:formvalid formid="formobj" layout="div" dialog="true" action="roleController.do?saveRole">
	<input name="id" type="hidden" value="${roleView.id}">
	<fieldset class="step">
	<div class="form"><label class="Validform_label"><t:language langKey="common.role.name"/>:</label> <input name="roleName" class="inputxt" value="${roleView.roleName }" datatype="s2-8"> <span class="Validform_checktip"><t:language langKey="rolescope.rang2to8.notnull"/></span>
	</div>
	<div class="form"><label class="Validform_label"> <t:language langKey="common.role.code"/>: </label> <input name="roleCode" id="roleCode" ajaxurl="roleController.do?checkRole&code=${roleView.roleCode }" class="inputxt"
		value="${roleView.roleCode }" datatype="s2-15"> <span class="Validform_checktip"><t:language langKey="rolecode.rang2to15.notnull"/></span></div>
	</fieldset>
</t:formvalid>
</body>
</html>
