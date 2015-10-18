<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>用户的鲜花信息</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
 </head>
 <body style="overflow-y: hidden" scroll="no">
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="userGiftController.do?save">
			<input id="id" name="id" type="hidden" value="${userGiftPage.id }">
			<input class="inputxt" id="costFlowerNum"  type="hidden"  name="costFlowerNum"  value="${userGiftPage.costFlowerNum}"  datatype="n1-5">
			<input class="inputxt" id="freeFlowerNum"  type="hidden"  name="freeFlowerNum"  value="${userGiftPage.freeFlowerNum}"  datatype="n1-5">				   
			<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
				
				<tr>
					<td align="right">
						<label class="Validform_label">
							用户号码:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="msisdn" name="msisdn" 
							   value="${userGiftPage.msisdn}" disabled="disabled">
					</td>
				</tr>
				
				<tr>
					<td align="right">
						<label class="Validform_label">
							添加免费鲜花:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="appendFreeFlowerNum" name="appendFreeFlowerNum" 
							   value="0"  datatype="n1-5">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							添加付费鲜花:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="appendCostFlowerNum" name="appendCostFlowerNum" 
							   value="0"  datatype="n1-5">
						<span class="Validform_checktip"></span>
					</td>
				</tr>				
				</tr>
			</table>
		</t:formvalid>
 </body>