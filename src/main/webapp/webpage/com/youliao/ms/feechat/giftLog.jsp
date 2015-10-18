\<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>手动添加礼物日志表</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
 </head>
 <body style="overflow-y: hidden" scroll="no">
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="giftLogController.do?save">
			<input id="id" name="id" type="hidden" value="${giftLogPage.id }">
			<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
				<tr>
					<td align="right">
						<label class="Validform_label">
							礼物拥有者:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="msisdn" name="msisdn" 
							   value="${giftLogPage.msisdn}" datatype="*">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							礼物类型-flower:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="giftType" name="giftType" 
							   value="${giftLogPage.giftType}" datatype="*">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							手动添加赠送花的数量:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="freeFlowerNum" name="freeFlowerNum" 
							   value="${giftLogPage.freeFlowerNum}" datatype="*">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							手动添加购买花的数量:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="costFlowerNum" name="costFlowerNum" 
							   value="${giftLogPage.costFlowerNum}" datatype="*">
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
						<input class="Wdate" onClick="WdatePicker()"  style="width: 150px" id="createTime" name="createTime" 
							   value="<fmt:formatDate value='${giftLogPage.createTime}' type="date" pattern="yyyy-MM-dd"/>" datatype="*">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
			</table>
		</t:formvalid>
 </body>