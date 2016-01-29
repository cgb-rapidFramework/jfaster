<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>用户管理</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
 </head>
 <body style="overflow-y: hidden" scroll="no">
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="bUserController.do?save">
			<input id="id" name="id" type="hidden" value="${bUserPage.id }">
			<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
				<tr>
					<td align="right">
						<label class="Validform_label">
							昵称:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="nickname" name="nickname" ignore="ignore"
							   value="${bUserPage.nickname}" datatype="*2-30">
						<span class="Validform_checktip"></span>
					</td>
				</tr>

				<tr>
					<td align="right">
						<label class="Validform_label">
							性别:
						</label>
					</td>
					<td class="value">
						<select id="sex" name="sex">
							<option value="0" <c:if test="${bUserPage.sex eq '0'}"> selected="selected"</c:if>>未知</option>
							<option value="1" <c:if test="${bUserPage.sex eq '1'}"> selected='selected'</c:if>>女</option>
							<option value="2" <c:if test="${bUserPage.sex eq '2'}"> selected="selected"</c:if>>男</option>
						</select>
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							出生日期:
						</label>
					</td>
					<td class="value">
						<input class="Wdate" onClick="WdatePicker()"  style="width: 150px" id="birthday" name="birthday" ignore="ignore"
							   value="<fmt:formatDate value='${bUserPage.birthday}' type="date" pattern="yyyy-MM-dd"/>">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							省份:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="province" name="province" ignore="ignore"
							   value="${bUserPage.province}">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							城市:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="city" name="city" ignore="ignore"
							   value="${bUserPage.city}">
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
						<select id="status" name="status">
							<option value="0" <c:if test="${bUserPage.status eq '0'}"> selected="selected"</c:if>>不可用</option>
							<option value="1" <c:if test="${bUserPage.status eq '1'}"> selected='selected'</c:if>>可用</option>
						</select>
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							用户类型:
						</label>
					</td>
					<td class="value">
						<select id="type" name="type">
							<option value="0" <c:if test="${bUserPage.type eq '0'}"> selected="selected"</c:if>>游客</option>
							<option value="1" <c:if test="${bUserPage.type eq '1'}"> selected='selected'</c:if>>正式用户</option>
							<option value="1" <c:if test="${bUserPage.type eq '2'}"> selected='selected'</c:if>>假资料</option>
						</select>
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							资料是否完善:
						</label>
					</td>
					<td class="value">

						<select id="isfull" name="isfull">
							<option value="0" <c:if test="${bUserPage.isfull eq '0'}"> selected="selected"</c:if>>否</option>
							<option value="1" <c:if test="${bUserPage.isfull eq '1'}"> selected='selected'</c:if>>是</option>
						</select>
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							签名:
						</label>
					</td>
					<td class="value">
						<textarea class=""  id="signature" name="signature" ignore="ignore">${bUserPage.signature}</textarea>
						<span class="Validform_checktip"></span>
					</td>
				</tr>
			</table>
		</t:formvalid>
 </body>