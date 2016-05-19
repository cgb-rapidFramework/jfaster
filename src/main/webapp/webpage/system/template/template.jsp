<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>模版管理</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
 </head>
 <body style="overflow-y: hidden" scroll="no">
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="templateController.do?save">
			<input id="id" name="id" type="hidden" value="${templatePage.id }">
			<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
				<tr>
					<td align="right">
						<label class="Validform_label">
							模版编码:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="theme" name="theme"
							   value="${templatePage.theme}" datatype="*">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							模版名称:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="name" name="name" 
							   value="${templatePage.name}" datatype="*">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							模版图片:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="image" name="image"
							   value="${templatePage.image}">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							主页:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="pageMain" name="pageMain" ignore="ignore"
							   value="${templatePage.pageMain}">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							登录页:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="pageLogin" name="pageLogin" ignore="ignore"
							   value="${templatePage.pageLogin}">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							状态:
						</label>
					</td>
					<td class="value">
						<select name="status" id="status" datatype="*">
							<option value="0" <c:if test="${templatePage.status eq 0}">selected="selected"</c:if>>未设置</option>
							<option value="1" <c:if test="${templatePage.status>0}"> selected="selected"</c:if>>已设置</option>
						</select>
						<span class="Validform_checktip"></span>
					</td>
				</tr>
			</table>
		</t:formvalid>
 </body>