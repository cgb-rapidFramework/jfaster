<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>任务执行日志</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
 </head>
 <body style="overflow-y: hidden" scroll="no">
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="taskLogController.do?save">
			<input id="id" name="id" type="hidden" value="${taskLogPage.id }">
			<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
				<tr>
					<td align="right">
						<label class="Validform_label">
							求聊任务ID:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="taskId" name="taskId" ignore="ignore"
							   value="${taskLogPage.taskId}">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							任务分组ID:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="groupNumber" name="groupNumber" ignore="ignore"
							   value="${taskLogPage.groupNumber}">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							执行轮次:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="orderIndex" name="orderIndex" ignore="ignore"
							   value="${taskLogPage.orderIndex}">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							创建时间:
						</label>
					</td>
					<td class="value">
						<input class="Wdate" onClick="WdatePicker()"  style="width: 150px" id="createTime" name="createTime" ignore="ignore"
							   value="<fmt:formatDate value='${taskLogPage.createTime}' type="date" pattern="yyyy-MM-dd hh:mm:ss"/>">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							执行结果:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="isSuccess" name="isSuccess" ignore="ignore"
							   value="<c:if test='${taskLogPage.isSuccess==0}'>失败 </c:if><c:if test='${taskLogPage.isSuccess==1}'>成功 </c:if> ">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							原因描述:
						</label>
					</td>
					<td class="value">
					    <textarea id="description" name="description" ignore="ignore" rows="10" cols="80" wrap="soft">${taskLogPage.description}</textarea>
						<span class="Validform_checktip"></span>
					</td>
				</tr>
			</table>
		</t:formvalid>
 </body>