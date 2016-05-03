<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>任务管理</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
 </head>
 <body style="overflow-y: hidden" scroll="no">
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="jobController.do?save">
			<input id="id" name="id" type="hidden" value="${jobPage.id }">
			<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
				<tr>
					<td align="right">
						<label class="Validform_label">
							任务名称:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="name" name="name" 
							   value="${jobPage.name}" datatype="*">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							任务分组:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="group" name="group" ignore="ignore"
							   value="${jobPage.group}">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							任务状态:
						</label>
					</td>
					<td class="value">
						<c:choose>
							<c:when test="${null==jobPage}">
								<t:dictSelect field="status" typeGroupCode="jobstatus" hasLabel="false" defaultVal="NORMAL"></t:dictSelect>
							</c:when>
							<c:otherwise>
								<t:dictSelect field="status" typeGroupCode="jobstatus" hasLabel="false" defaultVal="${jobPage.status}"></t:dictSelect>
							</c:otherwise>
						</c:choose>
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							任务运行时间表达式:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="expression" name="expression" 
							   value="${jobPage.expression}" datatype="*">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							是否异步:
						</label>
					</td>
					<td class="value">
						<t:dictSelect field="isSync" typeGroupCode="jobissync" hasLabel="false" defaultVal="${jobPage.isSync}"></t:dictSelect>
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							任务描述:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="description" name="description" ignore="ignore"
							   value="${jobPage.description}">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
			</table>
		</t:formvalid>
 </body>