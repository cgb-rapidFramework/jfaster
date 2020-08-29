<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>日志详情</title>
<t:base type="jquery,tools"></t:base>
</head>
<body style="overflow-y: hidden" scroll="no">
<t:formvalid formid="formobj" layout="div" dialog="true">
	<fieldset class="step"><legend> <%-- <t:language langKey="common.loginfo"/> --%> 日志详情</legend>
	
	<div class="form"><label class="form"> <%-- <t:language langKey="common.username"/> --%>操作人:${logView.TSUser.username } </label>
	 </div>
	<div class="form"><label class="form"> <%-- <t:language langKey="common.logtype"/> --%>日志类型: </label>
	   <!-- 以后再统一由数据字典维护  fangwenrong-->
	   <c:if test="${logView.operationType==1}"><t:language langKey="common.login"/></c:if>
	   <c:if test="${logView.operationType==2}"><t:language langKey="common.logout"/></c:if>
	   <c:if test="${logView.operationType==3}"><t:language langKey="common.insert"/></c:if>
	   <c:if test="${logView.operationType==4}"><t:language langKey="common.delete"/></c:if>
	   <c:if test="${logView.operationType==5}"><t:language langKey="common.update"/></c:if>
	   <c:if test="${logView.operationType==6}"><t:language langKey="common.upload"/></c:if>
	   <c:if test="${logView.operationType==7}"><t:language langKey="common.other"/></c:if>
	</div>
	<div class="form"><label class="form"> <t:language langKey="log.content"/>: </label> ${logView.logcontent }</div>
	<div class="form"><label class="form"> <%-- <t:language langKey="common.loglevel"/> --%>日志等级: </label>
	   <!-- 以后再统一由数据字典维护  fangwenrong-->
	   <c:if test="${logView.loglevel==1}">INFO</c:if>
	   <c:if test="${logView.loglevel==2}">WARRING</c:if>
	   <c:if test="${logView.loglevel==3}">ERROR</c:if>
	</div>

	<div class="form"><label class="form"> <t:language langKey="operate.ip"/>: </label> ${logView.note}</div>
	<div class="form"><label class="form"> <t:language langKey="operate.time"/>: </label>
	  <fmt:formatDate value="${logView.operatetime}" pattern="yyyy-MM-dd HH:mm:ss"/> 
	 </div>
	<div class="form"><label class="form"> <t:language langKey="common.browser"/>: </label> ${logView.broswer}</div>
	

	</fieldset>
	</form>
</t:formvalid>
</body>
</html>


