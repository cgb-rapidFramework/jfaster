<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>任务用户列表</title>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
</head>
<body style="overflow-y: hidden" scroll="no">
	<c:choose>
		<c:when test="${empty list}">
           该任务未发现执行用户
       </c:when>

		<c:otherwise>
			<c:forEach items="${list}" var="item">
        ${item} </br>
			</c:forEach>
		</c:otherwise>

	</c:choose>


</body>